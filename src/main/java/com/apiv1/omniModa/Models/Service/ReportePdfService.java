package com.apiv1.omniModa.Models.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.DTO.ReporteDatosDTO;
import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Entity.Ventas;

/**
 * Servicio generador de Reportes Administrativos en PDF en escala de grises / blanco y negro,
 * implementado en Java puro estándar para garantizar máxima compatibilidad y sobriedad ejecutiva.
 */
@Service
public class ReportePdfService {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    public byte[] generarReportePdf(ReporteDatosDTO datos) {
        PdfCanvas canvas = new PdfCanvas();

        double pageWidth = 595.0;
        double pageHeight = 842.0;
        double marginX = 40.0;
        double contentWidth = pageWidth - (2 * marginX);

        String tipo = datos.getTipoReporte() != null ? datos.getTipoReporte().toLowerCase() : "general";
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String adminNombre = (datos.getAdminNombre() != null && !datos.getAdminNombre().isBlank())
                ? datos.getAdminNombre() : "Administrador del Sistema";

        // 1. ENCABEZADO SIMPLE (Estilo documento natural / humano)
        double curY = pageHeight - 50;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("OMNIMODA S.A.S.", marginX, curY, "F2", 14);
        curY -= 16;

        // Título del documento según el tipo de reporte
        String tituloDocumento;
        if ("ventas".equals(tipo)) {
            tituloDocumento = "Reporte de Ventas, Ingresos y Facturacion";
        } else if ("inventario".equals(tipo)) {
            tituloDocumento = "Reporte de Control de Inventario y Existencias";
        } else {
            tituloDocumento = "Reporte General del Sistema";
        }

        canvas.setTextColor(0.1, 0.1, 0.1);
        canvas.drawText(tituloDocumento, marginX, curY, "F2", 11);
        curY -= 14;

        canvas.setTextColor(0.35, 0.35, 0.35);
        canvas.drawText("Fecha de emision: " + fechaHoraActual + "   |   Generado por: " + adminNombre + "   |   NIT: 901.458.789-2", marginX, curY, "F1", 8.0);
        curY -= 10;

        // Línea divisoria simple
        canvas.setStrokeColor(0.7, 0.7, 0.7);
        canvas.strokeLine(marginX, curY, pageWidth - marginX, curY, 0.8);
        curY -= 20;

        // SECCIONES / CONCEPTOS ESTRUCTURADOS (Sin cajas pesadas, estilo informe humano)
        if ("ventas".equals(tipo)) {
            curY = renderizarConceptoVentasDetalladas(canvas, datos, marginX, contentWidth, curY, pageWidth);
        } else if ("inventario".equals(tipo)) {
            curY = renderizarConceptoInventarioDetallado(canvas, datos, marginX, contentWidth, curY, pageWidth);
        } else {
            curY = renderizarReporteConsolidado(canvas, datos, marginX, contentWidth, curY, pageWidth);
        }

        // SECCIÓN FINAL: FIRMA SIMPLE
        renderizarFirmaAuditoria(canvas, adminNombre, fechaHoraActual, marginX, contentWidth, pageWidth);

        return canvas.buildPdf();
    }

