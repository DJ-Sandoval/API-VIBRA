package com.a.s.APIVibraBike.repository;

import com.a.s.APIVibraBike.model.entity.Asistencia;
import com.a.s.APIVibraBike.model.enums.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    // Para la regla de negocio de máximo 2 clases por día
    long countByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);

    // Para listar asistencias del día general (Ordenadas por llegada)
    List<Asistencia> findByFechaOrderByHoraDesc(LocalDate fecha);

    // Para segmentar los listados de horarios del día
    List<Asistencia> findByFechaAndHorarioOrderByHoraAsc(LocalDate fecha, Horario horario);
}
