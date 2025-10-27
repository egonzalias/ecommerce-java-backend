package co.com.egonzalias.service.impl;

public interface SqsService {
    void sendMessage(Object messageBody, String queueName);
}
