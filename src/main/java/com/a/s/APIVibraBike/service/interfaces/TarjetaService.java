package com.a.s.APIVibraBike.service.interfaces;

import com.a.s.APIVibraBike.model.dto.TarjetaRequestDTO;
import com.a.s.APIVibraBike.model.dto.TarjetaResponseDTO;

import java.util.List;

public interface TarjetaService {
    TarjetaResponseDTO crearTarjeta(TarjetaRequestDTO request);
    TarjetaResponseDTO obtenerPorUsuario(Long usuarioId);
    List<TarjetaResponseDTO> listarTodas();
    TarjetaResponseDTO actualizarTarjeta(Long id, TarjetaRequestDTO request); // o DTO específico
    void eliminarTarjeta(Long id);
    void descontarClase(Long tarjetaId); // Para uso futuro en asistencias
    byte[] generarPDFTarjeta(Long usuarioId);
}
