document.addEventListener("DOMContentLoaded", () => {

    const selects =
        document.querySelectorAll(".estado-cuenta-select");

    selects.forEach((select) => {

        if (select.dataset.customized === "true") {
            return;
        }

        select.dataset.customized = "true";

        const contenedor =
            select.closest(".input-wrapper");

        if (!contenedor) {
            return;
        }

        contenedor.classList.add(
            "custom-account-status-select"
        );

        select.classList.add(
            "account-status-real-select"
        );

        const trigger =
            document.createElement("div");

        trigger.className =
            "account-status-select-trigger";

        trigger.setAttribute("tabindex", "0");

        const texto =
            document.createElement("span");

        texto.className =
            "account-status-select-text";

        const flecha =
            document.createElement("i");

        flecha.className =
            "fa-solid fa-chevron-down account-status-select-arrow";

        trigger.appendChild(texto);
        trigger.appendChild(flecha);

        const dropdown =
            document.createElement("div");

        dropdown.className =
            "account-status-select-dropdown";

        const opcionesContainer =
            document.createElement("div");

        opcionesContainer.className =
            "account-status-select-options";

        dropdown.appendChild(opcionesContainer);

        contenedor.appendChild(trigger);
        contenedor.appendChild(dropdown);

        const opciones = [];

        Array.from(select.options).forEach(
            (opcion, index) => {

                const opcionVisual =
                    document.createElement("button");

                opcionVisual.type = "button";

                opcionVisual.className =
                    "account-status-select-option";

                const contenido =
                    document.createElement("div");

                contenido.className =
                    "account-status-option-content";

                const nombre =
                    document.createElement("span");

                nombre.className =
                    "account-status-option-name";

                nombre.textContent =
                    opcion.textContent.trim();

                const check =
                    document.createElement("i");

                check.className =
                    "fa-solid fa-check";

                contenido.appendChild(nombre);

                opcionVisual.appendChild(
                    contenido
                );

                opcionVisual.appendChild(check);

                opcionesContainer.appendChild(
                    opcionVisual
                );

                opciones.push({
                    elemento: opcionVisual,
                    index: index
                });

                opcionVisual.addEventListener(
                    "click",
                    (event) => {

                        event.stopPropagation();

                        select.selectedIndex =
                            index;

                        select.dispatchEvent(
                            new Event("change", {
                                bubbles: true
                            })
                        );

                        actualizarSeleccion();

                        cerrarDropdown();

                    }
                );

            }
        );

        function actualizarSeleccion() {

            const seleccionada =
                select.options[
                select.selectedIndex
                ];

            if (!seleccionada) {
                texto.textContent =
                    "Selecciona un estado";
            } else {
                texto.textContent =
                    seleccionada.textContent.trim();
            }

            opciones.forEach((opcion) => {

                opcion.elemento.classList.toggle(
                    "selected",
                    opcion.index ===
                    select.selectedIndex
                );

            });
        }

        function abrirDropdown() {

            document
                .querySelectorAll(
                    ".custom-account-status-select.account-status-select-open"
                )
                .forEach((otro) => {

                    if (otro !== contenedor) {

                        otro.classList.remove(
                            "account-status-select-open"
                        );

                    }

                });

            contenedor.classList.add(
                "account-status-select-open"
            );
        }

        function cerrarDropdown() {

            contenedor.classList.remove(
                "account-status-select-open"
            );
        }

        trigger.addEventListener(
            "click",
            (event) => {

                event.stopPropagation();

                if (
                    contenedor.classList.contains(
                        "account-status-select-open"
                    )
                ) {

                    cerrarDropdown();

                } else {

                    abrirDropdown();

                }

            }
        );

        trigger.addEventListener(
            "keydown",
            (event) => {

                if (
                    event.key === "Enter" ||
                    event.key === " "
                ) {

                    event.preventDefault();

                    if (
                        contenedor.classList.contains(
                            "account-status-select-open"
                        )
                    ) {

                        cerrarDropdown();

                    } else {

                        abrirDropdown();

                    }

                }

                if (event.key === "Escape") {
                    cerrarDropdown();
                }

            }
        );

        document.addEventListener(
            "click",
            (event) => {

                if (!contenedor.contains(event.target)) {
                    cerrarDropdown();
                }

            }
        );

        actualizarSeleccion();

    });

});