package com.devsoft.Stylashop.repository;

import com.devsoft.Stylashop.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Id(Long categoriaId);
    List<Producto> findByMarca_Id(Long marcaId);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
