package co.com.egonzalias.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentDTO {
    @NotNull(message = "User ID is required")
    private Long orderId;

    @NotEmpty(message = "Payment method is required")
    private String paymentMethod;

    @NotEmpty(message = "Amount is required")
    private BigDecimal amount;
}


