package com.example.demo.entity.Warehouse;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ N-1: Nhiều vị trí thuộc về 1 kho
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ware_house_id", nullable = false)
    private WareHouse wareHouse;

    @Column(name = "shelf_name", nullable = false)
    private String shelfName;

    @Column(name = "tier_name", nullable = false)
    private String tierName;

    @Column(name = "bin_name", nullable = false)
    private String binName;

    @Column(name = "location_code", unique = true, nullable = false)
    private String locationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LocationStatus status = LocationStatus.EMPTY;

    @CreationTimestamp // Hibernate tự động lấy giờ hiện tại khi tạo mới (INSERT)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}