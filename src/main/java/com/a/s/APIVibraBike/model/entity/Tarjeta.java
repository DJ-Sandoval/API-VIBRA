package com.a.s.APIVibraBike.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "tarjetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "contador_clases", nullable = false)
    private Integer contadorClases;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    // Método helper
    public void descontarClase() {
        if (this.contadorClases > 0) {
            this.contadorClases--;
        }
    }
}
