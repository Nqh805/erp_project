package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Warehouse.LocationStatus;
import com.example.demo.entity.Warehouse.WareHouseLocation;

import java.util.List;

@Repository
public interface WareHouseLocationRepository extends JpaRepository<WareHouseLocation, Long> {

    // --- Các hàm cũ ---
    List<WareHouseLocation> findByWareHouseIdAndStatus(Long wareHouseId, LocationStatus status);

    List<WareHouseLocation> findByStatus(LocationStatus status);

    // =========================================================================
    // CÁC HÀM TRUY VẤN CASCADING (KHO -> KỆ -> TẦNG -> Ô)
    // =========================================================================

    /**
     * 1. Lấy danh sách tên Kệ (Shelf) không trùng lặp theo Kho
     */
    @Query("SELECT DISTINCT w.shelfName FROM WareHouseLocation w " +
            "WHERE w.wareHouse.id = :warehouseId AND w.status = :status " +
            "ORDER BY w.shelfName ASC")
    List<String> findDistinctShelfNames(
            @Param("warehouseId") Long warehouseId,
            @Param("status") LocationStatus status);

    /**
     * 2. Lấy danh sách tên Tầng (Tier) không trùng lặp theo Kho và Kệ
     */
    @Query("SELECT DISTINCT w.tierName FROM WareHouseLocation w " +
            "WHERE w.wareHouse.id = :warehouseId " +
            "AND w.shelfName = :shelfName " +
            "AND w.status = :status " +
            "ORDER BY w.tierName ASC")
    List<String> findDistinctTierNames(
            @Param("warehouseId") Long warehouseId,
            @Param("shelfName") String shelfName,
            @Param("status") LocationStatus status);

    /**
     * 3. Lấy danh sách các Ô (Bin) cuối cùng
     * (Trả về nguyên object để lấy được ID của location lưu vào Batch)
     */
    @Query("SELECT w FROM WareHouseLocation w " +
            "WHERE w.wareHouse.id = :warehouseId " +
            "AND w.shelfName = :shelfName " +
            "AND w.tierName = :tierName " +
            "AND w.status = :status " +
            "ORDER BY w.binName ASC")
    List<WareHouseLocation> findAvailableBins(
            @Param("warehouseId") Long warehouseId,
            @Param("shelfName") String shelfName,
            @Param("tierName") String tierName,
            @Param("status") LocationStatus status);
}