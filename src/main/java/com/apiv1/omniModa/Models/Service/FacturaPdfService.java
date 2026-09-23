package com.apiv1.omniModa.Models.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.Entity.Detalle_venta;
import com.apiv1.omniModa.Models.Entity.Ventas;

@Service
public class FacturaPdfService {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    public byte[] generarFacturaPdf(Ventas venta, String metodoPago) {
        PdfCanvas canvas = new PdfCanvas();

        double pageWidth = 595.0;
        double pageHeight = 842.0;
        double marginX = 40.0;
        double contentWidth = pageWidth - (2 * marginX);

        int idVenta = venta.getIdVentas() != null ? venta.getIdVentas() : 1;
        String consecutivo = String.format("FE-%06d", idVenta);
        String fechaStr = venta.getFecha() != null ? venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String metodo = (metodoPago != null && !metodoPago.isBlank()) ? metodoPago : "Pago en Linea";

        double curY = pageHeight - 50;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("OMNIMODA S.A.S.", marginX, curY, "F2", 14);
        curY -= 16;

        canvas.setTextColor(0.1, 0.1, 0.1);
        canvas.drawText("Factura de Venta No. " + consecutivo, marginX, curY, "F2", 11);
        curY -= 14;

        canvas.setTextColor(0.35, 0.35, 0.35);
        canvas.drawText("NIT: 901.458.789-2   |   Cra. 7 # 123-45, Bogota D.C.   |   Tel: (+57) 601 320 0000", marginX,
                curY, "F1", 8.0);
        curY -= 12;
        canvas.drawText("Fecha: " + fechaStr + "   |   Medio de Pago: " + truncar(metodo, 30) + "   |   Estado: PAGADA",
                marginX, curY, "F1", 8.0);
        curY -= 10;

        canvas.setStrokeColor(0.7, 0.7, 0.7);
        canvas.strokeLine(marginX, curY, pageWidth - marginX, curY, 0.8);
        curY -= 20;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("Datos del Cliente", marginX, curY, "F2", 10.0);
        curY -= 15;

        String nomCliente = (venta.getCliente() != null && venta.getCliente().getNombreCompleto() != null)
                ? venta.getCliente().getNombreCompleto()
                : "Cliente General";
        String docCliente = (venta.getCliente() != null && venta.getCliente().getDocumento() != null)
                ? venta.getCliente().getDocumento()
                : "222222222222";
        String correoCliente = (venta.getCliente() != null && venta.getCliente().getCorreo() != null)
                ? venta.getCliente().getCorreo()
                : "No registrado";
        String telCliente = (venta.getCliente() != null && venta.getCliente().getTelefono() != null)
                ? venta.getCliente().getTelefono()
                : "Sin registrar";

        canvas.setTextColor(0.2, 0.2, 0.2);
        canvas.drawText(
                "- Nombre / Razon Social: " + truncar(nomCliente, 35) + "       - Identificacion: " + docCliente,
                marginX + 8, curY, "F1", 8.5);
        curY -= 13;
        canvas.drawText("- Correo: " + truncar(correoCliente, 35) + "       - Telefono: " + telCliente, marginX + 8,
                curY, "F1", 8.5);
        curY -= 22;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("Detalle de la Compra", marginX, curY, "F2", 10.0);
        curY -= 16;

        canvas.setTextColor(0.1, 0.1, 0.1);
        canvas.drawText("CANT", marginX + 8, curY, "F2", 7.5);
        canvas.drawText("CODIGO", marginX + 55, curY, "F2", 7.5);
        canvas.drawText("DESCRIPCION", marginX + 130, curY, "F2", 7.5);
        canvas.drawText("PRECIO UNIT.", marginX + 360, curY, "F2", 7.5);
        canvas.drawText("SUBTOTAL", marginX + 450, curY, "F2", 7.5);
        curY -= 5;
        canvas.setStrokeColor(0.6, 0.6, 0.6);
        canvas.strokeLine(marginX, curY, pageWidth - marginX, curY, 0.7);

