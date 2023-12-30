package br.com.brunno.messagestoreservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MessageIntegrationTest {
    public static final String BASE_PATH = "/message";
    public static final String POST_REQUEST_PAYLOAD = """
                {
                    "key": "HELLO",
                    "text": "Hello Text"
                }
                """;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldBePossibleToCreateRetrieveUpdateAndDeleteMessage() {
        ResponseEntity<Object> responseFromPost = restTemplate
                .postForEntity(BASE_PATH, POST_REQUEST_PAYLOAD, Object.class);
        assertThat(responseFromPost)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> responseFromGet = restTemplate
                .getForEntity(BASE_PATH + "/{key}", String.class, Map.of("key", "HELLO"));

        assertThat(responseFromGet).extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
        assertThat(responseFromGet).extracting(ResponseEntity::getBody)
                .isEqualTo("Hello Text");

        restTemplate.put(BASE_PATH + "/{key}", """
                {"text": "Hello Updated"}
                """, Map.of("key", "HELLO"));

        responseFromGet = null; // clean
        responseFromGet = restTemplate
                .getForEntity(BASE_PATH + "/{key}", String.class, Map.of("key", "HELLO"));

        assertThat(responseFromGet).extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
        assertThat(responseFromGet).extracting(ResponseEntity::getBody)
                .isEqualTo("Hello Updated");

        restTemplate.delete(BASE_PATH + "/{key}", Map.of("key", "HELLO"));

        responseFromGet = null; // clean
        responseFromGet = restTemplate
                .getForEntity(BASE_PATH + "/{key}", String.class, Map.of("key", "HELLO"));

        assertThat(responseFromGet).extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
