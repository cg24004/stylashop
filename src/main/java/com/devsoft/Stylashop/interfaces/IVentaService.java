package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.VentaDTO;

import java.util.List;

public interface IVentaService {
    List<VentaDTO> findAll();
    //List<VentaDTO> findByEstado(EstadoVenta estado);
    VentaDTO findById(Long id);
    VentaDTO registerOrUpdate(VentaDTO ventaDTO);
    void anular(Long id);
    VentaDTO changeState(VentaDTO dto);
}
