const searchInput = document.getElementById("searchClient");
const table = document.getElementById("clientsTable");

searchInput.addEventListener("input", function () {
    const search = this.value.toLowerCase().trim();

    const rows = table.querySelectorAll("tr");

    rows.forEach((row) => {
        const text = row.textContent.toLowerCase();

        row.style.display = text.includes(search) ? "" : "none";
    });
});