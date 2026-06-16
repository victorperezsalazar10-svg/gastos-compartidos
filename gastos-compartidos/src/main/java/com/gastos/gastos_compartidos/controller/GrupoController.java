package com.gastos.gastos_compartidos.controller;

import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.service.GrupoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public List<Grupo> listar() {
        return grupoService.listarTodos();
    }

    @PostMapping
    public Grupo guardar(@RequestBody Grupo grupo) {
        return grupoService.guardar(grupo);
    }
}