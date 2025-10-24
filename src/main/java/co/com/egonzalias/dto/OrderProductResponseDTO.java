package co.com.egonzalias.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductResponseDTO {
    private Long productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}