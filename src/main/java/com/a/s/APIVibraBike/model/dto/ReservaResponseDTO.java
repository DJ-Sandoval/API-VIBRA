package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import com.a.s.APIVibraBike.model.enums.MetodoPago;
import com.a.s.APIVibraBike.model.enums.Plan;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {

    private Long idReserva;

    // ── Datos del usuario (lista de asistentes) ───────────────────────────────
    private Long usuarioId;
    private String nombre;        // nombre completo
    private String contacto;      // teléfono
    private LocalDate cumple;
    private Plan plan;
    private String folio;

    // ── Datos de la reserva ───────────────────────────────────────────────────
    private LocalDate fechaClase;
    private HorarioReserva horario;
    private String etiquetaHorario; // "8:00 – 9:00"
    private Integer numeroBici;
    private MetodoPago metodoPago;
    private LocalDateTime creadoEn;
}
