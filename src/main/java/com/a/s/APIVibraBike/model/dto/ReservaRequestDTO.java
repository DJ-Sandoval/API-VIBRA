package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import com.a.s.APIVibraBike.model.enums.MetodoPago;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La fecha de la clase es obligatoria")
    @Future(message = "Solo se pueden reservar clases en fechas futuras")
    private LocalDate fechaClase;

    @NotNull(message = "El horario es obligatorio")
    private HorarioReserva horario;

    @NotNull(message = "El número de bici es obligatorio")
    @Min(value = 1, message = "El número de bici mínimo es 1")
    @Max(value = 31, message = "El número de bici máximo es 30")
    private Integer numeroBici;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;
}

