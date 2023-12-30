package br.com.brunno.messagestoreservice.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final ObjectMapper OM;
    private final MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<String> createMessage(@RequestBody String rawMessageRequest) {
        Map messageRequest = null;
        try {
            messageRequest = OM.readValue(rawMessageRequest, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        final String key = messageRequest.get("key").toString();
        final String text = messageRequest.get("text").toString();
        Assert.noNullElements(new String[]{key, text}, "should have 'key' and 'text' on message create request");

        Message message = new Message();
        message.setKey(key);
        message.setText(text);

        messageRepository.save(message);

        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{key}")
    public ResponseEntity<String> getMessage(@PathVariable String key) {
        Optional<Message> message = messageRepository.findByKey(key);
        if (message.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String text = message.get().getText();
        return ResponseEntity.ok(text);
    }

    @PutMapping("/{key}")
    public ResponseEntity<Void> updateMessage(@PathVariable String key, @RequestBody String rawUpdateRequest) {
        Map updateRequest;
        try {
            updateRequest = OM.readValue(rawUpdateRequest, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String text = updateRequest.getOrDefault("text", "").toString();

        Optional<Message> searchedMessage = this.messageRepository.findByKey(key);
        if (searchedMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message message = searchedMessage.get();

        message.setText(text);

        messageRepository.save(message);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String key) {
        messageRepository.deleteByKey(key);
        return ResponseEntity.noContent().build();
    }
}
