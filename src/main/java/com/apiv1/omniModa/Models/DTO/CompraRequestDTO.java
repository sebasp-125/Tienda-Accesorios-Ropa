package com.apiv1.omniModa.Models.DTO;

import java.util.List;

public class CompraRequestDTO {

    private List<ItemVentaDTO> items;

    public CompraRequestDTO() {
    }

    public CompraRequestDTO(List<ItemVentaDTO> items) {
        this.items = items;
    }

    public List<ItemVentaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemVentaDTO> items) {
        this.items = items;
    }
}
