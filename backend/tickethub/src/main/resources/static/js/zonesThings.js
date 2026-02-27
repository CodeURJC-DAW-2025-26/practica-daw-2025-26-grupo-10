import { attachRemoveButton, cloneRow } from './confirmation.js';

// ----------------- ZONES -----------------
const zonesContainer = document.getElementById('zones-container');
const addZoneBtn = document.getElementById('add-zone');

if (zonesContainer && addZoneBtn) {

  let zoneTemplate = Array.from(
    zonesContainer.querySelectorAll('.zone-row')
  ).find(row => row.querySelector('.remove-zone')) || null;

  if (!zoneTemplate) {
    zoneTemplate = document.createElement('div');
    zoneTemplate.classList.add('row', 'g-2', 'mb-2', 'zone-row', 'align-items-center');
    zoneTemplate.innerHTML = `
      <div class="col-auto">
        <div>
          <p class="mb-0">Zona: 
            <input type="text" name="zoneName" class="form-control form-control-sm">
          </p>
          <p class="mb-0">Capacidad: 
            <input type="number" name="zoneCapacity" class="form-control form-control-sm">
          </p>
          <p class="mb-0">Precio: 
            <input type="number" step="0.01" name="zonePrice" class="form-control form-control-sm">
          </p>
        </div>
      </div>
      <div class="col-auto">
        <button type="button" class="btn btn-danger btn-sm remove-zone">
          Eliminar
        </button>
      </div>
    `;
  }

  zonesContainer
    .querySelectorAll('.zone-row')
    .forEach(r => attachRemoveButton(r, '.remove-zone'));

  addZoneBtn.addEventListener('click', () => {

    const emptyMsg = document.getElementById('no-zones-asociated');
    if (emptyMsg) {
      emptyMsg.closest('.zone-row').remove();
    }

    const clone = cloneRow(zoneTemplate, ['zoneCapacity'], ['zones']);
    attachRemoveButton(clone, '.remove-zone');
    zonesContainer.appendChild(clone);
  });
}