import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.entity.Liquidacion;
import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.repository.DetalleGastoRepository;
import com.gastos.gastos_compartidos.repository.GastoRepository;
import com.gastos.gastos_compartidos.repository.LiquidacionRepository;
import com.gastos.gastos_compartidos.service.LiquidacionService;

public class LiquidacionServiceTest {

    private GastoRepository gastoRepository;
    private DetalleGastoRepository detalleGastoRepository;
    private LiquidacionRepository liquidacionRepository;

    private LiquidacionService liquidacionService;

    @BeforeEach
    void setUp() {

        gastoRepository =
                mock(GastoRepository.class);

        detalleGastoRepository =
                mock(DetalleGastoRepository.class);

        liquidacionRepository =
                mock(LiquidacionRepository.class);

        liquidacionService =
                new LiquidacionService(
                        gastoRepository,
                        detalleGastoRepository,
                        liquidacionRepository);
    }

    @Test
    void debeGenerarLiquidacionCorrectamente() {

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        Usuario ana = new Usuario();
        ana.setNombre("Ana");

        Grupo grupo = new Grupo();
        grupo.setNombre("Viaje");

        Gasto gasto = new Gasto();
        gasto.setGrupo(grupo);
        gasto.setPagador(juan);
        gasto.setMontoTotal(
                BigDecimal.valueOf(300));

        DetalleGasto detalleJuan =
                new DetalleGasto();

        detalleJuan.setUsuario(juan);
        detalleJuan.setMontoAsignado(
                BigDecimal.valueOf(100));

        DetalleGasto detalleAna =
                new DetalleGasto();

        detalleAna.setUsuario(ana);
        detalleAna.setMontoAsignado(
                BigDecimal.valueOf(200));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(gasto));

        when(detalleGastoRepository
                .findByGastoGrupoId(1L))
                .thenReturn(
                        List.of(
                                detalleJuan,
                                detalleAna));

        List<Liquidacion> resultado =
                liquidacionService
                        .calcularLiquidaciones(1L);

        assertEquals(
                1,
                resultado.size());

        Liquidacion liquidacion =
                resultado.get(0);

        assertEquals(
                "Ana",
                liquidacion
                        .getDeudor()
                        .getNombre());

        assertEquals(
                "Juan",
                liquidacion
                        .getAcreedor()
                        .getNombre());

