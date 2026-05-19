package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product.Category;
import com.example.demo.repository.CategoryRepository;

import org.springframework.util.StringUtils;
import jakarta.transaction.Transactional;

@Service // goi Repo de xu ly logic
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy ngành hàng cha
    public List<Category> getAllParentCategories() {
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> getAllChildCategories() {
        return categoryRepository.findByParentIsNotNull();
    }

    // Lấy loại sản phẩm con theo parent ID
    public List<Category> getChildCategoriesByParentId(Long parentId) {
        Category parent = categoryRepository.findById(parentId).orElse(null);
        if (parent == null)
            return List.of();
        return categoryRepository.findByParent(parent);
    }

    // validate theo cac case trong repo
    private void validateCategoryNames(String parentName, String childName, Category parent) {
        // validate nganh hang
        if (StringUtils.hasText(parentName)) {
            if (categoryRepository.existsByNameAndParentIsNull(parentName))
                throw new RuntimeException("Ngành hàng '" + parentName + "' đã tồn tại!");
            if (categoryRepository.existsByNameAndParentIsNotNull(parentName))
                throw new RuntimeException("Tên '" + parentName + "' đã được dùng cho một loại sản phẩm!");
        }

        // validate loai san pham
        if (StringUtils.hasText(childName)) {
            if (categoryRepository.existsByNameAndParentIsNull(childName))
                throw new RuntimeException("Tên '" + childName + "' đã được dùng cho một ngành hàng!");
            if (parent != null && categoryRepository.existsByNameAndParent(childName, parent))
                throw new RuntimeException("Loại sản phẩm '" + childName + "' đã tồn tại trong ngành hàng này!");
        }
    }

    @Transactional
    public Category childCategoryProcess(Long parentId, Long childId, String newParent, String newChild) {

        // TRƯỜNG HỢP 1: THÊM MỚI CẢ HAI
        if (StringUtils.hasText(newParent) && StringUtils.hasText(newChild)) {
            validateCategoryNames(newParent, null, null); // validate cha vừa tạo trước khi new đối tượng
            Category p = new Category();
            p.setName(newParent);
            p = categoryRepository.save(p);

            validateCategoryNames(null, newChild, p); // validate ca 2
            Category c = new Category();
            c.setName(newChild);
            c.setParent(p);
            return categoryRepository.save(c);
        }

        // TRƯỜNG HỢP 2: NGÀNH HÀNG CŨ - LOẠI SẢN PHẨM MỚI
        if (parentId != null && StringUtils.hasText(newChild)) {
            Category p = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Ngành hàng không tồn tại!"));

            validateCategoryNames(null, newChild, p); // validate con với cha có sẵn
            Category c = new Category();
            c.setName(newChild);
            c.setParent(p);
            return categoryRepository.save(c);
        }

        // TRƯỜNG HỢP 3: CHỌN CÓ SẴN
        if (childId != null) {
            return categoryRepository.findById(childId)
                    .orElseThrow(() -> new RuntimeException("Loại sản phẩm không tồn tại!"));
        }

        throw new RuntimeException("Vui lòng chọn hoặc nhập loại sản phẩm!");
    }
}