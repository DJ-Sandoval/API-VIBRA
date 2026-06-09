package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.AlumnoRequestDTO;
import com.a.s.APIVibraBike.model.dto.AlumnoResponseDTO;
import com.a.s.APIVibraBike.model.entity.Alumno;
import com.a.s.APIVibraBike.repository.AlumnoRepository;
import com.a.s.APIVibraBike.service.impl.PDFService;
import com.a.s.APIVibraBike.service.impl.QRService;
import com.a.s.APIVibraBike.service.interfaces.AlumnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final AlumnoRepository alumnoRepository;
    private final QRService qrService;
    private final PDFService pdfService;

    @PostMapping
    public AlumnoResponseDTO registrar(
            @RequestBody
            @Valid
            AlumnoRequestDTO request
    ) {
        return alumnoService.registrarAlumno(request);
    }

    @GetMapping("/{id}")
    public AlumnoResponseDTO obtener(
            @PathVariable Long id
    ) {
        return alumnoService.obtenerAlumno(id);
    }

    @GetMapping
    public List<AlumnoResponseDTO> listar() {
        return alumnoService.listarAlumnos();
    }

    // Obtener imagen QR de un alumno
    @GetMapping("/{id}/qr")
    public ResponseEntity<byte[]> obtenerQR(@PathVariable Long id) {
        AlumnoResponseDTO alumno = alumnoService.obtenerAlumno(id);
        byte[] qrBytes = qrService.generarQR(alumno.getQrUuid(), 300, 300);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrBytes);
    }

    /*
    // Generar PDF con TARJETAS DIGITALES de todos los alumnos
    @GetMapping("/tarjetas-pdf")
    public ResponseEntity<byte[]> generarPDFTarjetasTodos() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        byte[] pdfBytes = pdfService.generarTarjetaIndividual(Alumno alumnos);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tarjetas_vibra.pdf")
                .body(pdfBytes);
    }
    */
     
    /*
    // Generar tarjeta individual para un alumno
    @GetMapping("/{id}/tarjeta")
    public ResponseEntity<byte[]> generarTarjetaIndividual(@PathVariable Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        byte[] pdfBytes = pdfService.generarTarjetaIndividual(alumno);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tarjeta_" + id + ".pdf")
                .body(pdfBytes);
        }
        */

    }

