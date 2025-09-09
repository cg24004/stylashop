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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;

    // Ruta donde se guardan las imágenes (ajústala si es necesario)
    private final String UPLOAD_DIR = "uploads/productos/";

    /* =================== Helpers de archivo =================== */

    private Path getUploadRoot() {
        return Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
    }

    private String saveFile(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                Path uploadPath = getUploadRoot();
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String original = file.getOriginalFilename();
                String cleanName = (original == null ? "file" : original).replaceAll("\\s+", "_");
                String fileName = System.currentTimeMillis() + "_" + cleanName;

                Path target = uploadPath.resolve(fileName).normalize();
                if (!target.startsWith(uploadPath)) {
                    throw new SecurityException("Ruta de archivo inválida");
                }

                Files.copy(file.getInputStream(), target);
                return fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteFileSafe(String fileName) {
        try {
            if (fileName == null || fileName.isBlank()) return;
            Path uploadPath = getUploadRoot();
            Path target = uploadPath.resolve(fileName).normalize();
            if (target.startsWith(uploadPath)) {
                Files.deleteIfExists(target);
            }
        } catch (Exception e) {
            // Loguea pero no interrumpas la operación
            e.printStackTrace();
        }
    }

    /* =================== Mappers =================== */

    private ProductoDTO mapToDTO(Producto p) {
        MarcaDTO marcaDTO = null;
        if (p.getMarca() != null) {
            marcaDTO = new MarcaDTO(p.getMarca().getId(), p.getMarca().getNombre());
        }
        CategoriaDTO categoriaDTO = null;
        if (p.getCategoria() != null) {
            categoriaDTO = new CategoriaDTO(p.getCategoria().getId(), p.getCategoria().getNombre());
        }
        return ProductoDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precioUnitario(p.getPrecioUnitario())
                .imagenUrl(p.getImagenUrl())
                .marcaDTO(marcaDTO)
                .categoriaDTO(categoriaDTO)
                .build();
    }

    private Producto mapToEntity(ProductoDTO dto) {
        Producto p = new Producto();
        p.setId(dto.getId());
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecioUnitario(dto.getPrecioUnitario());
        p.setImagenUrl(dto.getImagenUrl());

        if (dto.getMarcaDTO() != null) {
            Optional<Marca> marcaOpt = marcaRepository.findById(dto.getMarcaDTO().getId());
            marcaOpt.ifPresent(p::setMarca);
        }
        if (dto.getCategoriaDTO() != null) {
            Optional<Categoria> catOpt = categoriaRepository.findById(dto.getCategoriaDTO().getId());
            catOpt.ifPresent(p::setCategoria);
        }
        return p;
    }

    /* =================== CRUD =================== */

    public List<ProductoDTO> listar() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public ProductoDTO crear(ProductoDTO dto, MultipartFile imagen) {
        Producto p = mapToEntity(dto);
        String fileName = saveFile(imagen);
        if (fileName != null) {
            p.setImagenUrl(fileName);
        }
        Producto guardado = productoRepository.save(p);
        return mapToDTO(guardado);
    }

    public ProductoDTO actualizar(Long id, ProductoDTO dto, MultipartFile imagen) {
        return productoRepository.findById(id).map(p -> {
            p.setNombre(dto.getNombre());
            p.setDescripcion(dto.getDescripcion());
            p.setPrecioUnitario(dto.getPrecioUnitario());

            if (dto.getMarcaDTO() != null) {
                marcaRepository.findById(dto.getMarcaDTO().getId()).ifPresent(p::setMarca);
            } else {
                p.setMarca(null);
            }

            if (dto.getCategoriaDTO() != null) {
                categoriaRepository.findById(dto.getCategoriaDTO().getId()).ifPresent(p::setCategoria);
            } else {
                p.setCategoria(null);
            }

            String oldImage = p.getImagenUrl();

            // A) Subieron una nueva imagen => guarda nueva y borra la vieja
            if (imagen != null && !imagen.isEmpty()) {
                String newFile = saveFile(imagen);
                if (newFile != null) {
                    p.setImagenUrl(newFile);
                    deleteFileSafe(oldImage);
                }
            } else {
                // B) No suben archivo, pero pidieron quitar la imagen (dto.imagenUrl == null)
                if (dto.getImagenUrl() == null && oldImage != null) {
                    deleteFileSafe(oldImage);
                    p.setImagenUrl(null);
                }
                // C) Si dto.imagenUrl trae algo (mantener), no hacemos nada
            }

            return mapToDTO(productoRepository.save(p));
        }).orElse(null);
    }

    public void eliminar(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            deleteFileSafe(p.getImagenUrl());
            productoRepository.deleteById(id);
        });
    }
}
