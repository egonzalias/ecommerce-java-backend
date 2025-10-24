package co.com.egonzalias.service.impl;

import co.com.egonzalias.dto.CreateOrderDTO;
import co.com.egonzalias.dto.OrderResponseDTO;
import co.com.egonzalias.dto.UpdateOrderDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponseDTO createOrder(CreateOrderDTO dto);
    OrderResponseDTO updateOrder(Long orderId, UpdateOrderDTO dto);
    void deleteOrder(Long orderId);
}
