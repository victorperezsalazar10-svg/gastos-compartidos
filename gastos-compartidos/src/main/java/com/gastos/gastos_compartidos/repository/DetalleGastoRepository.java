package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleGastoRepository
        extends JpaRepository<DetalleGasto, Long> {

    List<DetalleGasto> findByGastoGrupoId(Long grupoId);

    List<DetalleGasto> findByGastoId(Long gastoId);
}