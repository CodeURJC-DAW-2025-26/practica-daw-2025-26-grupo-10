const container = document.getElementById("artists-container");
const button = document.getElementById("load-button-artists");
const searchInput = document.getElementById("artistSearch");

let page = 0;
let searchTerm = "";

// cargar artistas
async function loadArtists(reset = false) {

  if (reset) {
    container.innerHTML = "";
    page = 0;
  }

  const res = await fetch(`/public/artists/fragments?page=${page}&search=${searchTerm}`);
  const html = await res.text();

  if (html.trim() === "") {
    button.style.display = "none";
    return;
  }

  container.insertAdjacentHTML("beforeend", html);
  button.style.display = "block";
  page++;
}

// escribir en el buscador
searchInput.addEventListener("input", e => {
  searchTerm = e.target.value;
  loadArtists(true);
});

// botón cargar más
button.addEventListener("click", () => loadArtists());

// carga inicial
loadArtists();