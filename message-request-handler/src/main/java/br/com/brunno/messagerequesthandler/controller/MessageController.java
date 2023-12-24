package br.com.brunno.messagerequesthandler.controller;

import br.com.brunno.messagerequesthandler.service.QueueProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private ObjectMapper om;

    @PostMapping
    public ResponseEntity<Map<String, String>> requestMessage(@RequestBody MessageRequestDTO requestDTO) throws JsonProcessingException {
        MessageData messageData = new MessageData(requestDTO);

        queueProducer.send(messageData);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("requestId", messageData.getRequestId()));
    }
}
