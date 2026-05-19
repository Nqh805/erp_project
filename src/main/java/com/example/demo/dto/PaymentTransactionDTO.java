package com.example.demo.dto;

import com.example.demo.entity.Order.PaymentTransaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentTransactionDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String referenceCode;
    private String note;
    private String receiptImageUrl;
    private String employeeName;

    public PaymentTransactionDTO(PaymentTransaction pt) {
        this.id = pt.getId();
        this.amount = pt.getAmount();
        this.paymentDate = pt.getPaymentDate();
        this.paymentMethod = pt.getPaymentMethod() != null ? pt.getPaymentMethod().name() : "";
        this.referenceCode = pt.getReferenceCode();
        this.note = pt.getNote();
        this.receiptImageUrl = pt.getReceiptImageUrl();

        if (pt.getEmployee() != null) {
            this.employeeName = pt.getEmployee().getFullName();
        } else {
            this.employeeName = "Hệ thống";
        }
    }
}