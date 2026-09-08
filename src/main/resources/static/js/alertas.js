
document.querySelectorAll(".delete-client").forEach((form) => {

    form.addEventListener("submit", function (event) {

        // Evita que el formulario se envíe inmediatamente
        event.preventDefault();

        Swal.fire({
            title: "¿Estas seguro?",
            text: "El cliente será eliminado permanentemente.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#9d824f",
            cancelButtonColor: "#101827",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar"
        }).then((result) => {

            if (result.isConfirmed) {
                form.submit();
            }

        });

    });

});

