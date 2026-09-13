const tallaCustom =
    document.getElementById("tallaCustom");

const tallaSelect =
    document.getElementById("talla");

const tallaTrigger =
    tallaCustom.querySelector(
        ".custom-select-trigger"
    );

const tallaOptions =
    document.getElementById("tallaOptions");

const tallaText =
    document.getElementById("tallaText");

const categoriaSelect =
    document.getElementById("categoria");


// =====================================================
// TALLAS
// =====================================================

const tallas = {

    accesorios: [
        "Única"
    ],

    ropa: [
        "XS",
        "S",
        "M",
        "L",
        "XL",
        "XXL"
    ],

    calzado: [
        "21", "22", "23", "24", "25",
        "26", "27", "28", "29", "30",
        "31", "32", "33", "34", "35",
        "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45",
        "46", "47"
    ]

};


// =====================================================
// OBTENER CATEGORÍA
// =====================================================

function obtenerCategoria() {

    const opcionSeleccionada =
        categoriaSelect.options[
        categoriaSelect.selectedIndex
        ];

    if (
        opcionSeleccionada &&
        opcionSeleccionada.value !== ""
    ) {

        return opcionSeleccionada.textContent
            .toLowerCase()
            .trim();

    }

    return "";
}


// =====================================================
// OBTENER TALLAS
// =====================================================

function obtenerTallas() {

    const categoria =
        obtenerCategoria();

    return tallas[categoria] || [];
}


// =====================================================
// TEXTO VISUAL
// =====================================================

function actualizarTextoTalla() {

    const opcionSeleccionada =
        tallaSelect.options[
        tallaSelect.selectedIndex
        ];

    if (
        opcionSeleccionada &&
        opcionSeleccionada.value !== ""
    ) {

        tallaText.textContent =
            opcionSeleccionada.textContent;

        tallaText.classList.remove(
            "placeholder"
        );

    } else {

        tallaText.textContent =
            "Selecciona una talla";

        tallaText.classList.add(
            "placeholder"
        );
    }
}


// =====================================================
// RENDER TALLAS
// =====================================================

function renderTallas(tallaSeleccionada = "") {

    tallaSelect.innerHTML = "";
    tallaOptions.innerHTML = "";

    const opcionInicial =
        document.createElement("option");

    opcionInicial.value = "";
    opcionInicial.textContent =
        "Selecciona una talla";

    tallaSelect.appendChild(opcionInicial);


    const opciones =
        obtenerTallas();


    opciones.forEach((talla) => {

        const option =
            document.createElement("option");

        option.value = talla;
        option.textContent = talla;

        if (talla === tallaSeleccionada) {
            option.selected = true;
        }

        tallaSelect.appendChild(option);

    });


    opciones.forEach((talla) => {

        const item =
            document.createElement("button");

        item.type = "button";

        item.className =
            "custom-select-option";


        if (tallaSelect.value === talla) {
            item.classList.add("selected");
        }


        item.innerHTML = `
            <span class="custom-option-content">

                <span class="custom-option-name">
                    ${talla}
                </span>

            </span>

            <span class="custom-option-check">
                <i class="fa-solid fa-check"></i>
            </span>
        `;


        item.addEventListener("click", () => {

            seleccionarTalla(talla);

        });


        tallaOptions.appendChild(item);

    });


    actualizarTextoTalla();

}


// =====================================================
// SELECCIONAR TALLA
// =====================================================

function seleccionarTalla(talla) {

    tallaSelect.value = talla;

    actualizarTextoTalla();

    tallaOptions
        .querySelectorAll(
            ".custom-select-option"
        )
        .forEach((item) => {

            const nombre =
                item.querySelector(
                    ".custom-option-name"
                );

            if (
                nombre &&
                nombre.textContent.trim() === talla
            ) {

                item.classList.add("selected");

            } else {

                item.classList.remove("selected");

            }

        });

    tallaCustom.classList.remove("open");
}


// =====================================================
// CAMBIO DE CATEGORÍA
// =====================================================

categoriaSelect.addEventListener(
    "change",
    () => {

        renderTallas("");

    }
);


// =====================================================
// ABRIR / CERRAR
// =====================================================

tallaTrigger.addEventListener(
    "click",
    (event) => {

        event.stopPropagation();

        tallaCustom.classList.toggle("open");

        renderTallas(
            tallaSelect.value
        );

    }
);


// =====================================================
// CERRAR AFUERA
// =====================================================

document.addEventListener(
    "click",
    (event) => {

        if (
            !tallaCustom.contains(
                event.target
            )
        ) {

            tallaCustom.classList.remove(
                "open"
            );

        }

    }
);


// =====================================================
// INICIALIZAR
// =====================================================

renderTallas(tallaGuardada);