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
@CrossOrigin(origins = "*")
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<VentaDTO> ventas = ventaService.findAll();
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al obtener las ventas");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            VentaDTO venta = ventaService.findById(id);
            if (venta == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "La venta con ID: " + id + " no existe");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(venta);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al obtener la venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody VentaDTO dto) {
        try {
            System.out.println("Recibiendo venta: " + dto); // Para debug
            VentaDTO persisted = ventaService.save(dto);
            return new ResponseEntity<>(persisted, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Para debug
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al guardar la venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody VentaDTO dto) {
        try {
            VentaDTO updated = ventaService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al actualizar la venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            VentaDTO actual = ventaService.findById(id);
            if (actual == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No existe una venta con el ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ventaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al eliminar la venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
