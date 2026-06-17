package com.a.s.APIVibraBike.model.entity;

import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import com.a.s.APIVibraBike.model.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "reservas",
        uniqueConstraints = {
                // Un usuario no puede reservar dos veces el mismo día/horario
                @UniqueConstraint(
                        name = "uk_usuario_fecha_horario",
                        columnNames = {"usuario_id", "fecha_clase", "horario"}
                ),
                // Una bici no puede estar reservada dos veces en la misma fecha/horario
                @UniqueConstraint(
                        name = "uk_bici_fecha_horario",
                        columnNames = {"numero_bici", "fecha_clase", "horario"}
                )
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Quién reserva ─────────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // ── Cuándo y en qué clase ─────────────────────────────────────────────────
    @Column(name = "fecha_clase", nullable = false)
    private LocalDate fechaClase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HorarioReserva horario;

    // ── Qué bici ──────────────────────────────────────────────────────────────
    /** Número de bici: 1–30. */
    @Column(name = "numero_bici", nullable = false)
    private Integer numeroBici;

    // ── Pago ──────────────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    // ── Auditoría ─────────────────────────────────────────────────────────────
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    private void prePersist() {
        this.creadoEn = LocalDateTime.now();
    }

}
