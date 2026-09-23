const formsWrapper =
    document.querySelector(".forms-wrapper");

const loginSection =
    document.getElementById("loginSection");

const registerSection =
    document.getElementById("registerSection");

const showRegister =
    document.getElementById("showRegister");

const showLogin =
    document.getElementById("showLogin");




showRegister.addEventListener("click", () => {

    formsWrapper.classList.add("register-active");

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

});




showLogin.addEventListener("click", () => {

    formsWrapper.classList.remove("register-active");

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

});




function setupPasswordToggle(buttonId, inputId) {

    const button =
        document.getElementById(buttonId);

    const input =
        document.getElementById(inputId);


    button.addEventListener("click", () => {

        const isPassword =
            input.type === "password";


        input.type =
            isPassword ? "text" : "password";


        button.innerHTML = isPassword

            ?
            '<i class="fa-regular fa-eye-slash"></i>'

            : '<i class="fa-regular fa-eye"></i>';

    });
}




setupPasswordToggle(
    "togglePassword",
    "password"
);




setupPasswordToggle(
    "toggleRegisterPassword",
    "registerPassword"
);




setupPasswordToggle(
    "toggleConfirmPassword",
    "confirmPassword"
);




const loginForm =
    document.getElementById("loginForm");

const emailInput =
    document.getElementById("email");

const passwordInput =
    document.getElementById("password");

const emailGroup =
    emailInput.closest(".form-group");

const passwordGroup =
    passwordInput.closest(".form-group");

const loginButton =
    document.getElementById("loginButton");


function validEmail(email) {

    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}


loginForm.addEventListener("submit", async (event) => {

    event.preventDefault();


    emailGroup.classList.remove("has-error");

    passwordGroup.classList.remove("has-error");


    const email =
        emailInput.value.trim();

    const password =
        passwordInput.value.trim();


    let valid = true;


    if (!email || !validEmail(email)) {

        emailGroup.classList.add("has-error");

        valid = false;
    }


    if (!password) {

        passwordGroup.classList.add("has-error");

        valid = false;
    }


    if (!valid) {

        return;
    }




    loginButton.classList.add("loading");

    loginButton.disabled = true;


    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email,
                password
            })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            Swal.fire({
                toast: true,
                position: "top-end",
                icon: "success",
                title: "¡Bienvenido, " + (data.userName || "Usuario") + "!",
                showConfirmButton: false,
                timer: 1500,
                timerProgressBar: true
            });

            setTimeout(() => {
                window.location.href = data.redirectUrl || "/dashboard";
            }, 1200);
        } else {
            Swal.fire({
                icon: "error",
                title: "Error al iniciar sesión",
                text: data.message || "Correo o contraseña incorrectos.",
                confirmButtonColor: "#9d824f"
            });
        }

    } catch (error) {
        console.error(error);
        Swal.fire({
            icon: "error",
            title: "Error de conexión",
            text: "No se pudo conectar con el servidor. Intenta de nuevo más tarde.",
            confirmButtonColor: "#9d824f"
        });
    } finally {
        loginButton.classList.remove("loading");
        loginButton.disabled = false;
    }
});




const registerForm =
    document.getElementById("registerForm");

const registerName =
    document.getElementById("registerName");

const registerEmail =
    document.getElementById("registerEmail");

const registerPassword =
    document.getElementById("registerPassword");

const confirmPassword =
    document.getElementById("confirmPassword");

const terms =
    document.getElementById("terms");

registerForm.addEventListener("submit", async (event) => {

    event.preventDefault();

    const name =
        registerName.value.trim();

    const email =
        registerEmail.value.trim();

    const password =
        registerPassword.value;

    const confirmation =
        confirmPassword.value;



    if (!name) {
        registerName.closest(".form-group")
            .classList.add("has-error");
        return;
    }

    if (!validEmail(email)) {
        registerEmail.closest(".form-group")
            .classList.add("has-error");
        return;
    }

    if (password.length < 6) {
        Swal.fire({
            icon: "warning",
            title: "Contraseña corta",
            text: "La contraseña debe tener mínimo 6 caracteres.",
            confirmButtonColor: "#9d824f"
        });
        return;
    }

    if (password !== confirmation) {
        Swal.fire({
            icon: "warning",
            title: "Contraseñas no coinciden",
            text: "Las contraseñas ingresadas no son iguales.",
            confirmButtonColor: "#9d824f"
        });
        return;
    }

    if (!terms.checked) {
        Swal.fire({
            icon: "warning",
            title: "Términos requeridos",
            text: "Debes aceptar los términos y condiciones.",
            confirmButtonColor: "#9d824f"
        });
        return;
    }

    const registerBtn = registerForm.querySelector("button[type='submit']");
    if (registerBtn) registerBtn.disabled = true;

    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name,
                email,
                password
            })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            Swal.fire({
                icon: "success",
                title: "¡Cuenta creada!",
                text: "Bienvenido a OmniModa, " + (data.userName || name) + ". Redirigiendo a tu cuenta...",
                confirmButtonColor: "#9d824f",
                timer: 2000,
                showConfirmButton: false,
                timerProgressBar: true
            });

            setTimeout(() => {
                window.location.href = data.redirectUrl || "/cliente/inicio";
            }, 1800);
        } else {
            Swal.fire({
                icon: "error",
                title: "Error de registro",
                text: data.message || "No se pudo registrar la cuenta.",
                confirmButtonColor: "#9d824f"
            });
        }
    } catch (error) {
        console.error("Error en registro:", error);
        Swal.fire({
            icon: "error",
            title: "Error de conexión",
            text: "Ocurrió un error al procesar el registro.",
            confirmButtonColor: "#9d824f"
        });
    } finally {
        if (registerBtn) registerBtn.disabled = false;
    }
});

window.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get("logout") === "true") {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "info",
            title: "Sesión cerrada correctamente",
            showConfirmButton: false,
            timer: 2500,
            timerProgressBar: true
        });
    }
});