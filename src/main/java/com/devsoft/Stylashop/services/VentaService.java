package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.DetalleVentaDTO;
import com.devsoft.Stylashop.dto.VentaDTO;
import com.devsoft.Stylashop.entities.Cliente;
import com.devsoft.Stylashop.entities.DetalleVenta;
import com.devsoft.Stylashop.entities.Producto;
import com.devsoft.Stylashop.entities.Usuario;
import com.devsoft.Stylashop.entities.Venta;
import com.devsoft.Stylashop.repository.ClienteRepository;
import com.devsoft.Stylashop.repository.DetalleVentaRepository;
import com.devsoft.Stylashop.repository.ProductoRepository;
import com.devsoft.Stylashop.repository.UserRepository;
import com.devsoft.Stylashop.repository.VentaRepository;
import com.devsoft.Stylashop.utils.EstadoVenta;
import com.devsoft.Stylashop.utils.VentaMapper;
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
    private ClienteRepository clienteRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Transactional(readOnly = true)
    public List<VentaDTO> findAll() {
        return ventaRepository.findAll().stream()
                .map(VentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaDTO findById(Long id) {
        return ventaRepository.findById(id)
                .map(VentaMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public VentaDTO save(VentaDTO dto) {
        try {
            // Obtener usuario
            Usuario usuario = null;
            if (dto.getUsuarioDTO() != null && dto.getUsuarioDTO().getId() != null) {
                usuario = userRepository.findById(dto.getUsuarioDTO().getId()).orElse(null);
            }

            // Obtener cliente
            Cliente cliente = null;
            if (dto.getClienteDTO() != null && dto.getClienteDTO().getId() != null) {
                cliente = clienteRepository.findById(dto.getClienteDTO().getId()).orElse(null);
            }

            // Crear detalles
            List<DetalleVenta> detalles = new ArrayList<>();
            if (dto.getDetallesVenta() != null) { // Cambió de getDetalles() a getDetallesVenta()
                for (DetalleVentaDTO detDto : dto.getDetallesVenta()) {
                    Producto producto = null;
                    if (detDto.getProductoDTO() != null && detDto.getProductoDTO().getId() != null) {
                        producto = productoRepository.findById(detDto.getProductoDTO().getId()).orElse(null);
                    }
                    DetalleVenta det = VentaMapper.detalleToEntity(detDto, null, producto);
                    detalles.add(det);
                }
            }

            // Crear venta
            Venta venta = VentaMapper.toEntity(dto, usuario, cliente, detalles);

            // Establecer relación bidireccional
            detalles.forEach(det -> det.setVenta(venta));

            // Guardar
            Venta saved = ventaRepository.save(venta);
            return VentaMapper.toDTO(saved);

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la venta: " + e.getMessage(), e);
        }
    }

    @Transactional
    public VentaDTO update(Long id, VentaDTO dto) {
        try {
            Venta existingVenta = ventaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            // Actualizar campos básicos
            existingVenta.setEstado(EstadoVenta.valueOf(dto.getEstado()));
            existingVenta.setTotal(dto.getTotal());

            // Guardar cambios
            Venta updated = ventaRepository.save(existingVenta);
            return VentaMapper.toDTO(updated);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la venta: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }
}
