package co.com.egonzalias.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateOrderDTO {
    private String status; // PENDING, PAID, CANCELLED, etc.

    private List<OrderProductDTO> products;
}
