package com.a.s.APIVibraBike.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaRequestDTO {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
}
