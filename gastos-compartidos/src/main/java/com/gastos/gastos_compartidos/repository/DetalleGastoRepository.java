package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleGastoRepository extends JpaRepository<DetalleGasto, Long> {
}