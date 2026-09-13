const colorSelector = document.getElementById("colorSelector");
const colorDropdown = document.getElementById("colorDropdown");
const colorList = document.getElementById("colorList");
const colorSearch = document.getElementById("colorSearch");
const selectedColorCircle = document.getElementById("selectedColorCircle");
const selectedColorText = document.getElementById("selectedColorText");
const colorInput = document.getElementById("color");

const colores = [
    { name: "Amarillo", value: "#FFD700" },
    { name: "Amarillo limón", value: "#FFF44F" },
    { name: "Amarillo mostaza", value: "#D4A017" },
    { name: "Amarillo pastel", value: "#FFF3A3" },
    { name: "Ámbar", value: "#FFBF00" },
    { name: "Azul", value: "#0000FF" },
    { name: "Azul acero", value: "#4682B4" },
    { name: "Azul cielo", value: "#87CEEB" },
    { name: "Azul celeste", value: "#B2FFFF" },
    { name: "Azul cobalto", value: "#0047AB" },
    { name: "Azul denim", value: "#1560BD" },
    { name: "Azul eléctrico", value: "#7DF9FF" },
    { name: "Azul marino", value: "#000080" },
    { name: "Azul noche", value: "#191970" },
    { name: "Azul pastel", value: "#AEC6CF" },
    { name: "Azul petróleo", value: "#005F6B" },
    { name: "Azul rey", value: "#4169E1" },
    { name: "Azul turquesa", value: "#40E0D0" },
    { name: "Beige", value: "#F5F5DC" },
    { name: "Beige claro", value: "#F1E5AC" },
    { name: "Beige oscuro", value: "#C8AD7F" },
    { name: "Blanco", value: "#FFFFFF" },
    { name: "Blanco hueso", value: "#F0EAD6" },
    { name: "Blanco marfil", value: "#FFFFF0" },
    { name: "Blanco roto", value: "#FAF9F6" },
    { name: "Borgoña", value: "#800020" },
    { name: "Burdeos", value: "#800000" },
    { name: "Café", value: "#6F4E37" },
    { name: "Café claro", value: "#A67B5B" },
    { name: "Café oscuro", value: "#3B2219" },
    { name: "Camel", value: "#C19A6B" },
    { name: "Canela", value: "#D2691E" },
    { name: "Caramelo", value: "#AF6F09" },
    { name: "Caoba", value: "#4A2511" },
    { name: "Chocolate", value: "#7B3F00" },
    { name: "Cobre", value: "#B87333" },
    { name: "Coral", value: "#FF7F50" },
    { name: "Coral claro", value: "#F88379" },
    { name: "Champán", value: "#F7E7CE" },
    { name: "Crema", value: "#FFFDD0" },
    { name: "Dorado", value: "#D4AF37" },
    { name: "Dorado claro", value: "#E6BE8A" },
    { name: "Fucsia", value: "#FF00FF" },
    { name: "Fucsia oscuro", value: "#C71585" },
    { name: "Gris", value: "#808080" },
    { name: "Gris carbón", value: "#36454F" },
    { name: "Gris claro", value: "#D3D3D3" },
    { name: "Gris oscuro", value: "#404040" },
    { name: "Gris grafito", value: "#41424C" },
    { name: "Gris perla", value: "#E2E2E2" },
    { name: "Gris plata", value: "#C0C0C0" },
    { name: "Lavanda", value: "#E6E6FA" },
    { name: "Lila", value: "#C8A2C8" },
    { name: "Lila pastel", value: "#D8BFD8" },
    { name: "Malva", value: "#E0B0FF" },
    { name: "Magenta", value: "#FF00FF" },
    { name: "Marrón", value: "#8B4513" },
    { name: "Marrón claro", value: "#B5651D" },
    { name: "Marrón oscuro", value: "#3B1F0B" },
    { name: "Naranja", value: "#FFA500" },
    { name: "Naranja coral", value: "#FF7F50" },
    { name: "Naranja pastel", value: "#FFDAB9" },
    { name: "Naranja quemado", value: "#CC5500" },
    { name: "Negro", value: "#000000" },
    { name: "Negro azabache", value: "#0A0A0A" },
    { name: "Negro carbón", value: "#1C1C1C" },
    { name: "Oliva", value: "#808000" },
    { name: "Púrpura", value: "#800080" },
    { name: "Púrpura pastel", value: "#B39EB5" },
    { name: "Rojo", value: "#FF0000" },
    { name: "Rojo carmesí", value: "#DC143C" },
    { name: "Rojo cereza", value: "#DE3163" },
    { name: "Rojo ladrillo", value: "#CB4154" },
    { name: "Rojo oscuro", value: "#8B0000" },
    { name: "Rojo vino", value: "#722F37" },
    { name: "Rosa", value: "#FFC0CB" },
    { name: "Rosa bebé", value: "#F4C2C2" },
    { name: "Rosa chicle", value: "#FF69B4" },
    { name: "Rosa fucsia", value: "#FF69B4" },
    { name: "Rosa palo", value: "#C08081" },
    { name: "Rosa pastel", value: "#FFD1DC" },
    { name: "Rosa salmón", value: "#FA8072" },
    { name: "Rosa viejo", value: "#C08081" },
    { name: "Plata", value: "#C0C0C0" },
    { name: "Plateado", value: "#C0C0C0" },
    { name: "Terracota", value: "#E2725B" },
    { name: "Turquesa", value: "#40E0D0" },
    { name: "Turquesa oscuro", value: "#00CED1" },
    { name: "Verde", value: "#008000" },
    { name: "Verde aguamarina", value: "#7FFFD4" },
    { name: "Verde botella", value: "#006A4E" },
    { name: "Verde esmeralda", value: "#50C878" },
    { name: "Verde bosque", value: "#228B22" },
    { name: "Verde jade", value: "#00A86B" },
    { name: "Verde lima", value: "#32CD32" },
    { name: "Verde militar", value: "#4B5320" },
    { name: "Verde menta", value: "#98FF98" },
    { name: "Verde oliva", value: "#6B8E23" },
    { name: "Verde pastel", value: "#77DD77" },
    { name: "Verde salvia", value: "#9CAF88" },
    { name: "Violeta", value: "#8A2BE2" },
    { name: "Violeta oscuro", value: "#4B0082" },
    { name: "Multicolor", value: "multicolor" }
];

