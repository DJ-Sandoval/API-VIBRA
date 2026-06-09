package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.TarjetaRequestDTO;
import com.a.s.APIVibraBike.model.dto.TarjetaResponseDTO;
import com.a.s.APIVibraBike.service.interfaces.TarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarjetas")
@RequiredArgsConstructor
public class TarjetaController {

    private final TarjetaService tarjetaService;

    @PostMapping
    public ResponseEntity<TarjetaResponseDTO> crear(@RequestBody TarjetaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tarjetaService.crearTarjeta(request));
    }

    @GetMapping
    public ResponseEntity<List<TarjetaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(tarjetaService.listarTodas());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<TarjetaResponseDTO> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tarjetaService.obtenerPorUsuario(usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarjetaResponseDTO> actualizar(@PathVariable Long id, @RequestBody TarjetaRequestDTO request) {
        return ResponseEntity.ok(tarjetaService.actualizarTarjeta(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tarjetaService.eliminarTarjeta(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint útil para registrar asistencia
    @PatchMapping("/{id}/descontar")
    public ResponseEntity<Void> descontarClase(@PathVariable Long id) {
        tarjetaService.descontarClase(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pdf/{usuarioId}")
    public ResponseEntity<byte[]> descargarTarjetaPDF(@PathVariable Long usuarioId) {
        byte[] pdfBytes = tarjetaService.generarPDFTarjeta(usuarioId);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"tarjeta_" + usuarioId + ".pdf\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
