package com.a.s.APIVibraBike.model.entity;
import com.a.s.APIVibraBike.model.enums.Plan;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_folio", columnNames = "folio"),
        @UniqueConstraint(name = "uk_telefono", columnNames = "telefono"),
        @UniqueConstraint(name = "uk_qr_uuid", columnNames = "qr_uuid")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"id"})
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 20, unique = true)
    private String telefono;

    @Column(name = "fecha_cumple")
    private LocalDate fechaCumple;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Plan plan;

    @Column(nullable = false, unique = true, length = 30)
    private String folio;

    @Column(name = "qr_uuid", nullable = false, unique = true, length = 36)
    private String qrUuid;

    //@Column(name = "qr_url", length = 255)
    //private String qrUrl;
}
