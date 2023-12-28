package br.com.brunno98.messageworker.application;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageRequestPayload {
    private String from;
    private String to;
    private String key;
    private String requestId;
}
