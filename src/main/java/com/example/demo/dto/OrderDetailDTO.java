package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import com.example.demo.entity.Order.OrderDetail;

@Data
public class OrderDetailDTO {
    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private Integer actualQuantity;
    private Integer putawayQuantity;
    private BigDecimal discount;
    private String warehouse;

    // Đối tượng ProductDTO lồng bên trong
    private ProductDTO product;

    public OrderDetailDTO(OrderDetail detail) {
        this.id = detail.getId();
        this.price = detail.getUnitPrice();
        this.quantity = detail.getQuantity();
        this.actualQuantity = detail.getActualQuantity() != null ? detail.getActualQuantity() : 0;
        this.discount = detail.getDiscountAmount() != null ? detail.getDiscountAmount() : BigDecimal.ZERO;

        // Tự động map đối tượng con
        this.product = new ProductDTO(detail.getProduct());

        // Lấy thông tin kho từ bảng cha OrderHeader sau khi bạn đã dời cột
        // ware_house_id
        if (detail.getOrderHeader() != null && detail.getOrderHeader().getWareHouse() != null) {
            this.warehouse = detail.getOrderHeader().getWareHouse().getName();
        } else {
            this.warehouse = "N/A";
        }

        // Tính toán số lượng đã cất dựa theo unallocated_quantity của Lô hàng
        if (detail.getImportBatch() != null && detail.getImportBatch().getUnallocatedQuantity() != null) {
            this.putawayQuantity = this.actualQuantity - detail.getImportBatch().getUnallocatedQuantity();
        } else {
            this.putawayQuantity = 0;
        }
    }
}