package com.devsoft.Stylashop.dto;

import com.devsoft.Stylashop.utils.MetodoPago;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MetodoPagoDTO {
    private MetodoPago metodo;  // Cambiado de String a MetodoPago
    private Long cantidadTransacciones;
    private BigDecimal total;

    // Constructor actualizado
    public MetodoPagoDTO(MetodoPago metodo, Long cantidadTransacciones, BigDecimal total) {
        this.metodo = metodo;
        this.cantidadTransacciones = cantidadTransacciones;
        this.total = total;
    }
}