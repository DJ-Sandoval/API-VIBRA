package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.UsuarioRequestDTO;
import com.a.s.APIVibraBike.model.dto.UsuarioResponseDTO;
import com.a.s.APIVibraBike.repository.UsuarioRepository;
import com.a.s.APIVibraBike.service.impl.QRService;
import com.a.s.APIVibraBike.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Ajustar según entorno
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final QRService qrService;

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable)
    {
        Page<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/qr")
    public ResponseEntity<byte[]> obtenerQR(@PathVariable Long id) {
        // Nota: Asegúrate de que tu interfaz UsuarioService tenga u obtenga el usuario por ID
        // Si no lo tienes, puedes inyectar el repositorio temporalmente o usar tu flujo existente.
        UsuarioResponseDTO usuario = usuarioService.listarUsuarios(Pageable.unpaged())
                .stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        byte[] qrBytes = qrService.generarQR(usuario.getQrUuid(), 300, 300);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrBytes);
    }
}
