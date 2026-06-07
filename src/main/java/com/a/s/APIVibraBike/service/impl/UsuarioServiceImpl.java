package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.UsuarioRequestDTO;
import com.a.s.APIVibraBike.model.dto.UsuarioResponseDTO;
import com.a.s.APIVibraBike.model.entity.Usuario;
import com.a.s.APIVibraBike.repository.UsuarioRepository;
import com.a.s.APIVibraBike.service.exception.UsuarioDuplicadoException;
import com.a.s.APIVibraBike.service.exception.UsuarioInvalidoException;
import com.a.s.APIVibraBike.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        validarRequest(request);

        // Verificar duplicados por teléfono
        if (usuarioRepository.existsByTelefono(request.getTelefono())) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con el teléfono: " + request.getTelefono());
        }

        String folio = generarFolioUnico();

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .apellidos(request.getApellidos().trim())
                .telefono(request.getTelefono().trim())
                .fechaCumple(request.getFechaCumple())
                .fechaInicio(request.getFechaInicio())
                .plan(request.getPlan())
                .folio(folio)
                .build();

        Usuario saved = usuarioRepository.save(usuario);

        return mapToResponseDTO(saved);
    }

    private void validarRequest(UsuarioRequestDTO request) {
        if (request.getFechaInicio() == null || request.getFechaInicio().isBefore(LocalDate.now().minusYears(5))) {
            throw new UsuarioInvalidoException("La fecha de inicio no puede ser muy antigua");
        }
        // Más validaciones si es necesario
    }

    private String generarFolioUnico() {
        String folio;
        do {
            folio = "AS-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (usuarioRepository.existsByFolio(folio));
        return folio;
    }

    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellidos(usuario.getApellidos())
                .telefono(usuario.getTelefono())
                .fechaCumple(usuario.getFechaCumple())
                .fechaInicio(usuario.getFechaInicio())
                .plan(usuario.getPlan())
                .folio(usuario.getFolio())
                .build();
    }
}
