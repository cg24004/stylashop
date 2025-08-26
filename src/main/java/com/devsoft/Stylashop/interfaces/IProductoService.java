package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.ProductoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductoService {
    List<ProductoDTO> findAll();
    ProductoDTO findById(Long id);
    ProductoDTO findByNombre(String nombre);
    ProductoDTO save(ProductoDTO dto, MultipartFile imageFile)throws IOException;
    void delete(Long id);
}
