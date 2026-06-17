package com.a.s.APIVibraBike.model.enums;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * Horarios disponibles para reservas.
 * Lunes–Jueves : todos los horarios (PRIMER_CLASE … SEXTA_CLASE)
 * Viernes       : matutinos (PRIMER–TERCERA) + CUARTA_CLASE vespertino
 * Sábado        : solo TERCERA_CLASE (8:00–9:00)
 */
public enum HorarioReserva {

    // ── Matutinos ──────────────────────────────────────────────────────────────
    PRIMER_CLASE("6:00 – 6:59",
            LocalTime.of(6, 0), LocalTime.of(6, 59, 59),
            List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),

    SEGUNDA_CLASE("7:00 – 7:59",
            LocalTime.of(7, 0), LocalTime.of(7, 59, 59),
            List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),

    TERCERA_CLASE("8:00 – 9:00",
            LocalTime.of(8, 0), LocalTime.of(9, 0, 59),
            List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)),

    // ── Vespertinos ────────────────────────────────────────────────────────────
    CUARTA_CLASE("18:00 – 18:59",
            LocalTime.of(18, 0), LocalTime.of(18, 59, 59),
            List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),

    QUINTA_CLASE("19:00 – 19:59",
            LocalTime.of(19, 0), LocalTime.of(19, 59, 59),
            List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY)),

    SEXTA_CLASE("20:00 – 21:00",
            LocalTime.of(20, 0), LocalTime.of(21, 0, 59),
            List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));

    // ──────────────────────────────────────────────────────────────────────────

    private final String etiqueta;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    /** Días de la semana en los que este horario está habilitado. */
    private final List<DayOfWeek> diasDisponibles;

    HorarioReserva(String etiqueta, LocalTime horaInicio,
                   LocalTime horaFin, List<DayOfWeek> diasDisponibles) {
        this.etiqueta        = etiqueta;
        this.horaInicio      = horaInicio;
        this.horaFin         = horaFin;
        this.diasDisponibles = diasDisponibles;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String getEtiqueta()              { return etiqueta; }
    public LocalTime getHoraInicio()         { return horaInicio; }
    public LocalTime getHoraFin()            { return horaFin; }
    public List<DayOfWeek> getDiasDisponibles() { return diasDisponibles; }

    /** Devuelve true si este horario opera el día indicado. */
    public boolean disponibleEl(DayOfWeek dia) {
        return diasDisponibles.contains(dia);
    }

    /**
     * Devuelve los horarios habilitados para un día concreto de la semana.
     * Domingo nunca tiene clases → lista vacía.
     */
    public static List<HorarioReserva> disponiblesParaDia(DayOfWeek dia) {
        return Arrays.stream(values())
                .filter(h -> h.disponibleEl(dia))
                .toList();
    }
}

