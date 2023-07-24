package com.example.config;

import org.springframework.cloud.openfeign.FeignAutoConfiguration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class MySSLContext {
    public static SSLContext createDefault() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // 加载信任的根证书，在实际应用时，一般是从一个指定的目录下读取，而不是像这样写死路径。
        Certificate ca = cf.generateCertificate(new FileInputStream("x509certificate\\server.crt"));
        Certificate ca2 = cf.generateCertificate(new FileInputStream("x509certificate\\server2.crt"));
        Certificate ca3 = cf.generateCertificate(MySSLContext.class.getResourceAsStream("x509certificate\\CFCA EV ROOT.crt"));
//        String keyStoreType = KeyStore.getDefaultType();
        String keyStoreType = "jks";
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        // alias没有特殊意义，只要不重复即可。
        keyStore.setCertificateEntry("ca", ca);
        keyStore.setCertificateEntry("ca2", ca2);
        keyStore.setCertificateEntry("ca3", ca3);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;

    }
}