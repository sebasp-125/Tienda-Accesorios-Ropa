/* =====================================================
   ELEMENTOS
===================================================== */

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


/* =====================================================
   CAMBIAR A REGISTRO
===================================================== */

showRegister.addEventListener("click", () => {

    formsWrapper.classList.add("register-active");

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

});


/* =====================================================
   CAMBIAR A LOGIN
===================================================== */

showLogin.addEventListener("click", () => {

    formsWrapper.classList.remove("register-active");

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

});


/* =====================================================
   MOSTRAR / OCULTAR PASSWORD
===================================================== */

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

            ? '<i class="fa-regular fa-eye-slash"></i>'

            : '<i class="fa-regular fa-eye"></i>';

    });
}


/* Login */

setupPasswordToggle(
    "togglePassword",
    "password"
);


/* Registro */

setupPasswordToggle(
    "toggleRegisterPassword",
    "registerPassword"
);


/* Confirmación */

setupPasswordToggle(
    "toggleConfirmPassword",
    "confirmPassword"
);


/* =====================================================
   LOGIN
===================================================== */

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


    /* Loading */

    loginButton.classList.add("loading");

    loginButton.disabled = true;


    try {

        /*
         * AQUÍ CONECTAREMOS SPRING BOOT
         *
         * const response = await fetch("/api/auth/login", {
         *
         *     method: "POST",
         *
         *     headers: {
         *         "Content-Type": "application/json"
         *     },
         *
         *     body: JSON.stringify({
         *         email,
         *         password
         *     })
         * });
         */


        await new Promise(resolve =>
            setTimeout(resolve, 1000)
        );


        console.log("Login:", {
            email,
            password
        });


    } catch (error) {

        console.error(error);

    } finally {

        loginButton.classList.remove("loading");

        loginButton.disabled = false;
    }

});


/* =====================================================
   REGISTRO
===================================================== */

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


    /* Validaciones */

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

        alert(
            "La contraseña debe tener mínimo 6 caracteres."
        );

        return;
    }


    if (password !== confirmation) {

        alert(
            "Las contraseñas no coinciden."
        );

        return;
    }


    if (!terms.checked) {

        alert(
            "Debes aceptar los términos y condiciones."
        );

        return;
    }


    /*
     * AQUÍ CONECTAREMOS SPRING BOOT
     *
     * fetch("/api/auth/register", {
     *     method: "POST",
     *     headers: {
     *         "Content-Type": "application/json"
     *     },
     *     body: JSON.stringify({
     *         name,
     *         email,
     *         password
     *     })
     * });
     */


    console.log("Registro:", {
        name,
        email,
        password
    });

});