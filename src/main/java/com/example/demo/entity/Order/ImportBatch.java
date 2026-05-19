package com.example.demo.entity.Order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.demo.entity.Product.Product;
import com.example.demo.entity.Warehouse.WareHouse;
import com.example.demo.entity.Warehouse.WareHouseLocation;

@Entity
@Table(name = "import_batch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "batch_code", unique = true)
    private String batchCode;

    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;

    @Column(name = "quantity_on_hand")
    private Integer quantityOnHand;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    @Column(name = "unallocated_quantity")
    private Integer unallocatedQuantity = 0;

    @Column(name = "import_price")
    private BigDecimal importPrice;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ware_house_id")
    private WareHouse wareHouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private WareHouseLocation location;
}