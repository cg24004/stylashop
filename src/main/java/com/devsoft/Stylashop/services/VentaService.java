package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.DetalleVentaDTO;
import com.devsoft.Stylashop.dto.VentaDTO;
import com.devsoft.Stylashop.entities.DetalleVenta;
import com.devsoft.Stylashop.entities.Producto;
import com.devsoft.Stylashop.entities.Usuario;
import com.devsoft.Stylashop.entities.Venta;
import com.devsoft.Stylashop.repository.DetalleVentaRepository;
import com.devsoft.Stylashop.repository.ProductoRepository;
import com.devsoft.Stylashop.repository.UserRepository;
import com.devsoft.Stylashop.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<VentaDTO> findAll() {
        return ventaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaDTO findById(Long id) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta == null) return null;
        return convertToDTO(venta);
    }

    @Transactional
    public VentaDTO save(VentaDTO dto) {
        Venta venta = new Venta();
        if (dto.getId() != null) venta.setId(dto.getId());
        venta.setFecha(dto.getFecha());
        venta.setHora(dto.getHora());
        venta.setTotal(dto.getTotal());
        Usuario usuario = userRepository.findById(dto.getUsuarioId()).orElse(null);
        venta.setUsuario(usuario);
        List<DetalleVenta> detalles = new ArrayList<>();
        if (dto.getDetalles() != null) {
            for (DetalleVentaDTO detDto : dto.getDetalles()) {
                DetalleVenta det = new DetalleVenta();
                if (detDto.getId() != null) det.setId(detDto.getId());
                det.setCantidad(detDto.getCantidad());
                det.setPrecio(detDto.getPrecio());
                det.setSubtotal(detDto.getSubtotal());
                Producto producto = productoRepository.findById(detDto.getProductoId()).orElse(null);
                det.setProducto(producto);
                det.setVenta(venta);
                detalles.add(det);
            }
        }
        venta.setDetalles(detalles);
        return convertToDTO(ventaRepository.save(venta));
    }

    @Transactional
    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    private VentaDTO convertToDTO(Venta venta) {
        List<DetalleVentaDTO> detalles = venta.getDetalles() != null ? venta.getDetalles().stream()
                .map(this::convertDetalleToDTO)
                .collect(Collectors.toList()) : null;
        return new VentaDTO(
                venta.getId(),
                venta.getFecha(),
                venta.getHora(),
                venta.getTotal(),
                venta.getUsuario() != null ? venta.getUsuario().getId() : null,
                detalles
        );
    }

    private DetalleVentaDTO convertDetalleToDTO(DetalleVenta det) {
        return new DetalleVentaDTO(
                det.getId(),
                det.getCantidad(),
                det.getPrecio(),
                det.getSubtotal(),
                det.getProducto() != null ? det.getProducto().getId() : null
        );
    }
}

