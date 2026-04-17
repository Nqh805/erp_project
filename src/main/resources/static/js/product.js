// --- 1. Hàm Toggle (Ngành hàng/Loại sản phẩm) ---
function setupToggle(checkboxId, inputId, selectId) {
    const checkbox = document.getElementById(checkboxId);
    const input = document.getElementById(inputId);
    const select = document.getElementById(selectId);
    
    if (!checkbox || !input || !select) return;

    checkbox.addEventListener('change', function() {
        if (this.checked) {
            input.disabled = false;
            input.focus();
            input.required = true;
            select.required = false;
            select.disabled = true;
            select.value = "";
        } else {
            input.disabled = true;
            input.value = "";
            input.required = false;
            select.required = true;
            select.disabled = false;
        }
    });
}

// --- 2. Hàm sinh mã vạch EAN-13 ---
function generateEAN13() {
    // Tạo 12 số đầu bắt đầu bằng "20"
    let code12 = "20";
    for (let i = 0; i < 10; i++) {
        code12 += Math.floor(Math.random() * 10);
    }

    // Thuật toán tính Check Digit (Modulo 10)
    let sum = 0;
    for (let i = 0; i < 12; i++) {
        let digit = parseInt(code12[i]);
        // Vị trí 1, 3, 5... (index 0, 2, 4) nhân 1
        // Vị trí 2, 4, 6... (index 1, 3, 5) nhân 3
        sum += (i % 2 === 0) ? digit : digit * 3;
    }
    
    let mod = sum % 10;
    let checkDigit = (mod === 0) ? 0 : 10 - mod;
    
    return code12 + checkDigit;
}

// --- 3. Hàm xóa ảnh ---
function removeImage() {
    const inputFile = document.getElementById('productImage');
    const btnRemove = document.getElementById('btnRemoveImage');
    if (inputFile) inputFile.value = "";
    if (btnRemove) btnRemove.classList.add('d-none');
}

// --- KHỞI TẠO TẬP TRUNG ---
document.addEventListener('DOMContentLoaded', function() {
    // Khởi tạo Toggle
    setupToggle('checkNewCategory', 'inputNewCategory', 'parentCategory');
    setupToggle('checkNewType', 'inputNewType', 'productType');

    // Khởi tạo sự kiện sinh mã vạch
    const btnBarcode = document.getElementById('btnGenerateBarcode');
    const inputBarcode = document.getElementById('barCode');

    if (btnBarcode && inputBarcode) {
        btnBarcode.addEventListener('click', function() {
            const newBarcode = generateEAN13();
            inputBarcode.value = newBarcode;
        });
    }


    // Khởi tạo sự kiện hiện nút xóa ảnh
    const inputFile = document.getElementById('productImage');
    const btnRemove = document.getElementById('btnRemoveImage');
    if (inputFile && btnRemove) {
        inputFile.addEventListener('change', function() {
            if (this.files && this.files.length > 0) {
                btnRemove.classList.remove('d-none');
            } else {
                btnRemove.classList.add('d-none');
            }
        });
    }

    // Khởi tạo SimpleLightbox
    var lightbox = new SimpleLightbox('.product-gallery', {
        // --- Các tùy chọn (Options) ---
        overlayOpacity: 0.8,
        docClose: true,
        swipeClose: true, //vuốt trên thiết bị di động
        fileExt: 'png|jpg|jpeg|gif', 
        closeText: '×',
        close: true,             
        captionSelector: 'self', 
        captionType: 'alt',    
        fadeSpeed: 200,
    });
});