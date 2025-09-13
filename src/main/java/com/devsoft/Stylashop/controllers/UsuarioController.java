package com.devsoft.Stylashop.controllers;

import com.devsoft.Stylashop.dto.RoleDTO;
import com.devsoft.Stylashop.dto.UsuarioDTO;
import com.devsoft.Stylashop.dto.UsuarioResponseDTO;
import com.devsoft.Stylashop.entities.Role;
import com.devsoft.Stylashop.entities.Usuario;
import com.devsoft.Stylashop.repository.RoleRepository;
import com.devsoft.Stylashop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Converter de Usuario a UsuarioResponseDTO
     */
    private UsuarioResponseDTO convertToResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .role(RoleDTO.builder()
                        .id(usuario.getRole().getId())
                        .nombre(usuario.getRole().getNombre())
                        .build())
                .build();
    }

    /**
     * Listar todos los usuarios
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Usuario> usuarios = userRepository.findAll();

            // Convertir a DTOs para evitar problemas de serialización
            List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(usuariosDTO);
        } catch (Exception e) {
            response.put("message", "Error al obtener los usuarios");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuario = userRepository.findById(id);
            if (usuario.isPresent()) {
                UsuarioResponseDTO usuarioDTO = convertToResponseDTO(usuario.get());
                return ResponseEntity.ok(usuarioDTO);
            } else {
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Error al obtener el usuario");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar usuario existente
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> optionalUsuario = userRepository.findById(id);
            if (!optionalUsuario.isPresent()) {
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Usuario usuario = optionalUsuario.get();

            // Verificar si el nuevo username ya existe (excepto para el mismo usuario)
            if (!usuario.getUsername().equals(dto.getUsername()) &&
                    userRepository.existsByUsername(dto.getUsername())) {
                response.put("message", "Ya existe un usuario con este nombre de usuario");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Actualizar campos básicos
            usuario.setUsername(dto.getUsername());

            // Solo actualizar la contraseña si se proporciona una nueva
            if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            // Actualizar rol si se proporciona
            if (dto.getRoleDTO() != null && dto.getRoleDTO().getId() != null) {
                Optional<Role> role = roleRepository.findById(dto.getRoleDTO().getId());
                if (role.isPresent()) {
                    usuario.setRole(role.get());
                } else {
                    response.put("message", "Rol no encontrado con ID: " + dto.getRoleDTO().getId());
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }

            // Guardar cambios
            Usuario updatedUsuario = userRepository.save(usuario);
            UsuarioResponseDTO usuarioResponseDTO = convertToResponseDTO(updatedUsuario);

            response.put("message", "Usuario actualizado correctamente");
            response.put("usuario", usuarioResponseDTO);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put("message", "Error de base de datos al actualizar el usuario");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error inesperado al actualizar el usuario");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Eliminar usuario
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> usuario = userRepository.findById(id);
            if (!usuario.isPresent()) {
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            userRepository.deleteById(id);
            response.put("message", "Usuario eliminado correctamente");
            response.put("deletedUserId", id);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put("message", "Error de base de datos al eliminar el usuario");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error inesperado al eliminar el usuario");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtener todos los roles disponibles
     * GET /api/usuarios/roles
     */
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Role> roles = roleRepository.findAll();

            // Convertir a DTOs
            List<RoleDTO> rolesDTO = roles.stream()
                    .map(role -> RoleDTO.builder()
                            .id(role.getId())
                            .nombre(role.getNombre())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(rolesDTO);
        } catch (Exception e) {
            response.put("message", "Error al obtener los roles");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}