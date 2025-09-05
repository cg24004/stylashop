package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.CategoriaDTO;
import com.devsoft.Stylashop.dto.MarcaDTO;
import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.entities.Categoria;
import com.devsoft.Stylashop.entities.Marca;
import com.devsoft.Stylashop.entities.Producto;
import com.devsoft.Stylashop.repository.CategoriaRepository;
import com.devsoft.Stylashop.repository.MarcaRepository;
import com.devsoft.Stylashop.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Cambia esta ruta por la que usas en tu servidor
    private final String UPLOAD_DIR = "C:/ruta/a/tu/carpeta/uploads/productos/";

    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDTO findById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) return null;
        return convertToDTO(producto);
    }

    @Transactional(readOnly = true)
    public ProductoDTO findByNombre(String nombre) {
        Producto producto = productoRepository.findByNombre(nombre);
        if (producto == null) return null;
        return convertToDTO(producto);
    }

    @Transactional
    public ProductoDTO save(ProductoDTO dto, MultipartFile imagen) {
        Producto producto = new Producto();
        if (dto.getId() != null) producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioUnitario(dto.getPrecioUnitario());

        // Manejo de imagen
        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                imagen.transferTo(new File(UPLOAD_DIR + nombreArchivo));
                producto.setImagenUrl(nombreArchivo);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        } else {
            producto.setImagenUrl(dto.getImagenUrl());
        }

        Marca marca = null;
        if (dto.getMarca() != null && dto.getMarca().getId() != null) {
            marca = marcaRepository.findById(dto.getMarca().getId()).orElse(null);
        }
        producto.setMarca(marca);

        Categoria categoria = null;
        if (dto.getCategoria() != null && dto.getCategoria().getId() != null) {
            categoria = categoriaRepository.findById(dto.getCategoria().getId()).orElse(null);
        }
        producto.setCategoria(categoria);

        return convertToDTO(productoRepository.save(producto));
    }

    @Transactional
    public void delete(Long id) {
        productoRepository.deleteById(id);
    }

    private ProductoDTO convertToDTO(Producto producto) {
        MarcaDTO marcaDTO = null;
        if (producto.getMarca() != null) {
            marcaDTO = MarcaDTO.builder()
                    .id(producto.getMarca().getId())
                    .nombre(producto.getMarca().getNombre())
                    .build();
        }
        CategoriaDTO categoriaDTO = null;
        if (producto.getCategoria() != null) {
            categoriaDTO = CategoriaDTO.builder()
                    .id(producto.getCategoria().getId())
                    .nombre(producto.getCategoria().getNombre())
                    .build();
        }
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioUnitario(),
                producto.getImagenUrl(),
                marcaDTO,
                categoriaDTO
        );
    }
}