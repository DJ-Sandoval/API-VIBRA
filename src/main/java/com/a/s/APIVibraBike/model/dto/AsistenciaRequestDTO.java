package com.a.s.APIVibraBike.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AsistenciaRequestDTO {

    @NotBlank
    private String qrUuid;

}
