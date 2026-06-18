package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.entity.Liquidacion;
import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.repository.DetalleGastoRepository;
import com.gastos.gastos_compartidos.repository.GastoRepository;
import com.gastos.gastos_compartidos.repository.LiquidacionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class LiquidacionService {

    private final GastoRepository gastoRepository;
    private final DetalleGastoRepository detalleGastoRepository;
    private final LiquidacionRepository liquidacionRepository;

    public LiquidacionService(
            GastoRepository gastoRepository,
            DetalleGastoRepository detalleGastoRepository,
            LiquidacionRepository liquidacionRepository) {

        this.gastoRepository = gastoRepository;
        this.detalleGastoRepository = detalleGastoRepository;
        this.liquidacionRepository = liquidacionRepository;
    }

    public List<Liquidacion> calcularLiquidaciones(Long grupoId) {

        List<Gasto> gastos =
                gastoRepository.findByGrupoId(grupoId);

        if (gastos.isEmpty()) {
            throw new RuntimeException(
                    "El grupo no tiene gastos registrados");
        }

        List<DetalleGasto> detalles =
                detalleGastoRepository.findByGastoGrupoId(grupoId);

        liquidacionRepository.deleteByGrupoId(grupoId);

        Map<Usuario, BigDecimal> pagado =
                new HashMap<>();

        Map<Usuario, BigDecimal> consumido =
                new HashMap<>();

        for (Gasto gasto : gastos) {

            Usuario pagador =
                    gasto.getPagador();

            pagado.put(
                    pagador,
                    pagado.getOrDefault(
                                    pagador,
                                    BigDecimal.ZERO)
                            .add(gasto.getMontoTotal()));
        }

        for (DetalleGasto detalle : detalles) {

            Usuario usuario =
                    detalle.getUsuario();

            consumido.put(
                    usuario,
                    consumido.getOrDefault(
                                    usuario,
                                    BigDecimal.ZERO)
                            .add(detalle.getMontoAsignado()));
        }

        List<UsuarioSaldo> acreedores =
                new ArrayList<>();

        List<UsuarioSaldo> deudores =
                new ArrayList<>();

        Set<Usuario> usuarios =
                new HashSet<>();

        usuarios.addAll(pagado.keySet());
        usuarios.addAll(consumido.keySet());

        for (Usuario usuario : usuarios) {

            BigDecimal totalPagado =
                    pagado.getOrDefault(
                            usuario,
                            BigDecimal.ZERO);

            BigDecimal totalConsumido =
                    consumido.getOrDefault(
                            usuario,
                            BigDecimal.ZERO);

            BigDecimal saldo =
                    totalPagado.subtract(
                            totalConsumido);

            if (saldo.compareTo(
                    BigDecimal.ZERO) > 0) {

                acreedores.add(
                        new UsuarioSaldo(
                                usuario,
                                saldo));
            }

            if (saldo.compareTo(
                    BigDecimal.ZERO) < 0) {

                deudores.add(
                        new UsuarioSaldo(
                                usuario,
                                saldo.abs()));
            }
        }

        List<Liquidacion> resultado =
                new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < deudores.size()
                && j < acreedores.size()) {

            UsuarioSaldo deudor =
                    deudores.get(i);

            UsuarioSaldo acreedor =
                    acreedores.get(j);

            BigDecimal montoLiquidar =
                    deudor.getSaldo()
                            .min(
                                    acreedor.getSaldo());

            Liquidacion liquidacion =
                    new Liquidacion();

            liquidacion.setDeudor(
                    deudor.getUsuario());

            liquidacion.setAcreedor(
                    acreedor.getUsuario());

            liquidacion.setMonto(
                    montoLiquidar);

            liquidacion.setFechaGeneracion(
                    LocalDate.now());

            liquidacion.setGrupo(
                    gastos.get(0).getGrupo());

            resultado.add(liquidacion);

            liquidacionRepository.save(
                    liquidacion);

            deudor.setSaldo(
                    deudor.getSaldo()
                            .subtract(
                                    montoLiquidar));

            acreedor.setSaldo(
                    acreedor.getSaldo()
                            .subtract(
                                    montoLiquidar));

            if (deudor.getSaldo()
                    .compareTo(BigDecimal.ZERO) == 0) {

                i++;
            }

            if (acreedor.getSaldo()
                    .compareTo(BigDecimal.ZERO) == 0) {

                j++;
            }
        }

        return resultado;
    }

    public List<Liquidacion> listarPorGrupo(
            Long grupoId) {

        return liquidacionRepository
                .findByGrupoId(grupoId);
    }

    private static class UsuarioSaldo {

        private Usuario usuario;
        private BigDecimal saldo;

        public UsuarioSaldo(
                Usuario usuario,
                BigDecimal saldo) {

            this.usuario = usuario;
            this.saldo = saldo;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public BigDecimal getSaldo() {
            return saldo;
        }

        public void setSaldo(
                BigDecimal saldo) {

            this.saldo = saldo;
        }
    }
}