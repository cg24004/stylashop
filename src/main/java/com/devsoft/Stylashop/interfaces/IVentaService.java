package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.VentaDTO;
import java.util.List;

public interface IVentaService {
    List<VentaDTO> findAll();
    VentaDTO findById(Long id);
    VentaDTO save(VentaDTO dto);
    void delete(Long id);
}

