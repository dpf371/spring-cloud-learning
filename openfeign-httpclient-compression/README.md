# Openfeign + Apache Httpclient 压缩与解压 Response body

## 1. 如何开启压缩

### 客户端
在pom.xml中添加以下依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```

添加以下配置 Openfeign 就能识别并给 HTTP Request 增加 Header，告诉服务端“我能接受服务端压缩 Response body，我有解压功能，以及我支持的压缩方式”。
```yaml
feign:
  compression:
    response:
      enabled: true
```
### 服务端
添加以下配置就能开启 Tomcat 压缩 HTTP Response Body 的功能，通过识别 HTTP 请求头 “Accept-Encoding” 决定是否压缩 Response Body。
```yaml
server:
  compression:
    enabled: true
    ## Response Body 达到 1KB Tomcat就压缩
    min-response-size: 1
    mime-types: "text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json,application/xml,image/png,image/jpg"
```

但是通过测试发现 HTTP Response Body 没有被压缩，问题出在哪？？？

## 2. 找原因

在客户端添加了以上配置后，即使服务端也开启了 HTTP 压缩响应内容的功能，但是仍然响应内容仍然没有被压缩，
这是因为虽然服务端会读取客户端发来的 HTTP Request Header，根据是否有 “accept-encoding" 这个请求头决定是否压缩（主要依据），
如果有并且对应的值包含“gzip”才压缩 Response body，否则不压缩。
当使用 Spring boot 2.1.6 时，相应的 Spring Cloud 版本是 Greenwich.SR2，对应的 Openfeign 是 10.2.3 版本，
该版本的 Spring Cloud 在集成 Openfeign 时是这样写的：
```java
@ConditionalOnProperty(
    value = {"feign.compression.response.enabled"},
    matchIfMissing = false
)
public class FeignAcceptGzipEncodingAutoConfiguration {
    @Bean
    public FeignAcceptGzipEncodingInterceptor feignAcceptGzipEncodingInterceptor(FeignClientEncodingProperties properties) {
        return new FeignAcceptGzipEncodingInterceptor(properties);
    }
}
```
```java
public class FeignAcceptGzipEncodingInterceptor extends BaseRequestInterceptor {
    protected FeignAcceptGzipEncodingInterceptor(FeignClientEncodingProperties properties) {
        super(properties);
    }

