package br.com.brunno.messagerequesthandler.integration;

import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@TestConfiguration
public class SqsInMemoryTest {
    public static final int SQS_SERVER_PORT = 9321;
    public static final String SQS_SERVER_HOST = "localhost";

//    public static SQSRestServer sqsServer = SQSRestServerBuilder
//            .withPort(SQS_SERVER_PORT)
//            .withServerAddress(new NodeAddress("http", SQS_SERVER_HOST, SQS_SERVER_PORT, ""))
//            .start();

    @Bean
    public SQSRestServer sqsRestServer() {
        return SQSRestServerBuilder
            .withPort(SQS_SERVER_PORT)
            .withServerAddress(new NodeAddress("http", SQS_SERVER_HOST, SQS_SERVER_PORT, ""))
            .start();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("x", "x")
                ))
                .endpointOverride(URI.create("http://" + SQS_SERVER_HOST + ":" + SQS_SERVER_PORT))
                .region(Region.US_EAST_1)
                .build();
    }
}
