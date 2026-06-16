package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import com.gastos.gastos_compartidos.repository.MiembroGrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiembroGrupoService {

    private final MiembroGrupoRepository miembroGrupoRepository;

    public MiembroGrupoService(MiembroGrupoRepository miembroGrupoRepository) {
        this.miembroGrupoRepository = miembroGrupoRepository;
    }

    public List<MiembroGrupo> listarTodos() {
        return miembroGrupoRepository.findAll();
    }

    public MiembroGrupo guardar(MiembroGrupo miembroGrupo) {
        return miembroGrupoRepository.save(miembroGrupo);
    }
}