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

    let foods = [];
    let combos = [];
    let toppings = [];
    let cart = [];

    // Загружаем меню
    Promise.all([
        fetch('data/food.json').then(res => res.json()),
        fetch('data/combos.json').then(res => res.json())
    ])
        .then(([foodData, comboData]) => {
            foods = foodData.food || [];
            combos = comboData.combos || [];
            displayMenu('all');
            updateCart();
        })
        .catch(err => console.log(err));

    // Загружаем топпинги
    fetch('data/toppings.json')
        .then(res => res.json())
        .then(data => { toppings = data.toppings || []; })
        .catch(err => console.log(err));

    async function updateCart() {
        try {
            const res = await fetch('http://localhost:8080/cart');
            if (!res.ok) throw new Error('Failed to fetch cart');
            cart = await res.json();
            updateCartCount();
        } catch (error) {
            console.log(error);
        }
    }

    function displayMenu(filterCategory = 'all') {
        menuContainer.innerHTML = '';

        const filteredFoods = filterCategory === 'all' ? foods
            : foods.filter(f => f.category === filterCategory);
        const filteredCombos = filterCategory === 'all' ? combos
            : combos.filter(c => c.category === filterCategory);

        [...filteredFoods, ...filteredCombos].forEach(item => {
            const card = createCard(item);
            menuContainer.appendChild(card);
        });
    }

    function createCard(item) {
        const card = document.createElement('div');
        card.classList.add('card');
        card.innerHTML = `
            <div class="img-container">
                <img src="${item.image}" alt="${item.name}">
            </div>
            <div class="card-body">
                <div class="card-title">${item.name}</div>
                <div class="card-description">${item.description}</div>
                <div class="card-price">${item.price} ₸</div>
            </div>
        `;
        card.addEventListener('click', () => showPopup(item));
        return card;
    }

    function showPopup(item) {
        currentPopupItem = item;
        selectedToppings = new Set();
        popupImg.src = item.image;
        popupName.textContent = item.name;
        popupDescription.textContent = item.description;
        popupPrice.textContent = `${item.price} ₸`;

        toppingsForCategory(item.category || '');

        popup.style.display = 'flex';

        addToCartBtn.onclick = async () => {
            const chosenToppings = toppings.filter(t => selectedToppings.has(t.id));
            const totalPrice = item.price + chosenToppings.reduce((sum, t) => sum + t.price, 0);

            const payload = {
                foodId: item.id || Date.now(),
                name: item.name,
                price: totalPrice,
                quantity: 1,
                image: item.image,
                toppings: chosenToppings.map(t => ({ id: t.id, name: t.name, price: t.price }))
            };
            try {
                const response = await fetch('http://localhost:8080/cart/add', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });

                if (response.ok) {
                    showToaster(`${item.name} added to cart`);
                    await updateCart();
                } else {
                    showToaster('Failed to add item to cart');
                }
            } catch (error) {
                console.error(error);
            }
            popup.style.display = 'none';
        };
    }

    let selectedToppings = new Set();
    let currentPopupItem = null;

    function toppingsForCategory(category) {
        const list = document.getElementById('toppings-list');
        list.innerHTML = '';

        const available = toppings.filter(t => t.category === category);
        if (available.length === 0) {
            list.textContent = 'No Toppings';
            return;
        }

        available.forEach(topping => {
            const wrap = document.createElement('div');
            wrap.className = 'topping';

            const input = document.createElement('input');
            input.type = 'checkbox';
            input.id = 'topping-' + topping.id;
            input.dataset.toppingid = topping.id;

            input.checked = selectedToppings.has(topping.id);

            const label = document.createElement('label');
            label.htmlFor = input.id;
            label.textContent = `${topping.name} (${topping.price} ₸)`;

            input.addEventListener('change', (event) => {
                const id = Number(event.target.dataset.toppingid);
                if (event.target.checked) {
                    selectedToppings.add(id);
                } else {
                    selectedToppings.delete(id);
                }
                updatePopupPrice();
            });
            wrap.append(input, label);
            list.appendChild(wrap);
        });
    }

    function updatePopupPrice() {
        if (!currentPopupItem) {
            return;
        }
        const toppingSum = toppings
            .filter(t => selectedToppings.has(t.id))
            .reduce((sum, topping) => { sum += topping.price; }, 0);
        popupPrice.textContent = `${currentPopupItem.price + toppingSum} ₸`;
    }

    closePopup.onclick = () => {
        popup.style.display = 'none';
    };

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

    updateCartCount();
});
