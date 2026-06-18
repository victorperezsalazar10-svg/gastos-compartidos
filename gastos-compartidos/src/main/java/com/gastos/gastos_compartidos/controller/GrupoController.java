package com.gastos.gastos_compartidos.controller;
import com.gastos.gastos_compartidos.dto.ResumenGrupoDTO;
import com.gastos.gastos_compartidos.dto.GrupoRequestDTO;
import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.service.GrupoService;
import com.gastos.gastos_compartidos.dto.RankingUsuarioDTO;
import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public Grupo buscarPorId(@PathVariable Long id) {
        return grupoService.buscarPorId(id);
    }

    @PostMapping
    public Grupo guardar(
            @RequestBody GrupoRequestDTO request) {

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