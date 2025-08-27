package com.devsoft.Stylashop.utils;

import com.devsoft.Stylashop.dto.CategoriaDTO;
import com.devsoft.Stylashop.dto.MarcaDTO;
import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.entities.Categoria;
import com.devsoft.Stylashop.entities.Marca;
import com.devsoft.Stylashop.entities.Producto;

public class ProductoMapper {

    public static ProductoDTO toDTO(Producto producto) {
        if (producto == null) return null;
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precioUnitario(producto.getPrecioUnitario())
                .imagenUrl(producto.getImagenUrl())
                .marca(producto.getMarca() == null ? null :
                        MarcaDTO.builder()
                                .id(producto.getMarca().getId())
                                .nombre(producto.getMarca().getNombre())
                                .build())
                .categoria(producto.getCategoria() == null ? null :
                        CategoriaDTO.builder()
                                .id(producto.getCategoria().getId())
                                .nombre(producto.getCategoria().getNombre())
                                .build())
                .build();
    }

    public static Producto toEntity(ProductoDTO dto, Marca marca, Categoria categoria) {
        if (dto == null) return null;
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setMarca(marca);
        producto.setCategoria(categoria);
        return producto;
    }
}

