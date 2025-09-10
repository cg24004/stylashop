package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.entities.Pago;
import com.devsoft.Stylashop.services.PagoService;
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
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // 🔹 Listar todos los pagos
    @GetMapping("/pagos")
    public ResponseEntity<?> getAll() {
        List<Pago> pagos = pagoService.getAll();
        return ResponseEntity.ok(pagos);
    }

    // 🔹 Buscar pago por ID
    @GetMapping("/pagos/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Pago pago = pagoService.findById(id);
            if (pago == null) {
                response.put("message", "El pago con ID: " + id + " no existe en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(pago, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar la consulta a la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 🔹 Registrar un pago
    @PostMapping("/pagos")
    public ResponseEntity<?> save(@RequestBody Pago pago) {
        Map<String, Object> response = new HashMap<>();
        try {
            Pago pagoPersisted = pagoService.registrarPago(pago);
            response.put("message", "Pago registrado correctamente");
            response.put("pago", pagoPersisted);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            response.put("message", "Error al insertar el registro, intente de nuevo");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
