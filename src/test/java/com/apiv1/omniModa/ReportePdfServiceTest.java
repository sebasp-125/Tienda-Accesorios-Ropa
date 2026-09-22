package com.apiv1.omniModa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apiv1.omniModa.Models.DTO.ReporteDatosDTO;
import com.apiv1.omniModa.Models.Entity.Clientes;
import com.apiv1.omniModa.Models.Entity.Estados;
import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Entity.Ventas;
import com.apiv1.omniModa.Models.Service.ReportePdfService;

@SpringBootTest
public class ReportePdfServiceTest {

    @Autowired
    private ReportePdfService reportePdfService;

    @Test
    void testGenerarReporteConsolidadoPdf() {
        ReporteDatosDTO datos = crearDatosMuestra("general");
        byte[] pdf = reportePdfService.generarReportePdf(datos);

        Assertions.assertNotNull(pdf, "El PDF no debe ser nulo");
        Assertions.assertTrue(pdf.length > 500, "El PDF consolidado debe tener contenido válido");
        String header = new String(pdf, 0, 4);
        Assertions.assertEquals("%PDF", header, "El archivo debe iniciar con la cabecera estándar de PDF");
    }

    @Test
    void testGenerarReporteVentasPdf() {
        ReporteDatosDTO datos = crearDatosMuestra("ventas");
        byte[] pdf = reportePdfService.generarReportePdf(datos);

        Assertions.assertNotNull(pdf);
        Assertions.assertTrue(pdf.length > 500);
        String header = new String(pdf, 0, 4);
        Assertions.assertEquals("%PDF", header);
    }

    @Test
    void testGenerarReporteInventarioPdf() {
        ReporteDatosDTO datos = crearDatosMuestra("inventario");
        byte[] pdf = reportePdfService.generarReportePdf(datos);

        Assertions.assertNotNull(pdf);
        Assertions.assertTrue(pdf.length > 500);
        String header = new String(pdf, 0, 4);
        Assertions.assertEquals("%PDF", header);
    }

    private ReporteDatosDTO crearDatosMuestra(String tipo) {
        ReporteDatosDTO d = new ReporteDatosDTO();
        d.setAdminNombre("Administrador Prueba");
        d.setTipoReporte(tipo);
        d.setTotalClientes(15);
        d.setTotalProductos(42);
        d.setTotalProveedores(8);
        d.setTotalUsuarios(5);
        d.setTotalVentas(23);
        d.setTotalIngresos(4500000.0);
        d.setVentasPagadas(18);
        d.setVentasPendientes(3);
        d.setVentasCanceladas(2);

        Productos p1 = new Productos();
        p1.setCodigo("PRD-001");
        p1.setNombre("Camisa Oxford");
        p1.setColor("Blanco");
        p1.setTalla("M");
        p1.setStockDisponible(3);
        p1.setPrecio(120000.0);

        Productos p2 = new Productos();
        p2.setCodigo("PRD-002");
        p2.setNombre("Pantalon Lino");
        p2.setColor("Beige");
        p2.setTalla("32");
        p2.setStockDisponible(2);
        p2.setPrecio(180000.0);

        d.setProductosBajoStock(List.of(p1, p2));
        d.setTodosProductos(List.of(p1, p2));

        Ventas v1 = new Ventas();
        v1.setIdVentas(1);
        v1.setFecha(LocalDate.now());
        v1.setTotal(300000.0);
        v1.setCliente(new Clientes("1111", "Carlos Gomez", "carlos@test.com", "3000000000", null));
        v1.setEstado(new Estados(1, "PAGADA"));
        v1.setDetalles(new ArrayList<>());

        d.setVentasRecientes(List.of(v1));
        return d;
    }
}
