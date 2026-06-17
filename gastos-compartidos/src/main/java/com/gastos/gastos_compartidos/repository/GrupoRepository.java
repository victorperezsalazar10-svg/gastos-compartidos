package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    Optional<Grupo> findByCodigoInvitacion(String codigoInvitacion);

}