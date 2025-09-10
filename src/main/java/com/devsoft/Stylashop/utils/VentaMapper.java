package com.devsoft.Stylashop.utils;

import com.devsoft.Stylashop.dto.ClienteDTO;
import com.devsoft.Stylashop.dto.DetalleVentaDTO;
import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.dto.RoleDTO;
import com.devsoft.Stylashop.dto.UsuarioDTO;
import com.devsoft.Stylashop.dto.VentaDTO;
import com.devsoft.Stylashop.entities.Cliente;
import com.devsoft.Stylashop.entities.DetalleVenta;
import com.devsoft.Stylashop.entities.Producto;
import com.devsoft.Stylashop.entities.Usuario;
import com.devsoft.Stylashop.entities.Venta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class VentaMapper {

    // 🔹 Entity -> DTO
    public static VentaDTO toDTO(Venta venta) {
        if (venta == null) return null;

        List<DetalleVentaDTO> detalles = venta.getDetalles() != null
                ? venta.getDetalles().stream()
                .map(VentaMapper::detalleToDTO)
                .collect(Collectors.toList())
                : null;

        ClienteDTO clienteDTO = null;
        if (venta.getCliente() != null) {
            clienteDTO = ClienteDTO.builder()
                    .id(venta.getCliente().getId())
                    .nombre(venta.getCliente().getNombre())
                    .email(venta.getCliente().getEmail())
                    .telefono(venta.getCliente().getTelefono())
                    .tipoCliente(venta.getCliente().getTipoCliente())
                    .build();
        }

        UsuarioDTO usuarioDTO = null;
        if (venta.getUsuario() != null) {
            RoleDTO roleDTO = null;
            if (venta.getUsuario().getRole() != null) {
                roleDTO = RoleDTO.builder()
                        .id(venta.getUsuario().getRole().getId())
                        .nombre(venta.getUsuario().getRole().getNombre())
                        .build();
            }

            usuarioDTO = UsuarioDTO.builder()
                    .id(venta.getUsuario().getId())
                    .username(venta.getUsuario().getUsername())
                    .roleDTO(roleDTO)
                    .build();
        }

        return VentaDTO.builder()
                .id(venta.getId())
                .correlativo(venta.getCorrelativo())
                .fecha(String.valueOf(venta.getFecha()))
                .hora(String.valueOf(venta.getHora()))
                .estado(String.valueOf(venta.getEstado()))
                .total(venta.getTotal())
                .clienteDTO(clienteDTO)
                .usuarioDTO(usuarioDTO)
                .detallesVenta(detalles) // Cambió de 'detalles' a 'detallesVenta'
                .build();
    }

    // 🔹 DTO -> Entity
    public static Venta toEntity(VentaDTO dto, Usuario usuario, Cliente cliente, List<DetalleVenta> detalles) {
        if (dto == null) return null;

        Venta venta = new Venta();
        venta.setId(dto.getId());
        venta.setCorrelativo(dto.getCorrelativo());
        venta.setFecha(LocalDate.parse(dto.getFecha()));
        venta.setHora(LocalTime.parse(dto.getHora()));
        venta.setEstado(EstadoVenta.valueOf(dto.getEstado()));
        venta.setTotal(dto.getTotal());
        venta.setUsuario(usuario);
        venta.setCliente(cliente);
        venta.setDetalles(detalles);

        return venta;
    }

    // 🔹 DetalleVenta -> DTO
    public static DetalleVentaDTO detalleToDTO(DetalleVenta det) {
        if (det == null) return null;
        return DetalleVentaDTO.builder()
                .id(det.getId())
                .cantidad(det.getCantidad())
                .precio(det.getPrecio())
                .subtotal(det.getSubtotal())
                .productoDTO(det.getProducto() != null ?
                        ProductoDTO.builder()
                                .id(det.getProducto().getId())
                                .nombre(det.getProducto().getNombre())
                                .build() : null)
                .build();
    }

    // 🔹 DTO -> DetalleVenta
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
