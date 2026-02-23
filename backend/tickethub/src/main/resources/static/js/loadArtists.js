const url = 'artists/fragments?page='; 
const buttonSelector = 'load-button-artists';
const containerSelector = 'artists-container';
let actualPage = 1;

document.getElementById(buttonSelector).addEventListener('click', function() {
    const container = document.getElementById(containerSelector);
    const auxButton = this;

    fetch(url + actualPage)
        .then(response => response.text()) // text() because we receive HTML not JSON
        .then(htmlReceived => {
            // if it's an empty HTML
            if (htmlReceived.trim() === "") {
                auxButton.innerText = "No hay más artistas";
                auxButton.disabled = true;
                return;
            }
            // if we receive an HTML we insert it just before the end of the artist-container
            container.insertAdjacentHTML('beforeend', htmlReceived);
            actualPage++;
        })
        .catch(error => console.error("Error al cargar el fragmento de artista:", error));
})