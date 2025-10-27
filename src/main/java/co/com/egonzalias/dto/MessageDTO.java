package co.com.egonzalias.dto;

import java.math.BigDecimal;

public record MessageDTO (
        String idLoanRequest,
        String fullName,
        String status,
        String email,
        BigDecimal loanAmount){
}