        double totalCalculado = 0.0;
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            for (Detalle_venta det : venta.getDetalles()) {
                curY -= 15;
                String cod = (det.getProducto() != null && det.getProducto().getCodigo() != null)
                        ? det.getProducto().getCodigo()
                        : "ART";
                String desc = (det.getProducto() != null && det.getProducto().getNombre() != null)
                        ? det.getProducto().getNombre()
                        : "Prenda OmniModa";
                if (det.getProducto() != null && det.getProducto().getTalla() != null) {
                    desc += " (Talla: " + det.getProducto().getTalla() + ")";
                }

                double precioU = det.getPrecioUnitario() != null ? det.getPrecioUnitario() : 0.0;
                double sub = det.getSubtotal() != null ? det.getSubtotal() : (precioU * det.getCantidad());
                totalCalculado += sub;

                canvas.setTextColor(0.2, 0.2, 0.2);
                canvas.drawText(String.valueOf(det.getCantidad()), marginX + 8, curY, "F1", 7.5);
                canvas.drawText(cod, marginX + 55, curY, "F1", 7.5);
                canvas.drawText(truncar(desc, 36), marginX + 130, curY, "F1", 7.5);
                canvas.drawText(formatearMonedaSimple(precioU), marginX + 360, curY, "F1", 7.5);
                canvas.drawText(formatearMonedaSimple(sub), marginX + 450, curY, "F2", 7.5);
            }
        }
        curY -= 4;
        canvas.setStrokeColor(0.8, 0.8, 0.8);
        canvas.strokeLine(marginX, curY, pageWidth - marginX, curY, 0.5);
        curY -= 20;

        double totalFinal = (venta.getTotal() != null && venta.getTotal() > 0) ? venta.getTotal() : totalCalculado;
        double baseImponible = totalFinal / 1.19;
        double iva19 = totalFinal - baseImponible;

        canvas.setTextColor(0.2, 0.2, 0.2);
        canvas.drawText("Subtotal: " + formatearMonedaSimple(baseImponible) +
                "       IVA (19%): " + formatearMonedaSimple(iva19), marginX + 8, curY, "F1", 8.5);
        curY -= 14;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("TOTAL PAGADO: " + formatearMonedaSimple(totalFinal), marginX + 8, curY, "F2", 10.0);
        curY -= 14;

        String valorEnLetras = "SON: " + NumeroALetras.convertir(totalFinal);
        canvas.setTextColor(0.4, 0.4, 0.4);
        canvas.drawText(truncar(valorEnLetras, 60), marginX + 8, curY, "F1", 7.5);
        curY -= 30;

        double yFooter = 55;
        canvas.setStrokeColor(0.8, 0.8, 0.8);
        canvas.strokeLine(marginX, yFooter + 40, pageWidth - marginX, yFooter + 40, 0.5);

        canvas.setTextColor(0.4, 0.4, 0.4);
        canvas.drawText("Documento soporte de venta expedido por OmniModa S.A.S.", marginX, yFooter + 22, "F1", 7.5);
        canvas.drawText("Gracias por su compra en nuestra tienda.", marginX, yFooter + 10, "F1", 7.5);

        double xSign = pageWidth - marginX - 170;
        canvas.setStrokeColor(0.2, 0.2, 0.2);
        canvas.strokeLine(xSign, yFooter + 20, xSign + 160, yFooter + 20, 0.7);

        canvas.setTextColor(0.1, 0.1, 0.1);
        canvas.drawText("Firma Autorizada", xSign + 35, yFooter + 10, "F2", 7.5);
        canvas.setTextColor(0.4, 0.4, 0.4);
        canvas.drawText("OmniModa S.A.S.", xSign + 35, yFooter, "F1", 7.0);

