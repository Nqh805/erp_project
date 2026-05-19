package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Product.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNull();

    List<Category> findByParentIsNotNull();

    List<Category> findByParentId(Long parentId);

    List<Category> findByParent(Category parent);

    // validate category: ten cha unique, ten con unique trong cha do, ten con khong
    // trung ten cha
    boolean existsByNameAndParentIsNull(String name);

    boolean existsByNameAndParentIsNotNull(String name);

    boolean existsByNameAndParent(String name, Category parent);
}