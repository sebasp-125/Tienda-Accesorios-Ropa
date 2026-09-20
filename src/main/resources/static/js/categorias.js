const categoriaCustom =
    document.getElementById("categoriaCustom");

const categoria =
    document.getElementById("categoria");

const categoriaTrigger =
    categoriaCustom.querySelector(
        ".custom-select-trigger"
    );

const categoriaOptions =
    document.getElementById("categoriaOptions");

const categoriaText =
    document.getElementById("categoriaText");

const categoriaIcon =
    categoriaCustom.querySelector(
        ".custom-select-icon"
    );

const categorias =
    Array.from(categoria.options)
        .filter(option => option.value !== "");


function actualizarIconoCategoria() {

    const opcionSeleccionada =
        categoria.options[categoria.selectedIndex];

    if (
        !opcionSeleccionada ||
        opcionSeleccionada.value === ""
    ) {
        categoriaIcon.innerHTML =
            '<i class="fa-solid fa-tags"></i>';

        return;
    }

    switch (
    opcionSeleccionada.textContent.trim()
    ) {

        case "Ropa":

            categoriaIcon.innerHTML =
                '<i class="fa-solid fa-shirt"></i>';

            break;


        case "Calzado":

            categoriaIcon.innerHTML =
                '<span class="material-symbols-outlined">steps</span>';

            break;


        case "Accesorio":
        case "Accesorios":

            categoriaIcon.innerHTML =
                '<i class="fa-solid fa-bag-shopping"></i>';

            break;


        default:

            categoriaIcon.innerHTML =
                '<i class="fa-solid fa-tags"></i>';
    }
}


function actualizarTextoCategoria() {

    const opcionSeleccionada =
        categoria.options[categoria.selectedIndex];

    if (
        opcionSeleccionada &&
        opcionSeleccionada.value !== ""
    ) {

        categoriaText.textContent =
            opcionSeleccionada.textContent;

        categoriaText.classList.remove(
            "placeholder"
        );

    } else {

        categoriaText.textContent =
            "Seleccionar categoría";

        categoriaText.classList.add(
            "placeholder"
        );
    }

    actualizarIconoCategoria();
}


function renderCategorias() {

    categoriaOptions.innerHTML = "";

    categorias.forEach(option => {

        const item =
            document.createElement("button");

        item.type = "button";

        item.className =
            "custom-select-option";


        if (
            categoria.value === option.value
        ) {

            item.classList.add("selected");
        }


        let iconoCategoria;


        switch (option.textContent.trim()) {

            case "Ropa":

                iconoCategoria =
                    '<i class="fa-solid fa-shirt"></i>';

                break;


            case "Calzado":

                iconoCategoria =
                    '<span class="material-symbols-outlined">steps</span>';

                break;


            case "Accesorio":
            case "Accesorios":

                iconoCategoria =
                    '<i class="fa-solid fa-bag-shopping"></i>';

                break;


            default:

                iconoCategoria =
                    '<i class="fa-solid fa-tags"></i>';
        }


        item.innerHTML = `
            <span class="custom-option-content">

                <span class="custom-option-icon">
                    ${iconoCategoria}
                </span>

                <span class="custom-option-name">
                    ${option.textContent}
                </span>

            </span>

            <span class="custom-option-check">
                <i class="fa-solid fa-check"></i>
            </span>
        `;


        item.addEventListener(
            "click",
            () => {
                seleccionarCategoria(option);
            }
        );


        categoriaOptions.appendChild(item);
    });
}

function obtenerPrefijoCategoria() {

    const opcionSeleccionada =
        categoria.options[categoria.selectedIndex];

    if (
        !opcionSeleccionada ||
        opcionSeleccionada.value === ""
    ) {
        return "";
    }

    switch (
    opcionSeleccionada.textContent.trim()
    ) {

        case "Ropa":
            return "RP";

        case "Calzado":
            return "CZ";

        case "Accesorio":
        case "Accesorios":
            return "AC";

        default:
            return "";
    }
}


function actualizarCodigoConPrefijo(
    codigo
) {

    const codigoInput =
        document.getElementById("codigo");

    if (!codigoInput) {
        return;
    }

    const codigoNumerico =
        codigo
            .split("-")
            .pop()
            .trim();

    const prefijo =
        obtenerPrefijoCategoria();

    if (!prefijo) {
        codigoInput.value = codigoNumerico;
        return;
    }

    codigoInput.value =
        `${prefijo}-${codigoNumerico}`;
}

function seleccionarCategoria(option) {

    categoria.value =
        option.value;

    categoria.dispatchEvent(
        new Event("change", {
            bubbles: true
        })
    );

    actualizarTextoCategoria();

    const codigoInput =
        document.getElementById("codigo");

    if (
        codigoInput &&
        codigoInput.value
    ) {

        const codigoNumerico =
            codigoInput.value
                .split("-")
                .pop();

        actualizarCodigoConPrefijo(
            codigoNumerico
        );
    }

    categoriaCustom.classList.remove(
        "open"
    );

    renderCategorias();
}

categoriaTrigger.addEventListener(
    "click",
    event => {

        event.stopPropagation();

        categoriaCustom.classList.toggle(
            "open"
        );

        renderCategorias();
    }
);


document.addEventListener(
    "click",
    event => {

        if (
            !categoriaCustom.contains(
                event.target
            )
        ) {

            categoriaCustom.classList.remove(
                "open"
            );
        }
    }
);


actualizarTextoCategoria();

renderCategorias();
