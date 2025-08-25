package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.entities.Categoria;
import com.devsoft.Stylashop.entities.Marca;
import com.devsoft.Stylashop.entities.Producto;
import com.devsoft.Stylashop.interfaces.IProductoService;
import com.devsoft.Stylashop.repository.ProductoRepository;
import com.devsoft.Stylashop.utils.ProductoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Directorio donde se guardarán las imágenes
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll()
                .stream().map(ProductoMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO findById(Long id) {
        return productoRepository.findById(id)
                .map(ProductoMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre)
                .map(ProductoMapper::toDTO).orElse(null);
    }

    @Override
    public ProductoDTO save(ProductoDTO dto, MultipartFile imageFile) throws IOException {
        Producto producto;
        if (dto.getId() == null) {
            // Crear nuevo producto
            producto = ProductoMapper.toEntity(dto);
            String nombreImagen = procesarImagen(imageFile);
            if (nombreImagen != null) {
                producto.setUrlImagen(nombreImagen);
            }
        } else {
            // Actualizar producto existente
            Optional<Producto> productoActual = productoRepository.findById(dto.getId());
            if (productoActual.isEmpty()) {
                return null;
            }
            producto = productoActual.get();

            // Actualizamos los datos del producto
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecioUnitario(dto.getPrecioUnitario());
            producto.setCategoria(new Categoria(dto.getCategoriaDTO().getId(), dto.getCategoriaDTO().getNombre()));
            producto.setMarca(new Marca(dto.getMarcaDTO().getId(), dto.getMarcaDTO().getNombre()));

            // Si viene una nueva imagen, reemplazamos la anterior
            if (imageFile != null && !imageFile.isEmpty()) {
                if (producto.getUrlImagen() != null && !producto.getUrlImagen().isEmpty()) {
                    Path rutaAnterior = Paths.get(uploadDir, "productos", producto.getUrlImagen());
                    Files.deleteIfExists(rutaAnterior);
                }
                String nombreArchivo = procesarImagen(imageFile);
                producto.setUrlImagen(nombreArchivo);
            }
        }
        Producto productoGuardado = productoRepository.save(producto);
        return ProductoMapper.toDTO(productoGuardado);
    }

    @Override
    public void delete(Long id) {
        Producto productoActual = productoRepository.findById(id).orElse(null);
        try {
            productoRepository.deleteById(id);
            if (productoActual != null && productoActual.getUrlImagen() != null) {
                Path rutaAnterior = Paths.get(uploadDir, "productos", productoActual.getUrlImagen());
                Files.deleteIfExists(rutaAnterior);
            }
        } catch (DataAccessException e) {
            System.out.println("Error eliminando producto: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Procesa y guarda la imagen en el sistema de archivos
    private String procesarImagen(MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path rutaArchivo = Paths.get(uploadDir, "productos", nombreArchivo);
            Files.createDirectories(rutaArchivo.getParent());
            Files.copy(imageFile.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
            return nombreArchivo;
        }
        return null;
    }
}
