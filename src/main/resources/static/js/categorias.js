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

const categorias =
    Array.from(categoria.options)
        .filter(option => option.value !== "");

function actualizarTextoCategoria() {
    const opcionSeleccionada =
        categoria.options[
        categoria.selectedIndex
        ];

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

        item.innerHTML = `
            <span class="custom-option-content">
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

function seleccionarCategoria(option) {
    categoria.value =
        option.value;

    categoria.dispatchEvent(
        new Event("change", {
            bubbles: true
        })
    );

    actualizarTextoCategoria();

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