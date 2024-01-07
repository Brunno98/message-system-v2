package br.com.brunno98.messageworker.application;

import br.com.brunno98.messageworker.client.MessageClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRequestConsumer {
    private final SqsTemplate sqsTemplate;
    private final MessageClient messageClient;
    private final ObjectMapper OM;

    @Value("${application.queue.producer-queue}")
    private String producerQueue;

    @SqsListener("${application.queue.consumer-queue}")
    public void process(String rawPayload) throws JsonProcessingException {
        log.debug("starting consuming message");
        MessageRequestPayload payload = OM.readValue(rawPayload, MessageRequestPayload.class);
        // TODO:
        //  -- criar uma classe de converter
        //  -- Cenarios de exceptions para teste
        log.info("String payload converted to dto class successfully");

        log.debug("searching for text from key");
        String text = messageClient.getTextFromKey(payload.getKey());
        log.info("text got from key");

        Message message = Message.from(payload, text);

        log.debug("sending message to queue");
        sqsTemplate.send(producerQueue, OM.writeValueAsString(message));
        log.info("messege sent to queue successfully");
    }
}
