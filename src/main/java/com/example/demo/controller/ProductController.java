package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductStatus;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/products") // localhost:8080/products
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // danh sách sản phẩm
    @GetMapping("/view")
    public String viewProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Long childId,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Model model) {

        // 1. Thực hiện lọc và lấy đối tượng Page (Đặt tên là pageData cho rõ nghĩa)
        Page<Product> pageData = productService.findProduct(keyword, parentId, childId, minPrice, maxPrice, sortBy,
                direction, page);

        // 2. Gửi dữ liệu phân trang về cho View
        model.addAttribute("products", pageData.getContent()); // Lấy danh sách 10 sản phẩm thực tế
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("totalItems", pageData.getTotalElements());

        // 3. Trạng thái sắp xếp
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        // 4. Đổ dữ liệu cho các bộ lọc (Dropdowns)
        model.addAttribute("parentCategories", categoryService.getAllParentCategories());
        model.addAttribute("childCategories", categoryService.getAllChildCategories());

        // 5. Giữ lại các giá trị đã nhập để không bị mất khi reload trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedParent", parentId);
        model.addAttribute("selectedChild", childId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "product_list";
    }

    // Hiển thị category lên form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Category> parentCategories = categoryService.getAllParentCategories();
        List<Category> children = categoryService.getAllChildCategories();

        model.addAttribute("parentCategories", parentCategories);
        model.addAttribute("childCategories", children);
        model.addAttribute("allStatuses", ProductStatus.values());
        model.addAttribute("product", new Product());

        return "add_product";
    }

    // xử lý data form sản phẩm
    @PostMapping("/add") // localhost:8080/products/add
    public String addProduct(RedirectAttributes redirectAttributes,
            @ModelAttribute Product product, // gan du lieu vao product neu name trong form trung voi thuoc tinh
            Model model,
            @RequestParam(value = "parentCategory", required = false) Long parentId,
            @RequestParam(value = "productType", required = false) Long childId,
            @RequestParam(value = "inputNewCategory", required = false) String newParentName,
            @RequestParam(value = "inputNewType", required = false) String newTypeName,
            @RequestParam(value = "productImage", required = false) MultipartFile imageFile) {
        try {
            // Goi service xu ly category
            Category finalChildCategory = categoryService.childCategoryProcess(parentId, childId, newParentName,
                    newTypeName);
            product.setCategory(finalChildCategory);

            productService.saveProduct(product, imageFile);

            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm!");
            return "redirect:/products/view";

        } catch (RuntimeException e) {
            // gui message ve view thong qua model
            model.addAttribute("errorMessage", e.getMessage());

            // load lai data
            model.addAttribute("parentCategories",
                    categoryService.getAllParentCategories());
            model.addAttribute("childCategories",
                    categoryService.getAllChildCategories());
            model.addAttribute("allStatuses", ProductStatus.values());

            return "add_product";
        }
    }

    // fill du lieu cho form edit
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getById(id);

        if (product == null) {
            return "redirect:/products/view"; // load lai data cho trang view neu ko tim thay san pham de edit
        }

        List<Category> parentCategories = categoryService.getAllParentCategories();
        List<Category> children = categoryService.getAllChildCategories();

        model.addAttribute("parentCategories", parentCategories);
        model.addAttribute("childCategories", children);
        model.addAttribute("allStatuses", ProductStatus.values());
        model.addAttribute("product", product);

        return "add_product";
    }

    // update san pham
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("product") Product product,
            @RequestParam(value = "productImage", required = false) MultipartFile imageFile,
            @RequestParam(value = "parentCategory", required = false) Long parentId,
            @RequestParam(value = "productType", required = false) Long childId,
            @RequestParam(value = "inputNewCategory", required = false) String newParentName,
            @RequestParam(value = "inputNewType", required = false) String newTypeName) {

        product.setId(id);

        // throw redirect success message
        try {
            Category finalCategory = categoryService.childCategoryProcess(parentId, childId, newParentName,
                    newTypeName);
            product.setCategory(finalCategory);
            productService.updateProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật thông tin sản phẩm!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể cập nhật thông tin sản phẩm: " + e.getMessage());
        }
        return "redirect:/products/view";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/products/view";
    }
}
