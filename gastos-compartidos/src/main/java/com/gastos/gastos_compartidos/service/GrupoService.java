package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<Grupo> listarTodos() {
        return grupoRepository.findAll();
    }

    public Grupo guardar(Grupo grupo) {
        return grupoRepository.save(grupo);
    }
}