    /**
     * Renderiza el reporte consolidado con formato limpio y humano.
     */
    private double renderizarReporteConsolidado(PdfCanvas canvas, ReporteDatosDTO d, double x, double w, double startY, double pageWidth) {
        double curY = startY;

        // 1. RESUMEN GENERAL DEL SISTEMA
        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("1. Resumen General del Sistema", x, curY, "F2", 10.0);
        curY -= 16;

        canvas.setTextColor(0.2, 0.2, 0.2);
        canvas.drawText("- Clientes registrados: " + d.getTotalClientes() +
                "       - Productos en catalogo: " + d.getTotalProductos() +
                "       - Proveedores activos: " + d.getTotalProveedores(), x + 8, curY, "F1", 8.5);
        curY -= 13;
        canvas.drawText("- Cuentas de usuario: " + d.getTotalUsuarios() +
                "       - Total de ventas registradas: " + d.getTotalVentas(), x + 8, curY, "F1", 8.5);
        curY -= 24;

        // 2. BALANCE FINANCIERO Y VENTAS
        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("2. Balance Financiero y Ventas", x, curY, "F2", 10.0);
        curY -= 16;

        double baseImponible = d.getTotalIngresos() / 1.19;
        double iva19 = d.getTotalIngresos() - baseImponible;
        double ticketPromedio = d.getVentasPagadas() > 0 ? (d.getTotalIngresos() / d.getVentasPagadas()) : 0.0;

        canvas.setTextColor(0.2, 0.2, 0.2);
        canvas.drawText("- Total ingresos recaudados (ventas pagadas): " + formatearMoneda(d.getTotalIngresos()), x + 8, curY, "F1", 8.5);
        curY -= 13;
        canvas.drawText("- Base gravable (19%): " + formatearMoneda(baseImponible) +
                "       - IVA recaudado (19%): " + formatearMoneda(iva19), x + 8, curY, "F1", 8.5);
        curY -= 13;
        canvas.drawText("- Ticket promedio por venta: " + formatearMoneda(ticketPromedio), x + 8, curY, "F1", 8.5);
        curY -= 13;
        canvas.drawText("- Estado de pedidos: " + d.getVentasPagadas() + " pagadas,  " +
                d.getVentasPendientes() + " pendientes de pago,  " +
                d.getVentasCanceladas() + " canceladas.", x + 8, curY, "F1", 8.5);
        curY -= 24;

        // 3. CONTROL DE INVENTARIO Y ALERTA DE STOCK
        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("3. Control de Inventario y Alertas de Stock", x, curY, "F2", 10.0);
        curY -= 16;

        List<Productos> bajoStock = d.getProductosBajoStock() != null ? d.getProductosBajoStock() : List.of();
        if (bajoStock.isEmpty()) {
            canvas.setTextColor(0.3, 0.3, 0.3);
            canvas.drawText("- Estado del inventario: Actualmente no se registran articulos con stock bajo (<= 5 unidades).", x + 8, curY, "F1", 8.5);
            curY -= 24;
        } else {
            // Encabezado de tabla simple sin fondo negro
            canvas.setTextColor(0.1, 0.1, 0.1);
            canvas.drawText("CODIGO", x + 8, curY, "F2", 7.5);
            canvas.drawText("DESCRIPCION DE PRENDA", x + 85, curY, "F2", 7.5);
            canvas.drawText("TALLA", x + 310, curY, "F2", 7.5);
            canvas.drawText("STOCK", x + 375, curY, "F2", 7.5);
            canvas.drawText("PRECIO UNIT.", x + 440, curY, "F2", 7.5);
            curY -= 5;
            canvas.setStrokeColor(0.6, 0.6, 0.6);
            canvas.strokeLine(x, curY, pageWidth - x, curY, 0.7);

            int maxItems = Math.min(bajoStock.size(), 4);
            for (int i = 0; i < maxItems; i++) {
                Productos p = bajoStock.get(i);
                curY -= 14;
                canvas.setTextColor(0.2, 0.2, 0.2);
                canvas.drawText(p.getCodigo() != null ? p.getCodigo() : "S/C", x + 8, curY, "F1", 7.5);
                canvas.drawText(truncar(p.getNombre(), 36), x + 85, curY, "F1", 7.5);
                canvas.drawText(p.getTalla() != null ? p.getTalla() : "U", x + 310, curY, "F1", 7.5);
                canvas.drawText((p.getStockDisponible() != null ? p.getStockDisponible() : 0) + " unds", x + 375, curY, "F2", 7.5);
                canvas.drawText(formatearMoneda(p.getPrecio() != null ? p.getPrecio() : 0.0), x + 440, curY, "F1", 7.5);
            }
            curY -= 4;
            canvas.setStrokeColor(0.8, 0.8, 0.8);
            canvas.strokeLine(x, curY, pageWidth - x, curY, 0.5);
            curY -= 20;
        }

        // 4. HISTORIAL DE VENTAS RECIENTES
        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("4. Registro de Transacciones Recientes", x, curY, "F2", 10.0);
        curY -= 16;

        List<Ventas> recientes = d.getVentasRecientes() != null ? d.getVentasRecientes() : List.of();
        if (recientes.isEmpty()) {
            canvas.setTextColor(0.3, 0.3, 0.3);
            canvas.drawText("- No se registran transacciones recientes.", x + 8, curY, "F1", 8.5);
            curY -= 24;
        } else {
            canvas.setTextColor(0.1, 0.1, 0.1);
            canvas.drawText("ID VENTA", x + 8, curY, "F2", 7.5);
            canvas.drawText("FECHA", x + 70, curY, "F2", 7.5);
            canvas.drawText("CLIENTE ADQUIRENTE", x + 150, curY, "F2", 7.5);
            canvas.drawText("ESTADO", x + 360, curY, "F2", 7.5);
            canvas.drawText("TOTAL", x + 445, curY, "F2", 7.5);
            curY -= 5;
            canvas.setStrokeColor(0.6, 0.6, 0.6);
            canvas.strokeLine(x, curY, pageWidth - x, curY, 0.7);

            int maxVentas = Math.min(recientes.size(), 5);
            for (int i = 0; i < maxVentas; i++) {
                Ventas v = recientes.get(i);
                curY -= 14;

                String clienteNom = (v.getCliente() != null && v.getCliente().getNombreCompleto() != null)
                        ? v.getCliente().getNombreCompleto() : "Cliente General";
                String estadoStr = (v.getEstado() != null && v.getEstado().getTipo() != null)
                        ? v.getEstado().getTipo() : "PAGADA";
                String fechaStr = v.getFecha() != null ? v.getFecha().toString() : "";

                canvas.setTextColor(0.2, 0.2, 0.2);
                canvas.drawText("#" + String.format("%06d", v.getIdVentas() != null ? v.getIdVentas() : 0), x + 8, curY, "F1", 7.5);
                canvas.drawText(fechaStr, x + 70, curY, "F1", 7.5);
                canvas.drawText(truncar(clienteNom, 32), x + 150, curY, "F1", 7.5);
                canvas.drawText(estadoStr, x + 360, curY, "F1", 7.5);
                canvas.drawText(formatearMoneda(v.getTotal() != null ? v.getTotal() : 0.0), x + 445, curY, "F2", 7.5);
            }
            curY -= 4;
            canvas.setStrokeColor(0.8, 0.8, 0.8);
            canvas.strokeLine(x, curY, pageWidth - x, curY, 0.5);
            curY -= 20;
        }

        return curY;
    }

