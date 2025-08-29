package com.devsoft.Stylashop.security;


import com.devsoft.Stylashop.auth.dto.JwtResponse;
import com.devsoft.Stylashop.auth.dto.LoginDTO;
import com.devsoft.Stylashop.auth.dto.RegisterDTO;
import com.devsoft.Stylashop.entities.Role;
import com.devsoft.Stylashop.entities.Usuario;
import com.devsoft.Stylashop.repository.RoleRepository;
import com.devsoft.Stylashop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AuthService {
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        //metodo para registro de usuario
        public JwtResponse register(RegisterDTO registerDTO){
            Role role = roleRepository.findByNombre(registerDTO.getRole().getNombre())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            //creamos una instancia (objeto) de Usuario
            Usuario user = new Usuario();
            user.setUsername(registerDTO.getUsername());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setRole(role);
            //guardamos el usuario
            userRepository.save(user);
            String token = jwtService.generateToken(
                    User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .build()
            );
            return new JwtResponse(token);
        }

        //metodo para autenticacion de usuario
        public JwtResponse authenticate(LoginDTO dto){
            Usuario user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
            if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
                throw  new RuntimeException("Credenciales inválidas");
            }
            String token = jwtService.generateToken(
                    User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .roles(user.getRole().getNombre())
                            .build()
            );
            return new JwtResponse(token);
        }

    }