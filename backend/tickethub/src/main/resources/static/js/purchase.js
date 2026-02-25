const ticketSelect = document.getElementById("ticketCount");
const ticketsContainer = document.getElementById("ticketsContainer");

const discountContainer = document.getElementById("discountContainer");
const addDiscountBtn = document.getElementById("addDiscount");

let ticketCount = 0;

//Ticket generation
function generateTickets(count) {
  ticketsContainer.replaceChildren();
  ticketCount = count;

  for (let i = 0; i < count; i++) {
    ticketsContainer.insertAdjacentHTML("beforeend", `
      <div class="card mb-3 p-3 ticket-card">
        <div class="d-flex justify-content-between align-items-center">

          <strong>Entrada ${i + 1}</strong>

          <div class="d-flex gap-2 align-items-center">
            <span>Zona:</span>
            <select name="tickets[${i}].zoneId" class="form-select zone-select">
              ${zonesHTML}
            </select>
          </div>

          <a class="btn btn-sm btn-danger remove-ticket">Eliminar</a>

        </div>
      </div>
    `);
  }
}

// remove ticket
ticketsContainer.addEventListener("click", e => {
  if (e.target.classList.contains("remove-ticket")) {
    e.target.closest(".ticket-card").remove();
  }
});

//change tickets
ticketSelect.addEventListener("change", e => {
  generateTickets(e.target.value);
});

// load tickets
generateTickets(ticketSelect.value);

//Discounts
function addDiscountSelect() {
  const index = discountContainer.children.length;

  discountContainer.insertAdjacentHTML("beforeend", `
    <div class="mb-2 discount-item">
      <select name="discounts[${index}].id" class="form-select w-auto d-inline-block">
        ${discountsHTML}
      </select>
    </div>
  `);
}

addDiscountSelect();

addDiscountBtn.addEventListener("click", addDiscountSelect);