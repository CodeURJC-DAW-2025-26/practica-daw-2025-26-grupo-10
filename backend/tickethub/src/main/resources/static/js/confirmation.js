
/**
 * @param {HTMLElement} element
 * @param {string} message
 */
function showConfirmation(onConfirm, message = "Esta acción no se puede deshacer") {
    Swal.fire({
        title: "¿Eliminar?",
        text: message,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Eliminar",
        cancelButtonText: "Cancelar"
    }).then(result => {
        if (result.isConfirmed) onConfirm();
    });
}

/**
 * @param {string} msg
 */
export function showMsg(msg){
    Swal.fire( 'Información', msg, 'info');
}

/**
 * @param {string} msg
 */
export function showError(msg) {
    Swal.fire("Error", msg, "error");
}

/**
 * @param {string} msg
 */
export function showSuccess(msg) {
    Swal.fire("Éxito", msg, "success");
}

export function getCsrf() {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    return { token, header };
}

/**
 * @param {string} url
 * @param {HTMLElement} element
 */
export function deleteItem(url, element) {
  const { token, header } = getCsrf();
  fetch(url, {
        method: "DELETE",
        headers: {
            [header]: token
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("No se pudo eliminar");
        element.closest("tr")?.remove();
        Swal.fire("Eliminado", "El elemento ha sido eliminado.", "success");
    })
    .catch(err => Swal.fire("Error", err.message, "error"));
}



//To show the error message in the create/edit_event and create/edit artist pages
const form = document.querySelector('form[action*="edit_event"], form[action*="create_event"]');

if (form) {
  form.addEventListener('submit', e => {
    const files = form.querySelector('[name="images"]').files;

    for (const file of files) {
      if (!/\.(jpg|jpeg|png|webp)$/i.test(file.name)) {
        e.preventDefault();
        Swal.fire("Error", "Solo se permiten imágenes (.jpg, .jpeg, .png, .webp)", "error");
        return;
      }
    }
  });
}

// ----------------- FUNCTIONS -----------------
document.addEventListener('click', (e) => {
  const btn = e.target.closest('.delete-item');
  if (!btn) return;

  const baseUrl = btn.dataset.url;
  const id = btn.dataset.id;
  if (!baseUrl || !id) return;

  showConfirmation(() => deleteItem(`${baseUrl}/${id}`, btn));
});
/**
 * Attaches a click event listener to a button inside a given row
 * that removes the row from the DOM when clicked.
 *
 * @param {HTMLElement} row - The row element containing the button.
 * @param {string} selector - The CSS selector for the button within the row.
 */
export function attachRemoveButton(row, selector) {
  const btn = row.querySelector(selector);
  if (btn) btn.addEventListener('click', () => row.remove());
}

export function cloneRow(template, inputsToReset = [], selectsToReset = []) {
  const clone = template.cloneNode(true);
  inputsToReset.forEach(name => {
    const input = clone.querySelector(`[name="${name}"]`); //return the first element with that CSS name
    if (input) input.value = '';
  });
  selectsToReset.forEach(name => {
    const select = clone.querySelector(`[name="${name}"]`);
    if (select) select.selectedIndex = 0;
  });
  return clone;
}