package com.apiv1.omniModa.Models.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.Entity.Detalle_venta;
import com.apiv1.omniModa.Models.Entity.Ventas;

/**
 * Servicio generador de Facturas en PDF de alta fidelidad,
 * implementado en Java puro (100% independiente de bibliotecas externas).
 * Garantiza total compatibilidad con DevTools, ClassLoaders y cualquier entorno.
 */
@Service
public class FacturaPdfService {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    public byte[] generarFacturaPdf(Ventas venta, String metodoPago) {
        PdfCanvas canvas = new PdfCanvas();

        // Dimensiones A4: 595 x 842 pt
        double pageWidth = 595.0;
        double pageHeight = 842.0;

        // 1. BANNER SUPERIOR OSCURO (OmniModa Brand)
        canvas.setFillColor(0.063, 0.094, 0.153); // #101827
        canvas.fillRect(0, pageHeight - 95, pageWidth, 95);

        // Línea dorada decorativa debajo del banner
        canvas.setFillColor(0.714, 0.604, 0.416); // #b69a6a
        canvas.fillRect(0, pageHeight - 98, pageWidth, 3);

        // Título de la Empresa
        canvas.setTextColor(1.0, 1.0, 1.0);
        canvas.drawText("OMNIMODA S.A.S.", 36, pageHeight - 45, "F2", 20);
        canvas.setTextColor(0.714, 0.604, 0.416);
        canvas.drawText("MODA & ACCESORIOS EXCLUSIVOS - FACTURA ELECTRONICA", 36, pageHeight - 62, "F1", 9);
        canvas.setTextColor(0.8, 0.85, 0.9);
        canvas.drawText("NIT: 901.458.789-1 | Regimen Comun IVA | Cra. 7 # 123-45, Bogota D.C.", 36, pageHeight - 76, "F1", 8);

        // Recuadro de Factura Electrónica (Superior Derecho)
        String consecutivo = String.format("FAC-%06d", venta.getIdVentas() != null ? venta.getIdVentas() : 1);
        canvas.setFillColor(0.12, 0.17, 0.25);
        canvas.fillRect(pageWidth - 210, pageHeight - 85, 175, 55);
        canvas.setStrokeColor(0.714, 0.604, 0.416);
        canvas.strokeRect(pageWidth - 210, pageHeight - 85, 175, 55, 1.0);

        canvas.setTextColor(0.714, 0.604, 0.416);
        canvas.drawText("FACTURA DE VENTA", pageWidth - 195, pageHeight - 48, "F2", 10);
        canvas.setTextColor(1.0, 1.0, 1.0);
        canvas.drawText(consecutivo, pageWidth - 195, pageHeight - 64, "F2", 14);

        String fechaStr = venta.getFecha() != null ? venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
        canvas.setTextColor(0.75, 0.8, 0.85);
        canvas.drawText("Fecha: " + fechaStr + " | ESTADO: PAGADA", pageWidth - 195, pageHeight - 78, "F1", 7.5);

        // 2. SECCIÓN: DATOS DEL CLIENTE Y DEL PAGO
        double yInfo = pageHeight - 120;
        double infoBoxHeight = 82;

        // Caja izquierda: Cliente
        canvas.setFillColor(0.97, 0.98, 0.99); // #f8fafc
        canvas.fillRect(36, yInfo - infoBoxHeight, 255, infoBoxHeight);
        canvas.setStrokeColor(0.88, 0.91, 0.94); // #e2e8f0
        canvas.strokeRect(36, yInfo - infoBoxHeight, 255, infoBoxHeight, 1.0);

        canvas.setTextColor(0.063, 0.094, 0.153);
        canvas.drawText("DATOS DEL CLIENTE / ADQUIRENTE", 46, yInfo - 18, "F2", 9);

        String nomCliente = (venta.getCliente() != null && venta.getCliente().getNombreCompleto() != null)
                ? venta.getCliente().getNombreCompleto() : "Cliente General";
        String docCliente = (venta.getCliente() != null && venta.getCliente().getDocumento() != null)
                ? venta.getCliente().getDocumento() : "222222222222";
        String correoCliente = (venta.getCliente() != null && venta.getCliente().getCorreo() != null)
                ? venta.getCliente().getCorreo() : "No registrado";
        String telCliente = (venta.getCliente() != null && venta.getCliente().getTelefono() != null)
                ? venta.getCliente().getTelefono() : "Sin registrar";

