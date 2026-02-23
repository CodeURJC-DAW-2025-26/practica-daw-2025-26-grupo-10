const container = document.getElementById("artists-container");
const button = document.getElementById("load-button-artists");
const searchInput = document.getElementById("artistSearch");

let page = 1;
let searchTerm = "";

// load artists
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

// writing in search bar
searchInput.addEventListener("input", e => {
  searchTerm = e.target.value;
  loadArtists(true);
});

// load more button
button.addEventListener("click", () => loadArtists());
