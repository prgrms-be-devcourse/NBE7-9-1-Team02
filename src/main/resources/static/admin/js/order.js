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

    // 여기서 직접 UI 바꾸는 코드 지우고 → handleShip / handleCancel 호출만
    shipBtn.addEventListener("click", () => {
        const orderId = shipBtn.dataset.orderId;
        if (orderId) handleShip(orderId);
    });

    cancelBtn.addEventListener("click", () => {
        const orderId = cancelBtn.dataset.orderId;
        if (orderId) handleCancel(orderId);
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
        <p><strong>주문자:</strong> ${order.customerName}</p>
        <p><strong>이메일:</strong> ${order.email}</p>
        <p><strong>주소:</strong> ${order.address}</p>
        <p><strong>우편번호:</strong> ${order.zipcode}</p>
        <p><strong>주문일시:</strong> ${order.orderDate}</p>
        <p><strong>배송상태:</strong> <span id="orderStatus">${statusMap[order.status]}</span></p>
        <p><strong>총 결제 금액(전체):</strong> ${order.totalPrice} 원</p>
        <hr>
        <h5>상품 목록</h5>
        <table class="table table-sm">
            <thead>   
                <tr>
                    <th>상품명</th>
                    <th>단가</th>
                    <th>수량</th>
                    <th>총 금액</th>
                </tr>
            </thead>
            <tbody>
                ${order.products.map(p => `
                    <tr>
                        <td>${p.productName}</td>
                        <td>${p.price}원</td>
                        <td>${p.quantity}개</td>
                        <td>${p.price * p.quantity}원</td>
                    </tr>
                `).join('')}
            </tbody>
        </table>

    `;

    // 버튼 상태 업데이트
    const shipBtn = document.getElementById("shipBtn");
    const cancelBtn = document.getElementById("cancelBtn");

    // 현재 선택된 orderId 저장
    shipBtn.dataset.orderId = order.orderId;
    cancelBtn.dataset.orderId = order.orderId;

    if (order.status === "PAID") {
        shipBtn.disabled = false;
        shipBtn.textContent = "배송하기";
        cancelBtn.classList.add("d-none");
    } else if (order.status === "SHIPPED") {
        shipBtn.disabled = true;
        shipBtn.textContent = "처리중...";
        order.canCancel ? cancelBtn.classList.remove("d-none") : cancelBtn.classList.add("d-none");
        cancelBtn.disabled = !order.canCancel;
    } else { // DELIVERED
        shipBtn.disabled = true;
        shipBtn.textContent = "배송완료";
        cancelBtn.classList.add("d-none");
    }
}

function handleShip(orderId) {
    const shipBtn = document.getElementById("shipBtn");
    shipBtn.disabled = true;
    shipBtn.textContent = "처리중...";

    fetch(`/admin/orders/${orderId}/ship`, {method: 'POST'})
        .then(res => {
            if (!res.ok) return res.text().then(t => {
                throw new Error(t || '응답 오류');
            });
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
    cancelBtn.textContent = "취소하기";

    fetch(`/admin/orders/${orderId}/cancel`, {method: 'POST'})
        .then(res => {
            if (!res.ok) return res.text().then(t => {
                throw new Error(t || '응답 오류');
            });
            return loadOrderDetail(orderId);
        })
        .catch(err => {
            console.error("취소 처리 실패:", err);
            alert("취소 처리 실패: " + err.message);
            cancelBtn.disabled = false;
            cancelBtn.textContent = "취소하기";
        });
}