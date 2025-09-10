package com.devsoft.Stylashop.dto;

import com.devsoft.Stylashop.utils.EstadoVenta;
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
    private String correlativo;
    private String fecha;
    private String hora;
    private String estado;
    private BigDecimal total;
    private ClienteDTO clienteDTO;
    private UsuarioDTO usuarioDTO; // Cambió de usuarioId a usuarioDTO
    private List<DetalleVentaDTO> detallesVenta; // Cambió de detalles a detallesVenta
}

