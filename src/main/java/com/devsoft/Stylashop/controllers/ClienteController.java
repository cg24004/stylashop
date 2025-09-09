package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.ClienteDTO;
import com.devsoft.Stylashop.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar(){
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtener(@PathVariable Long id){
        ClienteDTO dto = clienteService.obtenerPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ClienteDTO dto){
        ClienteDTO creado = clienteService.crear(dto);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Cliente creado correctamente",
                "cliente", creado
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ClienteDTO dto){
        ClienteDTO act = clienteService.actualizar(id, dto);
        if (act == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of(
                "message", "Cliente actualizado correctamente",
                "cliente", act
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        clienteService.eliminar(id);
        return ResponseEntity.ok(Map.of("message", "Cliente eliminado correctamente"));
    }
}