    public void apply(RequestTemplate template) {
        this.addHeader(template, "Accept-Encoding", new String[]{"gzip", "deflate"});
    }
}
```
Openfeign 会调用 Apache httpclient 的 Apache HttpClient 类中的 execute(...) 方法完成 HTTP 调用，
execute(...) 会先将 Openfeign 的 Request 对象转成 Apache HttpClient 的 Request 对象，其中就包括了读取并组装请求头的过程：
```java
public final class ApacheHttpClient implements Client {
    /**
     * 将 Openfeign 的 Request 对象转成 Apache HttpClient 的 Request 对象
     */
    HttpUriRequest toHttpUriRequest(Request request, Request.Options options) {
        // ...略
        // 转换header
        for (Map.Entry<String, Collection<String>> headerEntry : request.headers().entrySet()) {
            String headerName = headerEntry.getKey();
            // ...略
            for (String headerValue : headerEntry.getValue()) {
                requestBuilder.addHeader(headerName, headerValue); // ①
            }
        }
        // 转换body
        // ...略
    }
}
```
通过 debug 发现 request 对象的 headers 属性保存的是一个 Map.Entry<String, Collection<String>> 结构，
key 为 "Accept-Encoding"，value 是一个 Set<String>，有2个值 "gzip" 和 "deflate"，因此 ① 这一行会走2次，
走完之后 HTTP 请求头就会多 2 行，是的，没错，就是多了 2 行：
```
Accept-Encoding: deflate
Accept-Encoding: gzip
```



服务端是 Tomcat，在向客户端发送 Response 之前会查找 Request Header 中是否有压缩标识 "Accept-Encoding"，
有的话就再进一步检查这个请求头的值是否包含 “gzip”，有的话才压缩，否则不压缩。
查找判断的逻辑在 CompressionConfig 类中的 useCompression(...) 方法中：
```java
public class CompressionConfig {
    public boolean useCompression(Request request, Response response) {
        // ...略
        MessageBytes acceptEncodingMB = request.getMimeHeaders().getValue("accept-encoding"); // ①
        if (acceptEncodingMB != null && acceptEncodingMB.indexOf("gzip") != -1) { // ③
            // ...略（进一步判断是否忽略特定类型的客户端的压缩标识）
            response.setContentLength(-1L);
            responseHeaders.setValue("Content-Encoding").setString("gzip");
            return true;
        } else {
            return false;
        }
        // ...略
    }
}
```
这里的关键在于 ① 处这一行，查看 MimeHeaders 类的 getValue(...) 方法：
```java
public class MimeHeaders {
    public MessageBytes getValue(String name) {
        for(int i = 0; i < this.count; ++i) {
            if (this.headers[i].getName().equalsIgnoreCase(name)) { // ②
                return this.headers[i].getValue();
            }
        }
        return null;
    }
}
```
答案在 ② 这一行代码，headers[] 数组中有 2 条数据：
```
{“name": "Accept-Encoding", "value": "deflate"}
{"name": "Accept-Encoding", "value": "gzip"}
```
这个方法 return 的是 headers[] 数组中的找到的第一个 name 为 "accept-encoding" 的 value 属性值。
拿到这个 “accept-encoding” 请求头的值之后，就进一步检查是否包含 “gzip”，见 ③ 那一行代码，
包含则启用压缩功能，否则不启用压缩。


## 3. 解决方案
知道了问题原因，就好解决了，增加一个 Openfeign 的 Interceptor 完成 "accept-encoding:deflate,gzip" 的添加就 OK 啦。
```java
@Configuration
public class FeignConfiguration {
    @Bean
    public RequestInterceptor gzipInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("Accept-Encoding", "gzip, deflate");
            }
        };
    }
}
```
这里和 FeignAcceptGzipEncodingInterceptor 的写法就只有下面这么一点点区别而已：
```java
template.header("Accept-Encoding", "gzip, deflate"); // 字符串
```
```java
this.addHeader(template, "Accept-Encoding", new String[]{"gzip", "deflate"}); // 数组
```


## 4.Apache Httpclient 控制压缩功能开启与关闭的开关

```java
public class HttpClientBuilder {
    /**
     * 压缩 Response Body 功能的控制开关
     */
    private boolean contentCompressionDisabled;
}
```

#### 1.如果创建了 Feign RequestInterceptor 添加了请求头，响应能被压缩了，但是响应内容是乱码，也就是没有开启解压功能。
如果 pom.xml 中只添加了 Openfeign、Apache Httpclient 相关的 jar，
而没有添加 ribbon 相关的jar，那么 Httpclient 实例是在 FeignAutoConfiguration 类中创建的：
```java
public class FeignAutoConfiguration {
    @Configuration
    @ConditionalOnClass(ApacheHttpClient.class)
    // 因为没有引入 ribbon 相关的jar，因此没有“com.netflix.loadbalancer.ILoadBalancer“
    @ConditionalOnMissingClass("com.netflix.loadbalancer.ILoadBalancer")
    @ConditionalOnMissingBean(CloseableHttpClient.class)
    @ConditionalOnProperty(value = "feign.httpclient.enabled", matchIfMissing = true)
    protected static class HttpClientFeignConfiguration {
        @Bean
        public CloseableHttpClient httpClient(ApacheHttpClientFactory httpClientFactory,
                                              HttpClientConnectionManager httpClientConnectionManager,
                                              FeignHttpClientProperties httpClientProperties) {
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(httpClientProperties.getConnectionTimeout())
                    .setRedirectsEnabled(httpClientProperties.isFollowRedirects())
                    .build();
            this.httpClient = httpClientFactory.createBuilder() // ①
                    .setConnectionManager(httpClientConnectionManager)
                    .setDefaultRequestConfig(defaultRequestConfig).build();
            return this.httpClient;
        }
    }
}
```

点开 ① 这一行代码，会发现“压缩Response Body的功能”被禁用了：
```java
public class DefaultApacheHttpClientFactory implements ApacheHttpClientFactory {
    private HttpClientBuilder builder;

