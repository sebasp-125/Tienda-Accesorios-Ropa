package com.apiv1.omniModa;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.apiv1.omniModa.Controllers.Clientes.ClientePortalController;
import com.apiv1.omniModa.Controllers.Ventas.VentasController;
import com.apiv1.omniModa.Models.DTO.CompraRequestDTO;
import com.apiv1.omniModa.Models.DTO.ItemVentaDTO;
import com.apiv1.omniModa.Models.Entity.Categorias;
import com.apiv1.omniModa.Models.Entity.Clientes;
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

@SpringBootTest
@Transactional
class VentasWebTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ClientePortalController clientePortalController;

    @Autowired
    private VentasController ventasController;

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

    @Autowired
    private VentaRepository ventaRepository;

    private Usuarios usuarioCliente;
    private Productos productoTest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        TipoCliente tipoCliente = tipoClienteRepository.findAll().stream().findFirst()
                .orElseGet(() -> tipoClienteRepository.save(new TipoCliente("Natural")));

        clienteRepository.findById("WEB-DOC-01")
                .orElseGet(() -> clienteRepository.save(new Clientes("WEB-DOC-01", "Web Client", "webclient@test.com", "3001112233", tipoCliente)));

        Categorias cat = categoriaRepository.findAll().stream().findFirst()
                .orElseGet(() -> categoriaRepository.save(new Categorias(null, "Accesorios")));

        productoTest = productoRepository.findById("WEB-PROD-01")
                .orElseGet(() -> productoRepository.save(new Productos("WEB-PROD-01", "Gorra Urbana", "U", "Negro", 35000.0, 15, cat)));

        usuarioCliente = usuarioRepository.findByCorreo("webclient@test.com")
                .orElseGet(() -> {
                    Usuarios u = new Usuarios();
                    u.setNombreUsuario("Web Client");
                    u.setCorreo("webclient@test.com");
                    u.setPassword("hashed123");
                    u.setEstado("ACTIVO");
                    return usuarioRepository.save(u);
                });
    }

    @Test
    void testClienteComprarApi() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogueado", usuarioCliente);
        session.setAttribute("rolPrincipal", "CLIENTE");

        CompraRequestDTO req = new CompraRequestDTO();
        req.setItems(List.of(new ItemVentaDTO(productoTest.getCodigo(), null, 2, null, null)));

        ResponseEntity<?> resp = clientePortalController.comprar(req, session);
        Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode());

        ResponseEntity<?> comprasResp = clientePortalController.misCompras(session);
        Assertions.assertEquals(HttpStatus.OK, comprasResp.getStatusCode());
    }

    @Test
    void testVentasControllerCambiarEstado() {
        // Registrar venta
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogueado", usuarioCliente);
        CompraRequestDTO req = new CompraRequestDTO(List.of(new ItemVentaDTO(productoTest.getCodigo(), null, 1, null, null)));
        clientePortalController.comprar(req, session);

        List<Ventas> ventas = ventaRepository.findAll();
        Assertions.assertFalse(ventas.isEmpty());
        Ventas ultima = ventas.get(ventas.size() - 1);

        Integer estadoCanceladaId = estadoRepository.findByTipoIgnoreCase("CANCELADA").orElseThrow().getIdEstado();
        ResponseEntity<?> resp = ventasController.cambiarEstado(ultima.getIdVentas(), estadoCanceladaId);
        Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode());

        Ventas ventaActualizada = ventaRepository.findById(ultima.getIdVentas()).orElseThrow();
        Assertions.assertEquals("CANCELADA", ventaActualizada.getEstado().getTipo());
    }

    @Test
    void testVentasHtmlTemplateRender() throws Exception {
        // Asegurar que hay al menos una venta para probar el bucle de filas
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioLogueado", usuarioCliente);
        session.setAttribute("rolPrincipal", "ADMINISTRADOR");
        CompraRequestDTO req = new CompraRequestDTO(List.of(new ItemVentaDTO(productoTest.getCodigo(), null, 1, null, null)));
        clientePortalController.comprar(req, session);

        mockMvc.perform(get("/ventas").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("sale/ventas"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Historial de Ventas")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("verDetalleVenta")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("cambiarEstadoRapido")));
    }
}
