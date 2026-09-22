const searchInput = document.getElementById("searchProvider");
const table = document.getElementById("providersTable");
searchInput.addEventListener("input", function () {
    const search = this.value.toLowerCase().trim();
    const rows = table.querySelectorAll("tr");
    rows.forEach((row) => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(search) ? "" : "none";
    });
});