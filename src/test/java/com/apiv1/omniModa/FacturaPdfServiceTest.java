package com.apiv1.omniModa;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apiv1.omniModa.Models.Entity.Clientes;
import com.apiv1.omniModa.Models.Entity.Detalle_venta;
import com.apiv1.omniModa.Models.Entity.Estados;
import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Entity.TipoCliente;
import com.apiv1.omniModa.Models.Entity.Ventas;
import com.apiv1.omniModa.Models.Service.FacturaPdfService;

@SpringBootTest
public class FacturaPdfServiceTest {

    @Autowired
    private FacturaPdfService facturaPdfService;

    @Test
    void testGenerarFacturaPdf() {
        TipoCliente tipo = new TipoCliente("Natural");
        Clientes cliente = new Clientes("1020304050", "Sebastián Cliente", "sebas@ejemplo.com", "3001234567", tipo);
        Estados estado = new Estados(1, "PAGADA");

        Ventas venta = new Ventas();
        venta.setIdVentas(101);
        venta.setFecha(LocalDate.now());
        venta.setCliente(cliente);
        venta.setEstado(estado);
        venta.setTotal(150000.0);
        venta.setDetalles(new ArrayList<>());

        Productos prod = new Productos();
        prod.setCodigo("ROPA-001");
        prod.setNombre("Camisa de Lino Elegante");
        prod.setTalla("M");
        prod.setColor("Azul Marino");
        prod.setPrecio(150000.0);

        Detalle_venta det = new Detalle_venta(null, 1, 150000.0, 150000.0, venta, prod);
        venta.addDetalle(det);

        byte[] pdfBytes = facturaPdfService.generarFacturaPdf(venta, "PayPal");

        Assertions.assertNotNull(pdfBytes);
        Assertions.assertTrue(pdfBytes.length > 500, "El PDF generado debe tener un tamaño válido");

        // Los archivos PDF siempre inician con el encabezado %PDF
        String header = new String(pdfBytes, 0, 4);
        Assertions.assertEquals("%PDF", header, "El archivo debe iniciar con la cabecera estándar de PDF");
    }
}
