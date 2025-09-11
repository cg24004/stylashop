package com.devsoft.Stylashop.repository;

import com.devsoft.Stylashop.dto.IngresoDTO;
import com.devsoft.Stylashop.dto.MetodoPagoDTO;
import com.devsoft.Stylashop.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query("SELECT new com.devsoft.Stylashop.dto.IngresoDTO(" +
            "v.id, v.correlativo, p.fechaPago, v.total, c.nombre, u.username, SUM(p.monto)) " +
            "FROM Pago p " +
            "JOIN p.venta v " +
            "JOIN v.cliente c " +
            "JOIN v.usuario u " +
            "WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY v.id, v.correlativo, p.fechaPago, v.total, c.nombre, u.username " +
            "ORDER BY p.fechaPago ASC")
    List<IngresoDTO> obtenerIngresosRangoFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);



    @Query("SELECT new com.devsoft.Stylashop.dto.MetodoPagoDTO(" +
            "p.metodo, COUNT(p), SUM(p.monto)) " +  // Cambiado a "metodo"
            "FROM Pago p " +
            "WHERE p.fechaPago BETWEEN :inicio AND :fin " +
            "GROUP BY p.metodo")  // Cambiado a "metodo"
    List<MetodoPagoDTO> obtenerResumenPorMetodoPago(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin);
}
