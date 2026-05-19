// Hàm mở Modal Chi tiết Đơn hàng (Đã loại bỏ edit link, thêm trường hiển thị kho)
function openPurchaseDetailModal(row) {
    const poId = row.getAttribute('data-id');

    // 1. Đổ dữ liệu các trường thông tin chung lên Header Modal
    document.getElementById('detailPoCode').textContent = row.getAttribute('data-code');
    document.getElementById('detailPoName').textContent = row.getAttribute('data-name');
    document.getElementById('detailPoSupplier').textContent = row.getAttribute('data-supplier');
    document.getElementById('detailPoWarehouse').textContent = row.getAttribute('data-warehouse');
    document.getElementById('detailPoCreated').textContent = row.getAttribute('data-created');
    document.getElementById('detailPoExpected').textContent = row.getAttribute('data-expected');
    document.getElementById('detailPoNote').textContent = row.getAttribute('data-note') || 'Không có ghi chú';
    document.getElementById('detailPoReceivedNote').textContent = row.getAttribute('data-received-note') || 'Chưa có ghi chú nhận hàng';
    document.getElementById('detailPoActualArrival').textContent = row.getAttribute('data-actual') || 'Chưa nhận';
    
    let result = row.getAttribute('data-result');
    let resultHtml = '<span class="text-muted">Chưa kiểm đếm</span>';
    if (result === 'EARLY') resultHtml = '<span class="badge bg-info">Giao sớm</span>';
    else if (result === 'LATE') resultHtml = '<span class="badge bg-warning text-dark">Giao trễ</span>';
    document.getElementById('detailPoResult').innerHTML = resultHtml;

    let amount = parseFloat(row.getAttribute('data-amount') || 0);
    document.getElementById('detailPoAmount').textContent = amount.toLocaleString('vi-VN') + ' đ';

    let paid = parseFloat(row.getAttribute('data-paid') || 0);
    let remaining = parseFloat(row.getAttribute('data-remaining') || 0);
    const paidAmountEl = document.getElementById('detailPoPaidAmount');
    const remainingAmountEl = document.getElementById('detailPoRemainingAmount');
    if (paidAmountEl) {
        paidAmountEl.textContent = paid.toLocaleString('vi-VN') + ' đ';
    }
    if (remainingAmountEl) {
        remainingAmountEl.textContent = remaining.toLocaleString('vi-VN') + ' đ';
    }

    let paymentStatus = row.getAttribute('data-payment');
    document.getElementById('detailPoPayment').innerHTML = paymentStatus === 'PAID' 
        ? '<span class="badge bg-success-subtle text-success border border-success">Đã thanh toán</span>' 
        : paymentStatus === 'PARTIAL'
        ? '<span class="badge bg-warning-subtle text-warning border border-warning">Thanh toán một phần</span>'
        : '<span class="badge bg-danger-subtle text-danger border border-danger">Chưa thanh toán</span>';

    let deliveryStatus = row.getAttribute('data-delivery');
    let deliveryHtml = '';
    switch (deliveryStatus) {
        case 'PENDING': deliveryHtml = '<span class="badge bg-secondary">Chờ xử lý</span>'; break;
        case 'IN_TRANSIT': deliveryHtml = '<span class="badge bg-primary">Đang giao</span>'; break;
        case 'DELIVERED': deliveryHtml = '<span class="badge bg-success">Đã nhận hàng</span>'; break;
        case 'FAILED': deliveryHtml = '<span class="badge bg-danger">Thất bại</span>'; break;
    }
    document.getElementById('detailPoDelivery').innerHTML = deliveryHtml;

    const paymentBtn = document.getElementById('detailPaymentBtn');
    if (paymentBtn) {
        // Gắn thêm dữ liệu vào nút thanh toán
        paymentBtn.setAttribute('data-id', poId);
        paymentBtn.setAttribute('data-code', row.getAttribute('data-code'));
        paymentBtn.setAttribute('data-delivery', deliveryStatus);
        paymentBtn.setAttribute('data-total', row.getAttribute('data-amount'));
        paymentBtn.setAttribute('data-paid', row.getAttribute('data-paid'));
        paymentBtn.setAttribute('data-remaining', row.getAttribute('data-remaining'));

        if (paymentStatus === 'PAID') {
            paymentBtn.disabled = true;
            paymentBtn.classList.replace('btn-success', 'btn-outline-success');
            paymentBtn.innerHTML = '<i class="bi bi-check2-circle me-1"></i>Đã thanh toán đủ';
        } else {
            paymentBtn.disabled = false;
            paymentBtn.classList.replace('btn-outline-success', 'btn-success');
            paymentBtn.innerHTML = '<i class="bi bi-cash-coin me-1"></i>Thanh toán';
        }
    }

    // 2. Tạo hiệu ứng xoay Loading trong bảng khi đợi nạp dữ liệu
    let tbody = document.getElementById('detailPoProductsBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="13" class="text-center py-4">
                <div class="spinner-border text-primary" role="status"></div>
                <div class="mt-2 text-muted">Đang tải danh sách sản phẩm thực tế...</div>
            </td>
        </tr>`;

    // 3. Kích hoạt bật hiển thị Modal của Bootstrap lên trước
    var myModal = new bootstrap.Modal(document.getElementById('purchaseDetailModal'));
    myModal.show();

    // 4. KHỚP ROUTE: Tạo Form Data để gửi lên `@RequestParam` của Controller
    const params = new URLSearchParams();
    params.append('poId', poId);

    fetch('/purchases/get-order-details', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded' // Định dạng bắt buộc của RequestParam
        },
        body: params
    })
    .then(response => {
        if (!response.ok) throw new Error("Lỗi kết nối từ server");
        return response.json();
    })
    .then(data => {
        tbody.innerHTML = ''; // Clear dòng loading đi

        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="13" class="text-center text-muted py-4">Không có sản phẩm nào thuộc đơn hàng này.</td></tr>';
            return;
        }

        // 5. Duyệt danh sách DTO lồng nhau (Nested DTO) nhận được từ Java và render
        data.forEach((item, index) => {
            let qtyToCalculate = item.actualQuantity > 0 ? item.actualQuantity : item.quantity;
            let total = (item.price * qtyToCalculate) - item.discount;
            
            let putawayColor = item.putawayQuantity === item.actualQuantity && item.actualQuantity > 0 
                ? "text-success fw-bold" 
                : (item.putawayQuantity > 0 ? "text-warning fw-bold" : "text-danger");

            let tooltipText = item.actualQuantity > 0 ? 'Thành tiền (Thực tế)' : 'Tạm tính (Dựa trên SL đặt)';

            let tr = `
                <tr class="text-center">
                    <td>${index + 1}</td>
                    <td class="text-start fw-semibold text-primary">${item.product.name || 'N/A'}</td>
                    <td>${item.product.brand || ''}</td>
                    <td><code>${item.product.sku || ''}</code></td>
                    <td>${item.product.barcode || ''}</td>
                    <td class="fw-medium text-secondary">${item.warehouse || ''}</td>
                    <td>${item.product.unit || ''}</td>
                    <td class="text-end">${item.price.toLocaleString('vi-VN')} đ</td>
                    <td>${item.quantity}</td>
                    <td class="text-primary fw-bold">${item.actualQuantity}</td>
                    <td class="${putawayColor}">${item.putawayQuantity} / ${item.actualQuantity}</td>
                    <td class="text-end text-danger">-${item.discount.toLocaleString('vi-VN')} đ</td>
                    <td class="text-end fw-bold text-success" title="${tooltipText}">
                        ${total.toLocaleString('vi-VN')} đ
                    </td>
                </tr>
            `;
            tbody.innerHTML += tr;
        });
    })
    .catch(error => {
        console.error("Lỗi Ajax Fetch:", error);
        tbody.innerHTML = `
            <tr>
                <td colspan="13" class="text-center text-danger py-4">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Không thể tải dữ liệu chi tiết sản phẩm. Vui lòng thử lại.
                </td>
            </tr>`;
    });
    // ---------------------------------------------------------
    // BẮT ĐẦU ĐOẠN JS LẤY LỊCH SỬ THANH TOÁN
    // ---------------------------------------------------------
    let historyBody = document.getElementById('detailPoPaymentHistoryBody');
    
    // Hiển thị loading mờ mờ cho bảng lịch sử
    historyBody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-3"><div class="spinner-border spinner-border-sm text-secondary me-2"></div>Đang nạp lịch sử...</td></tr>';

    // Gọi API bằng phương thức GET
    fetch(`/purchases/get-payment-history?poId=${poId}`)
    .then(response => {
        if (!response.ok) throw new Error("Lỗi tải lịch sử");
        return response.json();
    })
    .then(data => {
        historyBody.innerHTML = ''; // Xóa loading

        if (data.length === 0) {
            historyBody.innerHTML = '<tr><td colspan="6" class="text-muted py-3 text-center">Chưa có giao dịch thanh toán nào.</td></tr>';
            return;
        }

        // Lặp dữ liệu vẽ ra bảng
        data.forEach(item => {
            // 1. Format lại ngày tháng từ chuỗi Java (VD: 2026-05-18T10:30:00)
            let dateObj = new Date(item.paymentDate);
            let dateStr = dateObj.toLocaleDateString('vi-VN') + ' ' + dateObj.toLocaleTimeString('vi-VN', {hour: '2-digit', minute:'2-digit'});

            // 2. Format Badge Phương thức
            let methodBadge = '';
            if (item.paymentMethod === 'CASH') methodBadge = '<span class="badge bg-secondary">Tiền mặt</span>';
            else if (item.paymentMethod === 'TRANSFER') methodBadge = '<span class="badge bg-primary">Chuyển khoản</span>';
            else if (item.paymentMethod === 'CREDIT_CARD') methodBadge = '<span class="badge bg-info text-dark">Thẻ tín dụng</span>';

            // 3. Format Nút xem ảnh chứng từ (Nếu có ảnh thì hiện nút, không thì gạch ngang)
            let receiptHtml = item.receiptImageUrl 
                ? `<a href="${item.receiptImageUrl}" target="_blank" class="btn btn-sm btn-outline-primary" title="Xem chứng từ"><i class="bi bi-eye"></i></a>`
                : '<span class="text-muted">-</span>';

            // 4. Lắp ráp HTML
            let tr = `
                <tr class="align-middle text-center">
                    <td class="text-muted">${dateStr}</td>
                    <td class=" fw-bold text-success">${item.amount.toLocaleString('vi-VN')} đ</td>
                    <td>${methodBadge}</td>
                    <td><code>${item.referenceCode || '-'}</code></td>
                    <td class="text-start text-wrap" style="max-width: 200px;">${item.note || '-'}</td>
                    <td>${receiptHtml}</td>
                </tr>
            `;
            historyBody.innerHTML += tr;
        });
    })
    .catch(error => {
        console.error("Lỗi Fetch Lịch sử:", error);
        historyBody.innerHTML = '<tr><td colspan="6" class="text-danger py-3 text-center"><i class="bi bi-exclamation-triangle me-1"></i> Không thể tải dữ liệu.</td></tr>';
    });
    // ---------------------------------------------------------
    // KẾT THÚC ĐOẠN JS LẤY LỊCH SỬ THANH TOÁN
    // ---------------------------------------------------------
}
// Popup Modal nhan hang
function openReceiptModal(row) {
    const poId = row.getAttribute('data-id');
    const poCode = row.getAttribute('data-code');
    const poSupplier = row.getAttribute('data-supplier');
    const poWarehouse = row.getAttribute('data-warehouse');

    //validate ngày nhận hàng thực tế 
    const createdDateStr = row.getAttribute('data-created'); // Ví dụ: "15/05/2026 16:48:43"
    
    if (createdDateStr) {
        // Chuyển đổi định dạng "dd/MM/yyyy HH:mm:ss" sang "yyyy-MM-dd" để gán vào thuộc tính 'min' của thẻ input date
        const parts = createdDateStr.split(' ')[0].split('/');
        const yyyyMmDd = `${parts[2]}-${parts[1]}-${parts[0]}`; // Convert thành "2026-05-15"
        
        // Tìm ô input date trong Modal nhận hàng và thiết lập thuộc tính min
        const arrivalInput = document.querySelector('#receiptModal input[name="actualArrival"]');
        if (arrivalInput) {
            arrivalInput.min = yyyyMmDd;
        }
    }

    // 1. Đổ thông tin chung của đơn vào Header Modal nhận hàng
    document.getElementById('receiptPoCode').textContent = poCode;
    document.getElementById('receiptPoSupplier').textContent = poSupplier;
    document.getElementById('receiptPoWarehouse').textContent = poWarehouse;

    // Cấu hình Action Form để submit đúng ID đơn hàng về Controller xử lý nhận hàng
    document.getElementById('receiptForm').action = '/purchases/receive/' + poId;

    // 2. Tạo hiệu ứng xoay Loading trong bảng khi đợi nạp dữ liệu
    let tbody = document.getElementById('receiptProductsBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="6" class="text-center py-4">
                <div class="spinner-border text-warning" role="status"></div>
                <div class="mt-2 text-muted">Đang tải danh sách sản phẩm để nhận hàng...</div>
            </td>
        </tr>`;

    // 3. Mở Modal Nhận hàng lên trước để tăng trải nghiệm UX
    var rModal = new bootstrap.Modal(document.getElementById('receiptModal'));
    rModal.show();

    // 4. KHỚP ROUTE: Gọi API lấy chi tiết đơn hàng (Giống openPurchaseDetailModal)
    const params = new URLSearchParams();
    params.append('poId', poId);

    fetch('/purchases/get-order-details', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params
    })
    .then(response => {
        if (!response.ok) throw new Error("Lỗi kết nối từ server");
        return response.json();
    })
    .then(data => {
        tbody.innerHTML = ''; // Clear dòng loading đi

        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">Không có sản phẩm nào thuộc đơn hàng này.</td></tr>';
            return;
        }

        // 5. Duyệt danh sách render Input để thủ kho điền số lượng
        data.forEach((item, index) => {
            // Lưu ý: name="actualQuantities[${item.id}]" dùng item.id (ID của chi tiết đơn) để Spring Boot map vào Map/List ở Controller
            let tr = `
                <tr class="text-center">
                    <td>${index + 1}</td>
                    <td class="text-start fw-semibold text-dark">${item.product.name || 'N/A'}</td>
                    <td><code>${item.product.sku || ''}</code></td>
                    <td>${item.product.unit || ''}</td>
                    <td class="fw-bold fs-6 text-secondary">${item.quantity}</td>
                    <td class="bg-warning-subtle">
                        <input type="number" 
                               name="actualQuantities[${item.id}]" 
                               class="form-control form-control-sm text-center fw-semibold border-warning" 
                               min="0" 
                               max="${item.quantity}" 
                               value="${item.quantity}" 
                               required />
                    </td>
                </tr>
            `;
            tbody.innerHTML += tr;
        });
    })
    .catch(error => {
        console.error("Lỗi Ajax Fetch (Receipt):", error);
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center text-danger py-4">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Không thể tải dữ liệu sản phẩm. Vui lòng thử lại.
                </td>
            </tr>`;
    });
}
// Hàm xử lý nút Thanh toán
function handlePaymentClick(btn) {
    const deliveryStatus = btn.getAttribute('data-delivery');

    // Vẫn giữ logic chặn nếu chưa nhận hàng
    if (deliveryStatus === 'PENDING' || deliveryStatus === 'IN_TRANSIT') {
        alert('CẢNH BÁO: Không thể thanh toán!\n\nĐơn hàng này chưa được nhận thực tế tại kho.');
        return; 
    }
    else if (deliveryStatus === 'FAILED') {
        alert('CẢNH BÁO: Không thể thanh toán!\n\nĐơn hàng này đã bị đánh dấu giao thất bại. Vui lòng kiểm tra lại thông tin đơn hàng.');
        return; 
    }

    // 1. Ẩn modal chi tiết đi
    var detailModal = bootstrap.Modal.getInstance(document.getElementById('purchaseDetailModal'));
    if (detailModal) detailModal.hide();

    // 2. Lấy dữ liệu công nợ
    const poId = btn.getAttribute('data-id');
    const total = parseFloat(btn.getAttribute('data-total') || 0);
    const paid = parseFloat(btn.getAttribute('data-paid') || 0);
    const remaining = parseFloat(btn.getAttribute('data-remaining') || 0);

    // 3. Đổ dữ liệu lên Modal Thanh toán
    document.getElementById('payPoCode').textContent = btn.getAttribute('data-code');
    document.getElementById('payTotalAmount').textContent = total.toLocaleString('vi-VN') + ' đ';
    document.getElementById('payPaidAmount').textContent = paid.toLocaleString('vi-VN') + ' đ';
    document.getElementById('payRemainingAmount').textContent = remaining.toLocaleString('vi-VN') + ' đ';
    
    // Mặc định điền số tiền cần thanh toán là số tiền còn nợ
    let inputAmount = document.getElementById('payInputAmount');
    inputAmount.value = remaining;
    inputAmount.max = remaining; // Không cho phép trả dư tiền

    // Cấu hình Form Submit
    document.getElementById('paymentForm').action = '/purchases/pay/' + poId;

    // 4. Hiển thị Modal Thanh toán
    var payModal = new bootstrap.Modal(document.getElementById('paymentModal'));
    payModal.show();
}

// Hàm xử lý nút Xóa
function handleDeleteClick(btn) {
    const deliveryStatus = btn.getAttribute('data-delivery');
    const poId = btn.getAttribute('data-id');

    // Chặn nếu đơn đã bắt đầu quá trình giao nhận
    if (deliveryStatus !== 'PENDING') {
        alert('CẢNH BÁO: Không thể xóa đơn hàng!\n\nĐơn hàng này đang đi đường hoặc đã nhập kho. Chỉ được phép xóa những đơn hàng MỚI TẠO (Đang chờ xử lý).');
        return; // Dừng thực thi
    }

    // Nếu hợp lệ (PENDING) thì mới mở Modal Xác nhận Xóa
    const form = document.getElementById("deleteForm");
    if (form) {
        form.action = "/purchases/delete/" + poId;
        var myModal = new bootstrap.Modal(document.getElementById('deleteModal'));
        myModal.show();
    }
}
// Khởi tạo các thành phần DOM khi load trang hoàn tất
document.addEventListener("DOMContentLoaded", function () {
    // Kích hoạt Tooltip
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    [...tooltipTriggerList].map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl));

    // Truyền ID vào form xác nhận xóa
    const deleteModal = document.getElementById("deleteModal");
    if (deleteModal) {
        deleteModal.addEventListener("show.bs.modal", (event) => {
            const button = event.relatedTarget;
            const id = button.getAttribute("data-id");
            const form = document.getElementById("deleteForm");
            if (form && id) {
                form.action = "/purchases/delete/" + id;
            }
        });
    }
});