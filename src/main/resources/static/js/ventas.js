async function verDetalleVenta(idVenta) {
  try {
    const res = await fetch(`/ventas/api/detalle/${idVenta}`);
    if (!res.ok) throw new Error("No se pudo cargar el detalle");
    const det = await res.json();

    document.getElementById("modalVentaId").textContent = det.idVenta;
    document.getElementById("modalFecha").textContent = det.fecha;
    document.getElementById("modalClienteDoc").textContent = det.clienteDocumento;
    document.getElementById("modalClienteNombre").textContent = det.clienteNombre;
    document.getElementById("modalClienteCorreo").textContent = det.clienteCorreo;
    document.getElementById("modalClienteTel").textContent = det.clienteTelefono;

    const statusPill = document.getElementById("modalEstadoPill");
    statusPill.textContent = det.estado;
    statusPill.className = "status-pill " + (det.estado || "").toLowerCase();

    const tbody = document.getElementById("modalItemsBody");
    let rowsHtml = "";

    (det.items || []).forEach(item => {
      rowsHtml += `
        <tr>
          <td><strong>${item.nombre}</strong><br><small style="color: #64748b;">${item.codigo}</small></td>
          <td>${item.cantidad}</td>
          <td>$${item.precioUnitario.toLocaleString('es-CO', { minimumFractionDigits: 2 })}</td>
          <td><strong>$${item.subtotal.toLocaleString('es-CO', { minimumFractionDigits: 2 })}</strong></td>
        </tr>
      `;
    });

    tbody.innerHTML = rowsHtml;
    document.getElementById("modalTotalAmount").textContent = "$" + det.total.toLocaleString('es-CO', { minimumFractionDigits: 2 });

    document.getElementById("receiptModal").style.display = "flex";
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "No se pudo obtener el detalle de la venta.",
      confirmButtonColor: "#9d824f"
    });
  }
}

function cerrarModalDetalle() {
  document.getElementById("receiptModal").style.display = "none";
}


window.addEventListener("click", function (event) {
  const modal = document.getElementById("receiptModal");
  if (event.target === modal) {
    modal.style.display = "none";
  }
});


function cambiarEstadoRapido(idVenta, estadoActual) {

  Swal.fire({

    title: `Cambiar Estado - Venta #${idVenta}`,

    html: `
            <div class="swal-status-content">

                <div class="swal-status-current">
                    <span>Estado actual</span>
                    <strong>${estadoActual}</strong>
                </div>

                <div class="swal-status-select">

                    <div class="swal-status-trigger" tabindex="0">

                        <span class="swal-status-text">
                            Selecciona el nuevo estado
                        </span>

                        <i class="fa-solid fa-chevron-down swal-status-arrow"></i>

                    </div>

                    <div class="swal-status-dropdown">

                        <button
                            type="button"
                            class="swal-status-option"
                            data-value="1"
                        >
                            <span>
                                <strong>PENDIENTE</strong>
                                <small>La venta está pendiente</small>
                            </span>

                            <i class="fa-solid fa-check"></i>
                        </button>

                        <button
                            type="button"
                            class="swal-status-option"
                            data-value="2"
                        >
                            <span>
                                <strong>PAGADA</strong>
                                <small>La venta fue pagada</small>
                            </span>

                            <i class="fa-solid fa-check"></i>
                        </button>

                        <button
                            type="button"
                            class="swal-status-option"
                            data-value="3"
                        >
                            <span>
                                <strong>CANCELADA</strong>
                                <small>La venta fue cancelada</small>
                            </span>

                            <i class="fa-solid fa-check"></i>
                        </button>

                    </div>

                </div>

            </div>
        `,

    showCancelButton: true,

    confirmButtonText: "Actualizar",

    cancelButtonText: "Cancelar",

    confirmButtonColor: "#9d824f",

    cancelButtonColor: "#101827",

    customClass: {
      popup: "omnimoda-status-popup",
      title: "omnimoda-status-title",
      htmlContainer: "omnimoda-status-container",
      confirmButton: "omnimoda-status-confirm",
      cancelButton: "omnimoda-status-cancel"
    },

    didOpen: () => {

      const popup =
        Swal.getPopup();

      const select =
        popup.querySelector(
          ".swal-status-select"
        );

      const trigger =
        popup.querySelector(
          ".swal-status-trigger"
        );

      const texto =
        popup.querySelector(
          ".swal-status-text"
        );

      const opciones =
        popup.querySelectorAll(
          ".swal-status-option"
        );

      let estadoSeleccionado = "";

      opciones.forEach((opcion) => {

        opcion.addEventListener("click", () => {

          estadoSeleccionado =
            opcion.dataset.value;

          texto.textContent =
            opcion.querySelector(
              "strong"
            ).textContent;

          opciones.forEach((otra) => {
            otra.classList.remove(
              "selected"
            );
          });

          opcion.classList.add(
            "selected"
          );

          select.classList.remove(
            "status-select-open"
          );

        });

      });

      trigger.addEventListener("click", (event) => {

        event.stopPropagation();

        select.classList.toggle(
          "status-select-open"
        );

      });

      trigger.addEventListener("keydown", (event) => {

        if (
          event.key === "Enter" ||
          event.key === " "
        ) {

          event.preventDefault();

          select.classList.toggle(
            "status-select-open"
          );

        }

        if (event.key === "Escape") {

          select.classList.remove(
            "status-select-open"
          );

        }

      });

    },

    preConfirm: () => {

      const popup =
        Swal.getPopup();

      const opcionSeleccionada =
        popup.querySelector(
          ".swal-status-option.selected"
        );

      if (!opcionSeleccionada) {

        Swal.showValidationMessage(
          "Debes seleccionar un estado"
        );

        return false;
      }

      return opcionSeleccionada.dataset.value;
    }

  }).then(async (result) => {

    if (
      result.isConfirmed &&
      result.value
    ) {

      const nuevoEstadoId =
        result.value;

      try {

        const formData =
          new FormData();

        formData.append(
          "idVenta",
          idVenta
        );

        formData.append(
          "estadoId",
          nuevoEstadoId
        );

        const res =
          await fetch(
            "/ventas/cambiar-estado", {
            method: "POST",
            body: formData
          }
          );

        const data =
          await res.json();

        if (
          res.ok &&
          data.success
        ) {

          Swal.fire({

            toast: true,

            position: "top-end",

            icon: "success",

            title: data.message ||
              "Estado actualizado exitosamente",

            showConfirmButton: false,

            timer: 1500

          }).then(() => {

            window.location.reload();

          });

        } else {

          Swal.fire({

            icon: "error",

            title: "No se pudo actualizar",

            text: data.message ||
              "Error al actualizar estado.",

            confirmButtonColor: "#9d824f"

          });

        }

      } catch (e) {

        Swal.fire({

          icon: "error",

          title: "Error de conexión",

          text: "No fue posible comunicarse con el servidor.",

          confirmButtonColor: "#9d824f"

        });

      }

    }

  });

}


document.querySelectorAll(".delete-sale-form").forEach((form) => {
  form.addEventListener("submit", function (event) {
    event.preventDefault();

    Swal.fire({
      title: "¿Eliminar esta venta?",
      text: "Si la venta no estaba cancelada, el stock de los productos será restaurado al inventario.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
      confirmButtonColor: "#dc2626",
      cancelButtonColor: "#101827",
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          toast: true,
          position: "top-end",
          icon: "success",
          title: "Venta eliminada exitosamente",
          showConfirmButton: false,
          timer: 1500
        });
        form.submit();
      }
    });
  });
});