package com.example.demo.controller;

import com.example.demo.entity.Partner.Supplier;
import com.example.demo.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // API danh sách
    @GetMapping("/view")
    public String viewSuppliers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Model model) {

        // 1. Lấy dữ liệu phân trang và tìm kiếm từ Service
        Page<Supplier> pageData = supplierService.findSuppliers(keyword, page, sortBy, direction);

        // 2. Đổ dữ liệu vào Model cho giao diện
        model.addAttribute("suppliers", pageData.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("totalItems", pageData.getTotalElements());

        // 3. Giữ trạng thái sắp xếp và tìm kiếm trên thanh điều hướng
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return "supplier_list"; // Trả về file HTML bạn vừa tạo
    }
}