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
const tiposCliente =
    Array.from(tipoCliente.options)
        .filter(option => option.value !== "");
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
function renderTiposCliente() {
    tipoClienteOptions.innerHTML = "";
    tiposCliente.forEach((option) => {
        const item =
            document.createElement("button");
        item.type = "button";
        item.className =
            "custom-select-option";
        if (
            tipoCliente.value === option.value
        ) {
            item.classList.add(
                "selected"
            );
        }
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
        item.addEventListener(
            "click",
            () => {
                seleccionarTipoCliente(option);
            }
        );
        tipoClienteOptions.appendChild(item);
    });
}
function seleccionarTipoCliente(option) {
    tipoCliente.value =
        option.value;
    tipoCliente.dispatchEvent(
        new Event("change", {
            bubbles: true
        })
    );
    actualizarTextoTipoCliente();
    tipoClienteCustom.classList.remove(
        "open"
    );
    renderTiposCliente();
}
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
actualizarTextoTipoCliente();
renderTiposCliente();
