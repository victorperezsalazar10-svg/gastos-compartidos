package com.gastos.gastos_compartidos.controller;

import com.gastos.gastos_compartidos.dto.GastoRequestDTO;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.service.GastoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping
    public List<Gasto> listar() {
        return gastoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Gasto buscarPorId(@PathVariable Long id) {
        return gastoService.buscarPorId(id);
    }

    @PostMapping
    public Gasto guardar(
            @RequestBody GastoRequestDTO request) {

        return gastoService.guardar(request);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {

        gastoService.eliminar(id);

        return "Gasto eliminado correctamente";
    }
}