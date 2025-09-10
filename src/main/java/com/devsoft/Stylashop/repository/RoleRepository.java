package com.devsoft.Stylashop.repository;

import com.devsoft.Stylashop.dto.IngresoDTO;
import com.devsoft.Stylashop.entities.Pago;
import com.devsoft.Stylashop.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(String nombre);

    interface PagoRepository extends JpaRepository<Pago, Long> {

        @Query("SELECT new com.devsoft.Stylashop.dto.IngresoDTO(" +
                "v.id, v.fecha, p.fechaPago, v.total, c.nombre, u.nombre, SUM(p.monto)) " +
                "FROM Pago p " +
                "JOIN p.venta v " +
                "JOIN v.cliente c " +
                "JOIN v.usuario u " +
                "WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin " +
                "GROUP BY v.id, v.fecha, p.fechaPago, v.total, c.nombre, u.nombre " +
                "ORDER BY p.fechaPago ASC")
        List<IngresoDTO> obtenerIngresosRangoFechas(
                @Param("fechaInicio") LocalDate fechaInicio,
                @Param("fechaFin") LocalDate fechaFin
        );
    }
}