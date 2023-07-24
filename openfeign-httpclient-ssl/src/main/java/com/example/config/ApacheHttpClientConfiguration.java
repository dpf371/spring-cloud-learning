package com.example.config;

import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({HttpClient.class})
@AutoConfigureBefore({HttpClientConfiguration.class})
public class ApacheHttpClientConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ApacheHttpClientConnectionManagerFactory connManFactory() {
        return new ReplaceDefaultApacheHttpClientConnectionManagerFactory();
    }
}
