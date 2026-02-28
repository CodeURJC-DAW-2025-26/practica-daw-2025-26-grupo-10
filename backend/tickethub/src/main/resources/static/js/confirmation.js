

/**
 * @param {Function} onConfirm
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


function getCsrf() {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    return { token, header };
}

/**
 * @param {string} url
 * @param {HTMLElement} element
 */
function deleteItem(url, element) {
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

// ----------------- EVENT LISTENER GLOBAL -----------------
document.addEventListener("click", e => {
    const btn = e.target.closest(".delete-item");
    if (!btn) return;

    const baseUrl = btn.dataset.url;
    const id = btn.dataset.id;
    if (!baseUrl || !id) return;

    showConfirmation(() => deleteItem(`${baseUrl}/${id}`, btn));
});