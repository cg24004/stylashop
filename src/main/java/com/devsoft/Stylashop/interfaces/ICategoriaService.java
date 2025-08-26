package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.CategoriaDTO;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaDTO> findAll();
    CategoriaDTO findById(Long id);
    CategoriaDTO save(CategoriaDTO categoriaDTO);
    CategoriaDTO findByNombre(String nombre);
    void delete(Long id);
}
