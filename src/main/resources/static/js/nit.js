document.addEventListener("DOMContentLoaded", () => {

    const nitBase = document.getElementById("nitBase");
    const nitDv = document.getElementById("nitDv");
    const nitHidden = document.getElementById("nit");

    if (!nitBase || !nitDv || !nitHidden) {
        return;
    }





    function calcularDV(nit) {

        const pesos = [71, 67, 59, 53, 47, 43, 41, 37, 29, 23, 19, 17, 13, 7, 3];

        let suma = 0;

        const digitos = nit.split("").map(Number);

        let posicion = pesos.length - digitos.length;

        for (let i = 0; i < digitos.length; i++) {
            suma += digitos[i] * pesos[posicion + i];
        }

        const residuo = suma % 11;

        if (residuo === 0 || residuo === 1) {
            return residuo;
        }

        return 11 - residuo;
    }






    function actualizarDV() {

        const nit = nitBase.value.replace(/\D/g, "");

        nitBase.value = nit;



        if (nit.length !== 9) {

            nitDv.value = "";
            nitHidden.value = "";

            return;
        }

        const dv = calcularDV(nit);

        nitDv.value = dv;


        nitHidden.value = `${nit}-${dv}`;
    }






    nitBase.addEventListener("input", actualizarDV);






    const form = nitBase.closest("form");

    if (form) {

        form.addEventListener("submit", (event) => {

            const nit = nitBase.value.replace(/\D/g, "");

            if (nit.length !== 9) {

                event.preventDefault();

                alert("El NIT debe contener 9 dígitos.");

                nitBase.focus();

                return;
            }

            const dv = calcularDV(nit);

            nitDv.value = dv;

            nitHidden.value = `${nit}-${dv}`;
        });
    }

});