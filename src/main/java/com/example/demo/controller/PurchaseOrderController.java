package com.example.demo.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.dto.PaymentFormDTO;
import com.example.demo.dto.PaymentTransactionDTO;
import com.example.demo.dto.ReceiptFormDTO;
import com.example.demo.entity.Order.PurchaseOrder;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.PaymentTransactionRepository;
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.service.PaymentService;
import com.example.demo.service.PurchaseOrderService;

@Controller
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseOrderController {

    // Inject Repository trực tiếp (Nếu dự án lớn, bạn nên bọc qua tầng
    // PurchaseOrderService)
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PurchaseOrderService purchaseOrderService;
    private final PaymentService paymentService;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @GetMapping("/view")
    public String viewPurchaseOrders(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model) {

        // Cố định số lượng hiển thị trên 1 trang là 10 đơn hàng
        int pageSize = 10;

        // Tạo đối tượng phân trang, sắp xếp giảm dần theo ngày tạo (Đơn mới nhất lên
        // đầu)
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<PurchaseOrder> purchaseOrderPage;

        // Xử lý logic tìm kiếm
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Nếu có từ khóa, gọi hàm search
            purchaseOrderPage = purchaseOrderRepository.searchByKeyword(keyword.trim(), pageable);
            model.addAttribute("keyword", keyword.trim());
        } else {
            // Nếu không có từ khóa, lấy toàn bộ danh sách
            purchaseOrderPage = purchaseOrderRepository.findAll(pageable);
            model.addAttribute("keyword", "");
        }

        // Đẩy dữ liệu sang Thymeleaf
        model.addAttribute("purchaseOrders", purchaseOrderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", purchaseOrderPage.getTotalPages());

        // Nếu có thông báo từ RedirectAttributes (Ví dụ: Thêm/Sửa/Xóa thành công),
        // Spring Boot sẽ tự động đẩy nó vào Model, ta không cần khai báo tay ở đây.

        return "purchase_list";
    }

    @PostMapping("/get-order-details")
    @ResponseBody
    public List<OrderDetailDTO> getOrderDetails(@RequestParam Long poId) {
        // Trả thẳng về danh sách Object/DTO của Java
        return orderDetailRepository.findByOrderHeaderId(poId)
                .stream()
                .map(OrderDetailDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/receive/{id}")
    public String receivePurchaseOrder(@PathVariable("id") Long poId,
            @ModelAttribute ReceiptFormDTO formDTO,
            RedirectAttributes redirectAttributes) {
        try {
            // Gọi tầng Service xử lý toàn bộ Business Logic
            purchaseOrderService.receiveOrderAndGenerateBatches(poId, formDTO);

            redirectAttributes.addFlashAttribute("successMessage", "Xác nhận nhận hàng và sinh lô tự động thành công!");
        } catch (Exception e) {
            // Bắt lỗi nếu có ngoại lệ (ví dụ: không tìm thấy PO)
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi nhận hàng: " + e.getMessage());
        }

        return "redirect:/purchases/view";
    }

    // Hàm nhận dữ liệu thanh toán từ Form Modal
    @PostMapping("/pay/{id}")
    public String processPayment(@PathVariable("id") Long poId,
            @ModelAttribute PaymentFormDTO paymentFormDTO,
            RedirectAttributes redirectAttributes) {
        try {
            paymentService.processPayment(poId, paymentFormDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Ghi nhận thanh toán thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi thanh toán: " + e.getMessage());
        }
        return "redirect:/purchases/view";
    }

    // API LẤY LỊCH SỬ THANH TOÁN
    @GetMapping("/get-payment-history")
    @ResponseBody
    public List<PaymentTransactionDTO> getPaymentHistory(@RequestParam Long poId) {
        return paymentTransactionRepository.findByPurchaseOrderIdOrderByPaymentDateDesc(poId)
                .stream()
                .map(PaymentTransactionDTO::new)
                .collect(Collectors.toList());
    }
}