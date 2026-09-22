package com.apiv1.omniModa.Models.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiv1.omniModa.Models.DTO.ItemVentaDTO;
import com.apiv1.omniModa.Models.DTO.VentaDetalleDTO;
import com.apiv1.omniModa.Models.Entity.Clientes;
import com.apiv1.omniModa.Models.Entity.Detalle_venta;
import com.apiv1.omniModa.Models.Entity.Estados;
import com.apiv1.omniModa.Models.Entity.Productos;
import com.apiv1.omniModa.Models.Entity.TipoCliente;
import com.apiv1.omniModa.Models.Entity.Usuarios;
import com.apiv1.omniModa.Models.Entity.Ventas;
import com.apiv1.omniModa.Models.Repository.ClienteRepository;
import com.apiv1.omniModa.Models.Repository.DetalleVentaRepository;
import com.apiv1.omniModa.Models.Repository.EstadoRepository;
import com.apiv1.omniModa.Models.Repository.ProductoRepository;
import com.apiv1.omniModa.Models.Repository.TipoClienteRepository;
import com.apiv1.omniModa.Models.Repository.VentaRepository;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final EstadoRepository estadoRepository;
    private final TipoClienteRepository tipoClienteRepository;

    public VentaService(
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository,
            EstadoRepository estadoRepository,
            TipoClienteRepository tipoClienteRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.estadoRepository = estadoRepository;
        this.tipoClienteRepository = tipoClienteRepository;
    }

    public List<Ventas> listarVentas() {
        return ventaRepository.findAllByOrderByFechaDescIdVentasDesc();
    }

    public List<Ventas> filtrarVentas(String cliente, String producto, LocalDate fecha, Integer estadoId) {
        String c = (cliente != null && !cliente.isBlank()) ? cliente.trim() : null;
        String p = (producto != null && !producto.isBlank()) ? producto.trim() : null;
        if (c == null && p == null && fecha == null && estadoId == null) {
            return listarVentas();
        }
        return ventaRepository.filtrarVentas(c, p, fecha, estadoId);
    }

    public Ventas buscarPorId(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public List<Detalle_venta> obtenerDetallesVenta(Integer idVenta) {
        return detalleVentaRepository.findByVentaIdVentas(idVenta);
    }

    public List<Estados> listarEstados() {
        return estadoRepository.findAll();
    }

    public Estados buscarEstadoPorId(Integer id) {
        return estadoRepository.findById(id).orElse(null);
    }

    public Estados buscarEstadoPorTipo(String tipo) {
        return estadoRepository.findByTipoIgnoreCase(tipo).orElse(null);
    }

    public VentaDetalleDTO obtenerDetalleDTO(Integer idVenta) {
        Ventas venta = buscarPorId(idVenta);
        if (venta == null) {
            return null;
        }

        List<Detalle_venta> detalles = detalleVentaRepository.findByVentaIdVentas(idVenta);
        List<ItemVentaDTO> itemDTOs = detalles.stream().map(d -> new ItemVentaDTO(
                d.getProducto().getCodigo(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotal())).collect(Collectors.toList());

        return new VentaDetalleDTO(
                venta.getIdVentas(),
                venta.getFecha(),
                venta.getTotal(),
                venta.getEstado() != null ? venta.getEstado().getTipo() : "DESCONOCIDO",
                venta.getEstado() != null ? venta.getEstado().getIdEstado() : null,
                venta.getCliente() != null ? venta.getCliente().getDocumento() : "N/A",
                venta.getCliente() != null ? venta.getCliente().getNombreCompleto() : "Cliente General",
                venta.getCliente() != null ? venta.getCliente().getCorreo() : "N/A",
                venta.getCliente() != null ? venta.getCliente().getTelefono() : "N/A",
                itemDTOs);
    }

    @Transactional
    public Ventas registrarVenta(
            String clienteDocumento,
            LocalDate fecha,
            Integer estadoId,
            List<ItemVentaDTO> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un producto para la venta.");
        }

        Clientes cliente = clienteRepository.findById(clienteDocumento)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El cliente seleccionado no existe."));

        Estados estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El estado seleccionado no es válido."));

        boolean esCancelada = "CANCELADA".equalsIgnoreCase(estado.getTipo());

        for (ItemVentaDTO item : items) {

            Productos prod = productoRepository.findById(item.getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getCodigo()));

            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad para el producto "
                                + prod.getNombre()
                                + " debe ser mayor a 0.");
            }

            if (!esCancelada
                    && prod.getStockDisponible() < item.getCantidad()) {

                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto '"
                                + prod.getNombre()
                                + "'. Disponible: "
                                + prod.getStockDisponible()
                                + ", Solicitado: "
                                + item.getCantidad());
            }
        }

        Ventas venta = new Ventas();

        venta.setCliente(cliente);
        venta.setFecha(fecha != null ? fecha : LocalDate.now());
        venta.setEstado(estado);
        venta.setTotal(0.0);

        venta = ventaRepository.save(venta);

        double totalCalculado = 0.0;

        for (ItemVentaDTO item : items) {

            Productos prod = productoRepository.findById(item.getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: " + item.getCodigo()));

            if (!esCancelada) {
                prod.setStockDisponible(
                        prod.getStockDisponible() - item.getCantidad());

                productoRepository.save(prod);
            }

            double precioUnitario = prod.getPrecio() != null
                    ? prod.getPrecio()
                    : 0.0;

            double subtotal = precioUnitario * item.getCantidad();

            totalCalculado += subtotal;

            Detalle_venta detalle = new Detalle_venta(
                    null,
                    item.getCantidad(),
                    precioUnitario,
                    subtotal,
                    venta,
                    prod);

            venta.addDetalle(detalle);
        }

        venta.setTotal(totalCalculado);

        return ventaRepository.save(venta);
    }

    @Transactional
    public Ventas actualizarVenta(
            Integer idVenta,
            String clienteDocumento,
            LocalDate fecha,
            Integer estadoId,
            List<ItemVentaDTO> items) {

        Ventas venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Venta no encontrada con ID: " + idVenta));

        Clientes cliente = clienteRepository.findById(clienteDocumento)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El cliente seleccionado no existe."));

        Estados nuevoEstado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El estado seleccionado no es válido."));

        boolean estadoAnteriorEraCancelada = "CANCELADA".equalsIgnoreCase(
                venta.getEstado().getTipo());

        boolean nuevoEstadoEsCancelada = "CANCELADA".equalsIgnoreCase(
                nuevoEstado.getTipo());

        List<Detalle_venta> detallesActuales = new ArrayList<>(venta.getDetalles());

        if (!estadoAnteriorEraCancelada) {

            for (Detalle_venta d : detallesActuales) {

                Productos prod = d.getProducto();

                prod.setStockDisponible(
                        prod.getStockDisponible() + d.getCantidad());

                productoRepository.save(prod);
            }
        }

        List<ItemVentaDTO> itemsAProcesar = (items != null && !items.isEmpty())
                ? items
                : detallesActuales.stream()
                        .map(d -> new ItemVentaDTO(
                                d.getProducto().getCodigo(),
                                d.getProducto().getNombre(),
                                d.getCantidad(),
                                d.getPrecioUnitario(),
                                d.getSubtotal()))
                        .collect(Collectors.toList());

        if (!nuevoEstadoEsCancelada) {

            for (ItemVentaDTO item : itemsAProcesar) {

                Productos prod = productoRepository.findById(item.getCodigo())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Producto no encontrado: "
                                        + item.getCodigo()));

                if (prod.getStockDisponible() < item.getCantidad()) {

                    if (!estadoAnteriorEraCancelada) {

                        for (Detalle_venta d : detallesActuales) {

                            Productos p = d.getProducto();

                            p.setStockDisponible(
                                    p.getStockDisponible()
                                            - d.getCantidad());

                            productoRepository.save(p);
                        }
                    }

                    throw new IllegalArgumentException(
                            "Stock insuficiente para '"
                                    + prod.getNombre()
                                    + "'. Disponible: "
                                    + prod.getStockDisponible());
                }
            }
        }

        venta.getDetalles().clear();

        double totalCalculado = 0.0;

        for (ItemVentaDTO item : itemsAProcesar) {

            Productos prod = productoRepository.findById(item.getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado: "
                                    + item.getCodigo()));

            if (!nuevoEstadoEsCancelada) {

                prod.setStockDisponible(
                        prod.getStockDisponible()
                                - item.getCantidad());

                productoRepository.save(prod);
            }

            double precioUnitario = prod.getPrecio() != null
                    ? prod.getPrecio()
                    : 0.0;

            double subtotal = precioUnitario * item.getCantidad();

            totalCalculado += subtotal;

            Detalle_venta detalle = new Detalle_venta(
                    null,
                    item.getCantidad(),
                    precioUnitario,
                    subtotal,
                    venta,
                    prod);

            venta.addDetalle(detalle);
        }

        venta.setCliente(cliente);
        venta.setFecha(fecha != null ? fecha : venta.getFecha());
        venta.setEstado(nuevoEstado);
        venta.setTotal(totalCalculado);

        return ventaRepository.save(venta);
    }

    @Transactional
    public void cambiarEstado(Integer idVenta, Integer nuevoEstadoId) {
        Ventas venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        Estados nuevoEstado = estadoRepository.findById(nuevoEstadoId)
                .orElseThrow(() -> new IllegalArgumentException("Estado no válido"));

        if (venta.getEstado().getIdEstado().equals(nuevoEstadoId)) {
            return;
        }

        boolean anteriorCancelada = "CANCELADA".equalsIgnoreCase(venta.getEstado().getTipo());
        boolean nuevaCancelada = "CANCELADA".equalsIgnoreCase(nuevoEstado.getTipo());

        List<Detalle_venta> detalles = detalleVentaRepository.findByVentaIdVentas(idVenta);

        if (!anteriorCancelada && nuevaCancelada) {
            for (Detalle_venta d : detalles) {
                Productos p = d.getProducto();
                p.setStockDisponible(p.getStockDisponible() + d.getCantidad());
                productoRepository.save(p);
            }
        }

        if (anteriorCancelada && !nuevaCancelada) {
            for (Detalle_venta d : detalles) {
                Productos p = d.getProducto();
                if (p.getStockDisponible() < d.getCantidad()) {
                    throw new IllegalArgumentException(
                            "No se puede reactivar la venta. Stock insuficiente para " + p.getNombre());
                }
            }
            for (Detalle_venta d : detalles) {
                Productos p = d.getProducto();
                p.setStockDisponible(p.getStockDisponible() - d.getCantidad());
                productoRepository.save(p);
            }
        }

        venta.setEstado(nuevoEstado);
        ventaRepository.save(venta);
    }

    @Transactional
    public void eliminarVenta(Integer idVenta) {
        Ventas venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        List<Detalle_venta> detalles = detalleVentaRepository.findByVentaIdVentas(idVenta);

        if (!"CANCELADA".equalsIgnoreCase(venta.getEstado().getTipo())) {
            for (Detalle_venta d : detalles) {
                Productos p = d.getProducto();
                p.setStockDisponible(p.getStockDisponible() + d.getCantidad());
                productoRepository.save(p);
            }
        }

        detalleVentaRepository.deleteAll(detalles);
        ventaRepository.delete(venta);
    }

    @Transactional
    public Ventas procesarCompraCliente(Usuarios usuario, List<ItemVentaDTO> items) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no autenticado.");
        }

        Clientes cliente = clienteRepository.findByCorreoIgnoreCase(usuario.getCorreo()).orElse(null);
        if (cliente == null) {
            TipoCliente tipo = tipoClienteRepository.findAll().stream().findFirst()
                    .orElseGet(() -> tipoClienteRepository.save(new TipoCliente("Natural")));

            String doc = "C-" + System.currentTimeMillis();
            if (doc.length() > 20) {
                doc = doc.substring(0, 20);
            }

            cliente = new Clientes(doc, usuario.getNombreUsuario(), usuario.getCorreo(), "Sin registrar", tipo);
            cliente = clienteRepository.save(cliente);
        }

        Estados estadoPagada = estadoRepository.findByTipoIgnoreCase("PAGADA")
                .orElseGet(() -> estadoRepository.save(new Estados(null, "PAGADA")));

        return registrarVenta(cliente.getDocumento(), LocalDate.now(), estadoPagada.getIdEstado(), items);
    }

    public List<Ventas> obtenerComprasCliente(Usuarios usuario) {
        if (usuario == null) {
            return List.of();
        }
        Clientes cliente = clienteRepository.findByCorreoIgnoreCase(usuario.getCorreo()).orElse(null);
        if (cliente == null) {
            return List.of();
        }
        return ventaRepository.findByClienteDocumentoOrderByFechaDescIdVentasDesc(cliente.getDocumento());
    }

    public long contarTotalVentas() {
        return ventaRepository.count();
    }

    public double calcularTotalIngresos() {
        Double total = ventaRepository.sumTotalVentasPagadas();
        return total != null ? total : 0.0;
    }

    public long contarPorEstado(String tipo) {
        return ventaRepository.countByEstadoTipoIgnoreCase(tipo);
    }

    public List<Ventas> obtenerVentasRecientes(int limit) {
        return ventaRepository.findTop5ByOrderByFechaDescIdVentasDesc();
    }
}
