package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.Plan;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaResponseDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String telefono;
    private LocalDate fechaCumple;
    private LocalDate fechaInicio;
    private Plan plan;
    private Integer contadorClases;
    private LocalDate fechaCreacion;
}