    /**
     * Renderiza el reporte específico de ventas.
     */
    private double renderizarConceptoVentasDetalladas(PdfCanvas canvas, ReporteDatosDTO d, double x, double w, double startY, double pageWidth) {
        double curY = startY;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("1. Balance General de Ventas e Ingresos", x, curY, "F2", 10.0);
        curY -= 16;

        double baseImponible = d.getTotalIngresos() / 1.19;
        double iva19 = d.getTotalIngresos() - baseImponible;

        canvas.setTextColor(0.2, 0.2, 0.2);
        canvas.drawText("- Total recaudado: " + formatearMoneda(d.getTotalIngresos()) +
                "       - Ventas pagadas: " + d.getVentasPagadas() +
                "       - Ventas pendientes: " + d.getVentasPendientes(), x + 8, curY, "F1", 8.5);
        curY -= 13;
        canvas.drawText("- Base gravable (19%): " + formatearMoneda(baseImponible) +
                "       - Impuesto IVA (19%): " + formatearMoneda(iva19) +
                "       - Ventas canceladas: " + d.getVentasCanceladas(), x + 8, curY, "F1", 8.5);
        curY -= 24;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("2. Detalle de Ventas Registradas", x, curY, "F2", 10.0);
        curY -= 16;

        List<Ventas> recientes = d.getVentasRecientes() != null ? d.getVentasRecientes() : List.of();
        canvas.setTextColor(0.1, 0.1, 0.1);
        canvas.drawText("CONSECUTIVO", x + 8, curY, "F2", 7.5);
        canvas.drawText("FECHA", x + 80, curY, "F2", 7.5);
        canvas.drawText("CLIENTE ADQUIRENTE", x + 160, curY, "F2", 7.5);
        canvas.drawText("ESTADO", x + 360, curY, "F2", 7.5);
        canvas.drawText("VALOR TOTAL", x + 445, curY, "F2", 7.5);
        curY -= 5;
        canvas.setStrokeColor(0.6, 0.6, 0.6);
        canvas.strokeLine(x, curY, pageWidth - x, curY, 0.7);

        int maxVentas = Math.min(recientes.size(), 12);
        for (int i = 0; i < maxVentas; i++) {
            Ventas v = recientes.get(i);
            curY -= 15;

            String clienteNom = (v.getCliente() != null && v.getCliente().getNombreCompleto() != null)
                    ? v.getCliente().getNombreCompleto() : "Cliente General";
            String estadoStr = (v.getEstado() != null && v.getEstado().getTipo() != null)
                    ? v.getEstado().getTipo() : "PAGADA";
            String fechaStr = v.getFecha() != null ? v.getFecha().toString() : "";

            canvas.setTextColor(0.2, 0.2, 0.2);
            canvas.drawText("FE-" + String.format("%06d", v.getIdVentas() != null ? v.getIdVentas() : 0), x + 8, curY, "F1", 7.5);
            canvas.drawText(fechaStr, x + 80, curY, "F1", 7.5);
            canvas.drawText(truncar(clienteNom, 34), x + 160, curY, "F1", 7.5);
            canvas.drawText(estadoStr, x + 360, curY, "F1", 7.5);
            canvas.drawText(formatearMoneda(v.getTotal() != null ? v.getTotal() : 0.0), x + 445, curY, "F2", 7.5);
        }
        curY -= 4;
        canvas.setStrokeColor(0.8, 0.8, 0.8);
        canvas.strokeLine(x, curY, pageWidth - x, curY, 0.5);

        curY -= 20;
        return curY;
    }

