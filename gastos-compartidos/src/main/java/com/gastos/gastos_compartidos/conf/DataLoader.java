package com.gastos.gastos_compartidos.conf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gastos.gastos_compartidos.entity.DetalleGasto;
import com.gastos.gastos_compartidos.entity.Gasto;
import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.enums.TipoDivision;
import com.gastos.gastos_compartidos.repository.DetalleGastoRepository;
import com.gastos.gastos_compartidos.repository.GastoRepository;
import com.gastos.gastos_compartidos.repository.GrupoRepository;
import com.gastos.gastos_compartidos.repository.MiembroGrupoRepository;
import com.gastos.gastos_compartidos.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatos(
            UsuarioRepository usuarioRepository,
            GrupoRepository grupoRepository,
            GastoRepository gastoRepository,
            DetalleGastoRepository detalleGastoRepository,
            MiembroGrupoRepository miembroGrupoRepository) {

        return args -> {

            if (usuarioRepository.count() > 0) {
                System.out.println("Datos ya existentes. No se cargan ejemplos.");
                return;
            }

            System.out.println("Cargando datos de ejemplo...");

            
            
            Usuario juan = new Usuario();
            juan.setNombre("Juan");
            juan.setEmail("juan@gmail.com");
            usuarioRepository.save(juan);

            Usuario ana = new Usuario();
            ana.setNombre("Ana");
            ana.setEmail("ana@gmail.com");
            usuarioRepository.save(ana);

            Usuario pedro = new Usuario();
            pedro.setNombre("Pedro");
            pedro.setEmail("pedro@gmail.com");
            usuarioRepository.save(pedro);

            Grupo playa = new Grupo();
            playa.setNombre("Viaje a la Playa");
            playa.setDescripcion("Vacaciones 2025");
            playa.setCodigoInvitacion("PLAYA2025");
            playa.setCreador(juan);
            grupoRepository.save(playa);

            registrarMiembro(juan, playa, "ADMIN", miembroGrupoRepository);
            registrarMiembro(ana, playa, "MIEMBRO", miembroGrupoRepository);
            registrarMiembro(pedro, playa, "MIEMBRO", miembroGrupoRepository);
            
            Grupo departamento = new Grupo();
            departamento.setNombre("Departamento");
            departamento.setDescripcion("Gastos del apartamento");
            departamento.setCodigoInvitacion("DEPTO2025");
            departamento.setCreador(ana);
            grupoRepository.save(departamento);
            
            registrarMiembro(juan, departamento, "MIEMBRO", miembroGrupoRepository);
            registrarMiembro(ana, departamento, "ADMIN", miembroGrupoRepository);
            registrarMiembro(pedro, departamento, "MIEMBRO", miembroGrupoRepository);
            
            crearGasto("Hotel", 300, juan, playa, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Comida", 100, juan, playa, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Cena", 120, ana, playa, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Gasolina", 90, pedro, playa, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Snacks", 50, juan, playa, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            
            crearGasto("Internet", 45, ana, departamento, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Luz", 70, ana, departamento, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Agua", 30, pedro, departamento, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Limpieza", 25, juan, departamento, gastoRepository, detalleGastoRepository, juan, ana, pedro);
            crearGasto("Supermercado", 150, pedro, departamento, gastoRepository, detalleGastoRepository, juan, ana, pedro);

            System.out.println("Datos de ejemplo cargados correctamente con miembros registrados.");
        };
    }

    private void registrarMiembro(Usuario usuario, Grupo grupo, String rol, MiembroGrupoRepository repository) {
        MiembroGrupo miembro = new MiembroGrupo();
        miembro.setUsuario(usuario);
        miembro.setGrupo(grupo);
        miembro.setRol(rol);
        miembro.setFechaIngreso(LocalDate.now());
        repository.save(miembro);
    }

    private void crearGasto(
            String descripcion,
            double monto,
            Usuario pagador,
            Grupo grupo,
            GastoRepository gastoRepository,
            DetalleGastoRepository detalleGastoRepository,
            Usuario... participantes) {

        Gasto gasto = new Gasto();
        gasto.setDescripcion(descripcion);
        gasto.setMontoTotal(BigDecimal.valueOf(monto));
        gasto.setFechaGasto(LocalDate.now());
        gasto.setTipoDivision(TipoDivision.IGUAL);
        gasto.setPagador(pagador);
        gasto.setGrupo(grupo);

        gasto = gastoRepository.save(gasto);

        BigDecimal montoPorPersona = BigDecimal.valueOf(monto)
                .divide(BigDecimal.valueOf(participantes.length), 2, RoundingMode.HALF_UP);

        for (Usuario usuario : participantes) {
            DetalleGasto detalle = new DetalleGasto();
            detalle.setGasto(gasto);
            detalle.setUsuario(usuario);
            detalle.setMontoAsignado(montoPorPersona);
            detalle.setPorcentaje(BigDecimal.ZERO);
            detalleGastoRepository.save(detalle);
        }
    }
}