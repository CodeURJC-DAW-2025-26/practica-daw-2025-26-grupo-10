async function loadMorePurchases(button, userId) {
    try {
        let nextPage = button.getAttribute("data-next-page"); l
        button.innerText = "Cargando...";
        button.disabled = true;

        const response = await fetch(`/purchases/me/more?pageNumber=${nextPage}`);
        const htmlFragment = await response.text();

        const tbody = document.getElementById("purchase-table-body");
        tbody.insertAdjacentHTML("beforeend", htmlFragment);

        const indicator = document.querySelector(".has-next-indicator");
        if (indicator) {
            const hasNext = (indicator.value === "true");
            const noMoreMsg = document.getElementById("no-more-msg");
            if (!hasNext) {
                button.parentElement.remove();
                if (noMoreMsg) noMoreMsg.style.display = "block";
            } else {
                button.setAttribute("data-next-page", parseInt(nextPage) + 1);
                button.innerText = "Cargar más";
                button.disabled = false;
            }
            indicator.remove();
        }
    } catch (error) {
        console.error("Falló la carga de más compras:", error);
        button.innerText = "Error. Reintentar";
        button.disabled = false;
    }
}

async function toggleTickets(purchaseId, button) {
    const parentRow = button.closest('tr');
    if (!parentRow) return;
    let detailsRow = parentRow.nextElementSibling;

    if (detailsRow && detailsRow.classList.contains('details-row')) {
        const isHidden = detailsRow.classList.toggle('d-none');
        button.innerText = isHidden ? "Ver detalles" : "Ocultar detalles";
        return;
    }

    try {
        button.disabled = true;
        button.innerText = "Cargando...";

        const response = await fetch(`/purchases/${purchaseId}/tickets`);

        if (!response.ok) {
            if (response.status === 403) {
                window.location.href = "/public/error/403";
                return;
            } else if (response.status === 404) {
                window.location.href = "/public/error/404";
                return;
            } else {
                throw new Error("Error inesperado");
            }
        }

        const htmlFragment = await response.text();
        parentRow.insertAdjacentHTML('afterend', htmlFragment);

        button.innerText = "Ocultar detalles";
        button.disabled = false;

    } catch (error) {
        console.error(error);
        button.innerText = "Error";
        button.disabled = false;
    }
}
