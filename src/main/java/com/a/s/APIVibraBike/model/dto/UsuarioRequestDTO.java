package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.Plan;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+\\s()-]{8,20}$", message = "Formato de teléfono inválido")
    private String telefono;

    private LocalDate fechaCumple;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "El plan es obligatorio")
    private Plan plan;
}
