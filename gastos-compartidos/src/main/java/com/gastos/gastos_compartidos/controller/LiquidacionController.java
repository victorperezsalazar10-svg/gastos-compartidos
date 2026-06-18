package com.gastos.gastos_compartidos.controller;

import com.gastos.gastos_compartidos.entity.Liquidacion;
import com.gastos.gastos_compartidos.service.LiquidacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liquidaciones")
public class LiquidacionController {

    private final LiquidacionService liquidacionService;

    public LiquidacionController(
            LiquidacionService liquidacionService) {

        this.liquidacionService = liquidacionService;
    }

    @GetMapping("/grupo/{grupoId}")
    public List<Liquidacion> calcular(
            @PathVariable Long grupoId) {

        return liquidacionService
                .calcularLiquidaciones(grupoId);
    }

    @GetMapping("/grupo/{grupoId}/historial")
    public List<Liquidacion> historial(
            @PathVariable Long grupoId) {

        return liquidacionService
                .listarPorGrupo(grupoId);
    }
}