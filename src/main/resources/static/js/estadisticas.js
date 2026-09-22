const productosData = JSON.parse(productosJson);
const clientesData = JSON.parse(clientesJson);
const ingresosData = JSON.parse(ingresosJson);
const tiposData = JSON.parse(tiposJson);

console.log("Productos:", productosData);
console.log("Clientes:", clientesData);
console.log("Ingresos:", ingresosData);
console.log("Tipos:", tiposData);

console.log("Productos es array:", Array.isArray(productosData));
console.log("Clientes es array:", Array.isArray(clientesData));
console.log("Ingresos es array:", Array.isArray(ingresosData));
console.log("Tipos es array:", Array.isArray(tiposData));

// =============================================================
// DOM
// =============================================================

document.addEventListener("DOMContentLoaded", () => {


    // =========================================================
    // COMPROBAR CHART.JS
    // =========================================================

    if (typeof Chart === "undefined") {

        console.error(
            "ERROR: Chart.js no está cargado."
        );

        return;
    }


    console.log(
        "Chart.js cargado correctamente."
    );


    // =========================================================
    // CONFIGURACIÓN GENERAL
    // =========================================================

    Chart.defaults.font.family =
        "Playpen Sans, sans-serif";

    Chart.defaults.color =
        "#64748b";


    // =========================================================
    // INGRESOS POR PERÍODO
    // =========================================================

    const ingresosCanvas =
        document.getElementById("ingresosChart");


    if (ingresosCanvas) {

        if (ingresosData.length === 0) {

            console.warn(
                "No hay datos para la gráfica de ingresos."
            );

        } else {

            new Chart(
                ingresosCanvas, {

                type: "line",

                data: {

                    labels: ingresosData.map(
                        item => item.periodo
                    ),

                    datasets: [

                        {

                            label: "Ingresos",

                            data: ingresosData.map(
                                item => Number(item.ingresos)
                            ),

                            borderColor: "#b69a6a",

                            backgroundColor: "rgba(182, 154, 106, 0.10)",

                            borderWidth: 3,

                            pointRadius: 4,

                            pointHoverRadius: 6,

                            tension: 0.35,

                            fill: true

                        }

                    ]

                },

                options: {

                    responsive: true,

                    maintainAspectRatio: false,

                    plugins: {

                        legend: {

                            display: false

                        },

                        tooltip: {

                            callbacks: {

                                label: function (context) {

                                    return "$ " +
                                        Number(
                                            context.raw
                                        ).toLocaleString(
                                            "es-CO"
                                        );

                                }

                            }

                        }

                    },

                    scales: {

                        y: {

                            beginAtZero: true,

                            ticks: {

                                callback: function (value) {

                                    return "$ " +
                                        Number(
                                            value
                                        ).toLocaleString(
                                            "es-CO"
                                        );

                                }

                            }

                        }

                    }

                }

            }
            );

        }

    }


    // =========================================================
    // PRODUCTOS MÁS VENDIDOS
    // =========================================================

    const productosCanvas =
        document.getElementById("productosChart");


    if (productosCanvas) {

        if (productosData.length === 0) {

            console.warn(
                "No hay datos para la gráfica de productos."
            );

        } else {

            const productos =
                productosData.slice(0, 8);


            new Chart(
                productosCanvas, {

                type: "bar",

                data: {

                    labels: productos.map(
                        item => item.nombre
                    ),

                    datasets: [

                        {

                            label: "Unidades vendidas",

                            data: productos.map(
                                item =>
                                    Number(item.cantidad)
                            ),

                            backgroundColor: "#b69a6a",

                            borderRadius: 8

                        }

                    ]

                },

                options: {

                    responsive: true,

                    maintainAspectRatio: false,

                    plugins: {

                        legend: {

                            display: false

                        }

                    }

                }

            }
            );

        }

    }


    // =========================================================
    // CLIENTES FRECUENTES
    // =========================================================

    const clientesCanvas =
        document.getElementById("clientesChart");


    if (clientesCanvas) {

        if (clientesData.length === 0) {

            console.warn(
                "No hay datos para la gráfica de clientes."
            );

        } else {

            const clientes =
                clientesData.slice(0, 8);


            new Chart(
                clientesCanvas, {

                type: "bar",

                data: {

                    labels: clientes.map(
                        item => item.nombre
                    ),

                    datasets: [

                        {

                            label: "Compras",

                            data: clientes.map(
                                item =>
                                    Number(item.compras)
                            ),

                            backgroundColor: "#64748b",

                            borderRadius: 8

                        }

                    ]

                },

                options: {

                    indexAxis: "y",

                    responsive: true,

                    maintainAspectRatio: false,

                    plugins: {

                        legend: {

                            display: false

                        }

                    }

                }

            }
            );

        }

    }


    // =========================================================
    // INGRESOS POR TIPO DE CLIENTE
    // =========================================================

    const tipoClienteCanvas =
        document.getElementById(
            "tipoClienteChart"
        );


    if (tipoClienteCanvas) {

        if (tiposData.length === 0) {

            console.warn(
                "No hay datos para la gráfica de tipo de cliente."
            );

        } else {

            new Chart(
                tipoClienteCanvas, {

                type: "doughnut",

                data: {

                    labels: tiposData.map(
                        item => item.tipo
                    ),

                    datasets: [

                        {

                            data: tiposData.map(
                                item =>
                                    Number(item.ingresos)
                            ),

                            backgroundColor: [

                                "#b69a6a",

                                "#64748b"

                            ],

                            borderWidth: 0,

                            hoverOffset: 8

                        }

                    ]

                },

                options: {

                    responsive: true,

                    maintainAspectRatio: false,

                    cutout: "68%",

                    plugins: {

                        legend: {

                            position: "bottom",

                            labels: {

                                usePointStyle: true,

                                padding: 20

                            }

                        },

                        tooltip: {

                            callbacks: {

                                label: function (context) {

                                    return "$ " +
                                        Number(
                                            context.raw
                                        ).toLocaleString(
                                            "es-CO"
                                        );

                                }

                            }

                        }

                    }

                }

            }
            );

        }

    }

});