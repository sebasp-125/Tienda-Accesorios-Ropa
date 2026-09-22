package com.apiv1.omniModa.Models.DTO;

import java.util.List;

public class CompraRequestDTO {

    private List<ItemVentaDTO> items;
    private String metodoPago;

    public CompraRequestDTO() {
    }

    public CompraRequestDTO(List<ItemVentaDTO> items) {
        this.items = items;
    }

    public CompraRequestDTO(List<ItemVentaDTO> items, String metodoPago) {
        this.items = items;
        this.metodoPago = metodoPago;
    }

    public List<ItemVentaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemVentaDTO> items) {
        this.items = items;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
