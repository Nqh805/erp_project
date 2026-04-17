package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Lấy tất cả Ngành hàng cha (những dòng có parent_id là NULL)
    List<Category> findByParentIsNull();

    // Lấy category con (những dòng có parent_id khác NULL)
    List<Category> findByParentIsNotNull();

    // Lấy một Category theo ID
    Optional<Category> findById(Long id);

    // Lấy tất cả Loại sản phẩm thuộc một Ngành hàng cha cụ thể
    List<Category> findByParentId(Integer parentId);

    // Tìm kiếm nhanh theo tên (để kiểm tra trùng lặp trước khi thêm mới)
    Optional<Category> findByName(String name); // tra ve category, hoac empty
}