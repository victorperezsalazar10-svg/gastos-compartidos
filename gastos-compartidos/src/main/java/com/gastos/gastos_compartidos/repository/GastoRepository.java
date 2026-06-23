package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByGrupoId(Long grupoId);

}