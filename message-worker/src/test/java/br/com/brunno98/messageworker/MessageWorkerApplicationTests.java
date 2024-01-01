package br.com.brunno98.messageworker;

import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@DirtiesContext
@SpringBootTest
public class MessageWorkerApplicationTests {

	public static final String SQS_HOST = "localhost";
	public static final int SQS_PORT = 9321;
	public static final String SQS_SERVER_PROTOCOL = "http";
	public static final String SQS_SERVER_CONTEXT_PATH = "";

	private SQSRestServer sqsRestServer = SQSRestServerBuilder
			.withPort(SQS_PORT)
			.withServerAddress(NodeAddress.apply(SQS_SERVER_PROTOCOL, SQS_HOST, SQS_PORT, SQS_SERVER_CONTEXT_PATH))
			.start();

	@TestConfiguration
	static class AwsClientConfig {
		@Bean
		public SqsAsyncClient sqsAsyncClient() {
			return SqsAsyncClient.builder()
					.region(Region.US_EAST_1)
					.endpointOverride(
							URI.create(String.format("%s://%s:%d/%s",
									SQS_SERVER_PROTOCOL, SQS_HOST, SQS_PORT, SQS_SERVER_CONTEXT_PATH)
							)
					)
					.credentialsProvider(StaticCredentialsProvider
							.create(AwsBasicCredentials.create("x", "x"))
					)
					.build();
		}
	}

	@Test
	void contextLoads() {
	}

}