    /**
     * Renderiza el reporte específico de inventario.
     */
    private double renderizarConceptoInventarioDetallado(PdfCanvas canvas, ReporteDatosDTO d, double x, double w, double startY, double pageWidth) {
        double curY = startY;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("1. Balance General de Existencias y Almacen", x, curY, "F2", 10.0);
        curY -= 16;

        List<Productos> todos = d.getTodosProductos() != null ? d.getTodosProductos() : List.of();
        long totalStock = 0;
        double valorInventario = 0.0;
        for (Productos p : todos) {
            int stock = p.getStockDisponible() != null ? p.getStockDisponible() : 0;
            double precio = p.getPrecio() != null ? p.getPrecio() : 0.0;
            totalStock += stock;
            valorInventario += (stock * precio);
        }

        canvas.setTextColor(0.2, 0.2, 0.2);
        canvas.drawText("- Total referencias: " + d.getTotalProductos() +
                "       - Unidades fisicas en bodega: " + totalStock, x + 8, curY, "F1", 8.5);
        curY -= 13;
        canvas.drawText("- Articulos con stock critico (<= 5): " + (d.getProductosBajoStock() != null ? d.getProductosBajoStock().size() : 0) +
                "       - Valor estimado inventario: " + formatearMoneda(valorInventario), x + 8, curY, "F1", 8.5);
        curY -= 24;

        canvas.setTextColor(0.0, 0.0, 0.0);
        canvas.drawText("2. Detalle de Articulos que Requieren Reposicion", x, curY, "F2", 10.0);
        curY -= 16;

        List<Productos> bajo = d.getProductosBajoStock() != null ? d.getProductosBajoStock() : List.of();
        if (bajo.isEmpty()) {
            canvas.setTextColor(0.3, 0.3, 0.3);
            canvas.drawText("- Todas las referencias en catalogo cuentan con existencias superiores a 5 unidades.", x + 8, curY, "F1", 8.5);
            curY -= 24;
        } else {
            canvas.setTextColor(0.1, 0.1, 0.1);
            canvas.drawText("CODIGO", x + 8, curY, "F2", 7.5);
            canvas.drawText("PRENDA / ARTICULO", x + 85, curY, "F2", 7.5);
            canvas.drawText("COLOR", x + 260, curY, "F2", 7.5);
            canvas.drawText("TALLA", x + 340, curY, "F2", 7.5);
            canvas.drawText("STOCK", x + 390, curY, "F2", 7.5);
            canvas.drawText("PRECIO UNIT.", x + 450, curY, "F2", 7.5);
            curY -= 5;
            canvas.setStrokeColor(0.6, 0.6, 0.6);
            canvas.strokeLine(x, curY, pageWidth - x, curY, 0.7);

            int maxItems = Math.min(bajo.size(), 10);
            for (int i = 0; i < maxItems; i++) {
                Productos p = bajo.get(i);
                curY -= 15;
                canvas.setTextColor(0.2, 0.2, 0.2);
                canvas.drawText(p.getCodigo() != null ? p.getCodigo() : "S/C", x + 8, curY, "F1", 7.5);
                canvas.drawText(truncar(p.getNombre(), 28), x + 85, curY, "F1", 7.5);
                canvas.drawText(p.getColor() != null ? truncar(p.getColor(), 12) : "N/A", x + 260, curY, "F1", 7.5);
                canvas.drawText(p.getTalla() != null ? p.getTalla() : "U", x + 340, curY, "F1", 7.5);
                canvas.drawText((p.getStockDisponible() != null ? p.getStockDisponible() : 0) + " unds", x + 390, curY, "F2", 7.5);
                canvas.drawText(formatearMoneda(p.getPrecio() != null ? p.getPrecio() : 0.0), x + 450, curY, "F1", 7.5);
            }
            curY -= 4;
            canvas.setStrokeColor(0.8, 0.8, 0.8);
            canvas.strokeLine(x, curY, pageWidth - x, curY, 0.5);
            curY -= 20;
        }

        return curY;
    }

