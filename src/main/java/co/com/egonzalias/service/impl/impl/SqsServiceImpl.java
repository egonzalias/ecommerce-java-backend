package co.com.egonzalias.service.impl.impl;

import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.service.impl.SqsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.concurrent.CompletableFuture;

@Service
public class SqsServiceImpl implements SqsService {

    private static final Logger logger = LoggerFactory.getLogger(SqsServiceImpl.class);

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.url}")
    private String awsUrl;

    @Value("${aws.account-id}")
    private String accountId;

    public SqsServiceImpl(@Qualifier("sqsClient") SqsClient sqsClient, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendMessage(Object messageBody, String queueName) {
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(messageBody);
        } catch (JsonProcessingException e) {
            logger.error("Error serializando el mensaje para SQS", e);
            throw new CustomError("Error serializando mensaje: " + e.getMessage());
        }

        String queueUrl = String.format("%s/%s/%s", awsUrl, accountId, queueName);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(jsonMessage)
                .build();

        try {
            SendMessageResponse response = sqsClient.sendMessage(request);
            logger.info("Mensaje enviado a SQS ({}) con ID: {}", queueName, response.messageId());
        } catch (SqsException e) {
            logger.error("Error AWS SQS al enviar mensaje: {}", e.awsErrorDetails().errorMessage());
            throw new CustomError("Error AWS SQS: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al enviar mensaje a SQS", e);
            throw new CustomError("Error inesperado al enviar mensaje a SQS: " + e.getMessage());
        }
    }
}