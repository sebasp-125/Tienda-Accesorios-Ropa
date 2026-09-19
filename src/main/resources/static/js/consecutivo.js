document.addEventListener("DOMContentLoaded", async () => {
    const codigoInput = document.getElementById("codigo");
    if (!codigoInput) {
        return;
    }
    try {
        const response = await fetch("/productos/siguiente-codigo");
        if (!response.ok) {
            throw new Error("No se pudo obtener el siguiente código");
        }
        const codigo = await response.text();
        codigoInput.value = codigo;
    } catch (error) {
        console.error("Error al obtener el consecutivo:", error);
    }
});