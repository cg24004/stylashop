package entities;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;
    @Column(name = "telefono", nullable = true, length = 12)
    private String telefono;
    @Column(name = "email", nullable = true, length = 50)
    private String email;
}
