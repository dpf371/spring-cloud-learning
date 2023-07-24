# Openfeign + Apache Httpclient 访问 HTTPS 网站

## 1. 背景知识

HTTPS = HTTP + SSL/TLS
HTTP 是使用的 Socket 传输数据的，HTTPS 是使用的 SSLSocket 传输数据的。
SSL 握手协议 就是在 TCP 3次握手完成之后，client 和 server 就已经建立好 TCP 连接了，这时候 client 端就有了相应的 Socket 了，server 端也有了对应的 ServerSocket，
可以通信了，为了通信内容只能被发送方和接收方读懂，于是就有了 SSL 握手协议，就是在双方完成 TCP 3次握手后，再加一层握手逻辑，将 Socket/ServerSocket 包装成 SSLSocket/SSLServerSocket，
SSL 握手过程主要做的事情：
 1. 双方对彼此要做身份识别或者说身份坚定，客户端要识别服务端身份的流程叫单向认证、客户端和服务端都要识别对方的方式叫双向认证。
 2. 双方协商出加密解密的密钥、加解密采用的算法。

需要我们的代码参与的过程就是第 1 步，身份识别的过程，具体就是下载好 给我们想要访问的网站签发证书的根机构的证书（根证书），将其添加到 X509TrustManager 里，
在 client 首次访问 server 端时，双方要 SSL 握手，这个过程中 server 端将自己的签名证书发送给 client，client 收到后会调用 X509TrustManager.checkServerTrusted(receivedCertificates, clientTrustedCertificates)，
用 client 本地加载的信任的根证书中的公钥验证收到的 server 端发来的证书链，来识别 server 真假。

![SSL握手过程示意图，从网上找来的图稍微修改了下](file\SSL握手过程.png)

图片来源：https://www.jianshu.com/p/54afe380b83b

## 2. 运行该 Demo

该 Demo 工程提供了 3 个 URL：
1. /mock/pfdu
2. /mock/tax
3. /mock/12306

直接启动，可以访问的是第 3 个 URL，它是访问的12306的官网首页；

如果要访问通第 1 个和第 2 个 URL，则要将 resources/x509certificate/ 中的这 4 个文件部署到 nginx 中：
1. server.crt
2. server_no_passwd.key
3. server2.crt
4. server_no_passwd2.key
这 4 个文件是使用 OpenSSL 工具自制的 SSL 证书。

将这 4 个证书放入到 nginx/ssl 目录（没有ssl目录就创建），并编辑 nginx.conf 添加以下内容：
```
    server {
        listen       443 ssl;
        server_name  pfdu.com;

        ssl_certificate      ../ssl/server.crt;
        ssl_certificate_key  ../ssl/server_no_passwd.key;

        ssl_session_cache    shared:SSL:1m;
        ssl_session_timeout  5m;

        ssl_ciphers  HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers  on;

        location / {
            root   html;
            index  index.html index.htm;
        }
    }
    
    server {
        listen       1443 ssl;
        server_name  tax.com;

        ssl_certificate      ../ssl/server2.crt;
        ssl_certificate_key  ../ssl/server_no_passwd2.key;

        ssl_session_cache    shared:SSL:1m;
        ssl_session_timeout  5m;

        ssl_ciphers  HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers  on;

        location / {
            root   html;
            index  index.html index.htm;
        }
    }
```

在本机 hosts 文件中添加以下域名和IP的映射关系：
```
pfdu.com       127.0.0.1
tax.com        127.0.0.1
```

最后，启动 nginx，再启动该 Demo，即可访问第 1 个和第 2 个 URL 了。