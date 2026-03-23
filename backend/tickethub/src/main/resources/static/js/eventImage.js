import { getCsrf, showConfirmation } from './confirmation.js';

/**
 * Function to delete images from an event
 * @param {string} url 
 * @param {string} id
 * @param {HTMLElement} button 
 */
export function deleteEventImage(url, id, button) {
    const { token, header } = getCsrf();
    const fullUrl = `${url}/${id}`;

    showConfirmation(() => {
        fetch(fullUrl, {
            method: "DELETE",
            headers: {
                [header]: token
            }
        })
        .then(res => {
            if (!res.ok) throw new Error("No se pudo eliminar la imagen");
            
            const container = document.getElementById(`image-container-${id}`);
            if (container) {
                container.remove();
            }

            Swal.fire("Eliminado", "La imagen ha sido borrada.", "success");
        })
        .catch(err => Swal.fire("Error", err.message, "error"));
    });
}

document.addEventListener('click', (e) => {
    const btn = e.target.closest('.delete-item');
    if (!btn) return;

    const url = btn.dataset.url;
    const id = btn.dataset.id;
    
    if (url && url.includes('/image')) {
        e.preventDefault();
        deleteEventImage(url, id, btn);
    }
});