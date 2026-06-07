package com.a.s.APIVibraBike.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AlumnoResponseDTO {

    private Long id;

    private String nombre;

    private String apellido;

    private String correo;

    private String matricula;

    private String qrUuid;

    private String qrUrl;

    private LocalDateTime fechaCreacion;

}
