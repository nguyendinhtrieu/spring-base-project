package com.tzyel.springbaseproject.config.client.rest_template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Value("${application.client.requestTimeOutInMillis}")
    private Integer requestTimeOutInMillis;

    @Bean(name = "sbpRestTemplate")
    public RestTemplate sbpRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //noinspection NullableProblems
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        factory.setConnectTimeout(requestTimeOutInMillis);
        factory.setReadTimeout(requestTimeOutInMillis);
        restTemplate.setRequestFactory(factory);

        return restTemplate;
    }
}
