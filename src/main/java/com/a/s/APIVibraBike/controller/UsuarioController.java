package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.UsuarioRequestDTO;
import com.a.s.APIVibraBike.model.dto.UsuarioResponseDTO;
import com.a.s.APIVibraBike.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Ajustar según entorno
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