        assertEquals(
                BigDecimal.valueOf(200),
                liquidacion.getMonto());
    }

    @Test
    void debeRetornarListaVaciaSiNoHayGastos() {

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of());

        List<Liquidacion> resultado =
                liquidacionService
                        .calcularLiquidaciones(1L);

        assertTrue(resultado.isEmpty());

        verify(liquidacionRepository)
                .deleteByGrupoId(1L);
    }

    @Test
    void debeGenerarDosLiquidacionesConDosAcreedores() {

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        Usuario ana = new Usuario();
        ana.setNombre("Ana");

        Usuario pedro = new Usuario();
        pedro.setNombre("Pedro");

        Grupo grupo = new Grupo();
        grupo.setNombre("Viaje");

        Gasto gastoJuan = new Gasto();
        gastoJuan.setGrupo(grupo);
        gastoJuan.setPagador(juan);
        gastoJuan.setMontoTotal(
                BigDecimal.valueOf(650));

        Gasto gastoAna = new Gasto();
        gastoAna.setGrupo(grupo);
        gastoAna.setPagador(ana);
        gastoAna.setMontoTotal(
                BigDecimal.valueOf(420));

        Gasto gastoPedro = new Gasto();
        gastoPedro.setGrupo(grupo);
        gastoPedro.setPagador(pedro);
        gastoPedro.setMontoTotal(
                BigDecimal.valueOf(90));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(
                        List.of(
                                gastoJuan,
                                gastoAna,
                                gastoPedro));

        DetalleGasto d1 = new DetalleGasto();
        d1.setUsuario(juan);
        d1.setMontoAsignado(
                BigDecimal.valueOf(440));

        DetalleGasto d2 = new DetalleGasto();
        d2.setUsuario(ana);
        d2.setMontoAsignado(
                BigDecimal.valueOf(360));

        DetalleGasto d3 = new DetalleGasto();
        d3.setUsuario(pedro);
        d3.setMontoAsignado(
                BigDecimal.valueOf(360));

        when(detalleGastoRepository
                .findByGastoGrupoId(1L))
                .thenReturn(
                        List.of(
                                d1,
                                d2,
                                d3));

        List<Liquidacion> resultado =
                liquidacionService
                        .calcularLiquidaciones(1L);

        assertEquals(
                2,
                resultado.size());

        boolean existeLiquidacionJuan =
                resultado.stream()
                        .anyMatch(liq ->
                                liq.getDeudor()
                                        .getNombre()
                                        .equals("Pedro")
                                        &&
                                        liq.getAcreedor()
                                                .getNombre()
                                                .equals("Juan")
                                        &&
                                        liq.getMonto()
                                                .compareTo(
                                                        BigDecimal.valueOf(210))
                                                == 0);

        boolean existeLiquidacionAna =
                resultado.stream()
                        .anyMatch(liq ->
                                liq.getDeudor()
                                        .getNombre()
                                        .equals("Pedro")
                                        &&
                                        liq.getAcreedor()
                                                .getNombre()
                                                .equals("Ana")
                                        &&
                                        liq.getMonto()
                                                .compareTo(
                                                        BigDecimal.valueOf(60))
                                                == 0);

        assertTrue(existeLiquidacionJuan);
        assertTrue(existeLiquidacionAna);
    }

    @Test
    void debeSimplificarDeudasCircularesEntreTresUsuarios() {


        Usuario a = new Usuario();
        a.setNombre("Carla");

        Usuario b = new Usuario();
        b.setNombre("Diego");

        Usuario c = new Usuario();
        c.setNombre("Elena");

        Grupo grupo = new Grupo();
        grupo.setNombre("Departamento");

        // Carla pagó 100, Diego pagó 100, Elena pagó 100 → total 300
        Gasto gastoCarla = new Gasto();
        gastoCarla.setGrupo(grupo);
        gastoCarla.setPagador(a);
        gastoCarla.setMontoTotal(BigDecimal.valueOf(100));

        Gasto gastoDiego = new Gasto();
        gastoDiego.setGrupo(grupo);
        gastoDiego.setPagador(b);
        gastoDiego.setMontoTotal(BigDecimal.valueOf(100));

        Gasto gastoElena = new Gasto();
        gastoElena.setGrupo(grupo);
        gastoElena.setPagador(c);
        gastoElena.setMontoTotal(BigDecimal.valueOf(100));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(gastoCarla, gastoDiego, gastoElena));


        DetalleGasto dA = new DetalleGasto();
        dA.setUsuario(a);
        dA.setMontoAsignado(BigDecimal.valueOf(150));

        DetalleGasto dB = new DetalleGasto();
        dB.setUsuario(b);
        dB.setMontoAsignado(BigDecimal.valueOf(100));

        DetalleGasto dC = new DetalleGasto();
        dC.setUsuario(c);
        dC.setMontoAsignado(BigDecimal.valueOf(50));

        when(detalleGastoRepository.findByGastoGrupoId(1L))
                .thenReturn(List.of(dA, dB, dC));

        List<Liquidacion> resultado =
                liquidacionService.calcularLiquidaciones(1L);


        assertEquals(1, resultado.size());

        assertEquals("Carla", resultado.get(0).getDeudor().getNombre());
        assertEquals("Elena", resultado.get(0).getAcreedor().getNombre());
        assertEquals(BigDecimal.valueOf(50), resultado.get(0).getMonto());
    }

    @Test
    void debeRetornarListaVaciaCuandoTodosEstanSaldados() {

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        Usuario ana = new Usuario();
        ana.setNombre("Ana");

        Grupo grupo = new Grupo();
        grupo.setNombre("Cena");

        Gasto gasto = new Gasto();
        gasto.setGrupo(grupo);
        gasto.setPagador(juan);
        gasto.setMontoTotal(BigDecimal.valueOf(100));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(gasto));

        DetalleGasto detalleJuan = new DetalleGasto();
        detalleJuan.setUsuario(juan);
        detalleJuan.setMontoAsignado(BigDecimal.valueOf(100));

        when(detalleGastoRepository.findByGastoGrupoId(1L))
                .thenReturn(List.of(detalleJuan));

        List<Liquidacion> resultado =
                liquidacionService.calcularLiquidaciones(1L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeRepartirEntreVariosDeudoresConUnSoloPagador() {

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        Usuario ana = new Usuario();
        ana.setNombre("Ana");

        Usuario pedro = new Usuario();
        pedro.setNombre("Pedro");

        Usuario lucia = new Usuario();
        lucia.setNombre("Lucia");

        Grupo grupo = new Grupo();
        grupo.setNombre("Viaje grupal");

        // Juan paga todo (300), el resto no paga nada
        Gasto gasto = new Gasto();
        gasto.setGrupo(grupo);
        gasto.setPagador(juan);
        gasto.setMontoTotal(BigDecimal.valueOf(300));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(gasto));

        DetalleGasto dJuan = new DetalleGasto();
        dJuan.setUsuario(juan);
        dJuan.setMontoAsignado(BigDecimal.valueOf(75));

        DetalleGasto dAna = new DetalleGasto();
        dAna.setUsuario(ana);
        dAna.setMontoAsignado(BigDecimal.valueOf(75));

        DetalleGasto dPedro = new DetalleGasto();
        dPedro.setUsuario(pedro);
        dPedro.setMontoAsignado(BigDecimal.valueOf(75));

        DetalleGasto dLucia = new DetalleGasto();
        dLucia.setUsuario(lucia);
        dLucia.setMontoAsignado(BigDecimal.valueOf(75));

        when(detalleGastoRepository.findByGastoGrupoId(1L))
                .thenReturn(List.of(dJuan, dAna, dPedro, dLucia));

        List<Liquidacion> resultado =
                liquidacionService.calcularLiquidaciones(1L);


        assertEquals(3, resultado.size());

        resultado.forEach(liq -> {
            assertEquals("Juan", liq.getAcreedor().getNombre());
            assertEquals(BigDecimal.valueOf(75), liq.getMonto());
        });
    }

    @Test
    void debeManejarMontosDecimalesConCentavos() {

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        Usuario ana = new Usuario();
        ana.setNombre("Ana");

        Usuario pedro = new Usuario();
        pedro.setNombre("Pedro");

        Grupo grupo = new Grupo();
        grupo.setNombre("Cena con decimales");

        Gasto gasto = new Gasto();
        gasto.setGrupo(grupo);
        gasto.setPagador(juan);
        gasto.setMontoTotal(BigDecimal.valueOf(100.00));

        when(gastoRepository.findByGrupoId(1L))
                .thenReturn(List.of(gasto));

        DetalleGasto dJuan = new DetalleGasto();
        dJuan.setUsuario(juan);
        dJuan.setMontoAsignado(BigDecimal.valueOf(33.34));

        DetalleGasto dAna = new DetalleGasto();
        dAna.setUsuario(ana);
        dAna.setMontoAsignado(BigDecimal.valueOf(33.33));

        DetalleGasto dPedro = new DetalleGasto();
        dPedro.setUsuario(pedro);
        dPedro.setMontoAsignado(BigDecimal.valueOf(33.33));

        when(detalleGastoRepository.findByGastoGrupoId(1L))
                .thenReturn(List.of(dJuan, dAna, dPedro));

        List<Liquidacion> resultado =
                liquidacionService.calcularLiquidaciones(1L);

        assertEquals(2, resultado.size());


        BigDecimal totalLiquidado = resultado.stream()
                .map(Liquidacion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(
                0,
                totalLiquidado.compareTo(BigDecimal.valueOf(66.66)));
    }
}