package br.com.brunno98.messageworker.application;

import br.com.brunno98.messageworker.config.AwsClientConfig;
import br.com.brunno98.messageworker.config.SQSRestServerStarter;
import br.com.brunno98.messageworker.config.SqsServerConfig;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.awaitility.Duration;
import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Optional;

import static br.com.brunno98.messageworker.MessageWorkerApplicationTests.SQS_HOST;
import static br.com.brunno98.messageworker.MessageWorkerApplicationTests.SQS_PORT;
import static br.com.brunno98.messageworker.MessageWorkerApplicationTests.SQS_SERVER_CONTEXT_PATH;
import static br.com.brunno98.messageworker.MessageWorkerApplicationTests.SQS_SERVER_PROTOCOL;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@EnableFeignClients
@Import(value = {AwsClientConfig.class, SQSRestServerStarter.class})
@SpringBootTest
@AutoConfigureWireMock(port = 8081) //TODO: boas praticas de configuracao do wiremock
public class MessageRequestConsumerIntegrationTest {

    @Autowired
    private SQSRestServerStarter sqsRestServerStarter;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Test
    void whenConsumeMessageRequestFromQueueThenShouldFindTextFromKeyAndSendToQueue() throws JSONException {
        stubFor(get(urlEqualTo("/message/HELLO"))
                .willReturn(aResponse().withBody("Hello Text")));

        // TODO:
        //  -- parametrizar nomme da fila
        //  -- levar payload para variavel
        sqsTemplate.send("message-request-queue","""
                {
                    "from": "foo",
                    "to": "bar",
                    "key": "HELLO",
                    "requestId": "1dd1d70e-237d-48d8-8be9-e051cceda760"
                }
                """);

        await().atMost(Duration.ONE_SECOND);

        // TODO:
        //  -- parametrizar nomme da fila
        Optional<Message<String>> enqueuedMessage = sqsTemplate.receive("send-message-queue", String.class);

        assertThat(enqueuedMessage).isPresent();

        // TODO:
        //  -- levar payload para variavel
        String expectedMessage = """
                {
                    "from": "foo",
                    "to": "bar",
                    "text": "Hello Text",
                    "requestId": "1dd1d70e-237d-48d8-8be9-e051cceda760"
                }
                """;
        JSONAssert.assertEquals(expectedMessage, enqueuedMessage.get().getPayload(), JSONCompareMode.LENIENT);

    }
}
