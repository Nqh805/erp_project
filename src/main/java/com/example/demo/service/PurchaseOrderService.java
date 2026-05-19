package com.example.demo.service;

import com.example.demo.dto.ReceiptFormDTO;
import com.example.demo.entity.Order.DeliveryResult;
import com.example.demo.entity.Order.DeliveryStatus;
import com.example.demo.entity.Order.ImportBatch;
import com.example.demo.entity.Order.OrderDetail;
import com.example.demo.entity.Order.PurchaseOrder;
import com.example.demo.repository.ImportBatchRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ImportBatchRepository importBatchRepository;

    // Đánh dấu Transactional để đảm bảo toàn vẹn dữ liệu
    @Transactional
    public void receiveOrderAndGenerateBatches(Long poId, ReceiptFormDTO formDTO) {
        // 1. Tìm đơn hàng
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng mã: " + poId));

        // validate start
        if (formDTO.getActualArrival() != null && po.getCreatedAt() != null) {
            LocalDate dateCreated = po.getCreatedAt().toLocalDate();

            // Nếu ngày thực tế nhỏ hơn ngày tạo đơn thì quăng lỗi chặn lại liền
            if (formDTO.getActualArrival().isBefore(dateCreated)) {
                throw new IllegalArgumentException(
                        "Lỗi nghiệp vụ: Ngày tới thực tế không được nhỏ hơn ngày tạo đơn hàng (" +
                                dateCreated.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
            }
        }
        // validate end

        LocalDate actualArrival = formDTO.getActualArrival();
        po.setActualArrival(actualArrival);

        // 2. Logic tự động đối chiếu để ra "Kết quả giao"
        if (po.getExpectedArrival() != null && actualArrival != null) {
            if (actualArrival.isEqual(po.getExpectedArrival())) {
                po.setDeliveryResult(DeliveryResult.ON_TIME); // Đúng hẹn
            } else if (actualArrival.isBefore(po.getExpectedArrival())) {
                po.setDeliveryResult(DeliveryResult.EARLY); // Giao sớm
            } else {
                po.setDeliveryResult(DeliveryResult.LATE); // Giao trễ
            }
        }
        po.setReceivedNote(formDTO.getReceivedNote());
        po.setDeliveryStatus(DeliveryStatus.DELIVERED);

        // 3. Lấy danh sách chi tiết
        List<OrderDetail> details = orderDetailRepository.findByOrderHeaderId(poId);
        BigDecimal newTotalAmount = BigDecimal.ZERO;

        // 4. Xử lý từng dòng sản phẩm
        for (OrderDetail detail : details) {
            Integer actualQty = formDTO.getActualQuantities().get(detail.getId());
            if (actualQty == null)
                actualQty = 0;

            detail.setActualQuantity(actualQty);

            // Tính tiền
            BigDecimal lineTotal = detail.getUnitPrice()
                    .multiply(BigDecimal.valueOf(actualQty))
                    .subtract(detail.getDiscountAmount() != null ? detail.getDiscountAmount() : BigDecimal.ZERO);
            newTotalAmount = newTotalAmount.add(lineTotal);

            // Sinh lô hàng
            if (actualQty > 0) {
                ImportBatch batch = new ImportBatch();

                String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
                batch.setBatchCode("BAT-" + datePart + "-" + detail.getId());

                batch.setManufacturingDate(LocalDate.now());
                batch.setProduct(detail.getProduct());
                batch.setPurchaseOrder(po);
                batch.setWareHouse(po.getWareHouse());
                batch.setImportPrice(detail.getUnitPrice());

                batch.setQuantityOnHand(actualQty);
                batch.setQuantityAvailable(actualQty);
                batch.setUnallocatedQuantity(actualQty);

                importBatchRepository.save(batch);
                detail.setImportBatch(batch);
            }
        }

        // 5. Cập nhật lại tổng tiền và lưu DB
        po.setTotalPurchaseAmount(newTotalAmount);

        orderDetailRepository.saveAll(details);
        purchaseOrderRepository.save(po);
    }
}