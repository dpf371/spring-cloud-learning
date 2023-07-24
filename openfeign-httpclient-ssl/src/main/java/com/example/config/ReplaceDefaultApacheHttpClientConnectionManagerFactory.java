package com.example.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientConnectionManagerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReplaceDefaultApacheHttpClientConnectionManagerFactory extends DefaultApacheHttpClientConnectionManagerFactory {

    public HttpClientConnectionManager newConnectionManager(boolean disableSslValidation, int maxTotalConnections, int maxConnectionsPerRoute) {
        return this.newConnectionManager(disableSslValidation, maxTotalConnections, maxConnectionsPerRoute, -1L, TimeUnit.MILLISECONDS, (RegistryBuilder)null);
    }

    @SneakyThrows
    public HttpClientConnectionManager newConnectionManager(boolean disableSslValidation, int maxTotalConnections, int maxConnectionsPerRoute, long timeToLive, TimeUnit timeUnit, RegistryBuilder registryBuilder) {
        if (registryBuilder == null) {
            registryBuilder = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.INSTANCE);
        }

        if (disableSslValidation) {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init((KeyManager[])null, new TrustManager[]{new DisabledValidationTrustManager()}, new SecureRandom());
                registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE));
            } catch (NoSuchAlgorithmException var10) {
                log.warn("Error creating SSLContext", var10);
            } catch (KeyManagementException var11) {
                log.warn("Error creating SSLContext", var11);
            }
        } else {
            // SSLConnectionSocketFactory 留了 protected 方法，用于SSL握手之前给SSLSocket设置属性，比如添加密钥算法CipherSuites。
            registryBuilder.register("https", new SSLConnectionSocketFactory(MySSLContext.createDefault()));
        }

        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry, (HttpConnectionFactory)null, (SchemePortResolver)null, (DnsResolver)null, timeToLive, timeUnit);
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        return connectionManager;
    }

    class DisabledValidationTrustManager implements X509TrustManager {
        DisabledValidationTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
