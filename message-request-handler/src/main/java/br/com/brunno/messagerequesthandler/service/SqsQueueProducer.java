package br.com.brunno.messagerequesthandler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SqsQueueProducer implements QueueProducer {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper om;

    public void send(Object message) {
        try {
            log.debug("producing message...");
            sqsTemplate.send("message-request-queue", om.writeValueAsString(message));
            log.info("message produced!");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            log.error("fail to send message", ex);
            throw ex;
        }
    }
}
