package br.com.brunno98.messageworker.application;

import br.com.brunno98.messageworker.config.AwsClientConfig;
import br.com.brunno98.messageworker.config.SQSRestServerStarter;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.awaitility.Duration;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@EnableFeignClients
@Import(value = {AwsClientConfig.class, SQSRestServerStarter.class})
@SpringBootTest
@AutoConfigureWireMock(port = 0)
public class MessageRequestConsumerIntegrationTest {
    public static final String REQUEST_PAYLAOD = """
                {
                    "from": "foo",
                    "to": "bar",
                    "key": "HELLO",
                    "requestId": "1dd1d70e-237d-48d8-8be9-e051cceda760"
                }
                """;
    public static final String EXPECTED_PAYLOAD = """
                {
                    "from": "foo",
                    "to": "bar",
                    "text": "Hello Text",
                    "requestId": "1dd1d70e-237d-48d8-8be9-e051cceda760"
                }
                """;

    @Value("${application.queue.consumer-queue}")
    private String consumerQueue;

    @Value("${application.queue.producer-queue}")
    private String producerQueue;

    @Autowired
    private SQSRestServerStarter sqsRestServerStarter;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Test
    void whenConsumeMessageRequestFromQueueThenShouldFindTextFromKeyAndSendToQueue() throws JSONException {
        stubFor(get(urlEqualTo("/message/HELLO"))
                .willReturn(aResponse().withBody("Hello Text")));

        sqsTemplate.send(consumerQueue,REQUEST_PAYLAOD);

        await().atMost(Duration.ONE_SECOND);

        Optional<Message<String>> enqueuedMessage = sqsTemplate.receive(producerQueue, String.class);

        assertThat(enqueuedMessage).isPresent();
        String receivedPayload = enqueuedMessage.get().getPayload();
        JSONAssert.assertEquals(EXPECTED_PAYLOAD, receivedPayload, JSONCompareMode.LENIENT);
    }
}
