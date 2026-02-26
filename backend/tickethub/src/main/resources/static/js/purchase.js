const ticketSelect = document.getElementById("ticketCount");
const ticketsContainer = document.getElementById("ticketsContainer");
const discountContainer = document.getElementById("discountContainer");
const addDiscountBtn = document.getElementById("addDiscount");

/**
 * Validates data and submits the purchase via a dynamic POST form
 */
function goToConfirmation() {
    const totalPriceElement = document.getElementById('totalPrice');
    const sessionSelect = document.getElementById('sessionSelect');
    
    const total = totalPriceElement.innerText;
    const sessionId = sessionSelect ? sessionSelect.value : null;
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    if (!sessionId || sessionId === "") {
        alert("Please select a valid session before proceeding.");
        return;
    }

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/purchases/save';

    // The eventId is retrieved from the URL or the global event object
    const fields = {
        'eventId': window.location.pathname.split('/').pop(), 
        'totalPrice': total.replace('€', '').trim(),
        '_csrf': csrfToken,
        'sessionId': sessionId
    };

    for (let key in fields) {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = key;
        input.value = fields[key];
        form.appendChild(input);
    }

    document.body.appendChild(form);
    form.submit();
}

/**
 * Calculates the total price based on selected zones and active discounts
 */
function updateTotalPrice() {
    let total = 0;

    // Calculate base price from all active zone selectors
    const zoneSelectors = document.querySelectorAll('.zone-select');
    zoneSelectors.forEach(select => {
        const selectedOption = select.options[select.selectedIndex];
        const price = parseFloat(selectedOption.getAttribute('data-price')) || 0;
        total += price;
    });

    // Apply subtractions or percentage deductions from discount selectors
    const discountSelectors = document.querySelectorAll('.discount-select');
    discountSelectors.forEach(select => {
        const opt = select.options[select.selectedIndex];
        if (opt && opt.value !== "") {
            const amount = parseFloat(opt.getAttribute('data-amount')) || 0;
            const percent = parseFloat(opt.getAttribute('data-percent')) || 0;
            
            if (amount > 0) total -= amount;
            if (percent > 0) total -= (total * (percent / 100));
        }
    });

    if (total < 0) total = 0;
    document.getElementById('totalPrice').innerText = total.toFixed(2);
}

/**
 * Generates ticket input rows dynamically based on the requested count
 */
function generateTickets(count) {
    ticketsContainer.innerHTML = "";
    for (let i = 0; i < count; i++) {
        ticketsContainer.insertAdjacentHTML("beforeend", `
            <div class="card mb-3 p-3 ticket-card">
                <div class="d-flex justify-content-between align-items-center">
                    <strong>Ticket ${i + 1}</strong>
                    <div class="d-flex gap-2 align-items-center">
                        <span>Zone:</span>
                        <select name="zone" class="form-select zone-select">
                            ${zonesHTML}
                        </select>
                    </div>
                    <button type="button" class="btn btn-sm btn-danger remove-ticket">Remove</button>
                </div>
            </div>
        `);
    }
    updateTotalPrice();
}

/**
 * Adds a new discount selector to the container
 */
function addDiscountSelect() {
    discountContainer.insertAdjacentHTML("beforeend", `
        <div class="mb-2 discount-item d-flex gap-2 align-items-center">
            <select name="discount" class="form-select w-auto d-inline-block discount-select">
                ${discountsHTML}
            </select>
            <button type="button" class="btn btn-sm btn-outline-danger remove-discount">x</button>
        </div>
    `);
    updateTotalPrice();
}

// Global Event Listeners for dynamic price recalculation
document.addEventListener("change", e => {
    if (e.target.classList.contains("zone-select") || 
        e.target.classList.contains("discount-select") || 
        e.target.id === "sessionSelect") {
        updateTotalPrice();
    }
});

// Listener for ticket removal
ticketsContainer.addEventListener("click", e => {
    if (e.target.classList.contains("remove-ticket")) {
        e.target.closest(".ticket-card").remove();
        updateTotalPrice();
    }
});

// Listener for discount removal
discountContainer.addEventListener("click", e => {
    if (e.target.classList.contains("remove-discount")) {
        e.target.closest(".discount-item").remove();
        updateTotalPrice();
    }
});

ticketSelect.addEventListener("change", e => {
    generateTickets(e.target.value);
});

addDiscountBtn.addEventListener("click", addDiscountSelect);

// Initialization
generateTickets(ticketSelect.value);
addDiscountSelect();