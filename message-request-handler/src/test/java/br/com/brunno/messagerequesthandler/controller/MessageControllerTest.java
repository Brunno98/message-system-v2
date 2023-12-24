package br.com.brunno.messagerequesthandler.controller;

import br.com.brunno.messagerequesthandler.service.QueueProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {
    public static final String REQUEST_BODY = """
            {
                "from": "foo",
                "to": "bar",
                "key": "HELLO"
            }
            """;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueueProducer queueProducer;

    @Test
    void postForMessageShouldReturnAcceptedAndRequestId() throws Exception {
        mockMvc.perform(post("/message")
                        .content(REQUEST_BODY)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isAccepted(),
                        jsonPath("requestId").isString()
                );

    }
}
