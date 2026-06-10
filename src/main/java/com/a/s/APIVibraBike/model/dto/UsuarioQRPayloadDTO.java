package com.a.s.APIVibraBike.model.dto;

import com.a.s.APIVibraBike.model.enums.Plan;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioQRPayloadDTO {
    private String qrUuid;
    private Long usuarioId;
    private String nombreCompleto;
    private String folio;
    private Plan plan;
}