    public HttpClientBuilder createBuilder() {
        return this.builder
                .disableContentCompression()
                .disableCookieManagement()
                .useSystemProperties();
    }
}
```
```java
public class HttpClientBuilder {
    /**
     * 压缩 Response Body 功能的控制开关
     */
    private boolean contentCompressionDisabled;
    
    public final HttpClientBuilder disableContentCompression() {
        contentCompressionDisabled = true;
        return this;
    }
}
```

#### 2.开启 Apache Httpclient 的解压功能

在 pom.xml 中添加 ribbon 相关的依赖：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```

添加该依赖后，FeignAutoConfiguration 类中创建 Httpclient 实例的代码就失效了，
因为jvm中有“com.netflix.loadbalancer.ILoadBalancer“类了，
这时创建 Httpclient 实例是由 HttpClientFeignLoadBalancedConfiguration 类完成的：
```java
class HttpClientFeignLoadBalancedConfiguration {
    @Configuration
    @ConditionalOnMissingBean(CloseableHttpClient.class)
    protected static class HttpClientFeignConfiguration {
        @Bean
        @ConditionalOnProperty(value = "feign.compression.response.enabled", havingValue = "true")
        public CloseableHttpClient customHttpClient(
                HttpClientConnectionManager httpClientConnectionManager,
                FeignHttpClientProperties httpClientProperties) {
            HttpClientBuilder builder = HttpClientBuilder.create() // ①
                    .disableCookieManagement().useSystemProperties();
            this.httpClient = createClient(builder, httpClientConnectionManager,
                    httpClientProperties);
            return this.httpClient;
        }

        @Bean
        @ConditionalOnProperty(value = "feign.compression.response.enabled", havingValue = "false", matchIfMissing = true)
        public CloseableHttpClient httpClient(ApacheHttpClientFactory httpClientFactory,
                                              HttpClientConnectionManager httpClientConnectionManager,
                                              FeignHttpClientProperties httpClientProperties) {
            this.httpClient = createClient(httpClientFactory.createBuilder(), // ②
                    httpClientConnectionManager, httpClientProperties);
            return this.httpClient;
        }

        private CloseableHttpClient createClient(HttpClientBuilder builder,
                                                 HttpClientConnectionManager httpClientConnectionManager,
                                                 FeignHttpClientProperties httpClientProperties) {
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(httpClientProperties.getConnectionTimeout())
                    .setRedirectsEnabled(httpClientProperties.isFollowRedirects())
                    .build();
            CloseableHttpClient httpClient = builder
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .setConnectionManager(httpClientConnectionManager).build();  // ③
            return httpClient;
        }
    }
}
```
如果添加了配置项 feign.compression.response.enabled=true，则 ① 处代码被执行，解压缩 Response Body 功能被设置为开启，
如果没有添加该配置项，则 ② 处代码被执行，解压缩功能被设置为关闭。
Httpclient 实例的创建过程在 ③ 处，点进去，就能找到控制 解压缩 的代码：
```java
public class HttpClientBuilder {
    public CloseableHttpClient build() {
        // ...略
        if (!contentCompressionDisabled) { // 控制开关
            if (contentDecoderMap != null) {
                final List<String> encodings = new ArrayList<String>(contentDecoderMap.keySet());
                Collections.sort(encodings);
                b.add(new RequestAcceptEncoding(encodings)); // 创建添加压缩标记请求头的Request拦截器
            } else {
                b.add(new RequestAcceptEncoding()); // 创建添加压缩标记请求头的Request拦截器
            }
        }
        // ...略
        if (!contentCompressionDisabled) { // 控制开关
            if (contentDecoderMap != null) {
                final RegistryBuilder<InputStreamFactory> b2 = RegistryBuilder.create();
                for (final Map.Entry<String, InputStreamFactory> entry: contentDecoderMap.entrySet()) {
                    b2.register(entry.getKey(), entry.getValue());
                }
                b.add(new ResponseContentEncoding(b2.build())); // 创建解压缩的工具类
            } else {
                b.add(new ResponseContentEncoding()); // 创建解压缩的工具类
            }
        }
        // ...略
    }
}
```



* [Spring Cloud Openfeign 开启压缩 Response Body 功能失败的分析](https://www.jianshu.com/p/eb8ea0999b89?v=1684919876940)