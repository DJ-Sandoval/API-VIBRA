package com.a.s.APIVibraBike.service.interfaces;
import com.a.s.APIVibraBike.model.dto.UsuarioRequestDTO;
import com.a.s.APIVibraBike.model.dto.UsuarioResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request);
    Page<UsuarioResponseDTO> listarUsuarios(Pageable pageable);
    UsuarioResponseDTO obtenerUsuario(Long id);
}
