package com.apiv1.omniModa.Models.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServicio {

    private static final Logger log = LoggerFactory.getLogger(EmailServicio.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private SpringTemplateEngine templateMotor;

    @Value("${spring.mail.username:donchuchopropietario@gmail.com}")
    private String remitenteConfigurado;

    public boolean enviarFacturaCliente(
            String destino,
            String asunto,
            Map<String, Object> variables,
            byte[] pdfBytes,
            String nombrePdf) {

        if (destino == null || destino.isBlank()) {
            log.warn("No se pudo enviar el correo: la dirección de destino está vacía.");
            return false;
        }

        boolean mailAvailable;
        try {
            Class.forName("org.springframework.mail.javamail.JavaMailSender");
            Class.forName("jakarta.mail.internet.MimeMessage");
            mailAvailable = true;
        } catch (Throwable t) {
            mailAvailable = false;
        }

        if (!mailAvailable) {
            log.warn("JavaMailSender / Jakarta Mail no está cargado en el ClassLoader de este proceso. "
                    + "Por favor sincroniza las dependencias en VS Code (Ctrl+Shift+P -> Java: Clean Java Language Server Workspace) o ejecuta './gradlew bootRun'.");
            return false;
        }

        try {
            return MailSenderDelegate.enviar(
                    applicationContext,
                    templateMotor,
                    remitenteConfigurado,
                    destino,
                    asunto,
                    variables,
                    pdfBytes,
                    nombrePdf);
        } catch (Throwable t) {
            log.warn(
                    "Aviso al enviar correo a [{}]: {}. (Asegúrate de colocar tu contraseña de aplicación de 16 dígitos de Gmail en application.properties).",
                    destino, t.getMessage());
            return false;
        }
    }

    private static class MailSenderDelegate {

        static boolean enviar(
                ApplicationContext ctx,
                SpringTemplateEngine engine,
                String remitente,
                String destino,
                String asunto,
                Map<String, Object> variables,
                byte[] pdfBytes,
                String nombrePdf) throws Exception {

            org.springframework.mail.javamail.JavaMailSender mailSender;
            try {
                mailSender = ctx.getBean(org.springframework.mail.javamail.JavaMailSender.class);
            } catch (Exception e) {
                log.warn("Bean JavaMailSender no encontrado en el contexto de Spring: {}", e.getMessage());
                return false;
            }

            if (mailSender == null) {
                log.warn("JavaMailSender es nulo.");
                return false;
            }

            String html = procesarHtml(engine, variables);

            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(
                    message, true, StandardCharsets.UTF_8.name());

            helper.setTo(destino);
            helper.setSubject(asunto != null ? asunto : "Comprobante de Compra - OmniModa");
            helper.setText(html, true);

            String from = (remitente != null && !remitente.isBlank()) ? remitente : "contacto@omnimoda.com";
            helper.setFrom(from);

            if (pdfBytes != null && pdfBytes.length > 0) {
                String nombreAdjunto = (nombrePdf != null && !nombrePdf.isBlank()) ? nombrePdf : "Factura-OmniModa.pdf";
                helper.addAttachment(nombreAdjunto, new ByteArrayResource(pdfBytes), "application/pdf");
            }

            mailSender.send(message);
            log.info("Factura enviada exitosamente por correo a: {}", destino);
            return true;
        }

        private static String procesarHtml(SpringTemplateEngine engine, Map<String, Object> vars) {
            if (engine != null) {
                try {
                    Context context = new Context();
                    if (vars != null)
                        context.setVariables(vars);
                    return engine.process("correo_factura", context);
                } catch (Exception e) {
                    log.debug("Fallback de plantilla HTML a generador por tokens: {}", e.getMessage());
                }
            }
            return construirHtmlTokens(vars);
        }

        private static String construirHtmlTokens(Map<String, Object> vars) {
            String nombre = vars != null && vars.containsKey("nombre") ? String.valueOf(vars.get("nombre")) : "Cliente";
            String numFac = vars != null && vars.containsKey("numeroFactura")
                    ? String.valueOf(vars.get("numeroFactura"))
                    : "FAC-000001";
            String fecha = vars != null && vars.containsKey("fecha") ? String.valueOf(vars.get("fecha")) : "";
            String metodo = vars != null && vars.containsKey("metodoPago") ? String.valueOf(vars.get("metodoPago"))
                    : "Pago en Línea";
            String codAut = vars != null && vars.containsKey("codigoAutorizacion")
                    ? String.valueOf(vars.get("codigoAutorizacion"))
                    : "AUT-OK";
            String total = vars != null && vars.containsKey("total") ? String.valueOf(vars.get("total")) : "$0.00";
            String subtotal = vars != null && vars.containsKey("subtotal") ? String.valueOf(vars.get("subtotal"))
                    : "$0.00";
            String iva = vars != null && vars.containsKey("iva") ? String.valueOf(vars.get("iva")) : "$0.00";

            return """
                    <!DOCTYPE html>
                    <html>
                    <head>
                      <meta charset="UTF-8">
                      <style>
                        body { font-family: -apple-system, BlinkMacSystemFont, Arial, sans-serif; background: #f8fafc; color: #334155; padding: 20px; }
                        .card { max-width: 580px; margin: auto; background: white; border-radius: 8px; border: 1px solid #e2e8f0; padding: 28px; }
                        .header { border-bottom: 1px solid #e2e8f0; padding-bottom: 14px; margin-bottom: 20px; }
                        .total { font-size: 15px; font-weight: bold; color: #0f172a; margin-top: 14px; }
                        .note { margin-top: 20px; background: #f1f5f9; padding: 10px 14px; border-radius: 6px; font-size: 13px; color: #475569; }
                      </style>
                    </head>
                    <body>
                      <div class="card">
                        <div class="header">
                          <h2 style="margin:0; font-size: 20px; color: #0f172a;">OmniModa</h2>
                          <span style="font-size: 13px; color: #64748b;">Comprobante de compra</span>
                        </div>
                        <div>
                          <p>Hola, <strong>%s</strong>:</p>
                          <p>Gracias por tu compra. Tu orden ha sido procesada con éxito.</p>
                          <p><strong>N° Factura:</strong> %s<br>
                             <strong>Fecha:</strong> %s<br>
                             <strong>Método de Pago:</strong> %s<br>
                             <strong>Subtotal:</strong> %s<br>
                             <strong>IVA (19%%):</strong> %s</p>
                          <div class="total">Total Pagado: %s</div>
                          <div class="note">
                            📎 Tu factura formal en formato PDF se encuentra adjunta a este correo.
                          </div>
                        </div>
                      </div>
                    </body>
                    </html>
                    """
                    .formatted(nombre, numFac, fecha, metodo, subtotal, iva, total);
        }
    }
}
