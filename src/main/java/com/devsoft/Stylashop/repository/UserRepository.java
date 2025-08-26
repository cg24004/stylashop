package com.devsoft.Stylashop.repository;

import com.devsoft.Stylashop.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    //metodos para verificar en el controlador que no se registre un usuario con el mismi username y nombre

    boolean existsByNombre(String nombre);


    boolean existsByUsername(String username);
}