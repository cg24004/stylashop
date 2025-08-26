package com.devsoft.Stylashop.repository;

import com.devsoft.Stylashop.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    List<Marca> findByNombreContainingIgnoreCase(String nombre);
}
