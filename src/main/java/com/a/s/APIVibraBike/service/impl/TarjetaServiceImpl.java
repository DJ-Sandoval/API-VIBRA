package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.TarjetaRequestDTO;
import com.a.s.APIVibraBike.model.dto.TarjetaResponseDTO;
import com.a.s.APIVibraBike.model.entity.Tarjeta;
import com.a.s.APIVibraBike.model.entity.Usuario;
import com.a.s.APIVibraBike.model.enums.Plan;
import com.a.s.APIVibraBike.repository.TarjetaRepository;
import com.a.s.APIVibraBike.repository.UsuarioRepository;
import com.a.s.APIVibraBike.service.exception.ResourceNotFoundException;
import com.a.s.APIVibraBike.service.exception.UsuarioDuplicadoException;
import com.a.s.APIVibraBike.service.interfaces.TarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TarjetaServiceImpl implements TarjetaService {

    private final TarjetaRepository tarjetaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PDFService pdfService;

    @Override
    public TarjetaResponseDTO crearTarjeta(TarjetaRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (tarjetaRepository.existsByUsuarioId(usuario.getId())) {
            throw new UsuarioDuplicadoException("El usuario ya tiene una tarjeta asociada");
        }

        int clasesIniciales = obtenerClasesPorPlan(usuario.getPlan());

        Tarjeta tarjeta = Tarjeta.builder()
                .usuario(usuario)
                .contadorClases(clasesIniciales)
                .fechaCreacion(LocalDate.now())
                .build();

        Tarjeta saved = tarjetaRepository.save(tarjeta);
        return mapToResponseDTO(saved);
    }

    private int obtenerClasesPorPlan(Plan plan) {
        return switch (plan) {
            case MENSUAL -> 26;
            case SEMANAL -> 6;
            case VISITA -> 1;
            case DOCE_DIAS -> 12;
        };
    }

    @Override
    public TarjetaResponseDTO obtenerPorUsuario(Long usuarioId) {
        Tarjeta tarjeta = tarjetaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró tarjeta para el usuario"));
        return mapToResponseDTO(tarjeta);
    }

    @Override
    public List<TarjetaResponseDTO> listarTodas() {
        return tarjetaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public TarjetaResponseDTO actualizarTarjeta(Long id, TarjetaRequestDTO request) {
        // Por ahora solo actualizamos el contador si se requiere en el futuro
        // Puedes extender este método según necesites
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarjeta no encontrada"));

        // Ejemplo: permitir actualizar contador manualmente
        if (request.getUsuarioId() != null) {
            // lógica de cambio de usuario si se requiere
        }
        return mapToResponseDTO(tarjetaRepository.save(tarjeta));
    }

    @Override
    public void eliminarTarjeta(Long id) {
        if (!tarjetaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarjeta no encontrada");
        }
        tarjetaRepository.deleteById(id);
    }

    @Override
    public void descontarClase(Long tarjetaId) {
        Tarjeta tarjeta = tarjetaRepository.findById(tarjetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarjeta no encontrada"));
        tarjeta.descontarClase();
        tarjetaRepository.save(tarjeta);
    }

    @Override
    public byte[] generarPDFTarjeta(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return pdfService.generarTarjetaDigital(usuario);   // Inyecta PDFService
    }

    private TarjetaResponseDTO mapToResponseDTO(Tarjeta tarjeta) {
        Usuario u = tarjeta.getUsuario();
        return TarjetaResponseDTO.builder()
                .id(tarjeta.getId())
                .usuarioId(u.getId())
                .nombre(u.getNombre())
                .telefono(u.getTelefono())
                .fechaCumple(u.getFechaCumple())
                .fechaInicio(u.getFechaInicio())
                .plan(u.getPlan())
                .contadorClases(tarjeta.getContadorClases())
                .fechaCreacion(tarjeta.getFechaCreacion())
                .build();
    }
}
