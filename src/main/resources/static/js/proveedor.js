const tabs = document.querySelectorAll(".supplier-tab");
const sections = document.querySelectorAll(".supplier-section");
function activateSection(sectionId) {
    tabs.forEach((tab) => {
        tab.classList.remove("active");
    });
    sections.forEach((section) => {
        section.classList.remove("active");
    });
    const activeTab = document.querySelector(
        `.supplier-tab[href="#${sectionId}"]`,
    );
    const activeSection =
        document.getElementById(sectionId);
    if (activeTab && activeSection) {
        activeTab.classList.add("active");
        activeSection.classList.add("active");
    }
}
tabs.forEach((tab) => {
    tab.addEventListener("click", function (event) {
        event.preventDefault();
        const sectionId =
            this.getAttribute("href").substring(1);
        activateSection(sectionId);
        history.pushState(
            null,
            "",
            `#${sectionId}`
        );
    });
});
window.addEventListener("load", function () {
    const hash = window.location.hash;
    if (hash === "#consultar") {
        activateSection("consultar");
    } else {
        activateSection("asociar");
    }
    if (hash) {
        window.scrollTo(0, 0);
    }
});
const checkboxes =
    document.querySelectorAll(".product-checkbox");
const selectedCount =
    document.getElementById("selectedCount");
function updateSelectedCount() {
    const selected =
        document.querySelectorAll(
            ".product-checkbox:checked",
        ).length;
    selectedCount.textContent =
        selected;
}
checkboxes.forEach((checkbox) => {
    checkbox.addEventListener(
        "change",
        updateSelectedCount
    );
});
const proveedorCustom =
    document.getElementById(
        "proveedorCustom"
    );
const proveedor =
    document.getElementById(
        "proveedor"
    );
const proveedorTrigger =
    proveedorCustom.querySelector(
        ".custom-select-trigger"
    );
const proveedorOptions =
    document.getElementById(
        "proveedorOptions"
    );
const proveedorText =
    document.getElementById(
        "proveedorText"
    );
const proveedoresOpciones =
    Array.from(proveedor.options)
        .filter(
            (option) =>
                option.value !== ""
        );
function actualizarTextoProveedor() {
    const opcionSeleccionada =
        proveedor.options[
        proveedor.selectedIndex
        ];
    if (
        opcionSeleccionada &&
        opcionSeleccionada.value !== ""
    ) {
        proveedorText.textContent =
            opcionSeleccionada.textContent;
        proveedorText.classList.remove(
            "placeholder"
        );
    } else {
        proveedorText.textContent =
            "Seleccionar proveedor";
        proveedorText.classList.add(
            "placeholder"
        );
    }
}
function renderProveedores() {
    proveedorOptions.innerHTML = "";
    proveedoresOpciones.forEach(
        (option) => {
            const item =
                document.createElement(
                    "button"
                );
            item.type = "button";
            item.className =
                "custom-select-option";
            if (
                proveedor.value ===
                option.value
            ) {
                item.classList.add(
                    "selected"
                );
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
                    seleccionarProveedor(
                        option
                    );
                }
            );
            proveedorOptions.appendChild(
                item
            );
        }
    );
}
function seleccionarProveedor(option) {
    proveedor.value =
        option.value;
    proveedor.dispatchEvent(
        new Event("change", {
            bubbles: true
        })
    );
    actualizarTextoProveedor();
    proveedorCustom.classList.remove(
        "open"
    );
    renderProveedores();
}
proveedorTrigger.addEventListener(
    "click",
    (event) => {
        event.stopPropagation();
        proveedorCustom.classList.toggle(
            "open"
        );
        renderProveedores();
    }
);
document.addEventListener(
    "click",
    (event) => {
        if (
            !proveedorCustom.contains(
                event.target
            )
        ) {
            proveedorCustom.classList.remove(
                "open"
            );
        }
    }
);
actualizarTextoProveedor();
renderProveedores();
proveedor.addEventListener(
    "change",
    async function () {
        const proveedorNit =
            this.value;
        if (!proveedorNit) {
            return;
        }
        try {
            const response = await fetch(
                `/proveedores-productos/consultar/${encodeURIComponent(
                    proveedorNit
                )}`,
            );
            if (!response.ok) {
                throw new Error(
                    "No se pudieron consultar las asociaciones."
                );
            }
            const productosAsociados =
                await response.json();
            const codigosAsociados =
                new Set(
                    productosAsociados.map(
                        (producto) =>
                            String(producto.codigo)
                    )
                );
            checkboxes.forEach(
                (checkbox) => {
                    checkbox.checked =
                        codigosAsociados.has(
                            String(checkbox.value)
                        );
                }
            );
            updateSelectedCount();
        } catch (error) {
            console.error(
                "Error al cargar productos asociados:",
                error
            );
            checkboxes.forEach(
                (checkbox) => {
                    checkbox.checked = false;
                }
            );
            updateSelectedCount();
        }
    }
);
const productSearch =
    document.getElementById(
        "productAssociationSearch"
    );
