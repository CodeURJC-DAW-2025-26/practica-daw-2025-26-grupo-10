const container = document.getElementById("event-container");
const button = document.getElementById("load-button");

const filterArtist = document.getElementById("filterArtist");
const filterCategory = document.getElementById("filterCategory");

let page = 0;
let artist = "";
let category = "";

async function loadEvents(reset = false) {

  if (reset) {
    container.innerHTML = "";
    page = 0;
  }

  const res = await fetch(
    `/public/events/fragments?page=${page}&artist=${artist}&category=${category}`
  );

  const html = await res.text();

  if (html.trim() === "") {
    button.style.display = "none";
    return;
  }

  container.insertAdjacentHTML("beforeend", html);
  button.style.display = "inline-block";
  page++;
}

// filters
filterArtist.addEventListener("input", e => {
  artist = e.target.value;
  loadEvents(true);
});

filterCategory.addEventListener("change", e => {
  category = e.target.value;
  loadEvents(true);
});

// load more
button.addEventListener("click", () => loadEvents());

// initial events
loadEvents();