package co.com.egonzalias.service;

import co.com.egonzalias.dto.CreatePaymentDTO;
import co.com.egonzalias.dto.PaymentResponseDTO;


public interface PaymentService {
    PaymentResponseDTO createPayment(CreatePaymentDTO dto);
}

