package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import com.gastos.gastos_compartidos.repository.DetalleGastoRepository;
import com.gastos.gastos_compartidos.repository.GastoRepository;
import com.gastos.gastos_compartidos.repository.MiembroGrupoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final MiembroGrupoRepository miembroGrupoRepository;
    private final DetalleGastoRepository detalleGastoRepository;

    public GastoService(
            GastoRepository gastoRepository,
            MiembroGrupoRepository miembroGrupoRepository,
            DetalleGastoRepository detalleGastoRepository) {

        this.gastoRepository = gastoRepository;
        this.miembroGrupoRepository = miembroGrupoRepository;
        this.detalleGastoRepository = detalleGastoRepository;
    }

    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }

    public Gasto guardar(Gasto gasto) {

        Gasto gastoGuardado = gastoRepository.save(gasto);

        List<MiembroGrupo> miembros =
                miembroGrupoRepository.findByGrupoId(
                        gasto.getGrupo().getId());

        BigDecimal montoPorPersona =
                gasto.getMontoTotal().divide(
                        BigDecimal.valueOf(miembros.size()),
                        2,
                        RoundingMode.HALF_UP);

        for (MiembroGrupo miembro : miembros) {

            DetalleGasto detalle = new DetalleGasto();

            detalle.setGasto(gastoGuardado);
            detalle.setUsuario(miembro.getUsuario());
            detalle.setMontoAsignado(montoPorPersona);

            detalleGastoRepository.save(detalle);
        }

        return gastoGuardado;
    }
}