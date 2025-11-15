document.addEventListener("DOMContentLoaded", () => {
    const cartContainer = document.getElementById("cart-container");
    const totalPrice = document.getElementById("total-price");
    const checkoutButton = document.getElementById("checkout-btn");
    const deliverySelect = document.getElementById("delivery-type");
    const toaster = document.getElementById("toaster");

    let cart = [];


    async function fetchCart() {
        try {
            const res = await fetch("http://localhost:8080/cart");
            if (!res.ok) throw new Error("Failed to load cart");
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

            const itemName = item.name || "Unnamed Product";
            const itemImage = item.image || "placeholder.png";
            let toppingsHTML = '';
            if (item.toppings && item.toppings.length > 0) {
                const toppingsNames = item.toppings.map(t => t.name).join(', ');
                toppingsHTML = `<p class="cart-item-toppings">+ ${toppingsNames}</p>`;
            }
            const cartItem = document.createElement("div");
            cartItem.classList.add("cart-item");

            const quantityText = item.quantity > 1 ? ` * ${item.quantity}` : '';
            cartItem.innerHTML = `
            <img src="${itemImage}" alt="${itemName}">
            <div class="cart-item-details">
                <h3>${itemName}</h3>
                ${toppingsHTML}
                <p class="cart-item-price">${item.price} ₸ ${quantityText}</p>
            </div>
            <button class="remove-btn" data-index="${index}">Remove</button>
        `;

            cartItem.querySelector("button").addEventListener("click", async () => {
                await fetch("http://localhost:8080/cart/remove", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(item)
                });
                showToaster(`${itemName} removed`);
                await fetchCart();
            });

            cartContainer.appendChild(cartItem);
        });

        totalPrice.textContent = total;
    }


    checkoutButton.addEventListener("click", async () => {
        if (cart.length === 0) return showToaster("Your cart is empty!");

        const deliveryType = deliverySelect.value;
        const cartItems = cart.map(item => ({
            id: item.foodId,
            name: item.name || "Unnamed Product",
            amount: item.price ,
            quantity: item.quantity || 1,
            currency: "KZT",
        }));

        try {
            const res = await fetch(`http://localhost:8080/orders/checkout?deliveryType=${deliveryType}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(cartItems)
            });
            const data = await res.json();
            if (data.sessionUrl) window.location.href = data.sessionUrl;
            else showToaster("Payment session failed");
        } catch (error) {
            console.error(error);
        }
    });

    function showToaster(message) {
        toaster.textContent = message;
        toaster.classList.add('show');
        setTimeout(() => toaster.classList.remove('show'), 3000);
    }

    fetchCart();
});
