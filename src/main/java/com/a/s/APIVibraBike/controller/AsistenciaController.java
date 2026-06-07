package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.AsistenciaRequestDTO;
import com.a.s.APIVibraBike.service.interfaces.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping
    public ResponseEntity<Void> registrar(
            @RequestBody
            @Valid
            AsistenciaRequestDTO request
    ) {

        asistenciaService
                .registrarAsistencia(request);

        return ResponseEntity.ok().build();
    }
}
