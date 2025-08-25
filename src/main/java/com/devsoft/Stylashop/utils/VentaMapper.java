package com.devsoft.Stylashop.utils;

import com.devsoft.Stylashop.dto.*;
import com.devsoft.Stylashop.entities.Venta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

public class VentaMapper {

    public static VentaDTO toDTO(Venta venta) {
        if (venta == null) return null;

        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        dto.setHora(venta.getHora());
        dto.setTotal(venta.getTotal());

        // Cliente
        dto.setClienteDTO(new ClienteDTO(
                venta.getCliente().getId(),
                venta.getCliente().getNombre(),
                venta.getCliente().getDireccion(),
                venta.getCliente().getTelefono(),
                venta.getCliente().getEmail(),
                venta.getCliente().getTipoCliente()
        ));

        // Usuario
        dto.setUsuarioDTO(new UsuarioDTO(
                venta.getUsuario().getId(),
                venta.getUsuario().getNombre(),
                venta.getUsuario().getUsername(),
                venta.getUsuario().isActivo(),
                new RoleDTO(
                        venta.getUsuario().getRole().getId(),
                        venta.getUsuario().getRole().getNombre()
                )
        ));

        // Detalle de la venta
        if (venta.getDetalleVenta() != null) {
            dto.setDetalle(venta.getDetalleVenta().stream().map(d -> {
                DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
                detalleDTO.setId(d.getId());
                detalleDTO.setCantidad(d.getCantidad());
                detalleDTO.setPrecio(d.getPrecio());
                detalleDTO.setSubtotal(
                        BigDecimal.valueOf(d.getCantidad())
                                .multiply(d.getPrecio())
                                .setScale(2, RoundingMode.HALF_UP)
                );
                detalleDTO.setProductoDTO(new ProductoDTO(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getProducto().getDescripcion(),
                        d.getProducto().getPrecioUnitario(),
                        d.getProducto().getUrlImagen(),
                        new CategoriaDTO(
                                d.getProducto().getCategoria().getId(),
                                d.getProducto().getCategoria().getNombre()
                        ),
                        new MarcaDTO(
                                d.getProducto().getMarca().getId(),
                                d.getProducto().getMarca().getNombre()
                        )
                ));
                return detalleDTO;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
}
