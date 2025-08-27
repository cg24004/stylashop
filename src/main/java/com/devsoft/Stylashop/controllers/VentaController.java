package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.VentaDTO;
import com.devsoft.Stylashop.services.VentaService;
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
public class VentaController {
    @Autowired
    private VentaService ventaService;

    @GetMapping("/ventas")
    public ResponseEntity<?> getAll() {
        List<VentaDTO> ventas = ventaService.findAll();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/ventas/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        VentaDTO venta = ventaService.findById(id);
        if (venta == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "La venta con ID: " + id + " no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(venta);
    }

    @PostMapping("/ventas")
    public ResponseEntity<?> save(@RequestBody VentaDTO dto) {
        try {
            VentaDTO persisted = ventaService.save(dto);
            return new ResponseEntity<>(persisted, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al guardar la venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/ventas/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        VentaDTO actual = ventaService.findById(id);
        if (actual == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No existe una venta con el ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            ventaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al eliminar la venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

