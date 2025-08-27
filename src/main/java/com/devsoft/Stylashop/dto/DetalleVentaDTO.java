package com.devsoft.Stylashop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVentaDTO {
    private Long id;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal subtotal;
    private Long productoId;
}

