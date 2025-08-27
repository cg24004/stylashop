package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.MarcaDTO;
import java.util.List;

public interface IMarcaService {
    List<MarcaDTO> findAll();
    MarcaDTO findById(Long id);
    MarcaDTO findByNombre(String nombre);
    MarcaDTO save(MarcaDTO dto);
    void delete(Long id);
}

