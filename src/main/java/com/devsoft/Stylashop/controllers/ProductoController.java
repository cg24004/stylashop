package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.ProductoDTO;
import com.devsoft.Stylashop.interfaces.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            ProductoDTO dto = productoService.findById(id);
            if (dto == null) {
                res.put("message", "El producto con ID " + id + " no existe");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al consultar el producto");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductoDTO dto) {
        Map<String, Object> res = new HashMap<>();
        try {
            ProductoDTO created = productoService.registerOrUpdate(dto);
            res.put("message", "Producto creado con éxito");
            res.put("producto", created);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            res.put("message", "Error al crear el producto");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        Map<String, Object> res = new HashMap<>();
        ProductoDTO current = productoService.findById(id);
        if (current == null) {
            res.put("message", "No se puede editar: el producto con ID " + id + " no existe");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            dto.setId(id);
            ProductoDTO updated = productoService.registerOrUpdate(dto);
            res.put("message", "Producto actualizado con éxito");
            res.put("producto", updated);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (DataAccessException e) {
            res.put("message", "Error al actualizar el producto");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        ProductoDTO current = productoService.findById(id);
        if (current == null) {
            res.put("message", "No existe el producto con ID " + id);
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            productoService.delete(id);
            res.put("message", "Producto eliminado con éxito");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al eliminar el producto");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
