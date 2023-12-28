package br.com.brunno98.messageworker.application;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Message {
    private String from;
    private String to;
    private String text;
    private String requestId;

    public static Message from(MessageRequestPayload requestPayload, String text) {
        Message message = new Message();
        message.from = requestPayload.getFrom();
        message.to = requestPayload.getTo();
        message.requestId = requestPayload.getRequestId();
        message.text = text;
        return message;
    }
}
