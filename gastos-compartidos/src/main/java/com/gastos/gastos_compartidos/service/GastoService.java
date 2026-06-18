package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.dto.DetalleGastoRequestDTO;
import com.gastos.gastos_compartidos.dto.GastoRequestDTO;
import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.enums.TipoDivision;
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
    private final UsuarioService usuarioService;
    private final GrupoService grupoService;

    public GastoService(
            GastoRepository gastoRepository,
            MiembroGrupoRepository miembroGrupoRepository,
            DetalleGastoRepository detalleGastoRepository,
            UsuarioService usuarioService,
            GrupoService grupoService) {

        this.gastoRepository = gastoRepository;
        this.miembroGrupoRepository = miembroGrupoRepository;
        this.detalleGastoRepository = detalleGastoRepository;
        this.usuarioService = usuarioService;
        this.grupoService = grupoService;
    }

    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }

    public Gasto buscarPorId(Long id) {

        return gastoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Gasto no encontrado"));
    }

    public Gasto guardar(GastoRequestDTO request) {

        Usuario pagador =
                usuarioService.buscarPorId(
                        request.getPagadorId());

        Grupo grupo =
                grupoService.buscarPorId(
                        request.getGrupoId());

        boolean pertenece =
                miembroGrupoRepository
                        .existsByUsuarioIdAndGrupoId(
                                pagador.getId(),
                                grupo.getId());

        if (!pertenece) {
            throw new RuntimeException(
                    "El pagador no pertenece al grupo");
        }

        Gasto gasto = new Gasto();

        gasto.setDescripcion(
                request.getDescripcion());

        gasto.setMontoTotal(
                request.getMontoTotal());

        gasto.setFechaGasto(
                request.getFechaGasto());

        gasto.setPagador(
                pagador);

        gasto.setGrupo(
                grupo);

        gasto.setTipoDivision(
                TipoDivision.valueOf(
                        request.getTipoDivision()
                                .toUpperCase()));

        Gasto gastoGuardado =
                gastoRepository.save(gasto);

        switch (gasto.getTipoDivision()) {

            case IGUAL ->
                    dividirIgual(gastoGuardado);

            case PORCENTUAL ->
                    dividirPorcentual(
                            gastoGuardado,
                            request.getDetalles());

            case PERSONALIZADA ->
                    dividirPersonalizada(
                            gastoGuardado,
                            request.getDetalles());
        }

        return gastoGuardado;
    }

    private void dividirIgual(Gasto gasto) {

        List<MiembroGrupo> miembros =
                miembroGrupoRepository.findByGrupoId(
                        gasto.getGrupo().getId());

        BigDecimal montoPorPersona =
                gasto.getMontoTotal().divide(
                        BigDecimal.valueOf(
                                miembros.size()),
                        2,
                        RoundingMode.HALF_UP);

        for (MiembroGrupo miembro : miembros) {

            DetalleGasto detalle =
                    new DetalleGasto();

            detalle.setGasto(gasto);
            detalle.setUsuario(
                    miembro.getUsuario());

            detalle.setMontoAsignado(
                    montoPorPersona);

            detalle.setPorcentaje(null);

            detalleGastoRepository.save(
                    detalle);
        }
    }

    private void dividirPorcentual(
            Gasto gasto,
            List<DetalleGastoRequestDTO> detalles) {

        if (detalles == null || detalles.isEmpty()) {
            throw new RuntimeException(
                    "Debe enviar los porcentajes");
        }

        BigDecimal sumaPorcentajes =
                BigDecimal.ZERO;

        for (DetalleGastoRequestDTO dto : detalles) {

            sumaPorcentajes =
                    sumaPorcentajes.add(
                            dto.getPorcentaje());
        }

        if (sumaPorcentajes.compareTo(
                BigDecimal.valueOf(100)) != 0) {

            throw new RuntimeException(
                    "La suma de porcentajes debe ser 100");
        }

        for (DetalleGastoRequestDTO dto : detalles) {

            Usuario usuario =
                    usuarioService.buscarPorId(
                            dto.getUsuarioId());

            boolean pertenece =
                    miembroGrupoRepository
                            .existsByUsuarioIdAndGrupoId(
                                    usuario.getId(),
                                    gasto.getGrupo().getId());

            if (!pertenece) {

                throw new RuntimeException(
                        "El usuario "
                                + usuario.getNombre()
                                + " no pertenece al grupo");
            }

            BigDecimal montoAsignado =
                    gasto.getMontoTotal()
                            .multiply(
                                    dto.getPorcentaje())
                            .divide(
                                    BigDecimal.valueOf(100),
                                    2,
                                    RoundingMode.HALF_UP);

            DetalleGasto detalle =
                    new DetalleGasto();

            detalle.setGasto(gasto);
            detalle.setUsuario(usuario);
            detalle.setPorcentaje(
                    dto.getPorcentaje());

            detalle.setMontoAsignado(
                    montoAsignado);

            detalleGastoRepository.save(
                    detalle);
        }
    }

    private void dividirPersonalizada(
            Gasto gasto,
            List<DetalleGastoRequestDTO> detalles) {

        if (detalles == null || detalles.isEmpty()) {
            throw new RuntimeException(
                    "Debe enviar los montos personalizados");
        }

        BigDecimal sumaMontos =
                BigDecimal.ZERO;

        for (DetalleGastoRequestDTO dto : detalles) {

            sumaMontos =
                    sumaMontos.add(
                            dto.getMontoAsignado());
        }

        if (sumaMontos.compareTo(
                gasto.getMontoTotal()) != 0) {

            throw new RuntimeException(
                    "La suma de montos debe coincidir con el monto total");
        }

        for (DetalleGastoRequestDTO dto : detalles) {

            Usuario usuario =
                    usuarioService.buscarPorId(
                            dto.getUsuarioId());

            boolean pertenece =
                    miembroGrupoRepository
                            .existsByUsuarioIdAndGrupoId(
                                    usuario.getId(),
                                    gasto.getGrupo().getId());

            if (!pertenece) {

                throw new RuntimeException(
                        "El usuario "
                                + usuario.getNombre()
                                + " no pertenece al grupo");
            }

            DetalleGasto detalle =
                    new DetalleGasto();

            detalle.setGasto(gasto);
            detalle.setUsuario(usuario);

            detalle.setMontoAsignado(
                    dto.getMontoAsignado());

            detalle.setPorcentaje(null);

            detalleGastoRepository.save(
                    detalle);
        }
    }

    public void eliminar(Long id) {

        Gasto gasto = buscarPorId(id);

        detalleGastoRepository.deleteAll(
                detalleGastoRepository
                        .findByGastoId(id));

        gastoRepository.delete(gasto);
    }
}