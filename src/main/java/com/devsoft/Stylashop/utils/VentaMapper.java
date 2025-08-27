package com.devsoft.Stylashop.utils;

import com.devsoft.Stylashop.dto.DetalleVentaDTO;
import com.devsoft.Stylashop.dto.VentaDTO;
import com.devsoft.Stylashop.entities.DetalleVenta;
import com.devsoft.Stylashop.entities.Producto;
import com.devsoft.Stylashop.entities.Usuario;
import com.devsoft.Stylashop.entities.Venta;

import java.util.List;
import java.util.stream.Collectors;

public class VentaMapper {
    public static VentaDTO toDTO(Venta venta) {
        if (venta == null) return null;
        List<DetalleVentaDTO> detalles = venta.getDetalles() != null ? venta.getDetalles().stream()
                .map(VentaMapper::detalleToDTO)
                .collect(Collectors.toList()) : null;
        return VentaDTO.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())
                .hora(venta.getHora())
                .total(venta.getTotal())
                .usuarioId(venta.getUsuario() != null ? venta.getUsuario().getId() : null)
                .detalles(detalles)
                .build();
    }

    public static Venta toEntity(VentaDTO dto, Usuario usuario, List<DetalleVenta> detalles) {
        if (dto == null) return null;
        Venta venta = new Venta();
        venta.setId(dto.getId());
        venta.setFecha(dto.getFecha());
        venta.setHora(dto.getHora());
        venta.setTotal(dto.getTotal());
        venta.setUsuario(usuario);
        venta.setDetalles(detalles);
        return venta;
    }

    public static DetalleVentaDTO detalleToDTO(DetalleVenta det) {
        if (det == null) return null;
        return DetalleVentaDTO.builder()
                .id(det.getId())
                .cantidad(det.getCantidad())
                .precio(det.getPrecio())
                .subtotal(det.getSubtotal())
                .productoId(det.getProducto() != null ? det.getProducto().getId() : null)
                .build();
    }

    public static DetalleVenta detalleToEntity(DetalleVentaDTO dto, Venta venta, Producto producto) {
        if (dto == null) return null;
        DetalleVenta det = new DetalleVenta();
        det.setId(dto.getId());
        det.setCantidad(dto.getCantidad());
        det.setPrecio(dto.getPrecio());
        det.setSubtotal(dto.getSubtotal());
        det.setVenta(venta);
        det.setProducto(producto);
        return det;
    }
}

