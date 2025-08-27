package com.devsoft.Stylashop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes", schema = "public", catalog = "StylaShop")
public class Cliente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Column(name = "email", nullable = true, length = 100)
    private String email;
    @Column(name = "telefono", nullable = true, length = 10)
    private String telefono;
    @Column(name = "tipo_cliente", nullable = false, length = 100)
    private String tipoCliente;
}
