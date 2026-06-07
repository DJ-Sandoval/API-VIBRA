package com.a.s.APIVibraBike.repository;

import com.a.s.APIVibraBike.model.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByQrUuid(String qrUuid);

    Optional<Alumno> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);

}
