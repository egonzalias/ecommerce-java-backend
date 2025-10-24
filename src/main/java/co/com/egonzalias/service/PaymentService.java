package co.com.egonzalias.service;

import co.com.egonzalias.dto.CreatePaymentDTO;
import co.com.egonzalias.dto.PaymentResponseDTO;
import co.com.egonzalias.entity.Orders;
import co.com.egonzalias.entity.Payments;
import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.repository.OrderRepository;
import co.com.egonzalias.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentResponseDTO createPayment(CreatePaymentDTO dto) {
        Orders order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new CustomError("Order not found: " + dto.getOrderId()));

        // Validar monto
        if (dto.getAmount().compareTo(order.getTotal()) != 0) {
            throw new CustomError("Amount does not match order total. Order total: " + order.getTotal());
        }

        Payments payment = new Payments();
        payment.setOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaid(true);
        payment.setPaidAt(LocalDateTime.now());

        Payments savedPayment = paymentRepository.save(payment);

        // Actualizar estado de la orden
        order.setStatus("PAID");
        orderRepository.save(order);

        return new PaymentResponseDTO(
                order.getId(),
                savedPayment.getId(),
                savedPayment.getPaymentMethod(),
                savedPayment.getPaidAt()
        );
    }

}

