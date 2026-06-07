package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.AlumnoRequestDTO;
import com.a.s.APIVibraBike.model.dto.AlumnoResponseDTO;
import com.a.s.APIVibraBike.model.entity.Alumno;
import com.a.s.APIVibraBike.repository.AlumnoRepository;
import com.a.s.APIVibraBike.service.interfaces.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Override
    public AlumnoResponseDTO registrarAlumno(
            AlumnoRequestDTO request
    ) {

        if(alumnoRepository.existsByMatricula(
                request.getMatricula())) {

            throw new RuntimeException(
                    "La matrícula ya existe");
        }

        Alumno alumno = new Alumno();

        alumno.setNombre(request.getNombre());
        alumno.setApellido(request.getApellido());
        alumno.setCorreo(request.getCorreo());
        alumno.setMatricula(request.getMatricula());
        alumno.setQrUuid(UUID.randomUUID().toString());
        alumno.setFechaCreacion(LocalDateTime.now());

        alumno = alumnoRepository.save(alumno);

        return mapToDto(alumno);
    }

    @Override
    public AlumnoResponseDTO obtenerAlumno(Long id) {

        Alumno alumno = alumnoRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Alumno no encontrado")
                );

        return mapToDto(alumno);
    }

    @Override
    public List<AlumnoResponseDTO> listarAlumnos() {

        return alumnoRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private AlumnoResponseDTO mapToDto(
            Alumno alumno
    ) {

        return AlumnoResponseDTO.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .correo(alumno.getCorreo())
                .matricula(alumno.getMatricula())
                .qrUuid(alumno.getQrUuid())
                .qrUrl("/api/v1/alumnos/" + alumno.getId() + "/qr")
                .fechaCreacion(alumno.getFechaCreacion())
                .build();
    }
}
