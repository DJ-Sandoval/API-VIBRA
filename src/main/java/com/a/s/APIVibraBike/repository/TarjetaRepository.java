package com.a.s.APIVibraBike.repository;

import com.a.s.APIVibraBike.model.entity.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    Optional<Tarjeta> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}