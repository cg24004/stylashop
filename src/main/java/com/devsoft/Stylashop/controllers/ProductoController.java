package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    public ResponseEntity<?> getAll() {
        List<ProductoDTO> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ProductoDTO producto = productoService.findById(id);
        if (producto == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "El producto con ID: " + id + " no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(producto);
    }

    @PostMapping(value = "/productos", consumes = {"multipart/form-data"})
    public ResponseEntity<?> save(
            @RequestPart("dto") ProductoDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            ProductoDTO persisted = productoService.save(dto, imagen);
            return new ResponseEntity<>(persisted, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al guardar el producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/productos/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestPart("dto") ProductoDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        ProductoDTO actual = productoService.findById(id);
        if (actual == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No existe un producto con el ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        dto.setId(id);
        try {
            ProductoDTO updated = productoService.save(dto, imagen);
            return ResponseEntity.ok(updated);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al actualizar el producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ProductoDTO actual = productoService.findById(id);
        if (actual == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No existe un producto con el ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            productoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al eliminar el producto");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}