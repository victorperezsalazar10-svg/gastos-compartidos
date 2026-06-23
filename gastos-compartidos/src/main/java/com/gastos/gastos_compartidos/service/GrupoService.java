package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.dto.GrupoRequestDTO;
import com.gastos.gastos_compartidos.dto.RankingUsuarioDTO;
import com.gastos.gastos_compartidos.dto.ResumenGrupoDTO;
import com.gastos.gastos_compartidos.entity.*;
import com.gastos.gastos_compartidos.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final MiembroGrupoRepository miembroGrupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GastoRepository gastoRepository;
    private final LiquidacionRepository liquidacionRepository;
    private final DetalleGastoRepository detalleGastoRepository;

    public GrupoService(
            GrupoRepository grupoRepository,
            MiembroGrupoRepository miembroGrupoRepository,
            UsuarioRepository usuarioRepository,
            GastoRepository gastoRepository,
            LiquidacionRepository liquidacionRepository,
            DetalleGastoRepository detalleGastoRepository) {

        this.grupoRepository = grupoRepository;
        this.miembroGrupoRepository = miembroGrupoRepository;
        this.usuarioRepository = usuarioRepository;
        this.gastoRepository = gastoRepository;
        this.liquidacionRepository = liquidacionRepository;
        this.detalleGastoRepository = detalleGastoRepository;
    }

    public List<Grupo> listarTodos() {
        return grupoRepository.findAll();
    }

    public Grupo buscarPorId(Long id) {

        return grupoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Grupo no encontrado"));
    }

    public Grupo guardar(GrupoRequestDTO request) {

        if (grupoRepository.existsByCodigoInvitacion(
                request.getCodigoInvitacion())) {

            throw new RuntimeException(
                    "Ya existe un grupo con ese código de invitación");
        }

        Usuario creador =
                usuarioRepository.findById(
                                request.getCreadorId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuario creador no encontrado"));

        Grupo grupo = new Grupo();

        grupo.setNombre(request.getNombre());
        grupo.setDescripcion(request.getDescripcion());
        grupo.setCodigoInvitacion(
                request.getCodigoInvitacion());
        grupo.setCreador(creador);

        Grupo grupoGuardado =
                grupoRepository.save(grupo);

        MiembroGrupo miembro =
                new MiembroGrupo();

        miembro.setUsuario(creador);
        miembro.setGrupo(grupoGuardado);
        miembro.setRol("ADMIN");
        miembro.setFechaIngreso(LocalDate.now());

        miembroGrupoRepository.save(miembro);

        return grupoGuardado;
    }

    public Grupo actualizar(
            Long id,
            Grupo grupoActualizado) {

        Grupo grupo =
                buscarPorId(id);

        if (!grupo.getCodigoInvitacion()
                .equals(
                        grupoActualizado.getCodigoInvitacion())
                &&
                grupoRepository.existsByCodigoInvitacion(
                        grupoActualizado.getCodigoInvitacion())) {

            throw new RuntimeException(
                    "Ya existe un grupo con ese código de invitación");
        }

        grupo.setNombre(
                grupoActualizado.getNombre());

        grupo.setDescripcion(
                grupoActualizado.getDescripcion());

        grupo.setCodigoInvitacion(
                grupoActualizado.getCodigoInvitacion());

        return grupoRepository.save(grupo);
    }

    public void eliminar(Long id) {

        Grupo grupo =
                buscarPorId(id);

        try {

            grupoRepository.delete(grupo);

        } catch (Exception e) {

            throw new RuntimeException(
                    "No se puede eliminar el grupo porque tiene miembros o gastos asociados");
        }
    }

    public ResumenGrupoDTO obtenerResumen(
            Long grupoId) {

        Grupo grupo =
                buscarPorId(grupoId);

        List<MiembroGrupo> miembros =
                miembroGrupoRepository.findByGrupoId(
                        grupoId);

        List<Gasto> gastos =
                gastoRepository.findByGrupoId(
                        grupoId);

        List<Liquidacion> liquidaciones =
                liquidacionRepository.findByGrupoId(
                        grupoId);

        BigDecimal totalGastado =
                BigDecimal.ZERO;

        for (Gasto gasto : gastos) {

            totalGastado =
                    totalGastado.add(
                            gasto.getMontoTotal());
        }

        BigDecimal pendiente =
                BigDecimal.ZERO;

        for (Liquidacion liquidacion :
                liquidaciones) {

            pendiente =
                    pendiente.add(
                            liquidacion.getMonto());
        }

        ResumenGrupoDTO dto =
                new ResumenGrupoDTO();

        dto.setGrupoId(grupo.getId());
        dto.setNombreGrupo(
                grupo.getNombre());
        dto.setTotalMiembros(
                miembros.size());
        dto.setTotalGastos(
                gastos.size());
        dto.setMontoTotalGastado(
                totalGastado);
        dto.setMontoPendienteLiquidar(
                pendiente);

        return dto;
    }

    public List<RankingUsuarioDTO> obtenerRanking(
            Long grupoId) {

        List<Gasto> gastos =
                gastoRepository.findByGrupoId(
                        grupoId);

        List<DetalleGasto> detalles =
                detalleGastoRepository
                        .findByGastoGrupoId(
                                grupoId);

        Map<Usuario, BigDecimal> pagado =
                new HashMap<>();

        Map<Usuario, BigDecimal> consumido =
                new HashMap<>();

        for (Gasto gasto : gastos) {

            Usuario usuario =
                    gasto.getPagador();

            pagado.put(
                    usuario,
                    pagado.getOrDefault(
                                    usuario,
                                    BigDecimal.ZERO)
                            .add(
                                    gasto.getMontoTotal()));
        }

        for (DetalleGasto detalle : detalles) {

            Usuario usuario =
                    detalle.getUsuario();

            consumido.put(
                    usuario,
                    consumido.getOrDefault(
                                    usuario,
                                    BigDecimal.ZERO)
                            .add(
                                    detalle.getMontoAsignado()));
        }

        Set<Usuario> usuarios =
                new HashSet<>();

        usuarios.addAll(
                pagado.keySet());

        usuarios.addAll(
                consumido.keySet());

        List<RankingUsuarioDTO> ranking =
                new ArrayList<>();

        for (Usuario usuario : usuarios) {

            BigDecimal totalPagado =
                    pagado.getOrDefault(
                            usuario,
                            BigDecimal.ZERO);

            BigDecimal totalConsumido =
                    consumido.getOrDefault(
                            usuario,
                            BigDecimal.ZERO);

            RankingUsuarioDTO dto =
                    new RankingUsuarioDTO();

            dto.setNombreUsuario(
                    usuario.getNombre());

            dto.setTotalPagado(
                    totalPagado);

            dto.setTotalConsumido(
                    totalConsumido);

            dto.setSaldo(
                    totalPagado.subtract(
                            totalConsumido));

            ranking.add(dto);
        }

        ranking.sort(
                (a, b) ->
                        b.getSaldo()
                                .compareTo(
                                        a.getSaldo()));

        return ranking;
    }
}

