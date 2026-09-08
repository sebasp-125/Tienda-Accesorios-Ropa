const countries = [
    { name: "Colombia", flag: "🇨🇴", code: "+57" },
    { name: "Argentina", flag: "🇦🇷", code: "+54" },
    { name: "Bolivia", flag: "🇧🇴", code: "+591" },
    { name: "Brasil", flag: "🇧🇷", code: "+55" },
    { name: "Canadá", flag: "🇨🇦", code: "+1" },
    { name: "Chile", flag: "🇨🇱", code: "+56" },
    { name: "Costa Rica", flag: "🇨🇷", code: "+506" },
    { name: "Cuba", flag: "🇨🇺", code: "+53" },
    { name: "Ecuador", flag: "🇪🇨", code: "+593" },
    { name: "El Salvador", flag: "🇸🇻", code: "+503" },
    { name: "España", flag: "🇪🇸", code: "+34" },
    { name: "Estados Unidos", flag: "🇺🇸", code: "+1" },
    { name: "Guatemala", flag: "🇬🇹", code: "+502" },
    { name: "Honduras", flag: "🇭🇳", code: "+504" },
    { name: "México", flag: "🇲🇽", code: "+52" },
    { name: "Nicaragua", flag: "🇳🇮", code: "+505" },
    { name: "Panamá", flag: "🇵🇦", code: "+507" },
    { name: "Paraguay", flag: "🇵🇾", code: "+595" },
    { name: "Perú", flag: "🇵🇪", code: "+51" },
    { name: "Puerto Rico", flag: "🇵🇷", code: "+1" },
    { name: "República Dominicana", flag: "🇩🇴", code: "+1" },
    { name: "Uruguay", flag: "🇺🇾", code: "+598" },
    { name: "Venezuela", flag: "🇻🇪", code: "+58" },

    { name: "Alemania", flag: "🇩🇪", code: "+49" },
    { name: "Francia", flag: "🇫🇷", code: "+33" },
    { name: "Italia", flag: "🇮🇹", code: "+39" },
    { name: "Portugal", flag: "🇵🇹", code: "+351" },
    { name: "Reino Unido", flag: "🇬🇧", code: "+44" },

    { name: "Australia", flag: "🇦🇺", code: "+61" },
    { name: "China", flag: "🇨🇳", code: "+86" },
    { name: "Corea del Sur", flag: "🇰🇷", code: "+82" },
    { name: "India", flag: "🇮🇳", code: "+91" },
    { name: "Japón", flag: "🇯🇵", code: "+81" },
    { name: "Nueva Zelanda", flag: "🇳🇿", code: "+64" },
];

const countrySelector = document.getElementById("countrySelector");

const countryDropdown = document.getElementById("countryDropdown");

const countryList = document.getElementById("countryList");

const countrySearch = document.getElementById("countrySearch");

const selectedFlag = document.getElementById("selectedFlag");

const selectedDialCode = document.getElementById("selectedDialCode");

const telefonoNumero = document.getElementById("telefonoNumero");

const telefono = document.getElementById("telefono");

let selectedCountry = countries[0];

/* RENDER */

function renderCountries(filter = "") {
    countryList.innerHTML = "";

    const search = filter.toLowerCase().trim();

    const filtered = countries.filter(
        (country) =>
            country.name.toLowerCase().includes(search) ||
            country.code.includes(search),
    );

    filtered.forEach((country) => {
        const option = document.createElement("button");

        option.type = "button";

        option.className = "country-option";

        if (
            country.code === selectedCountry.code &&
            country.name === selectedCountry.name
        ) {
            option.classList.add("selected");
        }

        option.innerHTML = `
            <span class="country-flag">
                ${country.flag}
            </span>

            <span class="country-name">
                ${country.name}
            </span>

            <span class="country-code">
                ${country.code}
            </span>
        `;

        option.addEventListener("click", () => {
            selectCountry(country);
        });

        countryList.appendChild(option);
    });
}

/* SELECCIONAR PAIS */

function selectCountry(country) {
    selectedCountry = country;

    selectedFlag.textContent = country.flag;

    selectedDialCode.textContent = country.code;

    countryDropdown.classList.remove("open");

    countrySearch.value = "";

    renderCountries();

    updateFullPhone();

    telefonoNumero.focus();
}

/* ACTUALIZAR TELEFONO COMPLETO */

function updateFullPhone() {
    const number = telefonoNumero.value.trim();

    if (!number) {
        telefono.value = "";

        return;
    }

    telefono.value = `${selectedCountry.code} ${number}`;
}

/* ABRIR / CERRAR */

countrySelector.addEventListener("click", (event) => {
    event.stopPropagation();

    countryDropdown.classList.toggle("open");

    if (countryDropdown.classList.contains("open")) {
        countrySearch.focus();
    }
});

/* BUSQUEDA */

countrySearch.addEventListener("input", () => {
    renderCountries(countrySearch.value);
});

/* CERRAR AL HACER CLICK AFUERA */

document.addEventListener("click", (event) => {
    if (
        !countryDropdown.contains(event.target) &&
        !countrySelector.contains(event.target)
    ) {
        countryDropdown.classList.remove("open");
    }
});

/* TELEFONO */

telefonoNumero.addEventListener("input", () => {
    updateFullPhone();
});

/* ANTES DE ENVIAR */

document.querySelector("form").addEventListener("submit", () => {
    updateFullPhone();
});

/* INICIALIZAR */

renderCountries();