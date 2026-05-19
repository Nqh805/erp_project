package com.example.demo.entity.Order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.demo.entity.Partner.Supplier;

@Entity
@Table(name = "purchase_order")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder extends OrderHeader {

    @Column(name = "actual_arrival")
    private LocalDate actualArrival;

    @Column(name = "total_purchase_amount")
    private BigDecimal totalPurchaseAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_result")
    private DeliveryResult deliveryResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount = BigDecimal.ZERO; // Mặc định ban đầu bằng 0

    @Transient
    public BigDecimal getRemainingAmount() {
        BigDecimal total = (this.totalPurchaseAmount != null) ? this.totalPurchaseAmount : BigDecimal.ZERO;
        BigDecimal paid = (this.paidAmount != null) ? this.paidAmount : BigDecimal.ZERO;

        // Trả về kết quả: Còn lại = Tổng - Đã trả
        return total.subtract(paid);
    }
}