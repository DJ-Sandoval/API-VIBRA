package com.a.s.APIVibraBike.service.interfaces;


import com.a.s.APIVibraBike.model.dto.AsistenciaRequestDTO;
import com.a.s.APIVibraBike.model.dto.AsistenciaResponseDTO;
import com.a.s.APIVibraBike.model.enums.Horario;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaService {
    AsistenciaResponseDTO registrarAsistencia(AsistenciaRequestDTO request);
    List<AsistenciaResponseDTO> listarAsistenciasPorDia(LocalDate fecha);
    List<AsistenciaResponseDTO> listarAsistenciasPorHorario(LocalDate fecha, Horario horario);
}
