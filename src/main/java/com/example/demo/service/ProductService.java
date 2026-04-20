package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

//service: chua logic xu ly du lieu, goi repository de tuong tac database, tra ve ket qua cho controller
@Service
public class ProductService {

    @Autowired // goi dependency ProductRepository
    private ProductRepository productRepository;

    public Page<Product> findProduct(String keyword, Long parentId, Long childId,
            Double minPrice, Double maxPrice,
            String sortBy, String direction, int pageNum) {

        // 1. Tiền xử lý dữ liệu
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        Long pId = (parentId != null && parentId > 0) ? parentId : null;
        Long cId = (childId != null && childId > 0) ? childId : null;

        // 2. Xử lý logic Sắp xếp (Sorting)
        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Xử lý riêng cho status nếu Huy đã áp dụng logic sort theo Enum Ordinal
        Sort sort;
        if ("status".equals(sortBy)) {
            sort = Sort.by(dir, "status");
        } else if (sortBy != null && !sortBy.isEmpty()) {
            sort = Sort.by(dir, sortBy);
        } else {
            sort = Sort.by(Sort.Direction.DESC, "id"); // Mặc định ID giảm dần
        }

        // 3. Khởi tạo Pageable (pageNum truyền vào từ Controller thường là 1, 2, 3...
        // nhưng Spring tính từ 0 nên phải -1)
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        // 4. Gọi Repository trả về Page thay vì List
        return productRepository.searchProducts(cleanKeyword, pId, cId, minPrice, maxPrice, pageable);
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    private String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        try {
            String uploadDir = "src/main/resources/static/asset/uploads/";
            java.io.File directory = new java.io.File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = imageFile.getOriginalFilename();
            java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);

            // neu url da ton tai
            if (java.nio.file.Files.exists(filePath)) {
                return "/asset/uploads/" + fileName;
            }
            // luu moi
            java.nio.file.Files.copy(imageFile.getInputStream(), filePath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            return "/asset/uploads/" + fileName;

        } catch (java.io.IOException e) {
            throw new RuntimeException("Lỗi hệ thống khi lưu file ảnh: " + e.getMessage());
        }
    }

    // Lưu sản phẩm mới vào database
    public void saveProduct(Product product, MultipartFile imageFile) {
        // 1. Kiểm tra SKU
        if (product.getSkuCode() != null && productRepository.existsBySkuCode(product.getSkuCode())) {
            throw new RuntimeException("Mã SKU '" + product.getSkuCode() + "' đã tồn tại!");
        }

        // 2. Kiểm tra Barcode
        if (product.getBarCode() != null && productRepository.existsByBarCode(product.getBarCode())) {
            throw new RuntimeException("Mã Vạch '" + product.getBarCode() + "' đã bị trùng!");
        }
        // 3. Xử lý lưu ảnh
        String imageUrl = saveImage(imageFile);
        if (imageUrl != null) {
            product.setImgURL(imageUrl);
        }

        productRepository.save(product);

    }

    public void updateProduct(Product product, MultipartFile imageFile) {
        // 1. Lấy dữ liệu cũ từ DB (BẮT BUỘC)
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

        // 2. Check trùng SKU (Chỉ check nếu user thay đổi SKU mới)
        if (!existingProduct.getSkuCode().equals(product.getSkuCode())) {
            if (productRepository.existsBySkuCode(product.getSkuCode())) {
                throw new RuntimeException("Mã SKU mới '" + product.getSkuCode() + "' đã bị trùng!");
            }
        }

        // 3. Check trùng Barcode (Tương tự SKU)
        if (!existingProduct.getBarCode().equals(product.getBarCode())) {
            if (productRepository.existsByBarCode(product.getBarCode())) {
                throw new RuntimeException("Mã Vạch mới '" + product.getBarCode() + "' đã bị trùng!");
            }
        }

        // 4. Logic xử lý ảnh cho Update
        if (imageFile != null && !imageFile.isEmpty()) {
            // Nếu có ảnh mới -> Lưu ảnh mới và cập nhật URL
            String newFileName = saveImage(imageFile); // Tách code lưu ảnh ra hàm riêng cho sạch
            existingProduct.setImgURL(newFileName);
        }
        // 4. Cập nhật các thông tin khác
        existingProduct.setName(product.getName());
        existingProduct.setBasePrice(product.getBasePrice());
        existingProduct.setQuantityStock(product.getQuantityStock());
        existingProduct.setSkuCode(product.getSkuCode());
        existingProduct.setBarCode(product.getBarCode());
        existingProduct.setReservedQuantity(product.getReservedQuantity());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setStatus(product.getStatus());
        existingProduct.setCategory(product.getCategory());

        // 5. Lưu đối tượng đã cập nhật
        productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại.");
        }
    }
}