document.addEventListener("DOMContentLoaded", () => {
    const menuContainer = document.getElementById('menu-container');
    const buttons = document.querySelectorAll('.filter-btn');
    const popup = document.getElementById('popup');
    const popupImg = document.getElementById('popup-img');
    const popupName = document.getElementById('popup-name');
    const popupDescription = document.getElementById('popup-description');
    const popupPrice = document.getElementById('popup-price');
    const closePopup = document.getElementById('close-popup');
    const addToCartBtn = document.getElementById('add-to-cart');
    const toaster = document.getElementById('toaster');
    const cartCount = document.getElementById('cart-count');


    let allMenuItems = [];
    let availableToppings = [];
    let currentItem = null;
    let selectedToppingIds = [];
    let cart = [];

    // Загружаем меню
    async function loadMenu() {
        try {
            const response = await fetch('http://localhost:8080/menu');
            if (!response.ok) {
                throw new Error('Not Found Menu!');
            }
            allMenuItems = await response.json();
            displayMenu('all')
            updateCart();
        } catch (error) {
            console.error(error);
        }
    }

    // Загружаем топпинги
    async function loadToppingsForCategory(category) {
        try {
            const response = await fetch(`http://localhost:8080/menu/toppings/${category}`);
            if (!response.ok) {
                throw new Error('Not Found Toppings!');
            }
            availableToppings = await response.json();
        } catch (error) {
            console.error(error);
            availableToppings = [];
        }
    }

    async function updateCart() {
        try {
            const res = await fetch('http://localhost:8080/cart');
            if (!res.ok) {
                throw new Error('Failed to fetch cart');
            }
            cart = await res.json();
            updateCartCount();
        } catch (error) {
            console.log(error);
        }
    }

    function displayMenu(filterCategory = 'all') {
        menuContainer.innerHTML = '';

        const filteredItems = filterCategory === 'all'
            ? allMenuItems
            : allMenuItems.filter(item => item.category === filterCategory);

        filteredItems.forEach((item) => {
            const card = createCard(item);
            menuContainer.appendChild(card);
        });
    }

    function createCard(item) {
        const card = document.createElement('div');
        card.classList.add('card');
        let priceHTML = '';
        if (item.discountedPrice && item.discountedPrice < item.price) {
            priceHTML = `
                <div class="card-price">
                    <span class="original-price">${item.price} ₸</span>
                    <span class="discounted-price">${item.discountedPrice} ₸</span>
                </div>`;
        } else {
            priceHTML = `<div class="card-price">${item.price} ₸</div>`;
        }

        card.innerHTML = `
            <div class="img-container">
                <img src="${item.image}" alt="${item.name}">
            </div>
            <div class="card-body">
                <div class="card-title">${item.name}</div>
                <div class="card-description">${item.description}</div>
                ${priceHTML}
            </div>
        `;
        card.addEventListener('click', () => showPopup(item));
        return card;
    }

    async function showPopup(item) {
        currentItem = item;
        selectedToppingIds = [];

        popupImg.src = item.image;
        popupName.textContent = item.name;
        popupDescription.textContent = item.description;

        if (item.discountedPrice && item.discountedPrice < item.price) {
            popupPrice.innerHTML = `
                <span class="original-price">${item.price} ₸</span>
                <span class="discounted-price">${item.discountedPrice} ₸</span>
            `;
        } else {
            popupPrice.textContent = `${item.price} ₸`;
        }

        await loadToppingsForCategory(item.category);
        displayToppings();

        popup.style.display = 'flex';

        addToCartBtn.onclick = async () => {
            await addItemToCart();
            popup.style.display = 'none';
        };
    }



    function displayToppings() {
        const list = document.getElementById('toppings-list');
        list.innerHTML = '';

        if (!availableToppings || availableToppings.length === 0) {
            list.textContent = 'No Toppings Available';
            return;
        }

        availableToppings.forEach(topping => {
            const wrap = document.createElement('div');
            wrap.className = 'topping';

            const input = document.createElement('input');
            input.type = 'checkbox';
            input.id = 'topping-' + topping.id;
            input.dataset.toppingid = topping.id;
            input.dataset.toppingprice = topping.price;


            const label = document.createElement('label');
            label.htmlFor = input.id;
            label.textContent = `${topping.name} (+${topping.price} ₸)`;

            input.addEventListener('change', (event) => {
                const toppingId = Number(event.target.dataset.toppingid);
                if (event.target.checked) {
                    if (!selectedToppingIds.includes(toppingId)) {
                        selectedToppingIds.push(toppingId);
                    }
                } else {
                    selectedToppingIds = selectedToppingIds.filter(id => id !== toppingId);
                }
                updatePopupPricePreview();
            });
            wrap.append(input, label);
            list.appendChild(wrap);
        });
    }

    function updatePopupPricePreview() {
        if (!currentItem) {
            return;
        }

        let basePrice = currentItem.discountedPrice || currentItem.price;
        let totalPrice = basePrice;

        selectedToppingIds.forEach(toppingId => {
            const topping = availableToppings.find(t => t.id === toppingId);
            if (topping) {
                totalPrice += topping.price;
            }
        });

        if (currentItem.discountedPrice && currentItem.discountedPrice < currentItem.price) {
            const originalWithToppings = currentItem.price + (totalPrice - basePrice);
            popupPrice.innerHTML = `
                <span class="original-price">${originalWithToppings} ₸</span>
                <span class="discounted-price">${totalPrice} ₸</span>
            `;
        } else {
            popupPrice.textContent = `${totalPrice} ₸`;
        }
    }

    async function addItemToCart() {
        if (!currentItem) {
            return;
        }

        try {
            if (selectedToppingIds.length === 0) {
                const cartItem = {
                    foodId: currentItem.id,
                    name: currentItem.name,
                    price: currentItem.discountedPrice || currentItem.price,
                    quantity: 1,
                    image: currentItem.image,
                    toppings: []
                };

                const response = await fetch('http://localhost:8080/cart/add', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(cartItem),
                });
                if (response.ok) {
                    showToaster(`${currentItem.name} added successfully.`);
                    await updateCart();
                }
                return;
            }

            const response = await fetch(`http://localhost:8080/menu/item/${currentItem.id}/customize/${selectedToppingIds[0]}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(selectedToppingIds),
            });

            if (!response.ok) {
                throw new Error('Failed to to customize item.');
            }

            const customizedItems = await response.json();
            const allToppings = [];
            let totalPrice = currentItem.discountedPrice || currentItem.price;

            for (const customizedItem of customizedItems) {
                if (customizedItem.topping) {
                    allToppings.push(customizedItem.topping);
                    totalPrice += customizedItem.topping.price;
                }
            }

            const cartItem = {
                foodId: currentItem.id,
                name: currentItem.name,
                price: totalPrice,
                quantity: 1,
                image: currentItem.image,
                toppings: allToppings,
            };

            await fetch('http://localhost:8080/cart/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(cartItem)
            });

            const toppingNames = allToppings.map(t => t.name).join(', ');
            showToaster(`${currentItem.name} with ${toppingNames} topping(s) added successfully.`);
            await updateCart();
        } catch (error) {
            console.log(error);
        }
    }

    closePopup.onclick = () => {
        popup.style.display = 'none';
    }


    function showToaster(message) {
        toaster.textContent = message;
        toaster.classList.add('show');
        setTimeout(() => toaster.classList.remove('show'), 3000);
    }

    buttons.forEach(btn => {
        btn.addEventListener('click', () => {
            const category = btn.getAttribute('data-category');
            displayMenu(category);
        });
    });

    function updateCartCount() {
        const totalItems = cart.reduce((sum, item) => sum + (item.quantity || 1), 0);
        cartCount.innerText = totalItems;
    }

    loadMenu();
});
