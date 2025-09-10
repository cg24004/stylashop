package com.devsoft.Stylashop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class IngresoDTO {
    private Long ventaId;
    private String correlativo;
    private LocalDate fechaPago;
    private BigDecimal totalVenta;
    private String clienteNombre;
    private String cajeroNombre;
    private BigDecimal sumaPagos;
}
