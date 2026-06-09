package com.a.s.APIVibraBike.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsistenciaRequestDTO {
    @NotNull(message = "El ID de usuario es requerido para registrar asistencia")
    private Long usuarioId;
}
