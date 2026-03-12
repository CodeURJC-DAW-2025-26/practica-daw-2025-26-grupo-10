import { showMsg } from "./confirmation.js";

/**
 * Global elements selection
 */
const ticketSelect = document.getElementById("ticketCount");
const ticketsContainer = document.getElementById("ticketsContainer");
const discountContainer = document.getElementById("discountContainer");
const addDiscountBtn = document.getElementById("addDiscount");
const goToConfirmationBtn = document.getElementById("goToConfirmation");

/**
 * Validates purchase data and handles authentication checks before submitting.
 * Submits the purchase via a dynamically created POST form to avoid Error 999.
 */
export function goToConfirmation() {
    const eventIdInput = document.getElementById('currentEventId');
    const currentEventId = eventIdInput ? eventIdInput.value : window.currentEventId;

    if (!currentEventId) {
        console.error("No Event ID found");
        return;
    }

    executeSubmit(currentEventId);
}

// This allows the function to be called from an HTML
if (goToConfirmationBtn) {
    goToConfirmationBtn.addEventListener("click", goToConfirmation);
}

/**
 * Creates a dynamic form and submits it via POST
 */
function executeSubmit(currentEventId) {
    const totalPriceElement = document.getElementById('totalPrice');
    const sessionId = parseInt(document.getElementById('sessionSelect').value) || null;
    const ticketCount = parseInt(document.getElementById('ticketCount').value) || 0;
    const ticketZones = [];
    for (let i = 1; i <= ticketCount; i++) {
        ticketZones.push(parseInt(document.getElementById(`zone-select-${i}`)?.value) || null);
    }

    const totalRaw = totalPriceElement ? totalPriceElement.innerText : "0";
    const totalClean = totalRaw.replace('€', '').replace(',', '.').trim();

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');

    const email = document.querySelector('input[name="email"]').value;
    const emailConfirm = document.querySelector('input[name="emailConfirm"]').value;

    const cardNumber = parseInt(document.getElementById('cardNumber').value) || null;
    const cvv = parseInt(document.getElementById('CVV').value) || null;

    if (!htmlConfirmations(sessionId, ticketCount, ticketZones, email, emailConfirm, cardNumber, cvv)) {
        return;
    }

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/purchases/save';

    const fields = {
        'eventId': currentEventId,
        'totalPrice': totalClean,
        'sessionId': sessionId,
        'email': email,
    };

    if (csrfToken) {
        fields['_csrf'] = csrfToken;
    }

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

function htmlConfirmations(sessionElement, ticketCount, ticketZones, email, emailConfirm, cardNumber, cvv) {
    // Basic validation checks
    if (sessionElement === null) {
        showMsg("Debes seleccionar una sesión.", "error");
    } else if (ticketCount <= 0) {
        showMsg("Debes seleccionar al menos una entrada.", "error");
    } else if (ticketZones.includes(null)) {
        showMsg("Debes seleccionar una zona para cada entrada.", "error");
    } else if (!email || !emailConfirm) {
        showMsg("Debes introducir un correo electrónico", "error");
    } else if (email !== emailConfirm) {
        showMsg("Los correos electrónicos no coinciden.", "error");
    } else if (cardNumber === null || cvv === null) {
        showMsg("Debes introducir los datos de la tarjeta.", "error");
    } else if (cardNumber.toString().length !== 16 || cvv.toString().length !== 3) {
        showMsg("Los datos de la tarjeta no son válidos.", "error");
    }
    return true;
}

/**
 * Re-indexes tickets and updates de ticketCount dropdown
 */
export function reindexTickets() {
    const cards = document.querySelectorAll(".ticket-card");
    cards.forEach((card, index) => {
        card.querySelector("strong").innerText = `Ticket ${index + 1}`;
    });

    if (ticketSelect) {
        ticketSelect.value = cards.length;
    }
}

/**
 * Calculates the total price based on selected zones and active discounts.
 */
export function updateTotalPrice() {
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
            const isPercent = (opt.getAttribute('data-percent')) === "true";

            if (isPercent) {
                total -= (total * (amount / 100));
            } else {
                total -= amount;
            }
        }
    });

    if (total < 0) total = 0;
    const priceDisplay = document.getElementById('totalPrice');
    if (priceDisplay) priceDisplay.innerText = total.toFixed(2);
}

/** 
 * Changes the available options in discount dropdowns to prevent selecting the same discount multiple times.
 * This must be changed to delete the option from the select instead of disabling it
*/
export function updateAvailableDiscounts() {
    const selects = document.querySelectorAll(".discount-select");

    const selectedValues = [...selects]
        .map(s => s.value)
        .filter(v => v !== "");

    selects.forEach(select => {
        const currentValue = select.value;

        [...select.options].forEach(option => {
            if (option.value === "") return;
            option.disabled = (selectedValues.includes(option.value) && option.value !== currentValue);
        });
    });
}

// To charge the zones options in the generate tickets function
const zonesHTML = document.getElementById("zones-template")?.innerHTML || "";
/**
 * Generates ticket rows dynamically based on the selected count.
 */
export function generateTickets(count) {
    if (!ticketsContainer) return;
    ticketsContainer.innerHTML = "";
    for (let i = 0; i < count; i++) {
        ticketsContainer.insertAdjacentHTML("beforeend", `
            <div class="card mb-3 p-3 ticket-card">
                <div class="d-flex justify-content-between align-items-center">
                    <strong>Ticket ${i + 1}</strong>
                    <div class="d-flex gap-2 align-items-center">
                        <span>Zona:</span>
                        <select name="zone" class="form-select zone-select" id= "zone-select-${i + 1}">
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

//To charge the discounts options in the add discount button
const discountsHTML = document.getElementById("discounts-template")?.innerHTML || "";
/**
 * Adds a new discount dropdown to the UI.
 */
export function addDiscountSelect() {
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
    updateAvailableDiscounts();
}

document.addEventListener("change", e => {
    if (e.target.classList.contains("zone-select") ||
        e.target.classList.contains("discount-select") ||
        e.target.id === "sessionSelect") {
        updateTotalPrice();
        updateAvailableDiscounts();
    }
});

if (ticketsContainer) {
    ticketsContainer.addEventListener("click", e => {
        if (e.target.classList.contains("remove-ticket")) {
            e.target.closest(".ticket-card").remove();
            reindexTickets();
            updateTotalPrice();
        }
    });
}

if (discountContainer) {
    discountContainer.addEventListener("click", e => {
        if (e.target.classList.contains("remove-discount")) {
            e.target.closest(".discount-item").remove();
            updateTotalPrice();
            updateAvailableDiscounts();
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
