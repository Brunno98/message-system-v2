package br.com.brunno98.messagesender.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Consumer {

    @Autowired
    private ObjectMapper OM;

    @Autowired
    private InboxRepository inboxRepository;

    @SqsListener(value = "send-message-queue")
    public void process(String rawMessage) {
        log.debug("starting processing message {}", rawMessage);
        Map message;
        try {
            message = OM.readValue(rawMessage, Map.class);
        } catch (JsonProcessingException e) {
            log.error("error converting message [{}] to map", rawMessage);
            throw new RuntimeException(e);
        }

        Assert.noNullElements(List.of(message.get("from"), message.get("to"), message.get("text")),
                "message should have keys 'from', 'to' and 'text'");

        log.debug("Mounting Inbox data struct");
        Inbox inbox = new Inbox();
        inbox.setFrom(message.get("from").toString());
        inbox.setTo(message.get("to").toString());
        inbox.setText(message.get("text").toString());
        log.debug("Inbox builded!");

        log.debug("saving message to inbox...");
        inboxRepository.save(inbox);
        log.info("Message saved to inbox successfully");
    }
}
