package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.DetalleVentaDTO;
import java.util.List;

public interface IDetalleVentaService {
    List<DetalleVentaDTO> findAll();
    DetalleVentaDTO findById(Long id);
    DetalleVentaDTO save(DetalleVentaDTO dto);
    void delete(Long id);
}

