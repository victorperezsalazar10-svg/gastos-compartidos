package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.Liquidacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiquidacionRepository
        extends JpaRepository<Liquidacion, Long> {

    void deleteByGrupoId(Long grupoId);

    List<Liquidacion> findByGrupoId(Long grupoId);
}