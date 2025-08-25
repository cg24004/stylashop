package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.DetalleVentaDTO;
import com.devsoft.Stylashop.dto.VentaDTO;
import com.devsoft.Stylashop.entities.*;
import com.devsoft.Stylashop.interfaces.IVentaService;
import com.devsoft.Stylashop.repository.*;
import com.devsoft.Stylashop.utils.VentaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> findAll() {
        return ventaRepository.findAll()
                .stream().map(VentaMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO findById(Long id) {
        return ventaRepository.findById(id)
                .map(VentaMapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public VentaDTO registerOrUpdate(VentaDTO ventaDTO) {
        // Validamos las referencias requeridas
        Optional<Cliente> clienteOpt = clienteRepository.findById(ventaDTO.getClienteDTO().getId());
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(ventaDTO.getUsuarioDTO().getId());

        if (clienteOpt.isEmpty() || usuarioOpt.isEmpty()
                || ventaDTO.getDetalle() == null || ventaDTO.getDetalle().isEmpty()) {
            return null; // se gestiona en el controlador
        }

        Venta venta;
        if (ventaDTO.getId() == null) {
            // Nueva venta
            venta = new Venta();
            venta.setFecha(LocalDate.now());
            venta.setHora(LocalTime.now());
            venta.setDetalleVenta(new ArrayList<>());
        } else {
            // Actualización de una venta existente
            Optional<Venta> ventaOpt = ventaRepository.findById(ventaDTO.getId());
            if (ventaOpt.isEmpty()) return null;
            venta = ventaOpt.get();
            // Limpiamos detalle anterior para reemplazarlo
            venta.getDetalleVenta().clear();
        }

        // Seteamos referencias y total
        venta.setCliente(clienteOpt.get());
        venta.setUsuario(usuarioOpt.get());
        venta.setTotal(ventaDTO.getTotal());

        // Procesamos detalle de la venta
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalle()) {
            Optional<Producto> productoOpt = productoRepository.findById(detalleDTO.getProductoDTO().getId());
            if (productoOpt.isEmpty()) return null;
            Producto producto = productoOpt.get();

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setCantidad(detalleDTO.getCantidad());
            detalleVenta.setPrecio(detalleDTO.getPrecio());
            detalleVenta.setSubtotal(detalleDTO.getSubtotal());
            detalleVenta.setProducto(producto);
            detalleVenta.setVenta(venta);

            venta.getDetalleVenta().add(detalleVenta);
        }

        Venta ventaPersisted = ventaRepository.save(venta);
        return VentaMapper.toDTO(ventaPersisted);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }
}
