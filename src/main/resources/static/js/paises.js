const countries = [
    { name: "Afganistán", flag: "🇦🇫", code: "+93" },
    { name: "Albania", flag: "🇦🇱", code: "+355" },
    { name: "Alemania", flag: "🇩🇪", code: "+49" },
    { name: "Andorra", flag: "🇦🇩", code: "+376" },
    { name: "Angola", flag: "🇦🇴", code: "+244" },
    { name: "Antigua y Barbuda", flag: "🇦🇬", code: "+1 268" },
    { name: "Arabia Saudita", flag: "🇸🇦", code: "+966" },
    { name: "Argelia", flag: "🇩🇿", code: "+213" },
    { name: "Argentina", flag: "🇦🇷", code: "+54" },
    { name: "Armenia", flag: "🇦🇲", code: "+374" },
    { name: "Australia", flag: "🇦🇺", code: "+61" },
    { name: "Austria", flag: "🇦🇹", code: "+43" },
    { name: "Azerbaiyán", flag: "🇦🇿", code: "+994" },

    { name: "Bahamas", flag: "🇧🇸", code: "+1 242" },
    { name: "Bangladés", flag: "🇧🇩", code: "+880" },
    { name: "Barbados", flag: "🇧🇧", code: "+1 246" },
    { name: "Baréin", flag: "🇧🇭", code: "+973" },
    { name: "Bélgica", flag: "🇧🇪", code: "+32" },
    { name: "Belice", flag: "🇧🇿", code: "+501" },
    { name: "Benín", flag: "🇧🇯", code: "+229" },
    { name: "Bielorrusia", flag: "🇧🇾", code: "+375" },
    { name: "Bolivia", flag: "🇧🇴", code: "+591" },
    { name: "Bosnia y Herzegovina", flag: "🇧🇦", code: "+387" },
    { name: "Botsuana", flag: "🇧🇼", code: "+267" },
    { name: "Brasil", flag: "🇧🇷", code: "+55" },
    { name: "Brunéi", flag: "🇧🇳", code: "+673" },
    {
        name: "Bulgaria",
        flag: "🇧🇬",
        code: "+359",
    },
    { name: "Burkina Faso", flag: "🇧🇫", code: "+226" },
    { name: "Burundi", flag: "🇧🇮", code: "+257" },

    { name: "Cabo Verde", flag: "🇨🇻", code: "+238" },
    { name: "Camboya", flag: "🇰🇭", code: "+855" },
    { name: "Camerún", flag: "🇨🇲", code: "+237" },
    { name: "Canadá", flag: "🇨🇦", code: "+1" },
    { name: "Catar", flag: "🇶🇦", code: "+974" },
    { name: "Chad", flag: "🇹🇩", code: "+235" },
    { name: "Chile", flag: "🇨🇱", code: "+56" },
    { name: "China", flag: "🇨🇳", code: "+86" },
    { name: "Chipre", flag: "🇨🇾", code: "+357" },
    { name: "Ciudad del Vaticano", flag: "🇻🇦", code: "+39" },
    { name: "Colombia", flag: "🇨🇴", code: "+57" },
    { name: "Comoras", flag: "🇰🇲", code: "+269" },
    { name: "Congo", flag: "🇨🇬", code: "+242" },
    { name: "Corea del Norte", flag: "🇰🇵", code: "+850" },
    { name: "Corea del Sur", flag: "🇰🇷", code: "+82" },
    { name: "Costa de Marfil", flag: "🇨🇮", code: "+225" },
    { name: "Costa Rica", flag: "🇨🇷", code: "+506" },
    { name: "Croacia", flag: "🇭🇷", code: "+385" },
    { name: "Cuba", flag: "🇨🇺", code: "+53" },

    { name: "Dinamarca", flag: "🇩🇰", code: "+45" },
    { name: "Dominica", flag: "🇩🇲", code: "+1 767" },

    { name: "Ecuador", flag: "🇪🇨", code: "+593" },
    { name: "Egipto", flag: "🇪🇬", code: "+20" },
    { name: "El Salvador", flag: "🇸🇻", code: "+503" },
    { name: "Emiratos Árabes Unidos", flag: "🇦🇪", code: "+971" },
    { name: "Eritrea", flag: "🇪🇷", code: "+291" },
    { name: "Eslovaquia", flag: "🇸🇰", code: "+421" },
    { name: "Eslovenia", flag: "🇸🇮", code: "+386" },
    { name: "España", flag: "🇪🇸", code: "+34" },
    { name: "Estados Unidos", flag: "🇺🇸", code: "+1" },
    { name: "Estonia", flag: "🇪🇪", code: "+372" },
    { name: "Esuatini", flag: "🇸🇿", code: "+268" },
    { name: "Etiopía", flag: "🇪🇹", code: "+251" },

    { name: "Fiji", flag: "🇫🇯", code: "+679" },
    { name: "Filipinas", flag: "🇵🇭", code: "+63" },
    { name: "Finlandia", flag: "🇫🇮", code: "+358" },
    { name: "Francia", flag: "🇫🇷", code: "+33" },

    { name: "Gabón", flag: "🇬🇦", code: "+241" },
    { name: "Gambia", flag: "🇬🇲", code: "+220" },
    { name: "Georgia", flag: "🇬🇪", code: "+995" },
    { name: "Ghana", flag: "🇬🇭", code: "+233" },
    { name: "Granada", flag: "🇬🇩", code: "+1 473" },
    { name: "Grecia", flag: "🇬🇷", code: "+30" },
    { name: "Guatemala", flag: "🇬🇹", code: "+502" },
    { name: "Guinea", flag: "🇬🇳", code: "+224" },
    { name: "Guinea-Bisáu", flag: "🇬🇼", code: "+245" },
    { name: "Guinea Ecuatorial", flag: "🇬🇶", code: "+240" },
    { name: "Guyana", flag: "🇬🇾", code: "+592" },

    { name: "Haití", flag: "🇭🇹", code: "+509" },
    { name: "Honduras", flag: "🇭🇳", code: "+504" },
    { name: "Hungría", flag: "🇭🇺", code: "+36" },

    { name: "India", flag: "🇮🇳", code: "+91" },
    { name: "Indonesia", flag: "🇮🇩", code: "+62" },
    { name: "Irak", flag: "🇮🇶", code: "+964" },
    { name: "Irán", flag: "🇮🇷", code: "+98" },
    { name: "Irlanda", flag: "🇮🇪", code: "+353" },
    { name: "Islandia", flag: "🇮🇸", code: "+354" },
    { name: "Islas Marshall", flag: "🇲🇭", code: "+692" },
    { name: "Islas Salomón", flag: "🇸🇧", code: "+677" },
    { name: "Israel", flag: "🇮🇱", code: "+972" },
    { name: "Italia", flag: "🇮🇹", code: "+39" },

    { name: "Jamaica", flag: "🇯🇲", code: "+1 876" },
    { name: "Japón", flag: "🇯🇵", code: "+81" },
    { name: "Jordania", flag: "🇯🇴", code: "+962" },

    { name: "Kazajistán", flag: "🇰🇿", code: "+7" },
    { name: "Kenia", flag: "🇰🇪", code: "+254" },
    { name: "Kirguistán", flag: "🇰🇬", code: "+996" },
    { name: "Kiribati", flag: "🇰🇮", code: "+686" },
    { name: "Kuwait", flag: "🇰🇼", code: "+965" },

    { name: "Laos", flag: "🇱🇦", code: "+856" },
    { name: "Lesoto", flag: "🇱🇸", code: "+266" },
    { name: "Letonia", flag: "🇱🇻", code: "+371" },
    { name: "Líbano", flag: "🇱🇧", code: "+961" },
    { name: "Liberia", flag: "🇱🇷", code: "+231" },
    { name: "Libia", flag: "🇱🇾", code: "+218" },
    { name: "Liechtenstein", flag: "🇱🇮", code: "+423" },
    {
        name: "Lituania",
        flag: "🇱🇹",
        code: "+370",
    },
    { name: "Luxemburgo", flag: "🇱🇺", code: "+352" },

    { name: "Macedonia del Norte", flag: "🇲🇰", code: "+389" },
    { name: "Madagascar", flag: "🇲🇬", code: "+261" },
    { name: "Malasia", flag: "🇲🇾", code: "+60" },
    { name: "Malaui", flag: "🇲🇼", code: "+265" },
    { name: "Maldivas", flag: "🇲🇻", code: "+960" },
    { name: "Malí", flag: "🇲🇱", code: "+223" },
    { name: "Malta", flag: "🇲🇹", code: "+356" },
    { name: "Marruecos", flag: "🇲🇦", code: "+212" },
    { name: "Mauricio", flag: "🇲🇺", code: "+230" },
    { name: "Mauritania", flag: "🇲🇷", code: "+222" },
    { name: "México", flag: "🇲🇽", code: "+52" },
    { name: "Micronesia", flag: "🇫🇲", code: "+691" },
    { name: "Moldavia", flag: "🇲🇩", code: "+373" },
    { name: "Mónaco", flag: "🇲🇨", code: "+377" },
    { name: "Mongolia", flag: "🇲🇳", code: "+976" },
    { name: "Montenegro", flag: "🇲🇪", code: "+382" },
    { name: "Mozambique", flag: "🇲🇿", code: "+258" },
    { name: "Myanmar", flag: "🇲🇲", code: "+95" },

    { name: "Namibia", flag: "🇳🇦", code: "+264" },
    { name: "Nauru", flag: "🇳🇷", code: "+674" },
    { name: "Nepal", flag: "🇳🇵", code: "+977" },
    { name: "Nicaragua", flag: "🇳🇮", code: "+505" },
    { name: "Níger", flag: "🇳🇪", code: "+227" },
    { name: "Nigeria", flag: "🇳🇬", code: "+234" },
    { name: "Noruega", flag: "🇳🇴", code: "+47" },
    { name: "Nueva Zelanda", flag: "🇳🇿", code: "+64" },

    { name: "Omán", flag: "🇴🇲", code: "+968" },

    { name: "Países Bajos", flag: "🇳🇱", code: "+31" },
    { name: "Pakistán", flag: "🇵🇰", code: "+92" },
    { name: "Palaos", flag: "🇵🇼", code: "+680" },
    { name: "Panamá", flag: "🇵🇦", code: "+507" },
    { name: "Papúa Nueva Guinea", flag: "🇵🇬", code: "+675" },
    { name: "Paraguay", flag: "🇵🇾", code: "+595" },
    { name: "Perú", flag: "🇵🇪", code: "+51" },
    { name: "Polonia", flag: "🇵🇱", code: "+48" },
    { name: "Portugal", flag: "🇵🇹", code: "+351" },

    { name: "Reino Unido", flag: "🇬🇧", code: "+44" },
    { name: "República Centroafricana", flag: "🇨🇫", code: "+236" },
    { name: "República Checa", flag: "🇨🇿", code: "+420" },
    { name: "República Democrática del Congo", flag: "🇨🇩", code: "+243" },
    { name: "República Dominicana", flag: "🇩🇴", code: "+1 809" },
    { name: "Ruanda", flag: "🇷🇼", code: "+250" },
    { name: "Rumania", flag: "🇷🇴", code: "+40" },
    { name: "Rusia", flag: "🇷🇺", code: "+7" },

    { name: "Samoa", flag: "🇼🇸", code: "+685" },
    { name: "San Cristóbal y Nieves", flag: "🇰🇳", code: "+1 869" },
    { name: "San Marino", flag: "🇸🇲", code: "+378" },
    { name: "San Vicente y las Granadinas", flag: "🇻🇨", code: "+1 784" },
    { name: "Santa Lucía", flag: "🇱🇨", code: "+1 758" },
    { name: "Santo Tomé y Príncipe", flag: "🇸🇹", code: "+239" },
    { name: "Senegal", flag: "🇸🇳", code: "+221" },
    { name: "Serbia", flag: "🇷🇸", code: "+381" },
    { name: "Seychelles", flag: "🇸🇨", code: "+248" },
    { name: "Sierra Leona", flag: "🇸🇱", code: "+232" },
    { name: "Singapur", flag: "🇸🇬", code: "+65" },
    { name: "Siria", flag: "🇸🇾", code: "+963" },
    { name: "Somalia", flag: "🇸🇴", code: "+252" },
    { name: "Sri Lanka", flag: "🇱🇰", code: "+94" },
    { name: "Sudáfrica", flag: "🇿🇦", code: "+27" },
    { name: "Sudán", flag: "🇸🇩", code: "+249" },
    { name: "Sudán del Sur", flag: "🇸🇸", code: "+211" },
    { name: "Suecia", flag: "🇸🇪", code: "+46" },
    { name: "Suiza", flag: "🇨🇭", code: "+41" },
    { name: "Surinam", flag: "🇸🇷", code: "+597" },

    { name: "Tailandia", flag: "🇹🇭", code: "+66" },
    { name: "Tanzania", flag: "🇹🇿", code: "+255" },
    { name: "Tayikistán", flag: "🇹🇯", code: "+992" },
    { name: "Timor Oriental", flag: "🇹🇱", code: "+670" },
    { name: "Togo", flag: "🇹🇬", code: "+228" },
    { name: "Tonga", flag: "🇹🇴", code: "+676" },
    { name: "Trinidad y Tobago", flag: "🇹🇹", code: "+1 868" },
    { name: "Túnez", flag: "🇹🇳", code: "+216" },
    { name: "Turkmenistán", flag: "🇹🇲", code: "+993" },
    { name: "Turquía", flag: "🇹🇷", code: "+90" },
    { name: "Tuvalu", flag: "🇹🇻", code: "+688" },

    { name: "Ucrania", flag: "🇺🇦", code: "+380" },
    { name: "Uganda", flag: "🇺🇬", code: "+256" },
    { name: "Uruguay", flag: "🇺🇾", code: "+598" },
    { name: "Uzbekistán", flag: "🇺🇿", code: "+998" },

    { name: "Vanuatu", flag: "🇻🇺", code: "+678" },
    { name: "Venezuela", flag: "🇻🇪", code: "+58" },
    { name: "Vietnam", flag: "🇻🇳", code: "+84" },

    { name: "Yemen", flag: "🇾🇪", code: "+967" },
    { name: "Yibuti", flag: "🇩🇯", code: "+253" },

    { name: "Zambia", flag: "🇿🇲", code: "+260" },
    { name: "Zimbabue", flag: "🇿🇼", code: "+263" }
];

