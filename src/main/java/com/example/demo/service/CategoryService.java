package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

import org.springframework.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy ngành hàng cha
    public List<Category> getAllParentCategories() {
        return categoryRepository.findByParentIsNull();
    }

    // Lấy loại sản phẩm con
    public List<Category> getAllChildCategories() {
        return categoryRepository.findByParentIsNotNull();
    }

    // Lưu category theo các case
    @Transactional
    public Category childCategoryProcess(Long parentId, Long childId, String newParent, String newChild) {

        // TRƯỜNG HỢP 1: THÊM MỚI CẢ HAI
        if (StringUtils.hasText(newParent) && StringUtils.hasText(newChild)) {
            Category p = new Category();
            p.setName(newParent);
            p = categoryRepository.save(p); // Lưu cha

            Category c = new Category();
            c.setName(newChild);
            c.setParent(p); // Gán cha vừa tạo
            return categoryRepository.save(c); // Trả về con
        }

        // TRƯỜNG HỢP 2: NGÀNH HÀNG CŨ - LOẠI SẢN PHẨM MỚI
        if (parentId != null && StringUtils.hasText(newChild)) {
            Category p = categoryRepository.findById(parentId).orElse(null);
            Category c = new Category();
            c.setName(newChild);
            c.setParent(p);
            return categoryRepository.save(c);
        }

        // TRƯỜNG HỢP 3: CHỌN CÓ SẴN (CẢ CHA VÀ CON)
        if (childId != null) {
            return categoryRepository.findById(childId).orElse(null);
        }

        return null;
    }
}