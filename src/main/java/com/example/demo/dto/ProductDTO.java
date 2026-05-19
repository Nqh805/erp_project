package com.example.demo.dto;

import lombok.Data;
import com.example.demo.entity.Product.Product;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private String sku;
    private String barcode;
    private String unit;

    public ProductDTO(Product product) {
        if (product != null) {
            this.id = product.getId();
            this.name = product.getName();
            this.brand = product.getBrand();
            this.sku = product.getSkuCode();
            this.barcode = product.getBarCode();
            this.unit = product.getUnit();
        }
    }
}