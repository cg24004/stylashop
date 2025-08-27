package com.devsoft.Stylashop.repository;

import com.devsoft.Stylashop.dto.ClienteDTO;
import com.devsoft.Stylashop.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByNombre(String nombre);
}
