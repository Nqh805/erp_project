package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Order.ImportBatch;

@Repository
public interface ImportBatchRepository extends JpaRepository<ImportBatch, Long> {
    boolean existsByBatchCode(String batchCode);

    // Tính tổng số lượng tồn kho (OnHand) của một sản phẩm dựa trên tất cả các lô
    // hàng
    @Query("SELECT SUM(b.quantityOnHand) FROM ImportBatch b WHERE b.product.id = :productId")
    Integer sumQuantityByProductId(@Param("productId") Long productId);
}