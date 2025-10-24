package co.com.egonzalias.service.impl;

import co.com.egonzalias.dto.CreateOrderDTO;
import co.com.egonzalias.dto.UpdateOrderDTO;
import co.com.egonzalias.entity.Orders;
import co.com.egonzalias.entity.Products;
import co.com.egonzalias.entity.Users;
import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.repository.OrderProductRepository;
import co.com.egonzalias.repository.OrderRepository;
import co.com.egonzalias.repository.ProductRepository;
import co.com.egonzalias.repository.UserRepository;
import co.com.egonzalias.service.impl.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ======================================
    // createOrder
    // ======================================
    @Test
    void createOrder_shouldReturnSavedOrder() {
        Users user = new Users();
        user.setId(1L);

        Products p1 = new Products();
        p1.setId(1L);
        p1.setPrice(BigDecimal.valueOf(100));

        Products p2 = new Products();
        p2.setId(2L);
        p2.setPrice(BigDecimal.valueOf(50));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(p2));

        Orders savedOrder = new Orders();
        savedOrder.setId(10L);
        savedOrder.setUser(user);
        when(orderRepository.save(any())).thenReturn(savedOrder);

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setUserId(1L);
        dto.setProducts(List.of(
                new co.com.egonzalias.dto.OrderProductDTO(1L, 2),
                new co.com.egonzalias.dto.OrderProductDTO(2L, 1)
        ));

        var response = orderService.createOrder(dto);

        assertEquals(10L, response.getOrderId());
        verify(orderRepository).save(any());
    }

    // ======================================
    // updateOrder
    // ======================================
    @Test
    void updateOrder_shouldUpdateStatusAndProducts() {
        Users user = new Users();
        user.setId(1L);

        Orders order = new Orders();
        order.setUser(user);
        order.setId(1L);
        order.setProducts(new ArrayList<>());

        Products product = new Products();
        product.setId(5L);
        product.setPrice(BigDecimal.valueOf(20));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenReturn(order);

        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setStatus("CANCELLED");
        dto.setProducts(List.of(new co.com.egonzalias.dto.OrderProductDTO(5L, 2)));

        var response = orderService.updateOrder(1L, dto);

        assertEquals("CANCELLED", response.getStatus());
        assertEquals(1, response.getProducts().size());
        assertEquals(40, response.getTotal().intValue());
    }

    // ======================================
    // deleteOrder
    // ======================================
    @Test
    void deleteOrder_shouldCallRepository() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteOrder_shouldThrowWhenNotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        CustomError ex = assertThrows(CustomError.class, () -> orderService.deleteOrder(1L));
        assertEquals("Order with ID 1 not found", ex.getMessage());
    }

}
