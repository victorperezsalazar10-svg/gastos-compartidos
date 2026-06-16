package com.gastos.gastos_compartidos.controller;

import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import com.gastos.gastos_compartidos.service.MiembroGrupoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/miembros")
public class MiembroGrupoController {

    private final MiembroGrupoService miembroGrupoService;

    public MiembroGrupoController(MiembroGrupoService miembroGrupoService) {
        this.miembroGrupoService = miembroGrupoService;
    }

    @GetMapping
    public List<MiembroGrupo> listar() {
        return miembroGrupoService.listarTodos();
    }

    @PostMapping
    public MiembroGrupo guardar(@RequestBody MiembroGrupo miembroGrupo) {
        return miembroGrupoService.guardar(miembroGrupo);
    }
}