function renderColors(filter = "") {
    colorList.innerHTML = "";

    const search = filter.toLowerCase().trim();

    const filtered = colores.filter(color =>
        color.name.toLowerCase().includes(search)
    );

    filtered.forEach(color => {
        const option = document.createElement("button");

        option.type = "button";
        option.className = "color-option";

        if (colorInput.value === color.name) {
            option.classList.add("selected");
        }

        const circle = document.createElement("span");

        circle.className = "color-circle";

        if (color.value === "multicolor") {
            circle.style.background =
                "linear-gradient(135deg, red, orange, yellow, green, blue, violet)";
        } else {
            circle.style.backgroundColor = color.value;
        }

        const name = document.createElement("span");

        name.className = "color-name";
        name.textContent = color.name;

        option.appendChild(circle);
        option.appendChild(name);

        option.addEventListener("click", () => {
            selectColor(color);
        });

        colorList.appendChild(option);
    });
}

function selectColor(color) {
    colorInput.value = color.name;

    selectedColorText.textContent = color.name;

    selectedColorCircle.style.background = "";
    selectedColorCircle.style.backgroundColor = "";

    if (color.value === "multicolor") {
        selectedColorCircle.style.background =
            "linear-gradient(135deg, red, orange, yellow, green, blue, violet)";
    } else {
        selectedColorCircle.style.backgroundColor = color.value;
    }

    colorDropdown.classList.remove("open");

    colorSearch.value = "";

    renderColors();
}

colorSelector.addEventListener("click", event => {
    event.stopPropagation();

    colorDropdown.classList.toggle("open");

    if (colorDropdown.classList.contains("open")) {
        colorSearch.focus();
    }
});

colorSearch.addEventListener("input", () => {
    renderColors(colorSearch.value);
});

document.addEventListener("click", event => {
    if (
        !colorDropdown.contains(event.target) &&
        !colorSelector.contains(event.target)
    ) {
        colorDropdown.classList.remove("open");
    }
});

function initColorSelector() {
    renderColors();

    const currentColor = colores.find(
        color => color.name === colorInput.value
    );

    if (currentColor) {
        selectColor(currentColor);
    }
}

initColorSelector();