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
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioUnitario;
    private String imagenUrl;
    private MarcaDTO marcaDTO;
    private CategoriaDTO categoriaDTO;
}