package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
}