package com.apiv1.omniModa.Models.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apiv1.omniModa.Models.Entity.Promociones;
import com.apiv1.omniModa.Models.Repository.PromocionesRepository;

@Service
public class PromocionesService {

    @Autowired
    private PromocionesRepository promocionesRepository;

    public List<Promociones> listarPromociones() {

        List<Promociones> promociones = promocionesRepository.findAll();

        for (Promociones promocion : promociones) {
            actualizarEstado(promocion);
        }

        return promociones;
    }

    public Promociones buscarPorId(Integer id) {
        return promocionesRepository.findById(id).orElse(null);
    }

    public void guardar(Promociones promocion) {

        actualizarEstado(promocion);

        promocionesRepository.save(promocion);
    }

    public void eliminar(Integer id) {
        promocionesRepository.deleteById(id);
    }

    private void actualizarEstado(Promociones promocion) {

        if (promocion.getFechaInicio() == null || promocion.getFechaFin() == null) {
            promocion.setEstado("Pendiente");
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();

        if (ahora.isBefore(promocion.getFechaInicio())) {

            promocion.setEstado("Programada");

        } else if (ahora.isAfter(promocion.getFechaFin())) {

            promocion.setEstado("Finalizada");

        } else {

            promocion.setEstado("Activa");
        }
    }
}