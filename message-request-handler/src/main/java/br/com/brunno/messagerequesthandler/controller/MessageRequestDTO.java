package br.com.brunno.messagerequesthandler.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MessageRequestDTO {
    private String from;
    private String to;
    private String key;
}
