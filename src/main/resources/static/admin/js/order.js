document.addEventListener("DOMContentLoaded", () => {
    const rows = document.querySelectorAll("table tbody tr");

    rows.forEach(row => {
        row.addEventListener("click", () => {
            const orderId = row.querySelector("td").innerText; // 첫 번째 칸이 주문번호라 가정
            loadOrderDetail(orderId);
        });
    });
});

function loadOrderDetail(orderId) {
    fetch(`/admin/api/orders/${orderId}`)  // AdminOrderApiController에서 주문 상세 조회
        .then(res => res.json())
        .then(data => {
            renderOrderDetail(data);
            const modal = new bootstrap.Modal(document.getElementById("orderDetailModal"));
            modal.show();
        })
        .catch(err => {
            console.error("주문 상세 불러오기 실패:", err);
        });
}

function renderOrderDetail(order) {
    let html = `
    <p><strong>주문번호:</strong> ${order.orderId}</p>
    <p><strong>이메일:</strong> ${order.email}</p>
    <p><strong>주문일시:</strong> ${order.orderDate}</p>
    <p><strong>상태:</strong> <span id="orderStatus">${order.status}</span></p>
    <p><strong>총 금액:</strong> ${order.totalPrice} 원</p>
    <hr>
    <h5>상품 목록</h5>
    <ul>
      ${order.products.map(p => `<li>${p.productName} (${p.quantity}개) - ${p.price}원</li>`).join("")}
    </ul>
  `;

    document.getElementById("orderDetailContent").innerHTML = html;

    // 버튼 상태 제어
    const shipBtn = document.getElementById("shipBtn");
    const cancelBtn = document.getElementById("cancelBtn");

    if (order.status === "PAID") {
        shipBtn.disabled = false;
        cancelBtn.classList.add("d-none");
    } else if (order.status === "SHIPPED") {
        shipBtn.disabled = true;
        cancelBtn.classList.remove("d-none");
    } else { // DELIVERED
        shipBtn.disabled = true;
        cancelBtn.classList.add("d-none");
    }
}
