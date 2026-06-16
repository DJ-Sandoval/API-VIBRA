package com.a.s.APIVibraBike.model.enums;
import java.time.LocalTime;

public enum Horario {
    // Horarios matutinos
    PRIMER_CLASE(LocalTime.of(6, 0), LocalTime.of(6, 59, 59)),
    SEGUNDA_CLASE(LocalTime.of(7, 0), LocalTime.of(7, 59, 59)),
    TERCERA_CLASE(LocalTime.of(8, 0), LocalTime.of(9, 0, 59)),
    // Horarios vespertinos
    CUARTA_CLASE(LocalTime.of(18, 0), LocalTime.of(18, 59, 59)),
    QUINTA_CLASE(LocalTime.of(19, 0), LocalTime.of(19, 59, 59)),
    SEXTA_CLASE(LocalTime.of(20, 0), LocalTime.of(21, 0, 59));

    private final LocalTime horaInicio;
    private final LocalTime horaFin;

    Horario(LocalTime horaInicio, LocalTime horaFin) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public static Horario determinarPorHora(LocalTime hora) {
        for (Horario h : values()) {
            if (!hora.isBefore(h.horaInicio) && !hora.isAfter(h.horaFin)) {
                return h;
            }
        }
        // Si llega fuera de los horarios establecidos, podemos lanzar una excepción
        // o mapearlo a un estado "FUERA_DE_HORARIO" si el negocio lo permite.
        throw new IllegalArgumentException("La hora actual (" + hora + ") está fuera de los rangos de clase permitidos.");
    }
}
