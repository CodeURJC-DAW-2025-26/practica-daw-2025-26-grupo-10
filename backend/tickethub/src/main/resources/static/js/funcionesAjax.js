// Función asíncrona que será llamada desde el HTML
async function cargarMasCompras(boton,userId) {
    try {
        let nextPage = boton.getAttribute("data-next-page");l
        boton.innerText = "Cargando...";
        boton.disabled = true;

        const response = await fetch(`/purchases/me/${userId}/more?pageNumber=${nextPage}`);
        const htmlFragment = await response.text();

        const tbody = document.getElementById("purchase-table-body");
        tbody.insertAdjacentHTML("beforeend", htmlFragment);

        const indicator = document.querySelector(".has-next-indicator");
        if (indicator) {
            const hasNext = (indicator.value === "true");
            const noMoreMsg = document.getElementById("no-more-msg");
            if (!hasNext) {
                boton.parentElement.remove();
                if (noMoreMsg) noMoreMsg.style.display = "block";
            } else {
                boton.setAttribute("data-next-page", parseInt(nextPage) + 1);
                boton.innerText = "Cargar más";
                boton.disabled = false;
            }
            indicator.remove();
        }

    } catch (error) {
        console.error("Falló la carga de más compras:", error);
        boton.innerText = "Error. Reintentar";
        boton.disabled = false;
    }
}

async function toggleTickets(purchaseId, boton) {
    const parentRow = boton.closest('tr');
    if (!parentRow) return;
    let detailsRow = parentRow.nextElementSibling;

    if (detailsRow && detailsRow.classList.contains('details-row')) {
        const estaOculta = detailsRow.classList.toggle('d-none');
        boton.innerText = estaOculta ? "Ver detalles" : "Ocultar detalles";
        return;
    }

    try {
        boton.disabled = true;
        boton.innerText = "Cargando...";

        const response = await fetch(`/purchases/${purchaseId}/tickets`);
        if (!response.ok) throw new Error("Error al cargar tickets");
        const htmlFragment = await response.text();
        parentRow.insertAdjacentHTML('afterend', htmlFragment);
        
        boton.innerText = "Ocultar detalles";
        boton.disabled = false;

    } catch (error) {
        console.error(error);
        boton.innerText = "Error";
        boton.disabled = false;
    }
}
