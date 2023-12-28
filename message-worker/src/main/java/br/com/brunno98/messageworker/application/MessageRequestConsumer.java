package br.com.brunno98.messageworker.application;

import br.com.brunno98.messageworker.client.MessageClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRequestConsumer {
    private final SqsTemplate sqsTemplate;
    private final MessageClient messageClient;
    private final ObjectMapper OM;

    @SqsListener("message-request-queue")
    public void process(String rawPayload) throws JsonProcessingException {
        log.debug("starting consuming message");
        MessageRequestPayload payload = OM.readValue(rawPayload, MessageRequestPayload.class);
        // TODO:
        //  -- criar uma classe de converter
        //  -- Cenarios de exceptions para teste
        log.info("String payload converted to dto class successfully");

        log.debug("searching for text from key");
        String text = messageClient.getTextFromKey(payload.getKey());
        // TODO: servidor em memoria para teste
        log.info("text got from key");

        Message message = Message.from(payload, text);

        log.debug("sending message to queue");
        sqsTemplate.send("send-message-queue", OM.writeValueAsString(message));
        log.info("messege sended to queue successfully");
    }
}
