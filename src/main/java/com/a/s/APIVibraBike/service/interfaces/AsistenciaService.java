package com.a.s.APIVibraBike.service.interfaces;


import com.a.s.APIVibraBike.model.dto.AsistenciaRequestDTO;

public interface AsistenciaService {

    void registrarAsistencia(
            AsistenciaRequestDTO request
    );

}
