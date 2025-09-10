package com.devsoft.Stylashop.entities;

import com.devsoft.Stylashop.utils.EstadoVenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ventas", schema = "public", catalog = "StylaShop")
public class Venta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correlativo", nullable = false, length = 80)
    private String correlativo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "estado", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;
}

