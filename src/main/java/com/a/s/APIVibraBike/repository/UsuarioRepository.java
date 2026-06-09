package com.a.s.APIVibraBike.repository;

import com.a.s.APIVibraBike.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByTelefono(String telefono);
    boolean existsByFolio(String folio);
    boolean existsByQrUuid(String qrUuid);
}
