document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".order-detail-btn");

    buttons.forEach(btn => {
        btn.addEventListener("click", () => {
            const orderId = btn.dataset.orderId;
            loadOrderDetail(orderId);
        });
    });

    // 버튼 이벤트는 DOMContentLoaded 시 한 번만 등록
    const shipBtn = document.getElementById("shipBtn");
    const cancelBtn = document.getElementById("cancelBtn");

    shipBtn.addEventListener("click", () => {
        const orderId = shipBtn.dataset.orderId;
        handleShip(orderId);
    });

    cancelBtn.addEventListener("click", () => {
        const orderId = cancelBtn.dataset.orderId;
        handleCancel(orderId);
    });
});

const statusMap = {
    PAID: "결제완료",
    SHIPPED: "배송중",
    DELIVERED: "배송완료"
};

function loadOrderDetail(orderId) {
    fetch(`/admin/orders/${orderId}/detail`)
        .then(res => {
            if (!res.ok) throw new Error("상세 정보 로드 실패");
            return res.json();
        })
        .then(order => {
            renderOrderDetail(order);

            // 모달 열기 (이미 열려있으면 새로 열지 않음)
            const modalEl = document.getElementById("orderDetailModal");
            const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
            modal.show();
        })
        .catch(err => console.error("주문 상세 불러오기 실패:", err));
}

function renderOrderDetail(order) {
    const content = document.getElementById("orderDetailContent");
    content.innerHTML = `
        <p><strong>주문번호:</strong> ${order.orderId}</p>
        <p><strong>이메일:</strong> ${order.email}</p>
        <p><strong>주문일시:</strong> ${order.orderDate}</p>
        <p><strong>배송상태:</strong> <span id="orderStatus">${statusMap[order.status]}</span></p>
        <p><strong>총 금액:</strong> ${order.totalPrice} 원</p>
        <hr>
        <h5>상품 목록</h5>
        <ul>
            ${order.products.map(p => `<li>${p.productName} (${p.quantity}개) - ${p.price}원</li>`).join("")}
        </ul>
    `;

    // 버튼 상태 업데이트
    const shipBtn = document.getElementById("shipBtn");
    const cancelBtn = document.getElementById("cancelBtn");

    shipBtn.dataset.orderId = order.orderId;
    cancelBtn.dataset.orderId = order.orderId;

    if(order.status === "PAID") {
        shipBtn.disabled = false;
        cancelBtn.classList.add("d-none");
    } else if(order.status === "SHIPPED") {
        shipBtn.disabled = true;
        order.canCancel ? cancelBtn.classList.remove("d-none") : cancelBtn.classList.add("d-none");
        cancelBtn.disabled = !order.canCancel;
    } else { // DELIVERED
        shipBtn.disabled = true;
        cancelBtn.classList.add("d-none");
    }
}

function handleShip(orderId) {
    const shipBtn = document.getElementById("shipBtn");
    shipBtn.disabled = true;
    shipBtn.textContent = "처리중...";

    fetch(`/admin/orders/${orderId}/ship`, { method: 'POST' })
        .then(res => {
            if (!res.ok) return res.text().then(t => { throw new Error(t || '응답 오류'); });
            return loadOrderDetail(orderId);
        })
        .catch(err => {
            console.error("배송 처리 실패:", err);
            alert("배송 처리 실패: " + err.message);
            shipBtn.disabled = false;
            shipBtn.textContent = "배송하기";
        });
}

function handleCancel(orderId) {
    const cancelBtn = document.getElementById("cancelBtn");
    cancelBtn.disabled = true;
    cancelBtn.textContent = "처리중...";

    fetch(`/admin/orders/${orderId}/cancel`, { method: 'POST' })
        .then(res => {
            if (!res.ok) return res.text().then(t => { throw new Error(t || '응답 오류'); });
            return loadOrderDetail(orderId);
        })
        .catch(err => {
            console.error("취소 처리 실패:", err);
            alert("취소 처리 실패: " + err.message);
            cancelBtn.disabled = false;
            cancelBtn.textContent = "취소하기";
        });
}

/**
 *
 * 주요 변경점
 * 1. cloneNode 제거 → 기존 DOM 그대로 사용
 * 2. 버튼 이벤트는 초기 한 번만 등록, orderId는 data-order-id 속성으로 동적 연결
 * 3. 모달 열기 시 기존 백드롭 확인 → 중첩 방지
 * 4. 버튼 상태(disabled, d-none)만 업데이트 → 목록 버튼이나 다른 UI에 영향 없음
 *
 */