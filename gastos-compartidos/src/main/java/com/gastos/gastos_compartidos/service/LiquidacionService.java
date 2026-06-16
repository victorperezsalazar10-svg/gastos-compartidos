package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.Liquidacion;
import com.gastos.gastos_compartidos.repository.LiquidacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiquidacionService {

    private final LiquidacionRepository liquidacionRepository;

    public LiquidacionService(LiquidacionRepository liquidacionRepository) {
        this.liquidacionRepository = liquidacionRepository;
    }

    public List<Liquidacion> listarTodos() {
        return liquidacionRepository.findAll();
    }

    public Liquidacion guardar(Liquidacion liquidacion) {
        return liquidacionRepository.save(liquidacion);
    }
}