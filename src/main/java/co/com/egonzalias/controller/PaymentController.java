package co.com.egonzalias.controller;

import co.com.egonzalias.dto.CreatePaymentDTO;
import co.com.egonzalias.dto.PaymentResponseDTO;
import co.com.egonzalias.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponseDTO> payOrder(@RequestBody CreatePaymentDTO dto) {
        PaymentResponseDTO response = paymentService.createPayment(dto);
        return ResponseEntity.ok(response);
    }
}

