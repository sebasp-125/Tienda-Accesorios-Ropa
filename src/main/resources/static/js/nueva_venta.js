if (typeof itemsVenta === "undefined") {
    var itemsVenta = [];
}

function agregarProductoALaLista() {
    const select = document.getElementById("selectProducto");
    const selectedOpt = select.options[select.selectedIndex];
    if (!selectedOpt || !selectedOpt.value) {
        Swal.fire({
            icon: "warning",
            title: "Producto requerido",
            text: "Selecciona un producto para agregar.",
            confirmButtonColor: "#9d824f",
        });
        return;
    }
    const codigo = selectedOpt.value;
    const nombre = selectedOpt.getAttribute("data-nombre");
    const precio =
        parseFloat(selectedOpt.getAttribute("data-precio")) || 0;
    const stockMax =
        parseInt(selectedOpt.getAttribute("data-stock")) || 0;
    const inputCant =
        document.getElementById("inputCantidad");
    const cantidad =
        parseInt(inputCant.value) || 1;
    if (cantidad <= 0) {
        Swal.fire({
            icon: "warning",
            title: "Cantidad inválida",
            text: "La cantidad debe ser mayor a 0.",
            confirmButtonColor: "#9d824f",
        });
        return;
    }
    const existente =
        itemsVenta.find((i) => i.codigo === codigo);
    const cantTotal =
        (existente ? existente.cantidad : 0) + cantidad;
    if (cantTotal > stockMax) {
        Swal.fire({
            icon: "warning",
            title: "Stock insuficiente",
            text: `Solo hay ${stockMax} unidades disponibles de este producto.`,
            confirmButtonColor: "#9d824f",
        });
        return;
    }
    if (existente) {
        existente.cantidad = cantTotal;
        existente.subtotal =
            existente.cantidad * existente.precio;
    } else {
        itemsVenta.push({
            codigo: codigo,
            nombre: nombre,
            precio: precio,
            cantidad: cantidad,
            subtotal: precio * cantidad,
        });
    }
    renderItems();
    inputCant.value = 1;
    select.selectedIndex = 0;
}

function removerItem(codigo) {
    itemsVenta =
        itemsVenta.filter((i) => i.codigo !== codigo);
    renderItems();
}

function renderItems() {
    const tbody =
        document.getElementById("itemsTableBody");
    const displayTotal =
        document.getElementById("displayTotalVenta");
    if (itemsVenta.length === 0) {
        tbody.innerHTML = `
            <tr id="emptyItemsRow">
                <td colspan="6" class="empty-items">
                    No has agregado productos a la venta.
                    Selecciona uno arriba y haz clic en "Agregar".
                </td>
            </tr>
        `;
        displayTotal.textContent = "$0.00";
        return;
    }
    let html = "";
    let total = 0;
    itemsVenta.forEach((i) => {
        total += i.subtotal;
        html += `
            <tr>
                <td>
                    <strong>${i.codigo}</strong>
                    <input
                        type="hidden"
                        name="codigos"
                        value="${i.codigo}"
                    />
                </td>
                <td>
                    ${i.nombre}
                </td>
                <td>
                    $${i.precio.toLocaleString("es-CO", {
            minimumFractionDigits: 2
        })}
                </td>
                <td>
                    ${i.cantidad}
                    <input
                        type="hidden"
                        name="cantidades"
                        value="${i.cantidad}"
                    />
                </td>
                <td>
                    <strong>
                        $${i.subtotal.toLocaleString("es-CO", {
            minimumFractionDigits: 2
        })}
                    </strong>
                </td>
                <td>
                    <button
                        type="button"
                        class="action-button delete"
                        data-codigo="${i.codigo}"
                        title="Eliminar de la venta"
                    >
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;
    });
    tbody.innerHTML = html;
    displayTotal.textContent =
        "$" +
        total.toLocaleString("es-CO", {
            minimumFractionDigits: 2
        });
}
document
    .getElementById("btnAgregarProducto")
    .addEventListener("click", agregarProductoALaLista);
document
    .getElementById("itemsTableBody")
    .addEventListener("click", function (e) {
        const boton =
            e.target.closest(".action-button.delete");
        if (!boton) {
            return;
        }
        const codigo =
            boton.getAttribute("data-codigo");
        removerItem(codigo);
    });
document
    .getElementById("formNuevaVenta")
    .addEventListener("submit", function (e) {
        if (itemsVenta.length === 0) {
            e.preventDefault();
            Swal.fire({
                icon: "warning",
                title: "Productos requeridos",
                text: "Debes agregar al menos un producto a la venta.",
                confirmButtonColor: "#9d824f",
            });
            return;
        }
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "Venta registrada exitosamente",
            showConfirmButton: false,
            timer: 1500,
        });
    });