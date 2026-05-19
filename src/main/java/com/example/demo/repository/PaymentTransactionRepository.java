package com.example.demo.repository;

import com.example.demo.entity.Order.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    // Hàm này hỗ trợ nếu sau này bạn muốn hiện lịch sử nộp tiền trong Modal Chi
    // tiết
    List<PaymentTransaction> findByPurchaseOrderIdOrderByPaymentDateDesc(Long purchaseOrderId);
}