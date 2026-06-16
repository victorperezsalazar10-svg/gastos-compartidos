package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.repository.DetalleGastoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleGastoService {

    private final DetalleGastoRepository detalleGastoRepository;

    public DetalleGastoService(DetalleGastoRepository detalleGastoRepository) {
        this.detalleGastoRepository = detalleGastoRepository;
    }

    public List<DetalleGasto> listarTodos() {
        return detalleGastoRepository.findAll();
    }

    public DetalleGasto guardar(DetalleGasto detalleGasto) {
        return detalleGastoRepository.save(detalleGasto);
    }
}