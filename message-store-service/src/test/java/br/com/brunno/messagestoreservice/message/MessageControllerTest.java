package br.com.brunno.messagestoreservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {
    public static final String CREATE_MESSAGE_REQUEST = """
                        {
                            "key": "HELLO",
                            "text": "Hello Text"
                        }
                        """;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MessageRepository messageRepository;

    @Test
    void whenCreateMessageShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_MESSAGE_REQUEST))
                .andExpect(status().isCreated());
    }

    @Test
    void whenGetForMessageKeyShouldReturnItText() throws Exception {
        Message message = new Message();
        message.setKey("HELLO");
        message.setText("Hello Text");
        doReturn(Optional.of(message)).when(messageRepository).findByKey("HELLO");

        mockMvc.perform(get("/message/{key}", "HELLO"))
                .andExpectAll(
                        status().isOk(),
                        content().string("Hello Text")
                );
    }
}
