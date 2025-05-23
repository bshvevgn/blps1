package com.eg.blps1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private static final String BASE_URL = "http://localhost:8083";

    @Bean
    public WebClient initWebClient() {
        return WebClient.builder().baseUrl(BASE_URL).build();
    }
}
