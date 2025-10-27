package co.com.egonzalias.service.impl.impl;

import co.com.egonzalias.dto.CreatePaymentDTO;
import co.com.egonzalias.dto.MessageDTO;
import co.com.egonzalias.dto.PaymentResponseDTO;
import co.com.egonzalias.entity.Orders;
import co.com.egonzalias.entity.Payments;
import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.repository.OrderRepository;
import co.com.egonzalias.repository.PaymentRepository;
import co.com.egonzalias.service.impl.PaymentService;
import co.com.egonzalias.service.impl.SqsService;
import co.com.egonzalias.util.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SqsService sqsService;

    @Value("${aws.queue.name}")
    private String awsQueueName;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository, SqsService sqsService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.sqsService = sqsService;
    }

    @Transactional
    @Override
    public PaymentResponseDTO createPayment(CreatePaymentDTO dto) {
        Orders order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new CustomError("Order not found: " + dto.getOrderId()));

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

        order.setStatus(OrderStatus.PAID.name());
        orderRepository.save(order);
        MessageDTO messageDTO = new MessageDTO(
                String.valueOf(dto.getOrderId()),
                "",
                OrderStatus.PAID.name(),
                "egonzalias@gmail.com",
                savedPayment.getAmount()
        );
        sqsService.sendMessage(messageDTO, awsQueueName);

        return new PaymentResponseDTO(
                order.getId(),
                savedPayment.getId(),
                savedPayment.getPaymentMethod(),
                savedPayment.getPaidAt()
        );
    }
}
