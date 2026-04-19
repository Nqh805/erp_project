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
            "(:kw IS NULL OR :kw = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR p.skuCode LIKE CONCAT('%', :kw, '%') OR p.barCode LIKE CONCAT('%', :kw, '%')) " +
            "AND (:pId IS NULL OR p.category.parent.id = :pId) " +
            "AND (:cId IS NULL OR p.category.id = :cId)")
    List<Product> searchProducts(@Param("kw") String kw,
            @Param("pId") Long pId,
            @Param("cId") Long cId);
}
