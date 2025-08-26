package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.CategoriaDTO;
import com.devsoft.Stylashop.interfaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            CategoriaDTO dto = categoriaService.findById(id);
            if (dto == null) {
                res.put("message", "La categoría con ID " + id + " no existe");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al consultar la base de datos");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoriaDTO dto) {
        Map<String, Object> res = new HashMap<>();
        try {
            CategoriaDTO created = categoriaService.registerOrUpdate(dto);
            res.put("message", "Categoría creada con éxito");
            res.put("categoria", created);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            res.put("message", "Error al crear la categoría");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        Map<String, Object> res = new HashMap<>();
        CategoriaDTO current = categoriaService.findById(id);
        if (current == null) {
            res.put("message", "No se puede editar: la categoría con ID " + id + " no existe");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            dto.setId(id);
            CategoriaDTO updated = categoriaService.registerOrUpdate(dto);
            res.put("message", "Categoría actualizada con éxito");
            res.put("categoria", updated);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (DataAccessException e) {
            res.put("message", "Error al actualizar la categoría");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        CategoriaDTO current = categoriaService.findById(id);
        if (current == null) {
            res.put("message", "No existe la categoría con ID " + id);
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            categoriaService.delete(id);
            res.put("message", "Categoría eliminada con éxito");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al eliminar la categoría");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
