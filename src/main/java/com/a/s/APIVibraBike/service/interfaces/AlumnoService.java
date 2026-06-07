package com.a.s.APIVibraBike.service.interfaces;

import com.a.s.APIVibraBike.model.dto.AlumnoRequestDTO;
import com.a.s.APIVibraBike.model.dto.AlumnoResponseDTO;

import java.util.List;

public interface AlumnoService {

    AlumnoResponseDTO registrarAlumno(
            AlumnoRequestDTO request
    );

    AlumnoResponseDTO obtenerAlumno(
            Long id
    );

    List<AlumnoResponseDTO> listarAlumnos();

}
