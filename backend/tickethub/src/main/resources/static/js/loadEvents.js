const container = document.getElementById("event-container");
const button = document.getElementById("load-button");

const filterArtist = document.getElementById("filterArtist");
const filterCategory = document.getElementById("filterCategory");
const filterDate = document.getElementById("filterDate");

let page = 0;
let artist = "";
let category = "";
let date = "";

/**
 * Loads events from the server and appends them to the container.
 * @param {boolean} reset - If true, clears the current list and starts from page 0.
 */
async function loadEvents(reset = false) {

  if (reset) {
    container.replaceChildren(); // Clear current events
    page = 0;
  }

  // Fetching fragments with all active filters
  const res = await fetch(
    `/public/events/fragments?page=${page}&artist=${artist}&category=${category}&date=${date}`
  );

  const html = await res.text();

  // If no content is returned, hide the "Load more" button
  if (html.trim() === "") {
    button.classList.add("d-none");
    return;
  }

  container.insertAdjacentHTML("beforeend", html);
  button.classList.remove("d-none");
  page++;
}

// Artist Filter: Triggers on every keystroke
filterArtist.addEventListener("input", e => {
  artist = e.target.value;
  loadEvents(true);
});

// Category Filter: Triggers when a new option is selected
filterCategory.addEventListener("change", e => {
  category = e.target.value;
  loadEvents(true);
});

// Date Filter: Triggers when a date is picked or cleared
filterDate.addEventListener("change", e => {
  date = e.target.value; // Format: YYYY-MM-DD
  loadEvents(true);
});

// Load More Button: Fetches the next page of results
button.addEventListener("click", () => loadEvents());

// Initial load on page startup
loadEvents();