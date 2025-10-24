package co.com.egonzalias.service.impl;

import co.com.egonzalias.dto.CreatePaymentDTO;
import co.com.egonzalias.dto.PaymentResponseDTO;


public interface PaymentService {
    PaymentResponseDTO createPayment(CreatePaymentDTO dto);
}

