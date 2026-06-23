package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MiembroGrupoRepository
        extends JpaRepository<MiembroGrupo, Long> {

    boolean existsByUsuarioIdAndGrupoId(
            Long usuarioId,
            Long grupoId);

    List<MiembroGrupo> findByGrupoId(
            Long grupoId);

    Optional<MiembroGrupo> findByUsuarioIdAndGrupoId(
            Long usuarioId,
            Long grupoId);
}