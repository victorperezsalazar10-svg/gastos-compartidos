package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.Liquidacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiquidacionRepository extends JpaRepository<Liquidacion, Long> {
}