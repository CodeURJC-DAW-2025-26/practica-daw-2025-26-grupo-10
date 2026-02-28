import { getCsrf, deleteItem, showSuccess, showError } from './confirmation.js';

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
      if (!date) { showError("Debes seleccionar una fecha"); return; }

      const data = new URLSearchParams();
      data.append('date', date);

      // CSRF
      const { token, header } = getCsrf();

      fetch('/admin/events/' + eventId + '/add_session', {
        method: 'POST',
        body: data,
        headers: { [header]: token }
      })
      .then(res => {
        if (!res.ok) throw new Error("No se pudo crear la sesión");
        showSuccess("Sesión creada");
        newRow.querySelector('.save-new').disabled = true;
      })
      .catch(err => showError(err.message));
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
        if (!newDate) {
          showError("Debes seleccionar una fecha");
          return;
        }

        const data = new URLSearchParams();
        data.append('newDate', newDate);
        data.append('sessionID', sessionId);

        // CSRF
        const { token, header } = getCsrf();

        fetch('/admin/events/' + eventId + '/update_session', {
          method: 'POST',
          body: data,
          headers: { [header]: token }
        })
        .then(res => {
          if (!res.ok) throw new Error("No se pudo actualizar la sesión");
          row.querySelector('.date-cell').textContent = newDate;
          row.querySelector('.actions-cell').innerHTML = `
            <button type="button" class="btn btn-sm btn-warning me-2 edit-session">Editar</button>
            <a class="btn btn-sm btn-danger delete-item" data-id="${sessionId}" data-url="/admin/events/delete_session">Eliminar</a>
          `;

          showSuccess("Sesión actualizada");
        })
        .catch(err => showError(err.message));
      });

    });

  });

  // DELETE SESSION
  document.addEventListener('click', function (e) {

    const btn = e.target.closest('.delete-item');
    if (!btn) return;

    const id = btn.dataset.id;
    const baseUrl = btn.dataset.url;

    if (!id || !baseUrl) return;

    deleteItem(baseUrl, btn);

  });
}