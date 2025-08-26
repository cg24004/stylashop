package com.devsoft.Stylashop.repository;

import com.devsoft.Stylashop.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaBetween(LocalDate desde, LocalDate hasta);
    List<Venta> findByCliente_Id(Long clienteId);
    List<Venta> findByUsuario_Id(Long usuarioId);
}
