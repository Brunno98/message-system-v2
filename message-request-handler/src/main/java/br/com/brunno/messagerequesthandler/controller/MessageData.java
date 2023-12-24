package br.com.brunno.messagerequesthandler.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageData implements Serializable {
    private String from;
    private String to;
    private String key;
    private String requestId;

    public MessageData(MessageRequestDTO requestDTO) {
        this.from = requestDTO.getFrom();
        this.to = requestDTO.getTo();
        this.key = requestDTO.getKey();
        this.requestId = UUID.randomUUID().toString();
    }
}
