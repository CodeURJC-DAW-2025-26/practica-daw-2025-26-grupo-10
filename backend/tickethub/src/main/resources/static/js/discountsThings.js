import { cloneRow } from './confirmation.js';

const discountsContainer = document.getElementById('discounts-container');
let discountTemplate = Array.from(
    discountsContainer.querySelectorAll('.discount-row')
  ).find(row => row.querySelector('.remove-discount')) || null;

// if there is no discounts associated, there's only the row with the message
if (!discountTemplate) {
  discountTemplate = document.createElement('div');
  discountTemplate.classList.add('row', 'g-2', 'mb-2', 'discount-row', 'align-items-center');
  discountTemplate.innerHTML = `
    <div class="col">
      <select name="discounts" class="form-select">
        <option value="">Seleccionar descuento</option>
        ${document.querySelector('select[name="discounts"]')?.innerHTML || ''}
      </select>
    </div>
    <div class="col-auto">
      <button type="button" class="btn btn-danger btn-sm remove-discount">
        Eliminar
      </button>
    </div>
  `;
}

function attachDiscountRemove(row) {
  const btn = row.querySelector('.remove-discount');
  if (!btn) return;

  btn.addEventListener('click', () => {
    row.remove();

    // Si no quedan filas con remove-discount, volver a poner mensaje
    if (!discountsContainer.querySelector('.remove-discount')) {
      const emptyRow = document.createElement('div');
      emptyRow.classList.add('row', 'g-2', 'mb-2', 'discount-row', 'align-items-center');
      emptyRow.innerHTML = `
        <div class="col" id="no-discounts-asociated">
          <p>Aún no hay descuentos asociados a este evento</p>
        </div>
      `;
      discountsContainer.appendChild(emptyRow);
    }
  });
}

discountsContainer.querySelectorAll('.discount-row').forEach(r => attachDiscountRemove(r, '.remove-discount'));

document.getElementById('add-discount').addEventListener('click', () => {
  
  const emptyMsg = document.getElementById('no-discounts-asociated');
  if (emptyMsg) {
    emptyMsg.closest('.discount-row').remove();
  }

  const clone = cloneRow(discountTemplate, ['discountName', 'discountValue'], ['discountType']);
  attachDiscountRemove(clone, '.remove-discount');
  discountsContainer.appendChild(clone);
});
