package com.apiv1.omniModa.Controllers.Estadisticas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apiv1.omniModa.Models.Service.EstadisticaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    /*
     * Creamos el ObjectMapper directamente.
     * No necesitamos que Spring lo registre como Bean.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    // =========================================================
    // VISTA PRINCIPAL
    // =========================================================

    @GetMapping("/estadisticas")
    public String estadisticas(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) String tipoCliente,
            Model model) {

        // =====================================================
        // FECHAS POR DEFECTO
        // =====================================================

        if (fechaInicio == null) {
            fechaInicio = LocalDate.now().withDayOfMonth(1);
        }

        if (fechaFin == null) {
            fechaFin = LocalDate.now();
        }

        // =====================================================
        // NORMALIZAR TIPO DE CLIENTE
        // =====================================================

        if (tipoCliente != null && tipoCliente.trim().isEmpty()) {
            tipoCliente = null;
        }

        // =====================================================
        // OBTENER DATOS DEL SERVICIO
        // =====================================================

        List<Object[]> productos = estadisticaService.obtenerProductosMasVendidos(
                fechaInicio,
                fechaFin,
                tipoCliente);

        List<Object[]> clientes = estadisticaService.obtenerClientesFrecuentes(
                fechaInicio,
                fechaFin,
                tipoCliente);

        List<Object[]> ingresos = estadisticaService.obtenerIngresosPorMes(
                fechaInicio,
                fechaFin,
                tipoCliente);

        List<Object[]> tiposCliente = estadisticaService.obtenerIngresosPorTipoCliente(
                fechaInicio,
                fechaFin,
                tipoCliente);

        // =====================================================
        // PRODUCTOS MÁS VENDIDOS
        // =====================================================

        List<Map<String, Object>> productosGrafica = new ArrayList<>();

        for (Object[] fila : productos) {

            Map<String, Object> item = new LinkedHashMap<>();

            item.put("nombre", fila[0]);
            item.put("codigo", fila[1]);
            item.put("cantidad", fila[2]);

            productosGrafica.add(item);
        }

        // =====================================================
        // CLIENTES FRECUENTES
        // =====================================================

        List<Map<String, Object>> clientesGrafica = new ArrayList<>();

        for (Object[] fila : clientes) {

            Map<String, Object> item = new LinkedHashMap<>();

            item.put("nombre", fila[0]);
            item.put("documento", fila[1]);
            item.put("compras", fila[2]);

            clientesGrafica.add(item);
        }

        // =====================================================
        // INGRESOS POR MES
        // =====================================================

        List<Map<String, Object>> ingresosGrafica = new ArrayList<>();

        String[] meses = {
                "",
                "Enero",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
        };

        for (Object[] fila : ingresos) {

            Integer mes = ((Number) fila[0]).intValue();
            Integer año = ((Number) fila[1]).intValue();

            Map<String, Object> item = new LinkedHashMap<>();

            item.put(
                    "periodo",
                    meses[mes] + " " + año);

            item.put(
                    "ingresos",
                    fila[2]);

            ingresosGrafica.add(item);
        }

        // =====================================================
        // INGRESOS POR TIPO DE CLIENTE
        // =====================================================

        List<Map<String, Object>> tiposGrafica = new ArrayList<>();

        for (Object[] fila : tiposCliente) {

            Map<String, Object> item = new LinkedHashMap<>();

            item.put("tipo", fila[0]);
            item.put("ingresos", fila[1]);

            tiposGrafica.add(item);
        }

        // =====================================================
        // FILTROS
        // =====================================================

        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("tipoCliente", tipoCliente);

        // =====================================================
        // CONVERTIR DATOS A JSON
        // =====================================================

        try {

            model.addAttribute(
                    "productosJson",
                    objectMapper.writeValueAsString(productosGrafica));

            model.addAttribute(
                    "clientesJson",
                    objectMapper.writeValueAsString(clientesGrafica));

            model.addAttribute(
                    "ingresosJson",
                    objectMapper.writeValueAsString(ingresosGrafica));

            model.addAttribute(
                    "tiposJson",
                    objectMapper.writeValueAsString(tiposGrafica));

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Error convirtiendo las estadísticas a JSON",
                    e);
        }

        // =====================================================
        // VISTA
        // =====================================================

        return "stats/estadisticas";
    }
}