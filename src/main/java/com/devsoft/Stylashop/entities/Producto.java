package com.devsoft.Stylashop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "productos", schema = "public", catalog = "StylaShop")
public class Producto implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "nombre", nullable = false, length = 50)
        private String nombre;
        @Column(name = "descripcion", nullable = false, length = 100)
        private String descripcion;
        @Column(name = "precio_unitario", nullable = false, precision = 8, scale = 2)
        private BigDecimal precioUnitario;
        @Column(name = "url_imagen", nullable = true, length = 250)
        private String urlImagen;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
        private Categoria categoria;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "marca_id", referencedColumnName = "id", nullable = false)
        private Marca marca;
    }