package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private LocalDate fechaCumple;
    private LocalDate fechaInicio;
    private Plan plan;
    private String folio;
}
