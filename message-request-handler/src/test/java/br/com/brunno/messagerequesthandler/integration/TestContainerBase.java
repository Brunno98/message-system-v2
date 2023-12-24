package br.com.brunno.messagerequesthandler.integration;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Testcontainers
public abstract class TestContainerBase {

    protected static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:3.0.2");

    @Container
    private static final LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(LocalStackContainer.Service.SQS);

    @DynamicPropertySource
    public static void setupProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.endpoint", localstack::getEndpoint);
    }

    @Bean
    @Primary
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }
}
