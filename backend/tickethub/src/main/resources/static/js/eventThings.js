/*LO QUE FALTA POR PONER EN ESTE JS:
- De session: NADA, YA LO HE HECHO Y FUNCIONA
- De discount: arreglar que se puedan crear líneas de descuentos si no hay ninguna (si eliminas todas el botón se bloquea)
- De zone: hacer exactamente lo mismo que con discounts (que puedan añadirse filas haya o no haya ninguna)

LOS CRUD DE ZONE Y DISCOUNT FUNCIONAN PERFECTAMENTE, LO PUEDES COMPROBAR EN LAS RUTAS DE /ADMIN/ZONES/MANAGE_ZONES Y /ADMIN/DISCOUNTS/MANAGE_DISCOUNTS
ASÍ QUE TIENES QUE TIENES QUE LINKEAR LAS RUTAS DEL BACKEND CON LO DEL JS

PARA COMPROBAR SI FUNCIONA LO DE DISCOUNT O ZONE AÑÁDELE AL EVENTO QUE QUIERAS EN EL DATABASEINITIALIZER UNA LISTA DE DESCUENTOS Y ZONAS VÁLIDA
(AHORA MISMO TODAS ESAS ESTÁN VACÍAS Y LOS ÚNICOS EVENTOS QUE TIENEN SESSIONS SON LOS DOS PRIMEROS)
*/

//Functions to delete session or discount
function attachSessionListeners(row) {
  const btn = row.querySelector('.remove-session');
  if (btn) btn.addEventListener('click', () => row.remove());
}

function attachDiscountListeners(row) {
  const btn = row.querySelector('.remove-discount');
  if (btn) btn.addEventListener('click', () => row.remove());
}

document.addEventListener('DOMContentLoaded', () => {

  //For Discounts (This is working to create new rows but only when there is one or more discounts,
  //if there are no discounts the add button doesn't work,
  //this is because the template is being cloned from an existing row, so if there are no rows to clone it doesn't work)
  document.querySelectorAll('.discount-row').forEach(attachDiscountListeners);

  const addDiscountBtn = document.getElementById('add-discount');
  if (addDiscountBtn) {
    addDiscountBtn.addEventListener('click', () => {
      const container = document.getElementById('discounts-container');
      const template = container.querySelector('.discount-row').cloneNode(true);
      template.querySelector('select').selectedIndex = 0;
      container.appendChild(template);
      attachDiscountListeners(template);
    });
  }

});

//For the create and edit functions of sessions
document.addEventListener('DOMContentLoaded', () => {

  //Consts to get all the elements we need to work with sessions
  const sessionsBody = document.getElementById('sessions-body');
  const addBtn = document.getElementById('add-session');
  const eventId = addBtn.dataset.eventid;

  // Add new session (working)
  addBtn.addEventListener('click', () => {

    //To delete the noRow message when there are no sessions
    const noRow = document.getElementById('no-sessions-row');
    if (noRow) noRow.remove();

    //This creates a new row for the session
    const newRow = document.createElement('tr');
    newRow.classList.add('session-row');
    newRow.innerHTML = `
      <td>
        <input type="datetime-local" class="form-control new-date">
      </td>
      <td class="text-center">
        <button class="btn btn-success btn-sm save-new">Guardar</button>
      </td>
    `;

    //This adds the new row to the table
    sessionsBody.appendChild(newRow);

    //The method to save the new session that links with the backend method with the route
    newRow.querySelector('.save-new').addEventListener('click', () => {
      const date = newRow.querySelector('.new-date').value;

      const data = new URLSearchParams();
      data.append('date', date);
      fetch('/admin/events/' + eventId + '/add_session', {
        method: 'POST',
        body: data
      })
      .then(res => {
        if (res.ok) location.reload();
      });

    });

  });

  // Edit existing session working
  document.querySelectorAll('.edit-session').forEach(btn => {

    btn.addEventListener('click', function () {

      const row = this.closest('tr');
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

});