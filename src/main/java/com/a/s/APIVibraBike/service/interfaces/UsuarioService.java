package com.a.s.APIVibraBike.service.interfaces;

import com.a.s.APIVibraBike.model.dto.UsuarioRequestDTO;
import com.a.s.APIVibraBike.model.dto.UsuarioResponseDTO;

public interface UsuarioService {
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request);
}