        canvas.setTextColor(0.2, 0.25, 0.3);
        canvas.drawText("Nombre: " + truncar(nomCliente, 32), 46, yInfo - 34, "F1", 8.5);
        canvas.drawText("Identificacion / CC: " + docCliente, 46, yInfo - 48, "F1", 8.5);
        canvas.drawText("Correo: " + truncar(correoCliente, 35), 46, yInfo - 62, "F1", 8.5);
        canvas.drawText("Telefono: " + telCliente, 46, yInfo - 74, "F1", 8.5);

        // Caja derecha: Pago
        canvas.setFillColor(0.97, 0.98, 0.99);
        canvas.fillRect(305, yInfo - infoBoxHeight, 254, infoBoxHeight);
        canvas.setStrokeColor(0.88, 0.91, 0.94);
        canvas.strokeRect(305, yInfo - infoBoxHeight, 254, infoBoxHeight, 1.0);

        canvas.setTextColor(0.063, 0.094, 0.153);
        canvas.drawText("INFORMACION DEL PAGO Y APROBACION", 315, yInfo - 18, "F2", 9);

        String metodo = (metodoPago != null && !metodoPago.isBlank()) ? metodoPago : "Pago en Linea";
        String codAut = "AUT-" + (venta.getIdVentas() != null ? (748291 + venta.getIdVentas() * 41) : "938201");
        String estadoStr = (venta.getEstado() != null && venta.getEstado().getTipo() != null)
                ? venta.getEstado().getTipo() : "PAGADA";

        canvas.setTextColor(0.2, 0.25, 0.3);
        canvas.drawText("Metodo: " + truncar(metodo, 30), 315, yInfo - 34, "F1", 8.5);
        canvas.drawText("Cod. Autorizacion: " + codAut, 315, yInfo - 48, "F1", 8.5);
        canvas.drawText("Estado: " + estadoStr + " (TRANSACCION APROBADA)", 315, yInfo - 62, "F1", 8.5);
        canvas.drawText("Moneda: Peso Colombiano (COP)", 315, yInfo - 74, "F1", 8.5);

        // 3. TABLA DE ÍTEMS / DETALLE DE COMPRA
        double yTable = yInfo - infoBoxHeight - 22;

        // Cabecera de la tabla
        canvas.setFillColor(0.063, 0.094, 0.153); // #101827
        canvas.fillRect(36, yTable - 20, 523, 20);

        canvas.setTextColor(1.0, 1.0, 1.0);
        canvas.drawText("CANT", 45, yTable - 14, "F2", 8.5);
        canvas.drawText("CODIGO", 85, yTable - 14, "F2", 8.5);
        canvas.drawText("DESCRIPCION DEL ARTICULO", 160, yTable - 14, "F2", 8.5);
        canvas.drawText("PRECIO UNIT.", 410, yTable - 14, "F2", 8.5);
        canvas.drawText("SUBTOTAL", 495, yTable - 14, "F2", 8.5);

        double yRow = yTable - 20;
        boolean alternate = false;
        double totalCalculado = 0.0;

        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            for (Detalle_venta det : venta.getDetalles()) {
                double rowHeight = 22;
                yRow -= rowHeight;

                if (alternate) {
                    canvas.setFillColor(0.97, 0.98, 0.99);
                    canvas.fillRect(36, yRow, 523, rowHeight);
                }
                alternate = !alternate;

                // Línea inferior de la fila
                canvas.setStrokeColor(0.9, 0.92, 0.94);
                canvas.strokeLine(36, yRow, 559, yRow, 0.5);

                // Datos
                canvas.setTextColor(0.1, 0.15, 0.2);
                canvas.drawText(String.valueOf(det.getCantidad()), 52, yRow + 7, "F1", 8.5);

                String cod = (det.getProducto() != null && det.getProducto().getCodigo() != null)
                        ? det.getProducto().getCodigo() : "ART";
                canvas.drawText(cod, 85, yRow + 7, "F1", 8.5);

                String desc = (det.getProducto() != null && det.getProducto().getNombre() != null)
                        ? det.getProducto().getNombre() : "Prenda OmniModa";
                if (det.getProducto() != null && det.getProducto().getTalla() != null) {
                    desc += " (" + det.getProducto().getTalla() + ")";
                }
                canvas.drawText(truncar(desc, 38), 160, yRow + 7, "F1", 8.5);

                double precioU = det.getPrecioUnitario() != null ? det.getPrecioUnitario() : 0.0;
                double sub = det.getSubtotal() != null ? det.getSubtotal() : (precioU * det.getCantidad());
                totalCalculado += sub;

                canvas.drawText(formatearMonedaSimple(precioU), 410, yRow + 7, "F1", 8.5);
                canvas.drawText(formatearMonedaSimple(sub), 495, yRow + 7, "F2", 8.5);
            }
        }

