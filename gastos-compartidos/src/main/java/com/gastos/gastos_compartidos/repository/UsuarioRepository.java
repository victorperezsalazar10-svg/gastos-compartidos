package com.gastos.gastos_compartidos.repository;

import com.gastos.gastos_compartidos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

}
