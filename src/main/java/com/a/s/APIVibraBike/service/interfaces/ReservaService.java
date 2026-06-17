package com.a.s.APIVibraBike.service.interfaces;

import com.a.s.APIVibraBike.model.dto.DisponibilidadResponseDTO;
import com.a.s.APIVibraBike.model.dto.ReservaRequestDTO;
import com.a.s.APIVibraBike.model.dto.ReservaResponseDTO;
import com.a.s.APIVibraBike.model.enums.HorarioReserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {

    /**
     * Crea una reserva para un usuario en el slot fecha+horario con la bici elegida.
     * Valida: horario habilitado ese día, bici no ocupada, usuario sin reserva previa en ese slot.
     */
    ReservaResponseDTO crearReserva(ReservaRequestDTO request);

    /**
     * Lista todos los asistentes reservados para un día completo,
     * ordenados por hora de creación (orden de llegada).
     */
    List<ReservaResponseDTO> listarPorFecha(LocalDate fecha);

    /**
     * Lista los asistentes de un slot concreto (fecha + horario).
     * Útil para imprimir la lista de asistentes a una clase específica.
     */
    List<ReservaResponseDTO> listarPorFechaYHorario(LocalDate fecha, HorarioReserva horario);

    /**
     * Devuelve qué bicis están ocupadas y cuáles libres en un slot.
     */
    DisponibilidadResponseDTO consultarDisponibilidad(LocalDate fecha, HorarioReserva horario);

    /**
     * Devuelve el historial de reservas de un usuario.
     */
    List<ReservaResponseDTO> listarPorUsuario(Long usuarioId);

    /**
     * Cancela la reserva de un usuario en un slot determinado.
     */
    void cancelarReserva(Long usuarioId, LocalDate fecha, HorarioReserva horario);

    /**
     * Devuelve los horarios habilitados para una fecha concreta,
     * incluyendo cuántas bicis quedan disponibles en cada uno.
     */
    List<DisponibilidadResponseDTO> horariosDisponiblesPorFecha(LocalDate fecha);
}
