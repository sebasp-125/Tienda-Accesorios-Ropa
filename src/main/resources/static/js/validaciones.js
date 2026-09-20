function validarTexto(input) {
    input.value = input.value.replace(/[^a-zA-ZñÑ\s]/g, '');
}

function validarTextoConAcentos(input) {
    input.value = input.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\s]/g, '');
}

function validarNumeros(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
}

function validarTelefono(input) {
    input.value = input.value.replace(/[^0-9]/g, '').slice(0, 10);
}

function validarTelefonoInternacional(input) {
    input.value = input.value.replace(/[^0-9]/g, '').slice(0, 15);
}

function validarCorreo(input) {
    input.value = input.value.replace(/[^a-zA-Z0-9@._%+\-]/g, '');
}

function validarFormatoCorreo(input) {
    const correo = input.value.trim();
    const patron = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (correo !== '' && !patron.test(correo)) {
        input.setCustomValidity('Ingrese un correo electrónico válido.');
    } else {
        input.setCustomValidity('');
    }
}

function validarPrecio(input) {
    let valor = input.value;

    valor = valor.replace(/[^0-9.]/g, '');

    const partes = valor.split('.');

    if (partes.length > 2) {
        valor = partes[0] + '.' + partes.slice(1).join('');
    }

    input.value = valor;
}

function validarPrecioEntero(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
}

function validarAlfanumerico(input) {
    input.value = input.value.replace(/[^a-zA-Z0-9]/g, '');
}

function validarAlfanumericoEspacios(input) {
    input.value = input.value.replace(/[^a-zA-Z0-9\s]/g, '');
}

function validarNIT(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
}

function validarDocumento(input) {
    input.value = input.value.replace(/[^0-9]/g, '').slice(0, 10);
}

function validarBuscador(input) {
    input.value = input.value.replace(/[^a-zA-Z0-9@._+\-\s]/g, '');
}

function validarTextoyCaracter(input) {
    input.value = input.value.replace(/[^a-zA-Z0-9ñÑ\s]/g, '');
}

function validarTextoPromocion(input) {
    input.value = input.value.replace(/[^a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ\s.,:%!¡¿?()\-]/g, '');
}

function validarPorcentaje(input) {
    let valor = input.value.replace(/[^0-9.]/g, '');

    const partes = valor.split('.');

    if (partes.length > 2) {
        valor = partes[0] + '.' + partes.slice(1).join('');
    }

    if (parseFloat(valor) > 100) {
        valor = '100';
    }

    input.value = valor;
}

function limitarCaracteres(input, maximo) {
    if (input.value.length > maximo) {
        input.value = input.value.slice(0, maximo);
    }
}
