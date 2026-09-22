document.addEventListener("DOMContentLoaded", () => {

    const selects = document.querySelectorAll(".cliente-select");

    selects.forEach((select) => {

        if (select.dataset.customized === "true") {
            return;
        }

        select.dataset.customized = "true";

        const contenedor = select.closest(".input-wrapper");

        if (!contenedor) {
            return;
        }

        contenedor.classList.add("custom-client-select");

        select.classList.add("client-real-select");

        const trigger = document.createElement("div");
        trigger.className = "client-select-trigger";
        trigger.setAttribute("tabindex", "0");

        const texto = document.createElement("span");
        texto.className = "client-select-text";

        const flecha = document.createElement("i");
        flecha.className =
            "fa-solid fa-chevron-down client-select-arrow";

        trigger.appendChild(texto);
        trigger.appendChild(flecha);

        const dropdown = document.createElement("div");
        dropdown.className = "client-select-dropdown";

        const buscador = document.createElement("div");
        buscador.className = "client-search";

        const iconoBusqueda = document.createElement("i");
        iconoBusqueda.className = "fa-solid fa-magnifying-glass";

        const inputBusqueda = document.createElement("input");
        inputBusqueda.type = "text";
        inputBusqueda.placeholder = "Buscar cliente...";
        inputBusqueda.autocomplete = "off";

        buscador.appendChild(iconoBusqueda);
        buscador.appendChild(inputBusqueda);

        const opcionesContainer = document.createElement("div");
        opcionesContainer.className = "client-select-options";

        dropdown.appendChild(buscador);
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
            opcionVisual.className = "client-select-option";

            const contenido = document.createElement("div");
            contenido.className = "client-option-content";

            const nombre = document.createElement("span");
            nombre.className = "client-option-name";

            const documento = document.createElement("span");
            documento.className = "client-option-document";

            const textoOpcion = opcion.textContent.trim();

            const separador = textoOpcion.lastIndexOf(" (");

            if (separador !== -1) {
                nombre.textContent =
                    textoOpcion.substring(0, separador);

                documento.textContent =
                    textoOpcion.substring(separador + 2)
                    .replace(")", "");
            } else {
                nombre.textContent = textoOpcion;
            }

            const check = document.createElement("i");
            check.className = "fa-solid fa-check";

            contenido.appendChild(nombre);
            contenido.appendChild(documento);

            opcionVisual.appendChild(contenido);
            opcionVisual.appendChild(check);

            opcionesContainer.appendChild(opcionVisual);

            opciones.push({
                elemento: opcionVisual,
                nombre: nombre.textContent.toLowerCase(),
                documento: documento.textContent.toLowerCase(),
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
                    "Selecciona un cliente...";

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

        function filtrarClientes() {

            const termino =
                inputBusqueda.value
                .trim()
                .toLowerCase();

            opciones.forEach((opcion) => {

                const coincide =
                    opcion.nombre.includes(termino) ||
                    opcion.documento.includes(termino);

                opcion.elemento.style.display =
                    coincide ? "flex" : "none";

            });

        }

        function abrirDropdown() {

            document
                .querySelectorAll(
                    ".custom-client-select.client-select-open"
                )
                .forEach((otro) => {

                    if (otro !== contenedor) {

                        otro.classList.remove(
                            "client-select-open"
                        );

                    }

                });

            contenedor.classList.add(
                "client-select-open"
            );

            setTimeout(() => {
                inputBusqueda.focus();
            }, 50);

        }

        function cerrarDropdown() {

            contenedor.classList.remove(
                "client-select-open"
            );

            inputBusqueda.value = "";

            filtrarClientes();

        }

        trigger.addEventListener("click", (event) => {

            event.stopPropagation();

            if (
                contenedor.classList.contains(
                    "client-select-open"
                )
            ) {

                cerrarDropdown();

            } else {

                abrirDropdown();

            }

        });

        inputBusqueda.addEventListener("click", (event) => {
            event.stopPropagation();
        });

        inputBusqueda.addEventListener("input", () => {
            filtrarClientes();
        });

        inputBusqueda.addEventListener("keydown", (event) => {

            if (event.key === "Escape") {

                cerrarDropdown();

                trigger.focus();

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
                        "client-select-open"
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