package com.a.s.APIVibraBike.controller;
import com.a.s.APIVibraBike.model.enums.Horario;
import com.a.s.APIVibraBike.service.interfaces.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final AsistenciaService asistenciaService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/panel-asistencia")
    public String verPanelAsistencia(Model model) {
        LocalDate hoy = LocalDate.now();

        // Lista completa del día (conforme van llegando)
        model.addAttribute("asistenciasGeneral", asistenciaService.listarAsistenciasPorDia(hoy));

        // Las 3 listas segmentadas
        model.addAttribute("primerHorario", asistenciaService.listarAsistenciasPorHorario(hoy, Horario.PRIMER_HORARIO));
        model.addAttribute("segundoHorario", asistenciaService.listarAsistenciasPorHorario(hoy, Horario.SEGUNDO_HORARIO));
        model.addAttribute("tercerHorario", asistenciaService.listarAsistenciasPorHorario(hoy, Horario.TERCER_HORARIO));

        return "asistencia"; // Nombre de tu archivo html en src/main/resources/templates
    }
}
