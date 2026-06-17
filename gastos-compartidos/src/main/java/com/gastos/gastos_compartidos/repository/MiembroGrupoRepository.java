package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiembroGrupoRepository extends JpaRepository<MiembroGrupo, Long> {

    boolean existsByUsuarioIdAndGrupoId(Long usuarioId, Long grupoId);

    List<MiembroGrupo> findByGrupoId(Long grupoId);

}