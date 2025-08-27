package com.devsoft.Stylashop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private BigDecimal total;
    private Long usuarioId;
    private List<DetalleVentaDTO> detalles;
}

