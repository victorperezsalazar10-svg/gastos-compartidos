package com.gastos.gastos_compartidos.controller;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.service.DetalleGastoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalles-gasto")
public class DetalleGastoController {

    private final DetalleGastoService detalleGastoService;

    public DetalleGastoController(DetalleGastoService detalleGastoService) {
        this.detalleGastoService = detalleGastoService;
    }

    @GetMapping
    public List<DetalleGasto> listar() {
        return detalleGastoService.listarTodos();
    }

    @PostMapping
    public DetalleGasto guardar(@RequestBody DetalleGasto detalleGasto) {
        return detalleGastoService.guardar(detalleGasto);
    }
}