export function deleteImage(eventId, imageId) {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;

    Swal.fire({
        title: '¿Eliminar?',
        text: "Esta acción no se puede deshacer",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#e98747',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Eliminar',
        cancelButtonText: 'Cancelar',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            // IMPORTANTE: Verifica que esta ruta sea la misma que en tu RestController
            fetch(`/api/v1/images/admin/events/${eventId}/images/${imageId}`, {
                method: 'DELETE',
                headers: {
                    [header]: token
                },
                credentials: 'same-origin'
            })
            .then(response => {
                if (response.ok) {
                    Swal.fire(
                        '¡Eliminado!',
                        'La imagen ha sido borrada.',
                        'success'
                    );
                    document.getElementById(`image-container-${imageId}`).remove();
                } else {
                    throw new Error('Error en la respuesta del servidor');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire(
                    'Error',
                    'No se pudo borrar la imagen. Revisa los permisos o la consola.',
                    'error'
                );
            });
        }
    });
}

window.deleteImage = deleteImage;