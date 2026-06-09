package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.AsistenciaRequestDTO;
import com.a.s.APIVibraBike.model.dto.AsistenciaResponseDTO;
import com.a.s.APIVibraBike.model.entity.Asistencia;
import com.a.s.APIVibraBike.model.entity.Tarjeta;
import com.a.s.APIVibraBike.model.entity.Usuario;
import com.a.s.APIVibraBike.model.enums.Horario;
import com.a.s.APIVibraBike.repository.AsistenciaRepository;
import com.a.s.APIVibraBike.repository.TarjetaRepository;
import com.a.s.APIVibraBike.repository.UsuarioRepository;
import com.a.s.APIVibraBike.service.exception.LimiteAsistenciaExcedidoException;
import com.a.s.APIVibraBike.service.exception.ResourceNotFoundException;
import com.a.s.APIVibraBike.service.interfaces.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TarjetaRepository tarjetaRepository;

    @Override
    @Transactional
    public AsistenciaResponseDTO registrarAsistencia(AsistenciaRequestDTO request) {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        // 1. Validar existencia del usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.getUsuarioId()));

        // 2. Validar que tenga tarjeta y clases disponibles
        Tarjeta tarjeta = tarjetaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no cuenta con una tarjeta activa"));

        if (tarjeta.getContadorClases() <= 0) {
            throw new LimiteAsistenciaExcedidoException("El usuario ya no cuenta con clases disponibles en su plan actual.");
        }

        // 3. Validar regla de negocio: Máximo 2 asistencias por día
        long asistenciasHoy = asistenciaRepository.countByUsuarioIdAndFecha(usuario.getId(), hoy);
        if (asistenciasHoy >= 2) {
            throw new LimiteAsistenciaExcedidoException("Límite diario alcanzado. El usuario ya registró 2 clases el día de hoy.");
        }

        // 4. Determinar horario según la hora del servidor
        Horario horarioClasificado = Horario.determinarPorHora(ahora);

        // 5. Descontar clase de la tarjeta (aprovechando el método helper que ya creaste en tu entidad Tarjeta)
        tarjeta.descontarClase();
        tarjetaRepository.save(tarjeta);

        // 6. Guardar asistencia
        Asistencia asistencia = Asistencia.builder()
                .usuario(usuario)
                .fecha(hoy)
                .hora(ahora)
                .horario(horarioClasificado)
                .build();

        Asistencia guardada = asistenciaRepository.save(asistencia);

        return mapToResponseDTO(guardada, tarjeta.getContadorClases());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponseDTO> listarAsistenciasPorDia(LocalDate fecha) {
        return asistenciaRepository.findByFechaOrderByHoraDesc(fecha).stream()
                .map(asistencia -> {
                    Tarjeta t = tarjetaRepository.findByUsuarioId(asistencia.getUsuario().getId()).orElse(null);
                    return mapToResponseDTO(asistencia, t != null ? t.getContadorClases() : 0);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponseDTO> listarAsistenciasPorHorario(LocalDate fecha, Horario horario) {
        return asistenciaRepository.findByFechaAndHorarioOrderByHoraAsc(fecha, horario).stream()
                .map(asistencia -> {
                    Tarjeta t = tarjetaRepository.findByUsuarioId(asistencia.getUsuario().getId()).orElse(null);
                    return mapToResponseDTO(asistencia, t != null ? t.getContadorClases() : 0);
                })
                .toList();
    }

    private AsistenciaResponseDTO mapToResponseDTO(Asistencia asistencia, Integer clasesRestantes) {
        Usuario u = asistencia.getUsuario();
        return AsistenciaResponseDTO.builder()
                .idAsistencia(asistencia.getId())
                .usuarioId(u.getId())
                .nombreUsuario(u.getNombre() + " " + u.getApellidos())
                .telefono(u.getTelefono())
                .cumple(u.getFechaCumple())
                .plan(u.getPlan())
                .folio(u.getFolio())
                .horaRegistro(asistencia.getHora())
                .horarioClasificado(asistencia.getHorario())
                .clasesRestantes(clasesRestantes)
                .build();
    }
}
