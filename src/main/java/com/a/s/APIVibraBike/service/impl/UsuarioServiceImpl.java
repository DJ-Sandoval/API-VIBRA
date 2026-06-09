package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.TarjetaRequestDTO;
import com.a.s.APIVibraBike.model.dto.UsuarioRequestDTO;
import com.a.s.APIVibraBike.model.dto.UsuarioResponseDTO;
import com.a.s.APIVibraBike.model.entity.Tarjeta;
import com.a.s.APIVibraBike.model.entity.Usuario;
import com.a.s.APIVibraBike.repository.TarjetaRepository;
import com.a.s.APIVibraBike.repository.UsuarioRepository;
import com.a.s.APIVibraBike.service.exception.UsuarioDuplicadoException;
import com.a.s.APIVibraBike.service.exception.UsuarioInvalidoException;
import com.a.s.APIVibraBike.service.interfaces.TarjetaService;
import com.a.s.APIVibraBike.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TarjetaService tarjetaService;
    private final TarjetaRepository tarjetaRepository;
    private final QRService qrService;


    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        validarRequest(request);

        // Verificar duplicados por teléfono
        if (usuarioRepository.existsByTelefono(request.getTelefono())) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con el teléfono: " + request.getTelefono());
        }
        String folio = generarFolioUnico();
        //String qrUuid = generarQrUuidUnico();

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .apellidos(request.getApellidos().trim())
                .telefono(request.getTelefono().trim())
                .fechaCumple(request.getFechaCumple())
                .fechaInicio(request.getFechaInicio())
                .plan(request.getPlan())
                .folio(folio)
                .qrUuid(UUID.randomUUID().toString())
                .build();

        Usuario saved = usuarioRepository.save(usuario);
        // Crear tarjeta digital en automatico
        TarjetaRequestDTO tarjetaRequest = TarjetaRequestDTO.builder()
                .usuarioId(saved.getId())
                .build();
        tarjetaService.crearTarjeta(tarjetaRequest);
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public Page<UsuarioResponseDTO> listarUsuarios(Pageable pageable) {
        Page<Usuario> usuariosPage = usuarioRepository.findAll(pageable);
        return usuariosPage.map(this::mapToResponseDTO);
    }

    @Override
    public UsuarioResponseDTO obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Uusario no encontrado"));
        return mapToResponseDTO(usuario);
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

    private String generarQrUuidUnico() {
        String qrUuid;
        do {
            qrUuid = UUID.randomUUID().toString();
        } while (usuarioRepository.existsByQrUuid(qrUuid)); // Necesitas agregar este método en el repo
        return qrUuid;
    }

    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
        Tarjeta tarjeta = tarjetaRepository.findByUsuarioId(usuario.getId()).orElse(null);
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellidos(usuario.getApellidos())
                .telefono(usuario.getTelefono())
                .fechaCumple(usuario.getFechaCumple())
                .fechaInicio(usuario.getFechaInicio())
                .plan(usuario.getPlan())
                .folio(usuario.getFolio())
                .tarjetaId(tarjeta != null ? tarjeta.getId() : null)
                .contadorClases(tarjeta != null ? tarjeta.getContadorClases() : null)
                .qrUuid(usuario.getQrUuid())
                .qrUrl("/api/v1/usuarios/" + usuario.getId() + "/qr")
                .build();
    }
}
