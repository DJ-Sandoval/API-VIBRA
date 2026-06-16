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

        // Horarios Matutinos
        model.addAttribute("primerClase", asistenciaService.listarAsistenciasPorHorario(hoy, Horario.PRIMER_CLASE));
        model.addAttribute("segundaClase", asistenciaService.listarAsistenciasPorHorario(hoy, Horario.SEGUNDA_CLASE));
        model.addAttribute("terceraClase", asistenciaService.listarAsistenciasPorHorario(hoy, Horario.TERCERA_CLASE));
        // Horarios Vespertinos
        model.addAttribute("cuartaClase",  asistenciaService.listarAsistenciasPorHorario(hoy, Horario.CUARTA_CLASE));
        model.addAttribute("quintaClase",  asistenciaService.listarAsistenciasPorHorario(hoy, Horario.QUINTA_CLASE));
        model.addAttribute("sextaClase",   asistenciaService.listarAsistenciasPorHorario(hoy, Horario.SEXTA_CLASE));
        return "asistencia"; // Nombre de tu archivo html en src/main/resources/templates
    }
}
