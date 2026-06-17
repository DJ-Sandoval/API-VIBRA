package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadResponseDTO {

    private LocalDate fechaClase;
    private HorarioReserva horario;
    private String etiquetaHorario;

    /** Bicis que ya están reservadas (ocupadas). */
    private List<Integer> bicisOcupadas;

    /** Bicis aún disponibles para elegir. */
    private List<Integer> bicisDisponibles;

    private int totalBicis;
    private int totalOcupadas;
    private int totalDisponibles;
}
