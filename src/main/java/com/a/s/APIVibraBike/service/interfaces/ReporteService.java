package com.a.s.APIVibraBike.service.interfaces;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

public interface ReporteService {
    ByteArrayOutputStream generarReporteAsistenciaDia(LocalDate fecha);
}
