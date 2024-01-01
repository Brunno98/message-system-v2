package br.com.brunno.messagerequesthandler.integration;

import br.com.brunno.messagerequesthandler.controller.MessageData;
import br.com.brunno.messagerequesthandler.controller.MessageRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@DirtiesContext
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MessageRequestHandlerIntegrationTest extends TestContainerBase {

    @Autowired
    SqsTemplate sqsTemplate;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper om;

    @Test
    void WhenRequestAPostMessageThenShouldEnqueueMessage() throws JsonProcessingException {
        MessageRequestDTO messageRequestDTO = new MessageRequestDTO();
        messageRequestDTO.setFrom("foo");
        messageRequestDTO.setTo("bar");
        messageRequestDTO.setKey("HELLO");
        ResponseEntity<Map> responsePost = restTemplate.postForEntity("/message", messageRequestDTO, Map.class);

        assertThat(responsePost).as("response from post request should not be null").isNotNull();
        assertThat(responsePost)
                .extracting(ResponseEntity::getStatusCode)
                .as("Status code for post response not match")
                .isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responsePost)
                .extracting(HttpEntity::getBody)
                .as("Response Body from post request should not be null")
                .isNotNull()
                .as("Response body should have requestId")
                .hasFieldOrProperty("requestId");

        MessageData messageExpected = new MessageData(messageRequestDTO);
        Optional<Message<String>> received = sqsTemplate.receive("message-request-queue", String.class);

        assertThat(received).as("Optional message from queue should be present").isPresent();
        assert received.isPresent(); // IDE issue to check presence...
        Message<String> messageDataMessage = received.get();
        String payload = messageDataMessage.getPayload();
        MessageData messageData = om.readValue(payload, MessageData.class);
        messageExpected.setRequestId(messageData.getRequestId()); // ignore request id
        assertThat(messageData)
                .as("message in queue should be equals to expected")
                .isEqualTo(messageExpected);
    }

}
