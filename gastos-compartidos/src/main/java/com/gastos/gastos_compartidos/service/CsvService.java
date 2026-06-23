package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.repository.GastoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvService {

    private final GastoRepository gastoRepository;

    public CsvService(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

    public String generarCsvGrupo(Long grupoId) {

        List<Gasto> gastos =
                gastoRepository.findByGrupoId(grupoId);

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "Fecha,Descripcion,Monto,Pagador\n");

        for (Gasto gasto : gastos) {

            csv.append(gasto.getFechaGasto())
                    .append(",")

                    .append(gasto.getDescripcion())
                    .append(",")

                    .append(gasto.getMontoTotal())
                    .append(",")

                    .append(gasto.getPagador().getNombre())
                    .append("\n");
        }

        return csv.toString();
    }
}