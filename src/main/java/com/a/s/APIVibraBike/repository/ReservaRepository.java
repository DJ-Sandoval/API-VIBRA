package com.a.s.APIVibraBike.repository;

import com.a.s.APIVibraBike.model.entity.Reserva;
import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    /** Reservas de un día ordenadas por hora de creación. */
    List<Reserva> findByFechaClaseOrderByCreadoEnAsc(LocalDate fechaClase);

    /** Reservas de un día + horario específico (lista de asistentes a un slot). */
    List<Reserva> findByFechaClaseAndHorarioOrderByCreadoEnAsc(
            LocalDate fechaClase, HorarioReserva horario);

    /** Bicis ya tomadas en un slot determinado. */
    @Query("SELECT r.numeroBici FROM Reserva r " +
            "WHERE r.fechaClase = :fecha AND r.horario = :horario")
    List<Integer> findBicisOcupadasByFechaAndHorario(
            @Param("fecha") LocalDate fecha,
            @Param("horario") HorarioReserva horario);

    /** Verificar si una bici ya está reservada en ese slot. */
    boolean existsByFechaClaseAndHorarioAndNumeroBici(
            LocalDate fechaClase, HorarioReserva horario, Integer numeroBici);

    /** Verificar si el usuario ya tiene reserva en ese slot. */
    boolean existsByFechaClaseAndHorarioAndUsuarioId(
            LocalDate fechaClase, HorarioReserva horario, Long usuarioId);

    /** Reservas del usuario (historial). */
    List<Reserva> findByUsuarioIdOrderByFechaClaseDescCreadoEnDesc(Long usuarioId);

    /** Cancelar: buscar reserva puntual de un usuario en un slot. */
    Optional<Reserva> findByUsuarioIdAndFechaClaseAndHorario(
            Long usuarioId, LocalDate fechaClase, HorarioReserva horario);

}
