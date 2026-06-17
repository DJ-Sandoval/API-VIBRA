package com.a.s.APIVibraBike.service.impl;

import com.a.s.APIVibraBike.model.dto.DisponibilidadResponseDTO;
import com.a.s.APIVibraBike.model.dto.ReservaRequestDTO;
import com.a.s.APIVibraBike.model.dto.ReservaResponseDTO;
import com.a.s.APIVibraBike.model.entity.Reserva;
import com.a.s.APIVibraBike.model.entity.Usuario;
import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import com.a.s.APIVibraBike.repository.ReservaRepository;
import com.a.s.APIVibraBike.repository.UsuarioRepository;
import com.a.s.APIVibraBike.service.exception.BiciOcupadaException;
import com.a.s.APIVibraBike.service.exception.HorarioNoDisponibleException;
import com.a.s.APIVibraBike.service.exception.ReservaDuplicadaException;
import com.a.s.APIVibraBike.service.exception.ResourceNotFoundException;
import com.a.s.APIVibraBike.service.interfaces.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private static final int TOTAL_BICIS = 30;

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    // ── Crear reserva ─────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ReservaResponseDTO crearReserva(ReservaRequestDTO request) {

        // 1. Validar que el horario opere ese día de la semana
        DayOfWeek diaSemana = request.getFechaClase().getDayOfWeek();
        if (!request.getHorario().disponibleEl(diaSemana)) {
            throw new HorarioNoDisponibleException(
                    "El horario " + request.getHorario().getEtiqueta() +
                            " no está disponible el " + diaSemana.name().toLowerCase() +
                            ". Días disponibles: " + request.getHorario().getDiasDisponibles());
        }

        // 2. Validar existencia del usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + request.getUsuarioId()));

        // 3. Verificar que el usuario no tenga ya reserva en ese slot
        boolean yaReservado = reservaRepository.existsByFechaClaseAndHorarioAndUsuarioId(
                request.getFechaClase(), request.getHorario(), usuario.getId());
        if (yaReservado) {
            throw new ReservaDuplicadaException(
                    "El usuario ya tiene una reserva para el " + request.getFechaClase() +
                            " en el horario " + request.getHorario().getEtiqueta() + ".");
        }

        // 4. Verificar que la bici no esté ocupada en ese slot
        boolean biciOcupada = reservaRepository.existsByFechaClaseAndHorarioAndNumeroBici(
                request.getFechaClase(), request.getHorario(), request.getNumeroBici());
        if (biciOcupada) {
            throw new BiciOcupadaException(
                    "La bici #" + request.getNumeroBici() +
                            " ya está reservada para ese horario. Por favor elige otra.");
        }

        // 5. Guardar reserva
        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .fechaClase(request.getFechaClase())
                .horario(request.getHorario())
                .numeroBici(request.getNumeroBici())
                .metodoPago(request.getMetodoPago())
                .build();

        return mapToResponseDTO(reservaRepository.save(reserva));
    }

    // ── Consultas ─────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarPorFecha(LocalDate fecha) {
        return reservaRepository.findByFechaClaseOrderByCreadoEnAsc(fecha)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarPorFechaYHorario(LocalDate fecha, HorarioReserva horario) {
        return reservaRepository
                .findByFechaClaseAndHorarioOrderByCreadoEnAsc(fecha, horario)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DisponibilidadResponseDTO consultarDisponibilidad(LocalDate fecha, HorarioReserva horario) {

        List<Integer> ocupadas = reservaRepository
                .findBicisOcupadasByFechaAndHorario(fecha, horario);

        List<Integer> disponibles = IntStream.rangeClosed(1, TOTAL_BICIS)
                .filter(n -> !ocupadas.contains(n))
                .boxed()
                .toList();

        return DisponibilidadResponseDTO.builder()
                .fechaClase(fecha)
                .horario(horario)
                .etiquetaHorario(horario.getEtiqueta())
                .bicisOcupadas(ocupadas)
                .bicisDisponibles(disponibles)
                .totalBicis(TOTAL_BICIS)
                .totalOcupadas(ocupadas.size())
                .totalDisponibles(disponibles.size())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarPorUsuario(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + usuarioId));

        return reservaRepository
                .findByUsuarioIdOrderByFechaClaseDescCreadoEnDesc(usuarioId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisponibilidadResponseDTO> horariosDisponiblesPorFecha(LocalDate fecha) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();

        return HorarioReserva.disponiblesParaDia(diaSemana)
                .stream()
                .map(horario -> consultarDisponibilidad(fecha, horario))
                .toList();
    }

    // ── Cancelar ──────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void cancelarReserva(Long usuarioId, LocalDate fecha, HorarioReserva horario) {
        Reserva reserva = reservaRepository
                .findByUsuarioIdAndFechaClaseAndHorario(usuarioId, fecha, horario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró una reserva activa para ese usuario en el slot indicado."));

        reservaRepository.delete(reserva);
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private ReservaResponseDTO mapToResponseDTO(Reserva r) {
        Usuario u = r.getUsuario();
        return ReservaResponseDTO.builder()
                .idReserva(r.getId())
                .usuarioId(u.getId())
                .nombre(u.getNombre() + " " + u.getApellidos())
                .contacto(u.getTelefono())
                .cumple(u.getFechaCumple())
                .plan(u.getPlan())
                .folio(u.getFolio())
                .fechaClase(r.getFechaClase())
                .horario(r.getHorario())
                .etiquetaHorario(r.getHorario().getEtiqueta())
                .numeroBici(r.getNumeroBici())
                .metodoPago(r.getMetodoPago())
                .creadoEn(r.getCreadoEn())
                .build();
    }
}
