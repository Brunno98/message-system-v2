package br.com.brunno98.messageworker;

import br.com.brunno98.messageworker.config.AwsClientConfig;
import br.com.brunno98.messageworker.config.SQSRestServerStarter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@Import(value = {SQSRestServerStarter.class, AwsClientConfig.class})
@SpringBootTest
@DirtiesContext
public class MessageWorkerApplicationTests {

	@Test
	void contextLoads() {
	}

}
