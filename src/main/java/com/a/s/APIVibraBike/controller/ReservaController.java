package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.DisponibilidadResponseDTO;
import com.a.s.APIVibraBike.model.dto.ReservaRequestDTO;
import com.a.s.APIVibraBike.model.dto.ReservaResponseDTO;
import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import com.a.s.APIVibraBike.service.interfaces.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReservaController {

    private final ReservaService reservaService;

    // 1. Crear una reserva (elegir bici + horario + método de pago)


    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @Valid ReservaRequestDTO request) {  // Quitamos @RequestBody
        return new ResponseEntity<>(reservaService.crearReserva(request), HttpStatus.CREATED);
    }

    // 2. Listar todos los asistentes/reservas de un día completo
    //    GET /api/v1/reservas/dia?fecha=2026-06-20
    @GetMapping("/dia")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorDia(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaBusqueda = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(reservaService.listarPorFecha(fechaBusqueda));
    }

    // 3. Listar asistentes de un horario específico (lista de asistentes a una clase)
    //    GET /api/v1/reservas/horario/TERCERA_CLASE?fecha=2026-06-20
    @GetMapping("/horario/{horario}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorHorario(
            @PathVariable HorarioReserva horario,
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaBusqueda = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(reservaService.listarPorFechaYHorario(fechaBusqueda, horario));
    }

    // 4. Consultar disponibilidad de bicis para un slot puntual
    //    GET /api/v1/reservas/disponibilidad?fecha=2026-06-20&horario=TERCERA_CLASE
    @GetMapping("/disponibilidad")
    public ResponseEntity<DisponibilidadResponseDTO> consultarDisponibilidad(
            @RequestParam("fecha")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("horario") HorarioReserva horario) {
        return ResponseEntity.ok(reservaService.consultarDisponibilidad(fecha, horario));
    }

    // 5. Consultar todos los horarios habilitados para una fecha, con su disponibilidad
    //    (útil para que el front pinte solo los horarios válidos según el día de la semana)
    //    GET /api/v1/reservas/disponibilidad/dia?fecha=2026-06-20
    @GetMapping("/disponibilidad/dia")
    public ResponseEntity<List<DisponibilidadResponseDTO>> horariosDisponiblesPorFecha(
            @RequestParam("fecha")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reservaService.horariosDisponiblesPorFecha(fecha));
    }

    // 6. Historial de reservas de un usuario
    //    GET /api/v1/reservas/usuario/15
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorUsuario(
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(reservaService.listarPorUsuario(usuarioId));
    }

    // 7. Cancelar una reserva puntual
    //    DELETE /api/v1/reservas?usuarioId=15&fecha=2026-06-20&horario=TERCERA_CLASE
    @DeleteMapping
    public ResponseEntity<Void> cancelarReserva(
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam("fecha")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("horario") HorarioReserva horario) {
        reservaService.cancelarReserva(usuarioId, fecha, horario);
        return ResponseEntity.noContent().build();
    }
}
