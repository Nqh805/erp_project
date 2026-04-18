package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySkuCode(String skuCode);

    boolean existsByBarCode(String barCode);

    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(p.skuCode) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "p.barCode LIKE CONCAT('%', :kw, '%')")
    List<Product> searchProducts(@Param("kw") String keyword);
}
