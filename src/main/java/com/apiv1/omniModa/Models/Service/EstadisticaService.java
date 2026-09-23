package com.apiv1.omniModa.Models.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.Repository.EstadisticaRepository;

@Service
public class EstadisticaService {

        private final EstadisticaRepository estadisticaRepository;

        public EstadisticaService(
                        EstadisticaRepository estadisticaRepository) {

                this.estadisticaRepository = estadisticaRepository;
        }

        public List<Object[]> obtenerProductosMasVendidos(
                        LocalDate fechaInicio,
                        LocalDate fechaFin,
                        String tipoCliente) {

                return estadisticaRepository.obtenerProductosMasVendidos(
                                fechaInicio,
                                fechaFin,
                                tipoCliente);
        }

        public List<Object[]> obtenerClientesFrecuentes(
                        LocalDate fechaInicio,
                        LocalDate fechaFin,
                        String tipoCliente) {

                return estadisticaRepository.obtenerClientesFrecuentes(
                                fechaInicio,
                                fechaFin,
                                tipoCliente);
        }

        public List<Object[]> obtenerIngresosPorMes(
                        LocalDate fechaInicio,
                        LocalDate fechaFin,
                        String tipoCliente) {

                return estadisticaRepository.obtenerIngresosPorMes(
                                fechaInicio,
                                fechaFin,
                                tipoCliente);
        }

        public List<Object[]> obtenerIngresosPorTipoCliente(
                        LocalDate fechaInicio,
                        LocalDate fechaFin,
                        String tipoCliente) {

                return estadisticaRepository.obtenerIngresosPorTipoCliente(
                                fechaInicio,
                                fechaFin,
                                tipoCliente);
        }
}