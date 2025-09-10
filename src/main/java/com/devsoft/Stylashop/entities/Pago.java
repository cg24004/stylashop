package com.devsoft.Stylashop.entities;

import com.devsoft.Stylashop.utils.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pagos", schema = "public", catalog = "orders")
public class Pago implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;
    @Column(name = "monto", precision = 10, scale = 2)
    private BigDecimal monto;
    @Enumerated(EnumType.STRING)
    private MetodoPago metodo; // EFECTIVO, TARJETA, TRANSFERENCIA
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
}
