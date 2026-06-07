package com.a.s.APIVibraBike.repository;

import com.a.s.APIVibraBike.model.entity.Alumno;
import com.a.s.APIVibraBike.model.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByAlumnoId(Long alumnoId);

    boolean existsByAlumnoAndFechaHoraAfter(
            Alumno alumno,
            LocalDateTime fechaHora
    );

}