const countrySelector = document.getElementById("countrySelector");

const countryDropdown = document.getElementById("countryDropdown");

const countryList = document.getElementById("countryList");

const countrySearch = document.getElementById("countrySearch");

const selectedFlag = document.getElementById("selectedFlag");

const selectedDialCode = document.getElementById("selectedDialCode");

const telefonoNumero = document.getElementById("telefonoNumero");

const telefono = document.getElementById("telefono");

let selectedCountry = countries.find(
    country => country.name === "Colombia"
);



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



function updateFullPhone() {
    const number = telefonoNumero.value.trim();

    if (!number) {
        telefono.value = "";

        return;
    }

    telefono.value = `${selectedCountry.code} ${number}`;
}



countrySelector.addEventListener("click", (event) => {
    event.stopPropagation();

    countryDropdown.classList.toggle("open");

    if (countryDropdown.classList.contains("open")) {
        countrySearch.focus();
    }
});



countrySearch.addEventListener("input", () => {
    renderCountries(countrySearch.value);
});



document.addEventListener("click", (event) => {
    if (!countryDropdown.contains(event.target) &&
        !countrySelector.contains(event.target)
    ) {
        countryDropdown.classList.remove("open");
    }
});



telefonoNumero.addEventListener("input", () => {
    updateFullPhone();
});



document.querySelector("form").addEventListener("submit", () => {
    updateFullPhone();
});



renderCountries();