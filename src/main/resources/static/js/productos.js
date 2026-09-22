document.addEventListener("DOMContentLoaded", () => {

    const selects = document.querySelectorAll(".producto-select");

    selects.forEach((select) => {

        if (select.dataset.customized === "true") {
            return;
        }

        select.dataset.customized = "true";

        const contenedor = select.closest(".input-wrapper");

        if (!contenedor) {
            return;
        }

        contenedor.classList.add("custom-product-select");

        select.classList.add("product-real-select");

        const trigger = document.createElement("div");
        trigger.className = "product-select-trigger";
        trigger.setAttribute("tabindex", "0");

        const texto = document.createElement("span");
        texto.className = "product-select-text";

        const flecha = document.createElement("i");
        flecha.className =
            "fa-solid fa-chevron-down product-select-arrow";

        trigger.appendChild(texto);
        trigger.appendChild(flecha);

        const dropdown = document.createElement("div");
        dropdown.className = "product-select-dropdown";

        const buscador = document.createElement("div");
        buscador.className = "product-search";

        const iconoBusqueda = document.createElement("i");
        iconoBusqueda.className =
            "fa-solid fa-magnifying-glass";

        const inputBusqueda = document.createElement("input");
        inputBusqueda.type = "text";
        inputBusqueda.placeholder = "Buscar producto...";
        inputBusqueda.autocomplete = "off";

        buscador.appendChild(iconoBusqueda);
        buscador.appendChild(inputBusqueda);

        const opcionesContainer = document.createElement("div");
        opcionesContainer.className = "product-select-options";

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
            opcionVisual.className = "product-select-option";

            const contenido = document.createElement("div");
            contenido.className = "product-option-content";

            const nombre = document.createElement("span");
            nombre.className = "product-option-name";

            const informacion = document.createElement("div");
            informacion.className = "product-option-info";

            const codigo = document.createElement("span");
            codigo.className = "product-option-code";

            const precio = document.createElement("span");
            precio.className = "product-option-price";

            const stock = document.createElement("span");
            stock.className = "product-option-stock";

            nombre.textContent =
                opcion.dataset.nombre || opcion.textContent.trim();

            codigo.textContent =
                opcion.value;

            const precioNumerico =
                Number(opcion.dataset.precio || 0);

            precio.textContent =
                `$${precioNumerico.toLocaleString("es-CO", {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                })}`;

            stock.textContent =
                `Stock: ${opcion.dataset.stock || 0}`;

            informacion.appendChild(codigo);
            informacion.appendChild(precio);
            informacion.appendChild(stock);

            contenido.appendChild(nombre);
            contenido.appendChild(informacion);

            const check = document.createElement("i");
            check.className = "fa-solid fa-check";

            opcionVisual.appendChild(contenido);
            opcionVisual.appendChild(check);

            opcionesContainer.appendChild(opcionVisual);

            opciones.push({
                elemento: opcionVisual,
                nombre: nombre.textContent.toLowerCase(),
                codigo: codigo.textContent.toLowerCase(),
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
                    "Seleccionar producto...";

            } else {

                texto.textContent =
                    seleccionada.dataset.nombre ||
                    seleccionada.textContent.trim();

            }

            opciones.forEach((opcion) => {

                opcion.elemento.classList.toggle(
                    "selected",
                    opcion.index === select.selectedIndex
                );

            });
        }

        function filtrarProductos() {

            const termino =
                inputBusqueda.value
                    .trim()
                    .toLowerCase();

            opciones.forEach((opcion) => {

                const coincide =
                    opcion.nombre.includes(termino) ||
                    opcion.codigo.includes(termino);

                opcion.elemento.style.display =
                    coincide ? "flex" : "none";

            });
        }

        function abrirDropdown() {

            document
                .querySelectorAll(
                    ".custom-product-select.product-select-open"
                )
                .forEach((otro) => {

                    if (otro !== contenedor) {

                        otro.classList.remove(
                            "product-select-open"
                        );

                    }

                });

            contenedor.classList.add(
                "product-select-open"
            );

            setTimeout(() => {
                inputBusqueda.focus();
            }, 50);
        }

        function cerrarDropdown() {

            contenedor.classList.remove(
                "product-select-open"
            );

            inputBusqueda.value = "";

            filtrarProductos();
        }

        trigger.addEventListener("click", (event) => {

            event.stopPropagation();

            if (
                contenedor.classList.contains(
                    "product-select-open"
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
            filtrarProductos();
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
                        "product-select-open"
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