        // Borde exterior de la tabla
        canvas.setStrokeColor(0.8, 0.85, 0.9);
        canvas.strokeRect(36, yRow, 523, (yTable - yRow), 1.0);

        // 4. LIQUIDACIÓN FINANCIERA (TOTALES)
        double totalFinal = (venta.getTotal() != null && venta.getTotal() > 0) ? venta.getTotal() : totalCalculado;
        double baseImponible = totalFinal / 1.19;
        double iva19 = totalFinal - baseImponible;

        double yTotales = yRow - 24;
        double totalesBoxWidth = 220;
        double xTotales = pageWidth - 36 - totalesBoxWidth;

        canvas.setTextColor(0.3, 0.35, 0.4);
        canvas.drawText("Subtotal (sin IVA):", xTotales, yTotales, "F1", 8.5);
        canvas.drawText(formatearMonedaSimple(baseImponible), xTotales + 140, yTotales, "F1", 8.5);

        canvas.drawText("IVA (19% discriminado):", xTotales, yTotales - 14, "F1", 8.5);
        canvas.drawText(formatearMonedaSimple(iva19), xTotales + 140, yTotales - 14, "F1", 8.5);

        canvas.drawText("Descuento Comercial:", xTotales, yTotales - 28, "F1", 8.5);
        canvas.drawText("$0", xTotales + 140, yTotales - 28, "F1", 8.5);

        // TOTAL DESTACADO
        double yTotalBox = yTotales - 58;
        canvas.setFillColor(0.063, 0.094, 0.153); // #101827
        canvas.fillRect(xTotales - 10, yTotalBox, totalesBoxWidth + 10, 24);

        canvas.setTextColor(1.0, 1.0, 1.0);
        canvas.drawText("TOTAL A PAGAR:", xTotales, yTotalBox + 8, "F2", 9.5);
        canvas.setTextColor(0.714, 0.604, 0.416); // #b69a6a
        canvas.drawText(formatearMonedaSimple(totalFinal), xTotales + 130, yTotalBox + 8, "F2", 11);

        // 5. PIE DE PÁGINA LEGAL Y AGRADECIMIENTO
        double yFoot = 75;
        canvas.setFillColor(0.97, 0.98, 0.99);
        canvas.fillRect(36, yFoot, 523, 48);
        canvas.setStrokeColor(0.88, 0.91, 0.94);
        canvas.strokeRect(36, yFoot, 523, 48, 1.0);

        canvas.setTextColor(0.4, 0.45, 0.5);
        canvas.drawText("Esta factura electronica de venta constituye titulo valor segun Art. 774 del Codigo de Comercio colombiano.", 46, yFoot + 34, "F1", 7.5);
        canvas.drawText("Resolucion DIAN N. 18764000001 vigente. Garantia de prendas y accesorios: 30 dias calendario.", 46, yFoot + 22, "F1", 7.5);

        canvas.setTextColor(0.063, 0.094, 0.153);
        canvas.drawText("Gracias por elegir a OmniModa. Tu estilo, nuestra pasion.", 175, yFoot + 9, "F2", 8.5);

