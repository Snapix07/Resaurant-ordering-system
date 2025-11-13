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

        const filteredFoods = filterCategory === 'all'
            ? foods
            : foods.filter(f => f.category === filterCategory);
        const filteredCombos = filterCategory === 'all'
            ? combos
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
        popupImg.src = item.image;
        popupName.textContent = item.name;
        popupDescription.textContent = item.description;
        popupPrice.textContent = `${item.price} ₸`;
        popup.style.display = 'flex';

        addToCartBtn.onclick = async () => {
            try {
                const response = await fetch('http://localhost:8080/cart/add', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        foodId: item.id || Date.now(), // безопасно, если нет id
                        name: item.name,
                        price: item.price,
                        quantity: 1,
                        image: item.image
                    })
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
