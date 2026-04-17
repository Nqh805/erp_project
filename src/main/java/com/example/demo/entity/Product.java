package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id tu khoi tao, tu tang
    private Long id;

    private String name;

    private String description;

    @Column(name = "barcode", unique = true, nullable = false)
    private String barCode;

    @Column(name = "sku_code", unique = true, nullable = false)
    private String skuCode;

    @Column(name = "img_url")
    private String imgURL;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "quantity_stock")
    private Integer quantityStock = 0;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;

    @ManyToOne // chi lay id cua category de luu vao product, ko lay toan bo category
    @JoinColumn(name = "category_id") // set id da luu la khoa ngoai toi category
    private Category category;
}
