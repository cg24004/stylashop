package com.devsoft.Stylashop.utils;

import com.devsoft.Stylashop.dto.CategoriaDTO;
import com.devsoft.Stylashop.dto.MarcaDTO;
import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.entities.Categoria;
import com.devsoft.Stylashop.entities.Marca;
import com.devsoft.Stylashop.entities.Producto;

public class ProductoMapper {

    // Convierte de entidad a DTO
    public static ProductoDTO toDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecioUnitario(producto.getPrecioUnitario());
        dto.setUrlImagen(producto.getUrlImagen());
        dto.setCategoriaDTO(new CategoriaDTO(
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
        ));
        dto.setMarcaDTO(new MarcaDTO(
                producto.getMarca().getId(),
                producto.getMarca().getNombre()
        ));
        return dto;
    }

    // Convierte de DTO a entidad
    public static Producto toEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        producto.setUrlImagen(dto.getUrlImagen());
        producto.setCategoria(new Categoria(
                dto.getCategoriaDTO().getId(),
                dto.getCategoriaDTO().getNombre()
        ));
        producto.setMarca(new Marca(
                dto.getMarcaDTO().getId(),
                dto.getMarcaDTO().getNombre()
        ));
        return producto;
    }
}
