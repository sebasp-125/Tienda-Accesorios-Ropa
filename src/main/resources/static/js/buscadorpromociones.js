document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("searchPromotion");
    const rows = document.querySelectorAll(".promotion-row");
    if (searchInput) {
        searchInput.addEventListener("input", function () {
            const value = this.value.toLowerCase().trim();
            rows.forEach((row) => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(value) ? "" : "none";
            });
        });
    }
});