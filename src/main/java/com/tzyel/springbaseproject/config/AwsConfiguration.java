package com.tzyel.springbaseproject.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {
    @Value("${application.aws.region}")
    private String region;
    @Value("${application.aws.localstack.serviceEndpoint:}")
    private String localstackServiceEndpoint;
    @Value("${application.aws.accessKey:}")
    private String accessKey;
    @Value("${application.aws.secretKey:}")
    private String secretKey;

    @Bean
    @ConditionalOnProperty(value = {"application.aws.serviceEnvironment"}, havingValue = "default")
    public AmazonS3 defaultAmazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new DefaultAWSCredentialsProviderChain()).build();
    }

    @Bean
    @ConditionalOnProperty(value = {"application.aws.serviceEnvironment"}, havingValue = "localstack")
    public AmazonS3 localstackAmazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(localstackServiceEndpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .enablePathStyleAccess() // Need to enable path style access for by setting localstack endpoint
                .build();
    }
}
