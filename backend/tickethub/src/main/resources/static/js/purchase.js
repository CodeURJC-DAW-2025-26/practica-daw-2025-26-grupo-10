const ticketSelect = document.getElementById("ticketCount");
const ticketsContainer = document.getElementById("ticketsContainer");

const discountContainer = document.getElementById("discountContainer");
const addDiscountBtn = document.getElementById("addDiscount");

let ticketCount = 0;

// 🎟 GENERAR ENTRADAS
function generateTickets(count) {
  ticketsContainer.innerHTML = "";
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

          <a class="btn btn-sm btn-danger">Eliminar</a>

        </div>
      </div>
    `);
  }
}

// eliminar ticket
ticketsContainer.addEventListener("click", e => {
  if (e.target.classList.contains("remove-ticket")) {
    e.target.closest(".ticket-card").remove();
  }
});

// cambio número entradas
ticketSelect.addEventListener("change", e => {
  generateTickets(e.target.value);
});

// carga inicial
generateTickets(ticketSelect.value);

// 🎟 DESCUENTOS DINÁMICOS

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

// primer descuento automático
addDiscountSelect();

addDiscountBtn.addEventListener("click", addDiscountSelect);