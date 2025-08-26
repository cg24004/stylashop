package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.ClienteDTO;
import com.devsoft.Stylashop.interfaces.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            ClienteDTO dto = clienteService.findById(id);
            if (dto == null) {
                res.put("message", "El cliente con ID " + id + " no existe");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al consultar el cliente");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClienteDTO dto) {
        Map<String, Object> res = new HashMap<>();
        try {
            ClienteDTO created = clienteService.registerOrUpdate(dto);
            res.put("message", "Cliente creado con éxito");
            res.put("cliente", created);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            res.put("message", "Error al crear el cliente");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        Map<String, Object> res = new HashMap<>();
        ClienteDTO current = clienteService.findById(id);
        if (current == null) {
            res.put("message", "No se puede editar: el cliente con ID " + id + " no existe");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            dto.setId(id);
            ClienteDTO updated = clienteService.registerOrUpdate(dto);
            res.put("message", "Cliente actualizado con éxito");
            res.put("cliente", updated);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (DataAccessException e) {
            res.put("message", "Error al actualizar el cliente");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        ClienteDTO current = clienteService.findById(id);
        if (current == null) {
            res.put("message", "No existe el cliente con ID " + id);
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        try {
            clienteService.delete(id);
            res.put("message", "Cliente eliminado con éxito");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (DataAccessException e) {
            res.put("message", "Error al eliminar el cliente");
            res.put("error", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
