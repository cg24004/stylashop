package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.VentaDTO;
import com.devsoft.Stylashop.interfaces.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private IVentaService ventaService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            VentaDTO dto = ventaService.findById(id);
            if (dto == null) {
                res.put("message", "La venta con ID " + id + " no existe");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al consultar la venta");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Filtro opcional por rango de fechas
    @GetMapping("/rango")
    public ResponseEntity<?> getByDateRange(@RequestParam LocalDate desde,
                                            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(ventaService.findByFechaBetween(desde, hasta));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody VentaDTO dto) {
        Map<String, Object> res = new HashMap<>();
        try {
            VentaDTO created = ventaService.registerOrUpdate(dto); // el servicio maneja detalleVenta
            res.put("message", "Venta creada con éxito");
            res.put("venta", created);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            res.put("message", "Error al crear la venta");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody VentaDTO dto) {
        Map<String, Object> res = new HashMap<>();
        VentaDTO current = ventaService.findById(id);
        if (current == null) {
            res.put("message", "No se puede editar: la venta con ID " + id + " no existe");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            dto.setId(id);
            VentaDTO updated = ventaService.registerOrUpdate(dto);
            res.put("message", "Venta actualizada con éxito");
            res.put("venta", updated);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (DataAccessException e) {
            res.put("message", "Error al actualizar la venta");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        VentaDTO current = ventaService.findById(id);
        if (current == null) {
            res.put("message", "No existe la venta con ID " + id);
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            ventaService.delete(id);
            res.put("message", "Venta eliminada con éxito");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al eliminar la venta");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
