package com.gastos.gastos_compartidos;

import com.gastos.gastos_compartidos.dto.GastoRequestDTO;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.enums.TipoDivision;
import com.gastos.gastos_compartidos.repository.DetalleGastoRepository;
import com.gastos.gastos_compartidos.repository.GastoRepository;
import com.gastos.gastos_compartidos.repository.MiembroGrupoRepository;
import com.gastos.gastos_compartidos.service.GastoService;
import com.gastos.gastos_compartidos.service.GrupoService;
import com.gastos.gastos_compartidos.service.LiquidacionService;
import com.gastos.gastos_compartidos.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GastoServiceTest {

    private GastoRepository gastoRepository;
    private MiembroGrupoRepository miembroGrupoRepository;
    private DetalleGastoRepository detalleGastoRepository;
    private UsuarioService usuarioService;
    private GrupoService grupoService;
    private LiquidacionService liquidacionService;

    private GastoService gastoService;

    @BeforeEach
    void setUp() {

        gastoRepository =
                mock(GastoRepository.class);

        miembroGrupoRepository =
                mock(MiembroGrupoRepository.class);

        detalleGastoRepository =
                mock(DetalleGastoRepository.class);

        usuarioService =
                mock(UsuarioService.class);

        grupoService =
                mock(GrupoService.class);

        liquidacionService =
                mock(LiquidacionService.class);

        gastoService =
                new GastoService(
                        gastoRepository,
                        miembroGrupoRepository,
                        detalleGastoRepository,
                        usuarioService,
                        grupoService,
                        liquidacionService);
    }

    @Test
    void debeGuardarGastoConDivisionIgual() {

        Usuario juan = new Usuario();
        juan.setId(1L);
        juan.setNombre("Juan");

        Grupo grupo = new Grupo();
        grupo.setId(1L);
        grupo.setNombre("Viaje");

        MiembroGrupo miembro = new MiembroGrupo();
        miembro.setUsuario(juan);

        GastoRequestDTO request =
                new GastoRequestDTO();

        request.setDescripcion("Cena");
        request.setMontoTotal(
                BigDecimal.valueOf(100));

        request.setFechaGasto(
                LocalDate.now());

        request.setTipoDivision("IGUAL");
        request.setPagadorId(1L);
        request.setGrupoId(1L);

        when(usuarioService.buscarPorId(1L))
                .thenReturn(juan);

        when(grupoService.buscarPorId(1L))
                .thenReturn(grupo);

        when(miembroGrupoRepository
                .existsByUsuarioIdAndGrupoId(
                        1L,
                        1L))
                .thenReturn(true);

        when(miembroGrupoRepository
                .findByGrupoId(1L))
                .thenReturn(List.of(miembro));

        when(gastoRepository.save(any(Gasto.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0));

        Gasto resultado =
                gastoService.guardar(request);

        assertNotNull(resultado);

        assertEquals(
                "Cena",
                resultado.getDescripcion());

        assertEquals(
                TipoDivision.IGUAL,
                resultado.getTipoDivision());

        verify(gastoRepository)
                .save(any(Gasto.class));

        verify(liquidacionService)
                .calcularLiquidaciones(1L);
    }

    @Test
    void debeLanzarExcepcionSiPagadorNoPerteneceAlGrupo() {

        Usuario juan = new Usuario();
        juan.setId(1L);

        Grupo grupo = new Grupo();
        grupo.setId(1L);

        GastoRequestDTO request =
                new GastoRequestDTO();

        request.setDescripcion("Cena");
        request.setMontoTotal(
                BigDecimal.valueOf(100));

        request.setFechaGasto(
                LocalDate.now());

        request.setTipoDivision("IGUAL");
        request.setPagadorId(1L);
        request.setGrupoId(1L);

        when(usuarioService.buscarPorId(1L))
                .thenReturn(juan);

        when(grupoService.buscarPorId(1L))
                .thenReturn(grupo);

        when(miembroGrupoRepository
                .existsByUsuarioIdAndGrupoId(
                        1L,
                        1L))
                .thenReturn(false);

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> gastoService.guardar(request));

        assertEquals(
                "El pagador no pertenece al grupo",
                ex.getMessage());
    }

    @Test
    void debeListarTodosLosGastos() {

        when(gastoRepository.findAll())
                .thenReturn(List.of(
                        new Gasto(),
                        new Gasto()));

        List<Gasto> gastos =
                gastoService.listarTodos();

        assertEquals(2, gastos.size());
    }

    @Test
    void debeListarGastosPorGrupo() {

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(
                        new Gasto(),
                        new Gasto(),
                        new Gasto()));

        List<Gasto> gastos =
                gastoService.listarPorGrupo(1L);

        assertEquals(3, gastos.size());
    }

    @Test
    void debeBuscarGastoPorId() {

        Gasto gasto = new Gasto();
        gasto.setDescripcion("Hotel");

        when(gastoRepository.findById(1L))
                .thenReturn(
                        java.util.Optional.of(gasto));

        Gasto resultado =
                gastoService.buscarPorId(1L);

        assertEquals(
                "Hotel",
                resultado.getDescripcion());
    }

    @Test
    void debeLanzarExcepcionSiGastoNoExiste() {

        when(gastoRepository.findById(99L))
                .thenReturn(
                        java.util.Optional.empty());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> gastoService.buscarPorId(99L));

        assertEquals(
                "Gasto no encontrado",
                ex.getMessage());
    }
}
