// ----------------- FUNCTIONS -----------------
function attachRemoveButton(row, selector) {
  const btn = row.querySelector(selector);
  if (btn) btn.addEventListener('click', () => row.remove());
}

function cloneRow(template, inputsToReset = [], selectsToReset = []) {
  const clone = template.cloneNode(true);
  inputsToReset.forEach(name => {
    const input = clone.querySelector(`[name="${name}"]`);
    if (input) input.value = '';
  });
  selectsToReset.forEach(name => {
    const select = clone.querySelector(`[name="${name}"]`);
    if (select) select.selectedIndex = 0;
  });
  return clone;
}

// ----------------- ZONES -----------------
const zonesContainer = document.getElementById('zones-container');
let zoneTemplate = zonesContainer.querySelector('.zone-row');

if (!zoneTemplate) {
  zoneTemplate = document.createElement('div');
  zoneTemplate.classList.add('row', 'g-2', 'mb-2', 'zone-row', 'align-items-center');
  zoneTemplate.innerHTML = `
    <div class="col">
      <select name="zones" class="form-select">
        {{#allZones}}
          <option value="{{id}}">{{name}}</option>
        {{/allZones}}
      </select>
    </div>
    <div class="col">
      <input type="number" name="zoneCapacity" class="form-control" placeholder="Capacidad">
    </div>
    <div class="col-auto">
      <button type="button" class="btn btn-danger btn-sm remove-zone">Eliminar</button>
    </div>
  `;
}

zonesContainer.querySelectorAll('.zone-row').forEach(r => attachRemoveButton(r, '.remove-zone'));

document.getElementById('add-zone').addEventListener('click', () => {
  const clone = cloneRow(zoneTemplate, ['zoneCapacity'], ['zones']);
  attachRemoveButton(clone, '.remove-zone');
  zonesContainer.appendChild(clone);
});

// ----------------- DISCOUNTS -----------------
const discountsContainer = document.getElementById('discounts-container');
let discountTemplate = discountsContainer.querySelector('.discount-row');

if (!discountTemplate) {
  discountTemplate = document.createElement('div');
  discountTemplate.classList.add('row', 'g-2', 'mb-2', 'discount-row', 'align-items-center');
  discountTemplate.innerHTML = `
    <div class="col">
      <input type="text" name="discountName" class="form-control" placeholder="Nombre del descuento">
    </div>
    <div class="col">
      <select name="discountType" class="form-select">
        <option value="amount">Cantidad</option>
        <option value="percent">Porcentaje</option>
      </select>
    </div>
    <div class="col">
      <input type="number" step="0.01" name="discountValue" class="form-control" placeholder="Valor del descuento">
    </div>
    <div class="col-auto">
      <button type="button" class="btn btn-danger btn-sm remove-discount">Eliminar</button>
    </div>
  `;
}

discountsContainer.querySelectorAll('.discount-row').forEach(r => attachRemoveButton(r, '.remove-discount'));

document.getElementById('add-discount').addEventListener('click', () => {
  const clone = cloneRow(discountTemplate, ['discountName', 'discountValue'], ['discountType']);
  attachRemoveButton(clone, '.remove-discount');
  discountsContainer.appendChild(clone);
});

// ----------------- SESSIONS -----------------
const sessionsBody = document.getElementById('sessions-body');
const addSessionBtn = document.getElementById('add-session');

if (sessionsBody && addSessionBtn) {
  const eventId = addSessionBtn.dataset.eventid;

  // CREATE NEW SESSION
  addSessionBtn.addEventListener('click', () => {
    const noRow = document.getElementById('no-sessions-row');
    if (noRow) noRow.remove();

    const newRow = document.createElement('tr');
    newRow.classList.add('session-row');
    newRow.innerHTML = `
      <td><input type="datetime-local" class="form-control new-date"></td>
      <td class="text-center"><button class="btn btn-success btn-sm save-new">Guardar</button></td>
    `;
    sessionsBody.appendChild(newRow);

    newRow.querySelector('.save-new').addEventListener('click', () => {

      const date = newRow.querySelector('.new-date').value;

      if (!date) { alert("Debes seleccionar una fecha"); return; }

      const data = new URLSearchParams();
      data.append('date', date);
      fetch('/admin/events/' + eventId + '/add_session', {
        method: 'POST',
        body: data
      }).then(res => { if (res.ok) location.reload(); });
    });
  });

  // EDIT ACTUAL SESSION
  document.querySelectorAll('.edit-session').forEach(btn => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      const sessionId = row.dataset.id;
      const eventId = row.dataset.eventid;
      const dateCell = row.querySelector('.date-cell');
      const actionsCell = row.querySelector('.actions-cell');

      dateCell.innerHTML =
        `<input type="datetime-local" class="form-control edit-date">`;

      actionsCell.innerHTML =
        `<button class="btn btn-success btn-sm save-edit">
            Guardar
          </button>`;

      row.querySelector('.save-edit').addEventListener('click', () => {

        const newDate = row.querySelector('.edit-date').value;

        const data = new URLSearchParams();
        data.append('newDate', newDate);
        data.append('sessionID', sessionId);
        if (!newDate) {
          alert("Debes seleccionar una fecha");
          return;
        }

        fetch('/admin/events/' + eventId + '/update_session', {
          method: 'POST',
          body: data
        })
        .then(res => {
          if (res.ok) location.reload();
        });

      });

    });

  });
}