package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.MarcaDTO;
import com.devsoft.Stylashop.interfaces.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private IMarcaService marcaService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(marcaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            MarcaDTO dto = marcaService.findById(id);
            if (dto == null) {
                res.put("message", "La marca con ID " + id + " no existe");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al consultar la marca");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MarcaDTO dto) {
        Map<String, Object> res = new HashMap<>();
        try {
            MarcaDTO created = marcaService.registerOrUpdate(dto);
            res.put("message", "Marca creada con éxito");
            res.put("marca", created);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            res.put("message", "Error al crear la marca");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MarcaDTO dto) {
        Map<String, Object> res = new HashMap<>();
        MarcaDTO current = marcaService.findById(id);
        if (current == null) {
            res.put("message", "No se puede editar: la marca con ID " + id + " no existe");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            dto.setId(id);
            MarcaDTO updated = marcaService.registerOrUpdate(dto);
            res.put("message", "Marca actualizada con éxito");
            res.put("marca", updated);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (DataAccessException e) {
            res.put("message", "Error al actualizar la marca");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        MarcaDTO current = marcaService.findById(id);
        if (current == null) {
            res.put("message", "No existe la marca con ID " + id);
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            marcaService.delete(id);
            res.put("message", "Marca eliminada con éxito");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al eliminar la marca");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
