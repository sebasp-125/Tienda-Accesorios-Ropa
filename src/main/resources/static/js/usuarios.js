const searchUserInput = document.getElementById("searchUser");
const usersTable = document.getElementById("usersTable");

if (searchUserInput && usersTable) {
    searchUserInput.addEventListener("input", function () {
        const search = this.value.toLowerCase().trim();
        const rows = usersTable.querySelectorAll("tr");

        rows.forEach((row) => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(search) ? "" : "none";
        });
    });
}
