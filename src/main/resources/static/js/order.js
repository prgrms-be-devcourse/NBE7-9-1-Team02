document.addEventListener('DOMContentLoaded', () => {
    // --- 1. 전역 변수 및 요소 선언 ---
    const productItemsContainer = document.getElementById('product-items-container');
    const summaryItemList = document.getElementById('summary-item-list');
    const totalPriceElement = document.getElementById('total-price');
    const orderForm = document.getElementById('order-form');
    let cart = {}; // 장바구니 데이터를 관리하는 클라이언트 사이드 객체

    // 상품 목록 '추가' 버튼 클릭
    productItemsContainer.addEventListener('click', (event) => {
        if (event.target.classList.contains('add-btn')) {
            const productItem = event.target.closest('.product-item');
            const id = productItem.dataset.id;
            const name = productItem.dataset.name;
            const price = parseInt(productItem.dataset.price, 10);
            const stock = parseInt(productItem.dataset.stock, 10);
            addToCart(id, name, price, stock);
        }
    });

    // 주문 내역 수량 변경(+,-) 버튼 클릭
    summaryItemList.addEventListener('click', (event) => {
        if (event.target.classList.contains('quantity-btn')) {
            const summaryItem = event.target.closest('.summary-item');
            const id = summaryItem.dataset.id;
            const change = parseInt(event.target.dataset.change, 10);
            updateQuantity(id, change);
        }
    });

    // '결제하기' 폼 제출
    orderForm.addEventListener('submit', (event) => {
        event.preventDefault();

        if (!prepareFormData()) {
            return;
        }
        // 폼 유효성 검사를 그 다음에 실행
        if (!validateForm()) {
            return;
        }
        orderForm.submit();
    });

    /**
     * 페이지 로드 시, 서버에서 받은 데이터로 cart 객체를 복원하는 함수
     */
    function initializeCart() {
        const initialCartItemsJson = document.body.dataset.initialCartItems;
        const initialCartItems = JSON.parse(initialCartItemsJson);

        if (initialCartItems && initialCartItems.length > 0) {
            const productsData = {};
            document.querySelectorAll('.product-item').forEach(item => {
                productsData[item.dataset.id] = {
                    name: item.dataset.name,
                    price: parseInt(item.dataset.price, 10),
                    stock: parseInt(item.dataset.stock, 10)
                };
            });

            initialCartItems.forEach(item => {
                const product = productsData[item.productId];
                if (product) {
                    cart[item.productId] = {
                        name: product.name,
                        price: product.price,
                        quantity: item.quantity,
                        stock: product.stock
                    };
                }
            });
            renderCart();
        }
    }

    function addToCart(id, name, price, stock) {
        if (cart[id] && cart[id].quantity >= stock) {
            alert('재고가 부족하여 더 이상 추가할 수 없습니다.');
            return;
        }
        if (cart[id]) {
            cart[id].quantity++;
        } else {
            cart[id] = {name, price, quantity: 1, stock: stock};
        }
        renderCart();
    }

    function updateQuantity(id, change) {
        if (!cart[id]) return;
        if (change > 0 && cart[id].quantity >= cart[id].stock) {
            alert('재고가 부족하여 더 이상 추가할 수 없습니다.');
            return;
        }
        cart[id].quantity += change;
        if (cart[id].quantity <= 0) {
            delete cart[id];
        }
        renderCart();
    }

    function calculateTotalPrice() {
        let total = 0;
        for (const id in cart) {
            total += cart[id].price * cart[id].quantity;
        }
        return total;
    }

    function renderCart() {
        summaryItemList.innerHTML = '';
        const totalPrice = calculateTotalPrice();
        for (const id in cart) {
            const item = cart[id];
            const li = document.createElement('li');
            li.className = 'summary-item';
            li.dataset.id = id;
            li.innerHTML = `
                    <span>${item.name}</span>
                    <div class="quantity-controls">
                        <button class="quantity-btn" data-change="-1">-</button>
                        <span class="quantity">${item.quantity}개</span>
                        <button type="button" class="quantity-btn" data-change="1">+</button>
                    </div>
                `;
            summaryItemList.appendChild(li);
        }
        totalPriceElement.textContent = `${totalPrice.toLocaleString()}원`;
    }

    /**
     * 폼 제출 전 유효성 검사를 수행하는 함수
     * @returns {boolean} - 유효하면 true, 아니면 false
     */
    function validateForm() {
        const emailInput = document.getElementById('email');
        const customerNameInput = document.getElementById('customerName');
        const addressInput = document.getElementById('address');

        if (emailInput.value.trim() === '') {
            alert('이메일을 입력해주세요.');
            emailInput.focus();
            return false;
        } else if (customerNameInput.value.trim() === '') {
            alert('주문자명을 입력해주세요.');
            customerNameInput.focus();
            return false;
        }
        if (addressInput.value.trim() === '') {
            alert('주소지를 입력해주세요.');
            addressInput.focus();
            return false;
        }
        return true;
    }

    /**
     * 폼 제출을 위해 cart 객체를 hidden input으로 변환하는 함수
     * @returns {boolean} - 장바구니에 상품이 있으면 true, 아니면 false
     */
    function prepareFormData() {
        if (Object.keys(cart).length === 0) {
            alert('주문할 상품을 추가해주세요.');
            return false;
        }

        orderForm.querySelectorAll('input[type="hidden"]').forEach(input => input.remove());

        let index = 0;
        for (const id in cart) {
            const item = cart[id];
            const productIdInput = document.createElement('input');
            productIdInput.type = 'hidden';
            productIdInput.name = `orderItems[${index}].productId`;
            productIdInput.value = id;
            orderForm.appendChild(productIdInput);

            const quantityInput = document.createElement('input');
            quantityInput.type = 'hidden';
            quantityInput.name = `orderItems[${index}].quantity`;
            quantityInput.value = item.quantity;
            orderForm.appendChild(quantityInput);
            index++;
        }
        const totalPriceInput = document.createElement('input');
        totalPriceInput.type = 'hidden';
        totalPriceInput.name = 'totalPrice';
        totalPriceInput.value = calculateTotalPrice();
        orderForm.appendChild(totalPriceInput);

        return true;
    }

    // --- 4. 페이지 초기화 실행 ---
    initializeCart();
});

document.querySelectorAll(".detail-btn").forEach(btn => {
    btn.addEventListener("click", function (event) {
        // '추가' 버튼을 눌렀을 때는 모달이 뜨지 않도록 예외 처리
        if (event.target.classList.contains('add-btn')) {
            return;
        }

        const productId = this.dataset.id;
        const name = this.dataset.name;
        const imageUrl = this.dataset.image;

        fetch(`/api/products/${productId}`)
            .then(res => {
                if (!res.ok) throw new Error(`HTTP error! Status: ${res.status}`);
                return res.json();
            })
            .then(data => {
                document.getElementById("modalImage").src = imageUrl;
                document.getElementById("modalName").innerText = name;
                document.getElementById("modalOrigin").innerText = data.origin ?? "상세 준비중";
                document.getElementById("modalFlavor").innerText = data.flavorAroma ?? "상세 준비중";
                document.getElementById("modalFeature").innerText = data.feature ?? "상세 준비중";
                document.getElementById("modalPrice").innerText = (data.price ?? 0) + "원";
                document.getElementById("productModal").style.display = "flex";
            })
            .catch(err => {
                console.error(err);
                alert("상세 정보를 불러오지 못했습니다.");
            });
    });
});

function closeModal() {
    document.getElementById("productModal").style.display = "none";
}

window.addEventListener("click", function (event) {
    const modal = document.getElementById("productModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
});