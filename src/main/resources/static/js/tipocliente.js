
const tipoClienteCustom =
    document.getElementById("tipoClienteCustom");

const tipoCliente =
    document.getElementById("tipoCliente");

const tipoClienteTrigger =
    tipoClienteCustom.querySelector(
        ".custom-select-trigger"
    );

const tipoClienteOptions =
    document.getElementById(
        "tipoClienteOptions"
    );

const tipoClienteText =
    document.getElementById("tipoClienteText");


/* =====================================================
   OBTENER OPCIONES
===================================================== */

const tiposCliente =
    Array.from(tipoCliente.options)
        .filter(option => option.value !== "");


/* =====================================================
   ACTUALIZAR TEXTO VISUAL
===================================================== */

function actualizarTextoTipoCliente() {

    const opcionSeleccionada =
        tipoCliente.options[
        tipoCliente.selectedIndex
        ];

    if (
        opcionSeleccionada &&
        opcionSeleccionada.value !== ""
    ) {

        tipoClienteText.textContent =
            opcionSeleccionada.textContent;

        tipoClienteText.classList.remove(
            "placeholder"
        );

    } else {

        tipoClienteText.textContent =
            "Seleccionar tipo de cliente";

        tipoClienteText.classList.add(
            "placeholder"
        );
    }
}


/* =====================================================
   MOSTRAR OPCIONES
===================================================== */

function renderTiposCliente() {

    tipoClienteOptions.innerHTML = "";

    tiposCliente.forEach((option) => {

        const item =
            document.createElement("button");

        item.type = "button";

        item.className =
            "custom-select-option";


        /* MARCAR OPCIÓN SELECCIONADA */

        if (
            tipoCliente.value === option.value
        ) {

            item.classList.add(
                "selected"
            );
        }


        /* CONTENIDO */

        item.innerHTML = `
            <!-- <span class="custom-option-icon">
                <i class="fa-brands fa-web-awesome"></i>
            </span> -->

            <span class="custom-option-content">

                <span class="custom-option-name">
                    ${option.textContent}
                </span>

            </span>

            <span class="custom-option-check">
                <i class="fa-solid fa-check"></i>
            </span>
        `;


        /* CLICK */

        item.addEventListener(
            "click",
            () => {
                seleccionarTipoCliente(option);
            }
        );


        tipoClienteOptions.appendChild(item);
    });
}


/* =====================================================
   SELECCIONAR
===================================================== */

function seleccionarTipoCliente(option) {

    tipoCliente.value =
        option.value;


    tipoCliente.dispatchEvent(
        new Event("change", {
            bubbles: true
        })
    );


    /* ACTUALIZAR TEXTO VISUAL */

    actualizarTextoTipoCliente();


    /* CERRAR */

    tipoClienteCustom.classList.remove(
        "open"
    );


    /* ACTUALIZAR OPCIONES */

    renderTiposCliente();
}


/* =====================================================
   ABRIR / CERRAR
===================================================== */

tipoClienteTrigger.addEventListener(
    "click",
    (event) => {

        event.stopPropagation();

        tipoClienteCustom.classList.toggle(
            "open"
        );

        renderTiposCliente();
    }
);


/* =====================================================
   CERRAR AL HACER CLICK AFUERA
===================================================== */

document.addEventListener(
    "click",
    (event) => {

        if (
            !tipoClienteCustom.contains(
                event.target
            )
        ) {

            tipoClienteCustom.classList.remove(
                "open"
            );
        }
    }
);


/* =====================================================
   INICIALIZAR
===================================================== */

/*
 * Primero mostramos el tipo que
 * Thymeleaf dejó seleccionado.
 */

actualizarTextoTipoCliente();


/*
 * Luego generamos las opciones.
 */

renderTiposCliente();

