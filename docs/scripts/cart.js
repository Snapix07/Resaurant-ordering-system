document.addEventListener("DOMContentLoaded", () => {
    const cartContainer = document.getElementById("cart-container");
    const totalPrice = document.getElementById("total-price");
    const checkoutButton = document.getElementById("checkout-btn");
    const deliverySelect = document.getElementById("delivery-type");
    const toaster = document.getElementById("toaster");

    let cart = [];

    async function fetchCart() {
        try {
            const res = await fetch("/cart");
            cart = await res.json();
            displayCart();
        } catch (error) {
            console.error(error);
        }
    }

    function displayCart() {
        cartContainer.innerHTML = "";
        let total = 0;

        cart.forEach((item, index) => {
            total += item.price * (item.quantity || 1);
            const cartItem = document.createElement("div");
            cartItem.classList.add("cart-item");

            cartItem.innerHTML = `
                <img src="${item.image}" alt="${item.name}">
                <div class="cart-item-details">
                    <h3>${item.name}</h3>
                    <p>${item.price} ₸</p>
                </div>
                <button data-index="${index}">Remove</button>
                `;

            const removeBtn = cartItem.querySelector("button");
            removeBtn.addEventListener("click", async () => {
                await fetch("/cart/remove", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(item)
                });
                await fetchCart();
                showToaster(`${item.name || "Item"} removed from cart`)
            });
            cartContainer.appendChild(cartItem);
        });

        totalPrice.textContent = total;

    }

    function showToaster(message) {
        toaster.textContent = message;
        toaster.classList.add('show');

        setTimeout(() => {
            toaster.classList.remove('show');
        }, 3000);
    }

    checkoutButton.addEventListener("click", async () => {
        if (cart.length === 0) {
            showToaster("Your cart is empty!");
            return;
        }
        const deliveryType = deliverySelect.value;

        const cartItems = cart.map(item => ({
                id: item.foodId,
                name: item.name,
                amount: item.price,
                quantity: item.quantity
        }));

        try {
            const res = await fetch(`/orders/checkout?deliveryType=${deliveryType}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(cartItems)
            });
            const data = await res.json();
            if (data.sessionUrl){
                window.location.href = data.sessionUrl;
            } else {
                showToaster("Failed to create payment session");
            }
        } catch (error) {
            console.log(error);
        }
    });

    fetchCart();
});