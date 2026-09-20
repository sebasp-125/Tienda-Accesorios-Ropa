// =====================================================
// TOAST - GUARDAR CLIENTE
// =====================================================

document.querySelectorAll(".save-client").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "El cliente ha sido guardado exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// TOAST - ACTUALIZAR CLIENTE
// =====================================================

document.querySelectorAll(".update-client").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "El cliente ha sido actualizado exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// ALERTA - ELIMINAR CLIENTE
// =====================================================

document.querySelectorAll(".delete-client").forEach((form) => {
    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: "btn btn-success",
                cancelButton: "btn btn-danger"
            },
            buttonsStyling: true
        });

        swalWithBootstrapButtons.fire({
            title: "¿Estás seguro?",
            text: "El cliente será eliminado permanentemente.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "No, cancelar",
            confirmButtonColor: "#9d824f",
            cancelButtonColor: "#101827",
            reverseButtons: true
        }).then((result) => {

            if (result.isConfirmed) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "success",
                    title: "El cliente ha sido eliminado exitosamente",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });

                form.submit();

            } else if (result.dismiss === Swal.DismissReason.cancel) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "info",
                    title: "El cliente no ha sido eliminado",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });
            }
        });
    });
});

// =====================================================
// TOAST - GUARDAR PRODUCTO
// =====================================================

document.querySelectorAll(".save-product").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "El producto ha sido guardado exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// TOAST - ACTUALIZAR PRODUCTO
// =====================================================

document.querySelectorAll(".update-product").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "El producto ha sido actualizado exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// ALERTA - ELIMINAR PRODUCTO
// =====================================================

document.querySelectorAll(".delete-product").forEach((form) => {
    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: "btn btn-success",
                cancelButton: "btn btn-danger"
            },
            buttonsStyling: true
        });

        swalWithBootstrapButtons.fire({
            title: "¿Estás seguro?",
            text: "El producto será eliminado permanentemente.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "No, cancelar",
            confirmButtonColor: "#9d824f",
            cancelButtonColor: "#101827",
            reverseButtons: true
        }).then((result) => {

            if (result.isConfirmed) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "success",
                    title: "El producto ha sido eliminado exitosamente",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });

                form.submit();

            } else if (result.dismiss === Swal.DismissReason.cancel) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "info",
                    title: "El producto no ha sido eliminado",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });
            }
        });
    });
});

// =====================================================
// TOAST - GUARDAR PROVEEDOR
// =====================================================

document.querySelectorAll(".save-provider").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "El proveedor ha sido guardado exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// TOAST - ACTUALIZAR PROVEEDOR
// =====================================================

document.querySelectorAll(".update-provider").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "El proveedor ha sido actualizado exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// ALERTA - ELIMINAR PROVEEDOR
// =====================================================

document.querySelectorAll(".delete-provider").forEach((form) => {
    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: "btn btn-success",
                cancelButton: "btn btn-danger"
            },
            buttonsStyling: true
        });

        swalWithBootstrapButtons.fire({
            title: "¿Estás seguro?",
            text: "El proveedor será eliminado permanentemente.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "No, cancelar",
            confirmButtonColor: "#9d824f",
            cancelButtonColor: "#101827",
            reverseButtons: true
        }).then((result) => {

            if (result.isConfirmed) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "success",
                    title: "El proveedor ha sido eliminado exitosamente",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });

                form.submit();

            } else if (result.dismiss === Swal.DismissReason.cancel) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "info",
                    title: "El proveedor no ha sido eliminado",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });
            }
        });
    });
});

// =====================================================
// TOAST - GUARDAR PROMOCION
// =====================================================

document.querySelectorAll(".save-promotion").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "La promoción ha sido registrada exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// TOAST - ACTUALIZAR PROMOCION
// =====================================================

document.querySelectorAll(".update-promotion").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "La promoción ha sido actualizada exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

// =====================================================
// ALERTA - ELIMINAR PROMOCION
// =====================================================

document.querySelectorAll(".delete-promotion").forEach((form) => {
    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: "btn btn-success",
                cancelButton: "btn btn-danger"
            },
            buttonsStyling: true
        });

        swalWithBootstrapButtons.fire({
            title: "¿Estás seguro?",
            text: "La promoción será eliminada permanentemente.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "No, cancelar",
            confirmButtonColor: "#9d824f",
            cancelButtonColor: "#101827",
            reverseButtons: true
        }).then((result) => {

            if (result.isConfirmed) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "success",
                    title: "La promoción ha sido eliminada exitosamente",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });

                form.submit();

            } else if (result.dismiss === Swal.DismissReason.cancel) {

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "info",
                    title: "La promoción no ha sido eliminada",
                    showConfirmButton: false,
                    timer: 2000,
                    timerProgressBar: false
                });
            }
        });
    });
});

// =====================================================
// TOAST - EFECTUAR ASOCIACION PROVEEDOR - PRODUCTO
// =====================================================

document.querySelectorAll(".save-asociacion").forEach((form) => {
    form.addEventListener("submit", function () {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "La asociacion de ha realizado exitosamente",
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: false
        });
    });
});