const productCards =
    document.querySelectorAll(
        ".product-selection-card"
    );
productSearch.addEventListener(
    "input",
    function () {
        const search =
            this.value
                .toLowerCase()
                .trim();
        productCards.forEach(
            (card) => {
                const text =
                    card.textContent
                        .toLowerCase();
                card.style.display =
                    text.includes(search)
                        ? ""
                        : "none";
            }
        );
    }
);
const proveedorConsulta =
    document.getElementById(
        "proveedorConsulta"
    );
const proveedorConsultaCustom =
    document.getElementById(
        "proveedorConsultaCustom"
    );
const proveedorConsultaTrigger =
    proveedorConsultaCustom.querySelector(
        ".custom-select-trigger"
    );
const proveedorConsultaOptions =
    document.getElementById(
        "proveedorConsultaOptions"
    );
const proveedorConsultaText =
    document.getElementById(
        "proveedorConsultaText"
    );
const proveedoresConsultaOpciones =
    Array.from(
        proveedorConsulta.options
    ).filter(
        (option) =>
            option.value !== ""
    );
function actualizarTextoProveedorConsulta() {
    const opcionSeleccionada =
        proveedorConsulta.options[
        proveedorConsulta.selectedIndex
        ];
    if (
        opcionSeleccionada &&
        opcionSeleccionada.value !== ""
    ) {
        proveedorConsultaText.textContent =
            opcionSeleccionada.textContent;
        proveedorConsultaText.classList.remove(
            "placeholder"
        );
    } else {
        proveedorConsultaText.textContent =
            "Seleccionar proveedor";
        proveedorConsultaText.classList.add(
            "placeholder"
        );
    }
}
function renderProveedoresConsulta() {
    proveedorConsultaOptions.innerHTML =
        "";
    proveedoresConsultaOpciones.forEach(
        (option) => {
            const item =
                document.createElement(
                    "button"
                );
            item.type = "button";
            item.className =
                "custom-select-option";
            if (
                proveedorConsulta.value ===
                option.value
            ) {
                item.classList.add(
                    "selected"
                );
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
                    seleccionarProveedorConsulta(
                        option
                    );
                }
            );
            proveedorConsultaOptions.appendChild(
                item
            );
        }
    );
}
function seleccionarProveedorConsulta(
    option
) {
    proveedorConsulta.value =
        option.value;
    proveedorConsulta.dispatchEvent(
        new Event("change", {
            bubbles: true
        })
    );
    actualizarTextoProveedorConsulta();
    proveedorConsultaCustom.classList.remove(
        "open"
    );
    renderProveedoresConsulta();
}
proveedorConsultaTrigger.addEventListener(
    "click",
    (event) => {
        event.stopPropagation();
        proveedorConsultaCustom.classList.toggle(
            "open"
        );
        renderProveedoresConsulta();
    }
);
document.addEventListener(
    "click",
    (event) => {
        if (
            !proveedorConsultaCustom.contains(
                event.target
            )
        ) {
            proveedorConsultaCustom.classList.remove(
                "open"
            );
        }
    }
);
actualizarTextoProveedorConsulta();
renderProveedoresConsulta();
const loadingProducts =
    document.getElementById(
        "loadingProducts"
    );
