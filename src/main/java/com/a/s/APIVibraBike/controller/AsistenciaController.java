package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.AsistenciaRequestDTO;
import com.a.s.APIVibraBike.model.dto.AsistenciaResponseDTO;
import com.a.s.APIVibraBike.model.enums.Horario;
import com.a.s.APIVibraBike.service.interfaces.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    // 1. Registrar Asistencia
    @PostMapping("/registrar")
    public ResponseEntity<AsistenciaResponseDTO> registrarAsistencia(@Valid @RequestBody AsistenciaRequestDTO request) {
        return new ResponseEntity<>(asistenciaService.registrarAsistencia(request), HttpStatus.CREATED);
    }

    // 2. Listar asistencias por día generales (Conforme se van registrando)
    @GetMapping("/dia")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorDia(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        // Si no mandan fecha, por defecto traemos lo de hoy
        LocalDate fechaBusqueda = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(asistenciaService.listarAsistenciasPorDia(fechaBusqueda));
    }

    // 3, 4 y 5. Listar usuarios por bloques de horarios específicos
    // URL ejemplo: GET /api/v1/asistencias/horario/PRIMER_HORARIO
    @GetMapping("/horario/{horario}")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorHorario(
            @PathVariable Horario horario,
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        LocalDate fechaBusqueda = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(asistenciaService.listarAsistenciasPorHorario(fechaBusqueda, horario));
    }
}
