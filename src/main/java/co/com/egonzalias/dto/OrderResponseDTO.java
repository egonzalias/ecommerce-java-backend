package co.com.egonzalias.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderProductResponseDTO> products;
}

