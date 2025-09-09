package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        ProductoDTO dto = productoService.obtenerPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crear(
            @RequestPart("dto") ProductoDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        ProductoDTO creado = productoService.crear(dto, imagen);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Producto registrado correctamente",
                "producto", creado
        ));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestPart("dto") ProductoDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        ProductoDTO actualizado = productoService.actualizar(id, dto, imagen);
        if (actualizado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of(
                "message", "Producto actualizado correctamente",
                "producto", actualizado
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok(Map.of("message", "Producto eliminado correctamente"));
    }
}
