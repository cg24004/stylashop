package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.MarcaDTO;
import com.devsoft.Stylashop.services.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class MarcaController {
    @Autowired
    private MarcaService marcaService;

    @GetMapping("/marcas")
    public ResponseEntity<?> getAll() {
        List<MarcaDTO> marcas = marcaService.findAll();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/marcas/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        MarcaDTO marca = marcaService.findById(id);
        if (marca == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "La marca con ID: " + id + " no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(marca);
    }

    @PostMapping("/marcas")
    public ResponseEntity<?> save(@RequestBody MarcaDTO dto) {
        try {
            MarcaDTO persisted = marcaService.save(dto);
            return new ResponseEntity<>(persisted, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al guardar la marca");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/marcas/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MarcaDTO dto) {
        MarcaDTO actual = marcaService.findById(id);
        if (actual == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No existe una marca con el ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        dto.setId(id);
        try {
            MarcaDTO updated = marcaService.save(dto);
            return ResponseEntity.ok(updated);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al actualizar la marca");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/marcas/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        MarcaDTO actual = marcaService.findById(id);
        if (actual == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No existe una marca con el ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            marcaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al eliminar la marca");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

