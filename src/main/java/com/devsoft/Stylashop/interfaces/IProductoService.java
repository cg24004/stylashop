package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.ProductoDTO;
import java.util.List;

public interface IProductoService {
    List<ProductoDTO> findAll();
    ProductoDTO findById(Long id);
    ProductoDTO findByNombre(String nombre);
    ProductoDTO save(ProductoDTO dto);
    void delete(Long id);
}

