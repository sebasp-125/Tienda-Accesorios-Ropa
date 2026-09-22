package com.apiv1.omniModa.Models.Service;

/**
 * Utilitario para convertir valores numéricos a su representación en letras en español,
 * siguiendo el estándar mercantil y tributario de Colombia (PESOS M/CTE).
 */
public class NumeroALetras {

    private static final String[] UNIDADES = {
        "", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE "
    };

    private static final String[] ESPECIALES = {
        "DIEZ ", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ",
        "DIECISEIS ", "DIECISIETE ", "DIECIOCHO ", "DIECINUEVE "
    };

    private static final String[] DECENAS = {
        "", "", "VEINTE ", "TREINTA ", "CUARENTA ", "CINCUENTA ",
        "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA "
    };

    private static final String[] CENTENAS = {
        "", "CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ",
        "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "
    };

    /**
     * Convierte un monto numérico a formato formal:
     * Ejemplo: 150000 -> "CIENTO CINCUENTA MIL PESOS M/CTE"
     */
    public static String convertir(double monto) {
        long entero = Math.round(monto);
        if (entero <= 0) {
            return "CERO PESOS M/CTE";
        }
        if (entero == 1) {
            return "UN PESO M/CTE";
        }

        String letras = convertirNumero(entero).trim();

        // En español: Si termina exactamente en millón/millones se añade "DE" (ej: UN MILLÓN DE PESOS)
        if (entero >= 1000000 && entero % 1000000 == 0) {
            return letras + " DE PESOS M/CTE";
        }

        return letras + " PESOS M/CTE";
    }

    private static String convertirNumero(long n) {
        if (n == 100) return "CIEN ";
        if (n < 10) return UNIDADES[(int) n];
        if (n < 20) return ESPECIALES[(int) (n - 10)];
        if (n < 30) {
            if (n == 20) return "VEINTE ";
            return "VEINTI" + UNIDADES[(int) (n - 20)].trim() + " ";
        }
        if (n < 100) {
            int d = (int) (n / 10);
            int u = (int) (n % 10);
            if (u == 0) return DECENAS[d];
            return DECENAS[d].trim() + " Y " + UNIDADES[u];
        }
        if (n < 1000) {
            int c = (int) (n / 100);
            int resto = (int) (n % 100);
            if (resto == 0 && c == 1) return "CIEN ";
            return CENTENAS[c] + convertirNumero(resto);
        }
        if (n < 1000000) {
            long miles = n / 1000;
            long resto = n % 1000;
            String strMiles = (miles == 1) ? "MIL " : (convertirNumero(miles).trim() + " MIL ");
            return strMiles + convertirNumero(resto);
        }
        if (n < 1000000000000L) {
            long millones = n / 1000000;
            long resto = n % 1000000;
            String strMillones = (millones == 1) ? "UN MILLON " : (convertirNumero(millones).trim() + " MILLONES ");
            return strMillones + convertirNumero(resto);
        }
        return String.valueOf(n) + " ";
    }
}
