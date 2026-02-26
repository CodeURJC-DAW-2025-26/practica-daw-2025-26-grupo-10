
/**
 * @param {HTMLElement} element
 * @param {string} message
 */
export function confirmRemove(element, message = "Esta acción no se puede deshacer") {
    return Swal.fire({
        title: "¿Eliminar?",
        text: message,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Eliminar",
        cancelButtonText: "Cancelar"
    }).then(result => {
        if (result.isConfirmed) {
            element.remove();
            return true;
        }
        return false;
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

/**
 * @param {string} url
 * @param {string} confirmText
 */
function deleteItem(baseUrl, element) {
    const id = element.dataset.id;
    // NOTE: This uses SweetAlert2
    // Swal is the global object from SweetAlert2
    // Swal.fire shows a modal alert with the options you provide:
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Esta acción no se puede deshacer",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/admin${baseUrl}/${id}`, { method: 'DELETE' })
                .then(response => {
                    if (response.ok) {
                        Swal.fire('Eliminado', 'El elemento ha sido eliminado.', 'success')
                            .then(() => location.reload());
                    }
                });
        }
    });
}