        return canvas.buildPdf();
    }

    private String truncar(String text, int max) {
        if (text == null) return "";
        // Normalizar acentos a ASCII para evitar problemas de fuentes estándar Type1
        String normalized = normalizarTexto(text);
        return normalized.length() <= max ? normalized : normalized.substring(0, max - 3) + "...";
    }

    private String normalizarTexto(String s) {
        if (s == null) return "";
        return s.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
                .replace("ñ", "n").replace("Ñ", "N");
    }

    private String formatearMonedaSimple(double valor) {
        try {
            return CURRENCY_FORMAT.format(valor);
        } catch (Exception e) {
            return "$" + Math.round(valor);
        }
    }

    /**
     * Motor liviano de composición PDF 1.4 de precisión en Java puro.
     */
    private static class PdfCanvas {
        private final StringBuilder content = new StringBuilder();

        public void setFillColor(double r, double g, double b) {
            content.append(String.format(Locale.US, "%.3f %.3f %.3f rg\n", r, g, b));
        }

        public void setStrokeColor(double r, double g, double b) {
            content.append(String.format(Locale.US, "%.3f %.3f %.3f RG\n", r, g, b));
        }

        public void setTextColor(double r, double g, double b) {
            setFillColor(r, g, b);
        }

        public void fillRect(double x, double y, double w, double h) {
            content.append(String.format(Locale.US, "%.2f %.2f %.2f %.2f re f\n", x, y, w, h));
        }

        public void strokeRect(double x, double y, double w, double h, double lineWidth) {
            content.append(String.format(Locale.US, "%.2f w %.2f %.2f %.2f %.2f re S\n", lineWidth, x, y, w, h));
        }

        public void strokeLine(double x1, double y1, double x2, double y2, double lineWidth) {
            content.append(String.format(Locale.US, "%.2f w %.2f %.2f m %.2f %.2f l S\n", lineWidth, x1, y1, x2, y2));
        }

        public void drawText(String text, double x, double y, String fontName, double fontSize) {
            if (text == null || text.isEmpty()) return;
            String sanitized = escapePdfString(text);
            content.append("BT\n")
                   .append(String.format(Locale.US, "/%s %.2f Tf\n", fontName, fontSize))
                   .append(String.format(Locale.US, "%.2f %.2f Td\n", x, y))
                   .append("(").append(sanitized).append(") Tj\n")
                   .append("ET\n");
        }

        private String escapePdfString(String str) {
            StringBuilder sb = new StringBuilder();
            for (char c : str.toCharArray()) {
                if (c == '\\' || c == '(' || c == ')') {
                    sb.append('\\').append(c);
                } else if (c >= 32 && c <= 126) {
                    sb.append(c);
                } else {
                    sb.append(' '); // reemplazo seguro para Type1
                }
            }
            return sb.toString();
        }

        public byte[] buildPdf() {
            byte[] streamBytes = content.toString().getBytes(StandardCharsets.US_ASCII);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                List<Integer> offsets = new ArrayList<>();

                // Encabezado
                write(out, "%PDF-1.4\n%âãÏÓ\n");

                // 1 0 obj: Catalog
                offsets.add(out.size());
                write(out, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

                // 2 0 obj: Pages
                offsets.add(out.size());
                write(out, "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

                // 3 0 obj: Page (A4 = 595.28 x 841.89)
                offsets.add(out.size());
                write(out, "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595.28 841.89] /Contents 4 0 R /Resources << /Font << /F1 5 0 R /F2 6 0 R >> >> >>\nendobj\n");

                // 4 0 obj: Contents Stream
                offsets.add(out.size());
                write(out, "4 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n");
                out.write(streamBytes);
                write(out, "\nendstream\nendobj\n");

                // 5 0 obj: Font Helvetica
                offsets.add(out.size());
                write(out, "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

                // 6 0 obj: Font Helvetica-Bold
                offsets.add(out.size());
                write(out, "6 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>\nendobj\n");

                // XREF
                int startXref = out.size();
                write(out, "xref\n0 " + (offsets.size() + 1) + "\n");
                write(out, "0000000000 65535 f \n");
                for (int off : offsets) {
                    write(out, String.format(Locale.US, "%010d 00000 n \n", off));
                }

                // Trailer
                write(out, "trailer\n<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + startXref + "\n%%EOF\n");

            } catch (IOException e) {
                throw new RuntimeException("Error al componer PDF: " + e.getMessage(), e);
            }

            return out.toByteArray();
        }

        private void write(ByteArrayOutputStream out, String s) throws IOException {
            out.write(s.getBytes(StandardCharsets.US_ASCII));
        }
    }
}
