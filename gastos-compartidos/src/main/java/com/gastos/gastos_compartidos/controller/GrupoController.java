package com.gastos.gastos_compartidos.controller;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gastos.gastos_compartidos.dto.GrupoRequestDTO;
import com.gastos.gastos_compartidos.dto.RankingUsuarioDTO;
import com.gastos.gastos_compartidos.dto.ResumenGrupoDTO;
import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.service.GrupoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/grupos")
@CrossOrigin(origins = "*")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public List<Grupo> listar() {
        return grupoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Grupo buscarPorId(@PathVariable Long id) {
        return grupoService.buscarPorId(id);
    }

    @PostMapping
    public Grupo guardar(
            @Valid @RequestBody GrupoRequestDTO request) {

        return grupoService.guardar(request);
    }
    @PutMapping("/{id}")
    public Grupo actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Grupo grupo) {

        return grupoService.actualizar(id, grupo);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {

        grupoService.eliminar(id);

        return "Grupo eliminado correctamente";
    }

    @GetMapping("/{id}/resumen")
    public ResumenGrupoDTO resumen(
            @PathVariable Long id) {

        return grupoService.obtenerResumen(id);
    }
    @GetMapping("/{id}/ranking")
    public List<RankingUsuarioDTO> ranking(
            @PathVariable Long id) {

        return grupoService.obtenerRanking(id);
    }
}