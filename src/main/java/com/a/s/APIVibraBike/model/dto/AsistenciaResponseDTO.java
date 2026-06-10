package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.Plan;
import com.a.s.APIVibraBike.model.enums.Horario;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaResponseDTO {
    private Long idAsistencia;
    private Long usuarioId;
    private String nombreUsuario;
    private String telefono;
    private LocalDate cumple;
    private Plan plan;
    private String folio;
    private LocalTime horaRegistro;
    private Horario horarioClasificado;
    private Integer clasesRestantes; // Para feedback visual inmediato del descuento
}
