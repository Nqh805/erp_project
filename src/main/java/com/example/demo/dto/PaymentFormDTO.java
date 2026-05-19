package com.example.demo.dto;

import com.example.demo.entity.Order.PaymentMethod;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Data
public class PaymentFormDTO {
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String referenceCode;
    private String note;

    // Biến này để hứng file ảnh đính kèm từ Form HTML
    private MultipartFile receiptImage;
}