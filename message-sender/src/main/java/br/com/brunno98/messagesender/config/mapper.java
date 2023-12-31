package br.com.brunno98.messagesender.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mapper {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