    private void renderizarFirmaAuditoria(PdfCanvas canvas, String adminNombre, String fechaHora, double x, double w, double pageWidth) {
        double yFirma = 55;

        canvas.setStrokeColor(0.8, 0.8, 0.8);
        canvas.strokeLine(x, yFirma + 45, x + w, yFirma + 45, 0.5);

        // Nota a la izquierda
        canvas.setTextColor(0.4, 0.4, 0.4);
        canvas.drawText("Reporte emitido para control administrativo interno.", x, yFirma + 25, "F1", 7.5);
        canvas.drawText("OmniModa S.A.S. - Sistema de Gestion Comercial", x, yFirma + 14, "F1", 7.0);

        // Línea de firma a la derecha
        double xSign = x + w - 170;
        canvas.setStrokeColor(0.2, 0.2, 0.2);
        canvas.strokeLine(xSign, yFirma + 22, xSign + 160, yFirma + 22, 0.7);

        canvas.setTextColor(0.1, 0.1, 0.1);
        canvas.drawText("Firma del Administrador", xSign + 25, yFirma + 12, "F2", 7.5);
        canvas.setTextColor(0.4, 0.4, 0.4);
        canvas.drawText(truncar(adminNombre, 25), xSign + 25, yFirma + 2, "F1", 7.0);
    }

    private String truncar(String text, int max) {
        if (text == null) return "";
        String normalized = normalizarTexto(text);
        return normalized.length() <= max ? normalized : normalized.substring(0, max - 3) + "...";
    }

    private String normalizarTexto(String s) {
        if (s == null) return "";
        return s.replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
                .replace("ñ", "n").replace("Ñ", "N");
    }

    private String formatearMoneda(double valor) {
        try {
            return CURRENCY_FORMAT.format(valor);
        } catch (Exception e) {
            return "$" + Math.round(valor);
        }
    }

    /**
     * Motor ligero de dibujo vectorial PDF 1.4 en Java puro (sin dependencias).
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
                write(out, "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595.28 841.89] /Contents 4 0 R /Resources << /Font << /F1 5 0 R /F2 6 0 R >> >> >>\nendobj\n");

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

                write(out, "trailer\n<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + startXref + "\n%%EOF\n");
            } catch (IOException e) {
                throw new RuntimeException("Error al componer PDF de reporte: " + e.getMessage(), e);
            }
            return out.toByteArray();
        }

        private void write(ByteArrayOutputStream out, String s) throws IOException {
            out.write(s.getBytes(StandardCharsets.US_ASCII));
        }
    }
}
