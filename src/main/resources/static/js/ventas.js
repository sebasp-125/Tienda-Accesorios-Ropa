// =====================================================
// GESTIÓN DE VENTAS - OMNIMODA
// =====================================================

// Ver Comprobante / Detalle de la Venta en Modal
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

// Cerrar modal al hacer clic afuera
window.addEventListener("click", function(event) {
  const modal = document.getElementById("receiptModal");
  if (event.target === modal) {
    modal.style.display = "none";
  }
});

// Cambiar estado de venta directamente desde la tabla con SweetAlert2
function cambiarEstadoRapido(idVenta, estadoActual) {
  Swal.fire({
    title: `Cambiar Estado - Venta #${idVenta}`,
    text: `Estado actual: ${estadoActual}`,
    icon: "question",
    input: "select",
    inputOptions: {
      "1": "PENDIENTE",
      "2": "PAGADA",
      "3": "CANCELADA"
    },
    inputPlaceholder: "Selecciona el nuevo estado",
    showCancelButton: true,
    confirmButtonText: "Actualizar",
    cancelButtonText: "Cancelar",
    confirmButtonColor: "#9d824f",
    cancelButtonColor: "#101827",
    inputValidator: (value) => {
      if (!value) {
        return "Debes seleccionar un estado";
      }
    }
  }).then(async (result) => {
    if (result.isConfirmed && result.value) {
      const nuevoEstadoId = result.value;

      try {
        const formData = new FormData();
        formData.append("idVenta", idVenta);
        formData.append("estadoId", nuevoEstadoId);

        const res = await fetch("/ventas/cambiar-estado", {
          method: "POST",
          body: formData
        });

        const data = await res.json();

        if (res.ok && data.success) {
          Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: data.message || "Estado actualizado exitosamente",
            showConfirmButton: false,
            timer: 1500
          }).then(() => {
            window.location.reload();
          });
        } else {
          Swal.fire({
            icon: "error",
            title: "No se pudo actualizar",
            text: data.message || "Error al actualizar estado.",
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

// Alerta de confirmación de eliminación de venta
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