        return canvas.buildPdf();
    }

    public static String generarCufe(String numFac, String fecha, double total, String nitEmisor, String docCliente) {
        try {
            String raw = numFac + ";" + fecha + ";" + String.format(Locale.US, "%.2f", total) + ";" + nitEmisor + ";"
                    + docCliente + ";OmniModaSeguridadFiscal2026";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "9a7f3c2b8e1d4f6a0c5b3e2a1d9f8e7c6b5a4d3c2b1a0f9e8d7c6b5a4f3e2d1c";
        }
    }

    private String truncar(String text, int max) {
        if (text == null)
            return "";
        String normalized = normalizarTexto(text);
        return normalized.length() <= max ? normalized : normalized.substring(0, max - 3) + "...";
    }

    private String normalizarTexto(String s) {
        if (s == null)
            return "";
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
            if (text == null || text.isEmpty())
                return;
            String sanitized = escapePdfString(text);
            content.append("BT\n")
                    .append(String.format(Locale.US, "/%s %.2f Tf\n", fontName, fontSize))
                    .append(String.format(Locale.US, "%.2f %.2f Td\n", x, y))
                    .append("(").append(sanitized).append(") Tj\n")
                    .append("ET\n");
        }

        public void drawQrCode(double x, double y, double size) {

            setFillColor(1.0, 1.0, 1.0);
            fillRect(x, y, size, size);
            setStrokeColor(0.85, 0.88, 0.92);
            strokeRect(x, y, size, size, 0.5);

            setFillColor(0.063, 0.094, 0.153);

            int modules = 25;
            double modSize = size / modules;

            drawFinderPattern(x, y + size - 7 * modSize, modSize);

            drawFinderPattern(x + size - 7 * modSize, y + size - 7 * modSize, modSize);

            drawFinderPattern(x, y, modSize);

            for (int r = 0; r < modules; r++) {
                for (int c = 0; c < modules; c++) {

                    boolean inTopLeft = (r < 8 && c < 8);
                    boolean inTopRight = (r < 8 && c >= modules - 8);
                    boolean inBottomLeft = (r >= modules - 8 && c < 8);

                    if (!inTopLeft && !inTopRight && !inBottomLeft) {

                        if ((r * 7 + c * 13 + (r % 3) * 5) % 2 == 0 || (r == 6 || c == 6)) {
                            fillRect(x + c * modSize, y + size - (r + 1) * modSize, modSize, modSize);
                        }
                    }
                }
            }
        }

        private void drawFinderPattern(double fx, double fy, double mod) {

            fillRect(fx, fy, 7 * mod, 7 * mod);

            setFillColor(1.0, 1.0, 1.0);
            fillRect(fx + mod, fy + mod, 5 * mod, 5 * mod);

            setFillColor(0.063, 0.094, 0.153);
            fillRect(fx + 2 * mod, fy + 2 * mod, 3 * mod, 3 * mod);
        }

        public void drawBarcode(double x, double y, double width, double height, String text) {
            setFillColor(0.063, 0.094, 0.153);

            double[] barWidths = { 1.0, 2.0, 0.8, 1.5, 2.2, 0.8, 1.8, 1.2, 2.0, 0.8, 2.4, 1.0, 1.5, 2.0, 0.8, 1.6, 2.0,
                    1.0 };
            double curX = x;
            int idx = 0;
            while (curX < (x + width - 10)) {
                double bw = barWidths[idx % barWidths.length];
                fillRect(curX, y + 9, bw, height - 9);
                curX += bw + (idx % 3 == 0 ? 1.8 : 1.0);
                idx++;
            }

            setTextColor(0.2, 0.25, 0.3);
            drawText(text, x, y, "F1", 6.5);
        }

        private String escapePdfString(String str) {
            StringBuilder sb = new StringBuilder();
            for (char c : str.toCharArray()) {
                if (c == '\\' || c == '(' || c == ')') {
                    sb.append('\\').append(c);
                } else if (c >= 32 && c <= 126) {
                    sb.append(c);
                } else {
                    sb.append(' ');
                }
            }
            return sb.toString();
        }

        public byte[] buildPdf() {
            byte[] streamBytes = content.toString().getBytes(StandardCharsets.US_ASCII);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                List<Integer> offsets = new ArrayList<>();

                write(out, "%PDF-1.4\n%âãÏÓ\n");

                offsets.add(out.size());
                write(out, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

                offsets.add(out.size());
                write(out, "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

                offsets.add(out.size());
                write(out,
                        "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595.28 841.89] /Contents 4 0 R /Resources << /Font << /F1 5 0 R /F2 6 0 R >> >> >>\nendobj\n");

                offsets.add(out.size());
                write(out, "4 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n");
                out.write(streamBytes);
                write(out, "\nendstream\nendobj\n");

                offsets.add(out.size());
                write(out, "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

                offsets.add(out.size());
                write(out, "6 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>\nendobj\n");

                int startXref = out.size();
                write(out, "xref\n0 " + (offsets.size() + 1) + "\n");
                write(out, "0000000000 65535 f \n");
                for (int off : offsets) {
                    write(out, String.format(Locale.US, "%010d 00000 n \n", off));
                }

                write(out, "trailer\n<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + startXref
                        + "\n%%EOF\n");

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
