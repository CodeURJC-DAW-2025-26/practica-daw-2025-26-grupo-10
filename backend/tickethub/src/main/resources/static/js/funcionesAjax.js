// Función asíncrona que será llamada desde el HTML
async function cargarMasCompras(boton) {
    try {
        let nextPage = boton.getAttribute("data-next-page");l
        boton.innerText = "Cargando...";
        boton.disabled = true;

        const response = await fetch(`/purchases/me/more?page=${nextPage}`);
        
        if (!response.ok) {
            throw new Error("Error en la petición AJAX");
        }

        const htmlFragment = await response.text();
        const tbody = document.getElementById("purchase-table-body");
        tbody.insertAdjacentHTML("beforeend", htmlFragment);

        boton.setAttribute("data-next-page", parseInt(nextPage) + 1);
        boton.innerText = "Cargar más";
        boton.disabled = false;
        if (htmlFragment.trim() === "") {
            boton.style.display = "none";
        }

    } catch (error) {
        console.error("Falló la carga de más compras:", error);
        boton.innerText = "Error. Reintentar";
        boton.disabled = false;
    }
}
