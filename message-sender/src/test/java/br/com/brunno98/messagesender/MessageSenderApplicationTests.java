package br.com.brunno98.messagesender;

import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageSenderApplicationTests {

	SQSRestServer sqsRestServer = SQSRestServerBuilder
			.withPort(9432)
			.withServerAddress(new NodeAddress("http", "localhost", 9432, ""))
			.start();

	@Test
	void contextLoads() {
	}

}
