package com.apiv1.omniModa;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.apiv1.omniModa.Models.DTO.ItemVentaDTO;
import com.apiv1.omniModa.Models.Entity.Categorias;
import com.apiv1.omniModa.Models.Entity.Clientes;
import com.apiv1.omniModa.Models.Entity.Estados;
import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Entity.TipoCliente;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Entity.Ventas;
import com.apiv1.omniModa.Models.Repository.CategoriaRepository;
import com.apiv1.omniModa.Models.Repository.ClienteRepository;
import com.apiv1.omniModa.Models.Repository.EstadoRepository;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;
import com.apiv1.omniModa.Models.Repository.TipoClienteRepository;
import com.apiv1.omniModa.Models.Repository.UsuarioRepository;
import com.apiv1.omniModa.Models.Repository.VentaRepository;
import com.apiv1.omniModa.Models.Service.VentaService;

@SpringBootTest
@Transactional
class VentaServiceTests {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TipoClienteRepository tipoClienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Clientes clientePrueba;
    private Productos productoPrueba;
    private Estados estadoPagada;
    private Estados estadoCancelada;
    private Estados estadoPendiente;

    @BeforeEach
    void setUp() {
        TipoCliente tipoCliente = tipoClienteRepository.findAll().stream().findFirst()
                .orElseGet(() -> tipoClienteRepository.save(new TipoCliente("Natural")));

        clientePrueba = clienteRepository.findById("TEST-DOC-01")
                .orElseGet(() -> clienteRepository.save(new Clientes("TEST-DOC-01", "Cliente Test", "test@cliente.com", "3000000000", tipoCliente)));

        Categorias cat = categoriaRepository.findAll().stream().findFirst()
                .orElseGet(() -> categoriaRepository.save(new Categorias(null, "Ropa")));

        productoPrueba = productoRepository.findById("TEST-PROD-01")
                .orElseGet(() -> productoRepository.save(new Productos("TEST-PROD-01", "Camisa Oxford Test", "L", "Azul", 50000.0, 20, cat)));

        estadoPagada = estadoRepository.findByTipoIgnoreCase("PAGADA").orElseThrow();
        estadoCancelada = estadoRepository.findByTipoIgnoreCase("CANCELADA").orElseThrow();
        estadoPendiente = estadoRepository.findByTipoIgnoreCase("PENDIENTE").orElseThrow();
    }

    @Test
    void testRegistrarVentaYDescontarStock() {
        int stockInicial = productoPrueba.getStockDisponible();

        ItemVentaDTO item = new ItemVentaDTO(productoPrueba.getCodigo(), null, 3, null, null);
        Ventas venta = ventaService.registrarVenta(clientePrueba.getDocumento(), LocalDate.now(), estadoPagada.getIdEstado(), List.of(item));

        Assertions.assertNotNull(venta.getIdVentas());
        Assertions.assertEquals(150000.0, venta.getTotal());
        Assertions.assertEquals("PAGADA", venta.getEstado().getTipo());

        Productos prodActualizado = productoRepository.findById(productoPrueba.getCodigo()).orElseThrow();
        Assertions.assertEquals(stockInicial - 3, prodActualizado.getStockDisponible());
    }

    @Test
    void testStockInsuficienteLanzaExcepcion() {
        ItemVentaDTO item = new ItemVentaDTO(productoPrueba.getCodigo(), null, 99999, null, null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ventaService.registrarVenta(clientePrueba.getDocumento(), LocalDate.now(), estadoPagada.getIdEstado(), List.of(item));
        });
    }

    @Test
    void testCambioEstadoACanceladaRestauraStock() {
        int stockInicial = productoPrueba.getStockDisponible();

        ItemVentaDTO item = new ItemVentaDTO(productoPrueba.getCodigo(), null, 5, null, null);
        Ventas venta = ventaService.registrarVenta(clientePrueba.getDocumento(), LocalDate.now(), estadoPagada.getIdEstado(), List.of(item));

        Productos prodPostVenta = productoRepository.findById(productoPrueba.getCodigo()).orElseThrow();
        Assertions.assertEquals(stockInicial - 5, prodPostVenta.getStockDisponible());

        // Cancelar venta
        ventaService.cambiarEstado(venta.getIdVentas(), estadoCancelada.getIdEstado());

        Productos prodPostCancelacion = productoRepository.findById(productoPrueba.getCodigo()).orElseThrow();
        Assertions.assertEquals(stockInicial, prodPostCancelacion.getStockDisponible());
    }

    @Test
    void testProcesarCompraCliente() {
        Usuarios usuarioCliente = new Usuarios();
        usuarioCliente.setNombreUsuario("Comprador Online");
        usuarioCliente.setCorreo("online@tienda.com");
        usuarioCliente.setPassword("pass123");
        usuarioCliente.setEstado("ACTIVO");
        usuarioCliente = usuarioRepository.save(usuarioCliente);

        int stockInicial = productoPrueba.getStockDisponible();

        ItemVentaDTO item = new ItemVentaDTO(productoPrueba.getCodigo(), null, 2, null, null);
        Ventas venta = ventaService.procesarCompraCliente(usuarioCliente, List.of(item));

        Assertions.assertNotNull(venta.getIdVentas());
        Assertions.assertEquals(100000.0, venta.getTotal());
        Assertions.assertEquals("PAGADA", venta.getEstado().getTipo());
        Assertions.assertEquals("online@tienda.com", venta.getCliente().getCorreo());

        Productos prodActualizado = productoRepository.findById(productoPrueba.getCodigo()).orElseThrow();
        Assertions.assertEquals(stockInicial - 2, prodActualizado.getStockDisponible());
    }

    @Test
    void testFiltrarVentas() {
        ItemVentaDTO item = new ItemVentaDTO(productoPrueba.getCodigo(), null, 1, null, null);
        Ventas venta = ventaService.registrarVenta(clientePrueba.getDocumento(), LocalDate.now(), estadoPendiente.getIdEstado(), List.of(item));

        List<Ventas> filtroCliente = ventaService.filtrarVentas("Cliente Test", null, null, null);
        Assertions.assertFalse(filtroCliente.isEmpty());

        List<Ventas> filtroProducto = ventaService.filtrarVentas(null, "Camisa Oxford", null, null);
        Assertions.assertFalse(filtroProducto.isEmpty());

        List<Ventas> filtroEstado = ventaService.filtrarVentas(null, null, null, estadoPendiente.getIdEstado());
        Assertions.assertFalse(filtroEstado.isEmpty());
    }
}
