package br.com.brunno98.messageworker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Import(SqsServerConfig.class)
@TestConfiguration
public class AwsClientConfig {
    public static final String ACCESS_KEY = "x";
    public static final String SECRET_ACCESS_KEY = "x";

    @Autowired
    SqsServerConfig sqsServerConfig;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        final URI sqsEnpoint = URI.create(String.format("%s://%s:%d/%s",
                sqsServerConfig.getProtocol(),
                sqsServerConfig.getHost(),
                sqsServerConfig.getPort(),
                sqsServerConfig.getContextPath()
            ));

        return SqsAsyncClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(sqsEnpoint)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(ACCESS_KEY, SECRET_ACCESS_KEY)))
                .build();
    }
}
