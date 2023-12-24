package br.com.brunno.messagerequesthandler.service;

public interface QueueProducer {
    void send(Object message);
}
