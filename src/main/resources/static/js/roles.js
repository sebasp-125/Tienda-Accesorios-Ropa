document.addEventListener("DOMContentLoaded", () => {

    const selects = document.querySelectorAll(".rol-select");

    selects.forEach((select) => {

        if (select.dataset.customized === "true") {
            return;
        }

        select.dataset.customized = "true";

        const contenedor = select.closest(".input-wrapper");

        if (!contenedor) {
            return;
        }

        contenedor.classList.add("custom-role-select");

        select.classList.add("role-real-select");

        const trigger = document.createElement("div");
        trigger.className = "role-select-trigger";
        trigger.setAttribute("tabindex", "0");

        const texto = document.createElement("span");
        texto.className = "role-select-text";

        const flecha = document.createElement("i");
        flecha.className =
            "fa-solid fa-chevron-down role-select-arrow";

        trigger.appendChild(texto);
        trigger.appendChild(flecha);

        const dropdown = document.createElement("div");
        dropdown.className = "role-select-dropdown";

        const opcionesContainer = document.createElement("div");
        opcionesContainer.className = "role-select-options";

        dropdown.appendChild(opcionesContainer);

        contenedor.appendChild(trigger);
        contenedor.appendChild(dropdown);

        const opciones = [];

        Array.from(select.options).forEach((opcion, index) => {

            if (opcion.value === "") {
                return;
            }

            const opcionVisual = document.createElement("button");

            opcionVisual.type = "button";
            opcionVisual.className = "role-select-option";

            const nombre = document.createElement("span");
            nombre.className = "role-option-name";
            nombre.textContent = opcion.textContent.trim();

            const check = document.createElement("i");
            check.className = "fa-solid fa-check";

            opcionVisual.appendChild(nombre);
            opcionVisual.appendChild(check);

            opcionesContainer.appendChild(opcionVisual);

            opciones.push({
                elemento: opcionVisual,
                texto: nombre.textContent.toLowerCase(),
                index: index
            });

            opcionVisual.addEventListener("click", (event) => {

                event.stopPropagation();

                select.selectedIndex = index;

                select.dispatchEvent(
                    new Event("change", {
                        bubbles: true
                    })
                );

                actualizarSeleccion();

                cerrarDropdown();

            });

        });

        function actualizarSeleccion() {

            const seleccionada =
                select.options[select.selectedIndex];

            if (!seleccionada || seleccionada.value === "") {

                texto.textContent =
                    "Selecciona un rol";

            } else {

                texto.textContent =
                    seleccionada.textContent.trim();

            }

            opciones.forEach((opcion) => {

                opcion.elemento.classList.toggle(
                    "selected",
                    opcion.index === select.selectedIndex
                );

            });

        }

        function abrirDropdown() {

            document
                .querySelectorAll(
                    ".custom-role-select.role-select-open"
                )
                .forEach((otro) => {

                    if (otro !== contenedor) {

                        otro.classList.remove(
                            "role-select-open"
                        );

                    }

                });

            contenedor.classList.add(
                "role-select-open"
            );

        }

        function cerrarDropdown() {

            contenedor.classList.remove(
                "role-select-open"
            );

        }

        trigger.addEventListener("click", (event) => {

            event.stopPropagation();

            if (
                contenedor.classList.contains(
                    "role-select-open"
                )
            ) {

                cerrarDropdown();

            } else {

                abrirDropdown();

            }

        });

        trigger.addEventListener("keydown", (event) => {

            if (
                event.key === "Enter" ||
                event.key === " "
            ) {

                event.preventDefault();

                if (
                    contenedor.classList.contains(
                        "role-select-open"
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

        });

        document.addEventListener("click", (event) => {

            if (!contenedor.contains(event.target)) {

                cerrarDropdown();

            }

        });

        actualizarSeleccion();

    });

});