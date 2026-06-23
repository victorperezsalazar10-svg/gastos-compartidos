package com.gastos.gastos_compartidos;
import org.mockito.ArgumentCaptor;
import com.gastos.gastos_compartidos.dto.GrupoRequestDTO;
import com.gastos.gastos_compartidos.dto.RankingUsuarioDTO;
import com.gastos.gastos_compartidos.dto.ResumenGrupoDTO;
import com.gastos.gastos_compartidos.entity.*;
import com.gastos.gastos_compartidos.repository.*;
import com.gastos.gastos_compartidos.service.GrupoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GrupoServiceTest {

    private GrupoRepository grupoRepository;
    private MiembroGrupoRepository miembroGrupoRepository;
    private UsuarioRepository usuarioRepository;
    private GastoRepository gastoRepository;
    private LiquidacionRepository liquidacionRepository;
    private DetalleGastoRepository detalleGastoRepository;

    private GrupoService grupoService;

    @BeforeEach
    void setUp() {

        grupoRepository = mock(GrupoRepository.class);
        miembroGrupoRepository = mock(MiembroGrupoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        gastoRepository = mock(GastoRepository.class);
        liquidacionRepository = mock(LiquidacionRepository.class);
        detalleGastoRepository = mock(DetalleGastoRepository.class);

        grupoService = new GrupoService(
                grupoRepository,
                miembroGrupoRepository,
                usuarioRepository,
                gastoRepository,
                liquidacionRepository,
                detalleGastoRepository);
    }

    @Test
    void debeBuscarGrupoPorIdCuandoExiste() {

        Grupo grupo = new Grupo();
        grupo.setId(1L);
        grupo.setNombre("Viaje");

        when(grupoRepository.findById(1L))
                .thenReturn(Optional.of(grupo));

        Grupo resultado = grupoService.buscarPorId(1L);

        assertEquals("Viaje", resultado.getNombre());
    }

    @Test
    void debeLanzarExcepcionSiGrupoNoExiste() {

        when(grupoRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> grupoService.buscarPorId(99L));

        assertEquals("Grupo no encontrado", ex.getMessage());
    }

    @Test
    void debeCrearGrupoYAgregarAlCreadorComoMiembroAdmin() {

        Usuario creador = new Usuario();
        creador.setId(1L);
        creador.setNombre("Juan");

        GrupoRequestDTO request = new GrupoRequestDTO();
        request.setNombre("Viaje a la playa");
        request.setDescripcion("Vacaciones de verano");
        request.setCodigoInvitacion("PLAYA2026");
        request.setCreadorId(1L);

        when(grupoRepository.existsByCodigoInvitacion("PLAYA2026"))
                .thenReturn(false);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(creador));

        when(grupoRepository.save(any(Grupo.class)))
                .thenAnswer(invocation -> {
                    Grupo g = invocation.getArgument(0);
                    g.setId(10L);
                    return g;
                });

        Grupo resultado = grupoService.guardar(request);

        assertEquals("Viaje a la playa", resultado.getNombre());
        assertEquals(creador, resultado.getCreador());

        // Verifica que se haya creado el MiembroGrupo del creador con rol ADMIN
        ArgumentCaptor<MiembroGrupo> captor =
                ArgumentCaptor.forClass(MiembroGrupo.class);

        verify(miembroGrupoRepository).save(captor.capture());

        MiembroGrupo miembroGuardado = captor.getValue();

        assertEquals(creador, miembroGuardado.getUsuario());
        assertEquals("ADMIN", miembroGuardado.getRol());
    }

    @Test
    void noDebeCrearGrupoSiElCodigoDeInvitacionYaExiste() {

        GrupoRequestDTO request = new GrupoRequestDTO();
        request.setNombre("Viaje a la playa");
        request.setCodigoInvitacion("PLAYA2026");
        request.setCreadorId(1L);

        when(grupoRepository.existsByCodigoInvitacion("PLAYA2026"))
                .thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> grupoService.guardar(request));

        assertEquals(
                "Ya existe un grupo con ese código de invitación",
                ex.getMessage());

        verify(usuarioRepository, never()).findById(any());
        verify(grupoRepository, never()).save(any());
    }

    @Test
    void noDebeCrearGrupoSiElUsuarioCreadorNoExiste() {

        GrupoRequestDTO request = new GrupoRequestDTO();
        request.setNombre("Viaje a la playa");
        request.setCodigoInvitacion("PLAYA2026");
        request.setCreadorId(99L);

        when(grupoRepository.existsByCodigoInvitacion("PLAYA2026"))
                .thenReturn(false);

        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> grupoService.guardar(request));

        assertEquals(
                "Usuario creador no encontrado",
                ex.getMessage());
    }

    @Test
    void debeCalcularResumenDeGrupoConGastosYLiquidacionesPendientes() {

        Grupo grupo = new Grupo();
        grupo.setId(1L);
        grupo.setNombre("Viaje");

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        MiembroGrupo miembro = new MiembroGrupo();
        miembro.setUsuario(juan);

        Gasto gasto1 = new Gasto();
        gasto1.setMontoTotal(BigDecimal.valueOf(100));

        Gasto gasto2 = new Gasto();
        gasto2.setMontoTotal(BigDecimal.valueOf(50));

        Liquidacion liquidacion = new Liquidacion();
        liquidacion.setMonto(BigDecimal.valueOf(30));

        when(grupoRepository.findById(1L))
                .thenReturn(Optional.of(grupo));

        when(miembroGrupoRepository.findByGrupoId(1L))
                .thenReturn(List.of(miembro));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(gasto1, gasto2));

        when(liquidacionRepository.findByGrupoId(1L))
                .thenReturn(List.of(liquidacion));

        ResumenGrupoDTO resumen =
                grupoService.obtenerResumen(1L);

        assertEquals(1, resumen.getTotalMiembros());
        assertEquals(2, resumen.getTotalGastos());
        assertEquals(
                0,
                resumen.getMontoTotalGastado()
                        .compareTo(BigDecimal.valueOf(150)));
        assertEquals(
                0,
                resumen.getMontoPendienteLiquidar()
                        .compareTo(BigDecimal.valueOf(30)));
    }

    @Test
    void debeGenerarRankingOrdenadoPorSaldoDescendente() {

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        Usuario ana = new Usuario();
        ana.setNombre("Ana");

        // Juan pagó 200, no consumió nada registrado en detalle -> saldo +200
        Gasto gastoJuan = new Gasto();
        gastoJuan.setPagador(juan);
        gastoJuan.setMontoTotal(BigDecimal.valueOf(200));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(gastoJuan));

        // Ana consumió 200 -> saldo -200
        DetalleGasto detalleAna = new DetalleGasto();
        detalleAna.setUsuario(ana);
        detalleAna.setMontoAsignado(BigDecimal.valueOf(200));

        when(detalleGastoRepository.findByGastoGrupoId(1L))
                .thenReturn(List.of(detalleAna));

        List<RankingUsuarioDTO> ranking =
                grupoService.obtenerRanking(1L);

        assertEquals(2, ranking.size());

        // El primero en el ranking debe ser quien tiene el saldo más alto (Juan, +200)
        assertEquals("Juan", ranking.get(0).getNombreUsuario());
        assertEquals(
                0,
                ranking.get(0).getSaldo()
                        .compareTo(BigDecimal.valueOf(200)));

        assertEquals("Ana", ranking.get(1).getNombreUsuario());
        assertEquals(
                0,
                ranking.get(1).getSaldo()
                        .compareTo(BigDecimal.valueOf(-200)));
    }

    @Test
    void debeLanzarExcepcionAlEliminarGrupoConDependenciasAsociadas() {

        Grupo grupo = new Grupo();
        grupo.setId(1L);
        grupo.setNombre("Viaje");

        when(grupoRepository.findById(1L))
                .thenReturn(Optional.of(grupo));

        doThrow(new RuntimeException("violación de integridad"))
                .when(grupoRepository).delete(grupo);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> grupoService.eliminar(1L));

        assertEquals(
                "No se puede eliminar el grupo porque tiene miembros o gastos asociados",
                ex.getMessage());
    }
}