const consultationResult =
    document.getElementById(
        "consultationResult"
    );
const consultationProducts =
    document.getElementById(
        "consultationProducts"
    );
const emptyConsultation =
    document.getElementById(
        "emptyConsultation"
    );
const resultCount =
    document.getElementById(
        "resultCount"
    );
const supplierResultText =
    document.getElementById(
        "supplierResultText"
    );
proveedorConsulta.addEventListener(
    "change",
    function () {
        const proveedorNit =
            this.value;
        if (!proveedorNit) {
            return;
        }
        loadingProducts.classList.add(
            "show"
        );
        consultationResult.classList.remove(
            "show"
        );
        setTimeout(() => {
            cargarProductosProveedor(
                proveedorNit
            );
        }, 700);
    }
);
async function cargarProductosProveedor(
    proveedorNit
) {
    try {
        const response = await fetch(
            `/proveedores-productos/consultar/${encodeURIComponent(
                proveedorNit
            )}`,
        );
        if (!response.ok) {
            throw new Error(
                "No se pudieron consultar los productos."
            );
        }
        const productos =
            await response.json();
        loadingProducts.classList.remove(
            "show"
        );
        consultationResult.classList.add(
            "show"
        );
        consultationProducts.innerHTML =
            "";
        emptyConsultation.style.display =
            "none";
        const proveedorSeleccionado =
            proveedores.find(
                (proveedor) =>
                    String(proveedor.nit) ===
                    String(proveedorNit)
            );
        const nombreProveedor =
            proveedorSeleccionado
                ? proveedorSeleccionado.nombre
                : "Proveedor seleccionado";
        supplierResultText.textContent =
            `Productos suministrados por ${nombreProveedor}`;
        resultCount.textContent =
            `${productos.length} ${productos.length === 1
                ? "producto"
                : "productos"
            }`;
        if (productos.length === 0) {
            emptyConsultation.style.display =
                "flex";
            emptyConsultation.querySelector(
                "h4"
            ).textContent =
                "Sin productos asociados";
            emptyConsultation.querySelector(
                "p"
            ).textContent =
                "Este proveedor todavía no tiene productos asociados.";
            return;
        }
        productos.forEach(
            (producto) => {
                const card =
                    document.createElement(
                        "div"
                    );
                card.className =
                    "consultation-product-card";
                let icon = "fa-box";
                if (
                    producto.categoria ===
                    "Ropa"
                ) {
                    icon = "fa-shirt";
                }
                if (
                    producto.categoria ===
                    "Calzado"
                ) {
                    icon = "fa-shoe-prints";
                }
                if (
                    producto.categoria ===
                    "Accesorios"
                ) {
                    icon = "fa-bag-shopping";
                }
                card.innerHTML = `
          <div class="consultation-product-icon">
            <i class="fa-solid ${icon}"></i>
          </div>
          <div class="consultation-product-info">
            <strong>
              ${producto.nombre}
            </strong>
            <span>
              ${producto.codigo}
            </span>
          </div>
          <div class="consultation-product-details">
            <span>
              Talla:
              <strong>
                ${producto.talla}
              </strong>
            </span>
            <span>
              Color:
              <strong>
                ${producto.color}
              </strong>
            </span>
            <span>
              Categoría:
              <strong>
                ${producto.categoria}
              </strong>
            </span>
          </div>
        `;
                consultationProducts.appendChild(
                    card
                );
            }
        );
    } catch (error) {
        console.error(
            "Error al consultar productos:",
            error
        );
        loadingProducts.classList.remove(
            "show"
        );
        consultationResult.classList.add(
            "show"
        );
        consultationProducts.innerHTML =
            "";
        emptyConsultation.style.display =
            "flex";
        emptyConsultation.querySelector(
            "h4"
        ).textContent =
            "No se pudieron consultar los productos";
        emptyConsultation.querySelector(
            "p"
        ).textContent =
            "Ocurrió un error al obtener los productos del proveedor.";
        resultCount.textContent =
            "0 productos";
    }
}
