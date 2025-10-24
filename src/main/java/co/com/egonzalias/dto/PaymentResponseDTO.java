package co.com.egonzalias.dto;

import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long orderId,
        Long paymentId,
        String paymentMethod,
        LocalDateTime paidAt
) {}

