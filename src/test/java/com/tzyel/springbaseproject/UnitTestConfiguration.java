package com.tzyel.springbaseproject;

import com.amazonaws.services.s3.AmazonS3;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UnitTestConfiguration {
    @Bean
    @ConditionalOnProperty(value = {"application.aws.serviceEnvironment"}, havingValue = "mock")
    public AmazonS3 mockAmazonS3() {
        return Mockito.mock(AmazonS3.class);
    }
}
