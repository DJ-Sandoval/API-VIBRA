package com.a.s.APIVibraBike.controller;

import com.a.s.APIVibraBike.model.dto.DisponibilidadResponseDTO;
import com.a.s.APIVibraBike.model.dto.ReservaResponseDTO;
import com.a.s.APIVibraBike.model.enums.HorarioReserva;
import com.a.s.APIVibraBike.service.interfaces.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador MVC (no REST) que renderiza la plantilla Thymeleaf
 * con el mapa de bicis y la lista de registrados, replicando el
 * formato físico de la hoja de control de sábados.
 */
@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaViewController {

    private final ReservaService reservaService;

    @GetMapping("/vista")
    public String vistaDia(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "horario", required = false) HorarioReserva horario,
            Model model) {

        LocalDate fechaClase = (fecha != null) ? fecha : LocalDate.now();

        // Horarios habilitados para ese día de la semana (viernes/sábado con reglas especiales)
        List<DisponibilidadResponseDTO> horariosDelDia =
                reservaService.horariosDisponiblesPorFecha(fechaClase);

        if (horariosDelDia.isEmpty()) {
            model.addAttribute("diaNombre", capitalizar(fechaClase));
            model.addAttribute("fechaClase", fechaClase);
            model.addAttribute("horariosDelDia", horariosDelDia);
            model.addAttribute("asistentes", List.of());
            model.addAttribute("bicisOcupadas", Set.of());
            model.addAttribute("totalBicis", 31);
            model.addAttribute("totalDisponibles", 0);
            model.addAttribute("horarioSeleccionado", null);
            return "Reservas";
        }

        // Si no mandan horario (o no es válido ese día), usamos el primero disponible
        HorarioReserva horarioSeleccionado = horariosDelDia.stream()
                .map(DisponibilidadResponseDTO::getHorario)
                .filter(h -> h == horario)
                .findFirst()
                .orElse(horariosDelDia.get(0).getHorario());

        DisponibilidadResponseDTO disponibilidad =
                reservaService.consultarDisponibilidad(fechaClase, horarioSeleccionado);

        List<ReservaResponseDTO> asistentes =
                reservaService.listarPorFechaYHorario(fechaClase, horarioSeleccionado);

        model.addAttribute("diaNombre", capitalizar(fechaClase));
        model.addAttribute("fechaClase", fechaClase);
        model.addAttribute("horariosDelDia", horariosDelDia);
        model.addAttribute("horarioSeleccionado", horarioSeleccionado);
        model.addAttribute("asistentes", asistentes);
        model.addAttribute("bicisOcupadas", Set.copyOf(disponibilidad.getBicisOcupadas()));
        model.addAttribute("totalBicis", disponibilidad.getTotalBicis());
        model.addAttribute("totalDisponibles", disponibilidad.getTotalDisponibles());

        return "Reservas";
    }

    private String capitalizar(LocalDate fecha) {
        String dia = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "MX"));
        return dia.substring(0, 1).toUpperCase() + dia.substring(1);
    }
}