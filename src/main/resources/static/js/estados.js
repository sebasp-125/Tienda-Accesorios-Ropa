document.addEventListener("DOMContentLoaded", () => {

    const selects = document.querySelectorAll(
        ".filter-input-group select, .estado-select"
    );

    selects.forEach((select) => {

        if (select.dataset.customized === "true") {
            return;
        }

        select.dataset.customized = "true";

        const contenedor =
            select.closest(".filter-input-group") ||
            select.closest(".input-wrapper");

        if (!contenedor) {
            return;
        }

        contenedor.classList.add("custom-filter-select");


        select.classList.add("filter-real-select");



        const trigger = document.createElement("div");
        trigger.className = "filter-select-trigger";
        trigger.setAttribute("tabindex", "0");

        const texto = document.createElement("span");
        texto.className = "filter-select-text";

        const flecha = document.createElement("i");
        flecha.className =
            "fa-solid fa-chevron-down filter-select-arrow";

        trigger.appendChild(texto);
        trigger.appendChild(flecha);



        const dropdown = document.createElement("div");
        dropdown.className = "filter-select-dropdown";

        const opcionesContainer = document.createElement("div");
        opcionesContainer.className = "filter-select-options";

        dropdown.appendChild(opcionesContainer);



        contenedor.appendChild(trigger);
        contenedor.appendChild(dropdown);



        Array.from(select.options).forEach((opcion, index) => {

            const opcionVisual = document.createElement("button");

            opcionVisual.type = "button";
            opcionVisual.className = "filter-select-option";

            const nombre = document.createElement("span");
            nombre.textContent = opcion.textContent;

            const check = document.createElement("i");
            check.className = "fa-solid fa-check";

            opcionVisual.appendChild(nombre);
            opcionVisual.appendChild(check);

            opcionesContainer.appendChild(opcionVisual);

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

            if (!seleccionada) {
                texto.textContent = "";
                return;
            }

            texto.textContent = seleccionada.textContent;

            const opcionesVisuales =
                opcionesContainer.querySelectorAll(
                    ".filter-select-option"
                );

            opcionesVisuales.forEach((opcionVisual, index) => {

                opcionVisual.classList.toggle(
                    "selected",
                    index === select.selectedIndex
                );

            });
        }


        function abrirDropdown(event) {

            if (event) {
                event.stopPropagation();
            }

            document
                .querySelectorAll(
                    ".custom-filter-select.custom-filter-open"
                )
                .forEach((otro) => {

                    if (otro !== contenedor) {
                        otro.classList.remove(
                            "custom-filter-open"
                        );
                    }

                });

            contenedor.classList.add(
                "custom-filter-open"
            );
        }



        function cerrarDropdown() {

            contenedor.classList.remove(
                "custom-filter-open"
            );
        }



        trigger.addEventListener("click", (event) => {

            event.stopPropagation();

            if (
                contenedor.classList.contains(
                    "custom-filter-open"
                )
            ) {

                cerrarDropdown();

            } else {

                abrirDropdown(event);

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
                        "custom-filter-open"
                    )
                ) {

                    cerrarDropdown();

                } else {

                    abrirDropdown(event);

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