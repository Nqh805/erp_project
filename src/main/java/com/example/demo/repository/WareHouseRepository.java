package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Warehouse.WareHouse;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
}