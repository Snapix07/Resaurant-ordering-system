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

    fetch('data/food.json')
        .then(res => res.json())
        .then(data => {
            foods = data.food;
            displayMenu('all');
        })
        .catch(err => console.log(err));

    fetch('data/combos.json')
        .then(res => res.json())
        .then(data => {
            combos = data.combos;
            displayMenu('all');
        })
        .catch(err => console.log(err));


    async function updateCart() {
        try {
            const res = await fetch('/cart');
            if (!res.ok){
                throw new Error('Failed to fetch cart.');
            }
            cart = await res.json();
            updateCartCount();
        } catch (error) {
            console.log(error);
        }
    }

    function displayMenu(filterCategory = 'all') {
        menuContainer.innerHTML = '';
        const displayedFoods = filterCategory === 'all' ? foods : foods.filter(food => food.category === filterCategory);

        displayedFoods.forEach(food => {
            menuContainer.appendChild(createCard(food.name, food.description, food.price, food.image));
        });

        const displayedCombos = filterCategory === 'all' ? combos : combos.filter(combo => combo.category === filterCategory);
        displayedCombos.forEach(combo => {
            const card = createCard(combo.name, combo.description, combo.price, combo.image);
            menuContainer.appendChild(card);
        });
    }

    function createCard(name, description, price, image) {
        const card = document.createElement('div');
        card.classList.add('card');
        card.innerHTML = `
            <div class="img-container">
            <img src="${image}" alt="${name}">
            </div>
            <div class ="card-body">
                <div class="card-title">${name}</div>
                <div class="card-description">${description}</div>
                <div class="card-price">${price}</div>
            </div>`;

        card.addEventListener('click', () => {
            showPopup({name, description, price, image});
        });
        return card;
    }

    function showPopup(item) {
        popupImg.onload = () => {
            popupImg.style.maxWidth = "92%";
            popupImg.style.height = "auto";
            popupImg.style.display = "block";
            popupImg.style.margin = "0 auto";
        };
        popupImg.src = item.image;
        popupName.textContent = item.name;
        popupDescription.textContent = item.description;
        popupPrice.textContent = `${item.price} ₸`;
        popup.style.display = 'flex';

        addToCartBtn.onclick = async () => {
            try {
                const response = await fetch('/cart/add', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        foodId: item.id,
                        quantity: 1,
                        price: item.price
                    })
                });

                if (response.ok) {
                    showToaster(`${item.name} added to cart`);
                    await updateCart();
                } else {
                    showToaster('Failed to add item to cart.');
                }
            }catch(error) {
                console.log(error);
            }
            popup.style.display = 'none';
        };
    }

    closePopup.onclick = () => {
        popup.style.display = 'none';
    }

    function showToaster(message) {
        toaster.textContent = message;
        toaster.classList.add('show');

        setTimeout(() => {
            toaster.classList.remove('show');
        }, 3000)
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