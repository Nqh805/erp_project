package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                        "(:kw IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')) " +
                        "OR LOWER(p.skuCode) LIKE LOWER(CONCAT('%', :kw, '%')) " +
                        "OR p.barCode LIKE CONCAT('%', :kw, '%')) " +
                        "AND (:pId IS NULL OR p.category.parent.id = :pId) " +
                        "AND (:cId IS NULL OR p.category.id = :cId) " +
                        "AND (:minP IS NULL OR p.basePrice >= :minP) " +
                        "AND (:maxP IS NULL OR p.basePrice <= :maxP)")
        Page<Product> searchProducts(@Param("kw") String kw,
                        @Param("pId") Long pId,
                        @Param("cId") Long cId,
                        @Param("minP") Double minP,
                        @Param("maxP") Double maxP,
                        Pageable pageable);
}
