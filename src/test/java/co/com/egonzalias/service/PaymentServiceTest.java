package co.com.egonzalias.service;

import co.com.egonzalias.dto.CreatePaymentDTO;
import co.com.egonzalias.dto.PaymentResponseDTO;
import co.com.egonzalias.entity.Orders;
import co.com.egonzalias.entity.Payments;
import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.repository.OrderRepository;
import co.com.egonzalias.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Orders order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Orders();
        order.setId(1L);
        order.setTotal(BigDecimal.valueOf(100));
    }

    @Test
    void createPayment_shouldSavePaymentAndUpdateOrderStatus() {
        CreatePaymentDTO dto = new CreatePaymentDTO();
        dto.setOrderId(1L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setPaymentMethod("CREDIT_CARD");

        Payments savedPayment = new Payments();
        savedPayment.setId(10L);
        savedPayment.setPaymentMethod("CREDIT_CARD");
        savedPayment.setPaidAt(LocalDateTime.now());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any())).thenReturn(savedPayment);
        when(orderRepository.save(any())).thenReturn(order);

        PaymentResponseDTO response = paymentService.createPayment(dto);

        // Verificar que la respuesta tenga los datos correctos
        assertEquals(1L, response.orderId());
        assertEquals(10L, response.paymentId());
        assertEquals("CREDIT_CARD", response.paymentMethod());
        assertNotNull(response.paidAt());

        // Verificar que la orden se actualizó a PAID
        assertEquals("PAID", order.getStatus());

        // Verificar que se llamó a los repositorios
        verify(paymentRepository, times(1)).save(any(Payments.class));
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void createPayment_shouldThrowWhenAmountMismatch() {
        CreatePaymentDTO dto = new CreatePaymentDTO();
        dto.setOrderId(1L);
        dto.setAmount(BigDecimal.valueOf(50)); // Monto incorrecto
        dto.setPaymentMethod("CREDIT_CARD");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        CustomError exception = assertThrows(CustomError.class, () -> paymentService.createPayment(dto));
        assertTrue(exception.getMessage().contains("Amount does not match order total"));

        verify(paymentRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createPayment_shouldThrowWhenOrderNotFound() {
        CreatePaymentDTO dto = new CreatePaymentDTO();
        dto.setOrderId(999L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setPaymentMethod("CREDIT_CARD");

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        CustomError exception = assertThrows(CustomError.class, () -> paymentService.createPayment(dto));
        assertTrue(exception.getMessage().contains("Order not found"));

        verify(paymentRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }
}
