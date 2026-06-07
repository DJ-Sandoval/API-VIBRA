package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.AsistenciaRequestDTO;
import com.a.s.APIVibraBike.model.entity.Alumno;
import com.a.s.APIVibraBike.model.entity.Asistencia;
import com.a.s.APIVibraBike.repository.AlumnoRepository;
import com.a.s.APIVibraBike.repository.AsistenciaRepository;
import com.a.s.APIVibraBike.service.interfaces.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl
        implements AsistenciaService {

    private final AlumnoRepository alumnoRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Override
    @Transactional
    public void registrarAsistencia(
            AsistenciaRequestDTO request
    ) {

        Alumno alumno = alumnoRepository
                .findByQrUuid(request.getQrUuid())
                .orElseThrow(
                        () -> new RuntimeException(
                                "QR inválido")
                );

        boolean existe =
                asistenciaRepository
                        .existsByAlumnoAndFechaHoraAfter(
                                alumno,
                                LocalDateTime.now()
                                        .minusMinutes(5)
                        );

        if(existe) {
            throw new RuntimeException(
                    "Asistencia ya registrada");
        }

        Asistencia asistencia =
                new Asistencia();

        asistencia.setAlumno(alumno);
        asistencia.setFechaHora(
                LocalDateTime.now()
        );

        asistenciaRepository.save(asistencia);
    }
}
