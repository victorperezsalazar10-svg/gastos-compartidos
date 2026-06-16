package com.gastos.gastos_compartidos.controller;

import com.gastos.gastos_compartidos.entity.Liquidacion;
import com.gastos.gastos_compartidos.service.LiquidacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liquidaciones")
public class LiquidacionController {

    private final LiquidacionService liquidacionService;

    public LiquidacionController(LiquidacionService liquidacionService) {
        this.liquidacionService = liquidacionService;
    }

    @GetMapping
    public List<Liquidacion> listar() {
        return liquidacionService.listarTodos();
    }

    @PostMapping
    public Liquidacion guardar(@RequestBody Liquidacion liquidacion) {
        return liquidacionService.guardar(liquidacion);
    }
}