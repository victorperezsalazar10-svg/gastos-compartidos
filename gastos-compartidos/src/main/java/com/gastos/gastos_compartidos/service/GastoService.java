package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.repository.GastoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;

    public GastoService(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }

    public Gasto guardar(Gasto gasto) {
        return gastoRepository.save(gasto);
    }
}