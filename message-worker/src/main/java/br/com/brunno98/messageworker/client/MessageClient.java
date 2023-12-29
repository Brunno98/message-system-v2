package br.com.brunno98.messageworker.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "message-client", url = "${application.client.message-client.url}")
public interface MessageClient {

    @GetMapping("/message/{key}")
    String getTextFromKey(@PathVariable String key);

}
