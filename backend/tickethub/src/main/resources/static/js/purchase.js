/**
 * Global elements selection
 */
const ticketSelect = document.getElementById("ticketCount");
const ticketsContainer = document.getElementById("ticketsContainer");
const discountContainer = document.getElementById("discountContainer");
const addDiscountBtn = document.getElementById("addDiscount");

/**
 * Validates purchase data and handles authentication checks before submitting.
 * Submits the purchase via a dynamically created POST form to avoid Error 999.
 */
function goToConfirmation() {
    const eventIdInput = document.getElementById('currentEventId');
    const currentEventId = eventIdInput ? eventIdInput.value : window.currentEventId;

    if (!currentEventId) {
        console.error("No Event ID found");
        return;
    }

    executeSubmit(currentEventId);
}

/**
 * Creates a dynamic form and submits it via POST
 */
function executeSubmit(currentEventId) {
    const totalPriceElement = document.getElementById('totalPrice');
    const sessionElement = document.getElementById('sessionSelect');

    const totalRaw = totalPriceElement ? totalPriceElement.innerText : "0";
    const totalClean = totalRaw.replace('€', '').replace(',', '.').trim();

    const sessionId = sessionElement ? sessionElement.value : null;
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/purchases/save';

    const fields = {
        'eventId': currentEventId,
        'totalPrice': totalClean,
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

    document.querySelectorAll('.zone-select').forEach(select => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = "zoneIds";
        input.value = select.value;
        form.appendChild(input);
    });

    document.body.appendChild(form);
    form.submit();
}

/**
 * Calculates the total price based on selected zones and active discounts.
 */
function updateTotalPrice() {
    let total = 0;

    const zoneSelectors = document.querySelectorAll('.zone-select');
    zoneSelectors.forEach(select => {
        const selectedOption = select.options[select.selectedIndex];
        const price = parseFloat(selectedOption.getAttribute('data-price')) || 0;
        total += price;
    });

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
    const priceDisplay = document.getElementById('totalPrice');
    if (priceDisplay) priceDisplay.innerText = total.toFixed(2);
}

/**
 * Generates ticket rows dynamically based on the selected count.
 */
function generateTickets(count) {
    if (!ticketsContainer) return;
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
 * Adds a new discount dropdown to the UI.
 */
function addDiscountSelect() {
    if (!discountContainer) return;
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

addDiscountSelect();
document.addEventListener("change", e => {
    if (e.target.classList.contains("zone-select") ||
        e.target.classList.contains("discount-select") ||
        e.target.id === "sessionSelect") {
        updateTotalPrice();
    }
});

if (ticketsContainer) {
    ticketsContainer.addEventListener("click", e => {
        if (e.target.classList.contains("remove-ticket")) {
            e.target.closest(".ticket-card").remove();
            updateTotalPrice();
        }
    });
}

if (discountContainer) {
    discountContainer.addEventListener("click", e => {
        if (e.target.classList.contains("remove-discount")) {
            e.target.closest(".discount-item").remove();
            updateTotalPrice();
        }
    });
}

if (ticketSelect) {
    ticketSelect.addEventListener("change", e => {
        generateTickets(e.target.value);
    });
}

if (addDiscountBtn) {
    addDiscountBtn.addEventListener("click", addDiscountSelect);
}

// Initial setup on page load
if (ticketSelect) generateTickets(ticketSelect.value);
addDiscountSelect();