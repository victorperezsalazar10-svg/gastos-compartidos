package com.gastos.gastos_compartidos;

import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.repository.UsuarioRepository;
import com.gastos.gastos_compartidos.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {

        usuarioRepository = mock(UsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void debeListarTodosLosUsuarios() {

        Usuario juan = new Usuario();
        juan.setNombre("Juan");

        Usuario ana = new Usuario();
        ana.setNombre("Ana");

        when(usuarioRepository.findAll())
                .thenReturn(List.of(juan, ana));

        List<Usuario> resultado =
                usuarioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void debeBuscarUsuarioPorIdCuandoExiste() {

        Usuario juan = new Usuario();
        juan.setId(1L);
        juan.setNombre("Juan");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(juan));

        Usuario resultado =
                usuarioService.buscarPorId(1L);

        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void debeLanzarExcepcionSiUsuarioNoExiste() {

        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.buscarPorId(99L));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void debeGuardarUsuarioCuandoElEmailNoExiste() {

        Usuario nuevo = new Usuario();
        nuevo.setNombre("Pedro");
        nuevo.setEmail("pedro@mail.com");

        when(usuarioRepository.existsByEmail("pedro@mail.com"))
                .thenReturn(false);

        when(usuarioRepository.save(nuevo))
                .thenReturn(nuevo);

        Usuario resultado =
                usuarioService.guardar(nuevo);

        assertEquals("Pedro", resultado.getNombre());
        verify(usuarioRepository).save(nuevo);
    }

    @Test
    void noDebeGuardarUsuarioSiElEmailYaExiste() {

        Usuario nuevo = new Usuario();
        nuevo.setNombre("Pedro");
        nuevo.setEmail("pedro@mail.com");

        when(usuarioRepository.existsByEmail("pedro@mail.com"))
                .thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.guardar(nuevo));

        assertEquals(
                "Ya existe un usuario con ese correo",
                ex.getMessage());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void debeActualizarUsuarioCuandoElNuevoEmailNoEstaEnUso() {

        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombre("Juan");
        existente.setEmail("juan@mail.com");

        Usuario actualizado = new Usuario();
        actualizado.setNombre("Juan Carlos");
        actualizado.setEmail("juancarlos@mail.com");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(usuarioRepository.existsByEmail("juancarlos@mail.com"))
                .thenReturn(false);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado =
                usuarioService.actualizar(1L, actualizado);

        assertEquals("Juan Carlos", resultado.getNombre());
        assertEquals("juancarlos@mail.com", resultado.getEmail());
    }

    @Test
    void noDebeActualizarUsuarioSiElNuevoEmailYaPerteneceAOtro() {

        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombre("Juan");
        existente.setEmail("juan@mail.com");

        Usuario actualizado = new Usuario();
        actualizado.setNombre("Juan");
        actualizado.setEmail("ana@mail.com");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        when(usuarioRepository.existsByEmail("ana@mail.com"))
                .thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.actualizar(1L, actualizado));

        assertEquals(
                "Ya existe un usuario con ese correo",
                ex.getMessage());
    }

    @Test
    void debeEliminarUsuarioCuandoNoTieneRestricciones() {

        Usuario juan = new Usuario();
        juan.setId(1L);
        juan.setNombre("Juan");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(juan));

        usuarioService.eliminar(1L);

        verify(usuarioRepository).delete(juan);
    }

    @Test
    void debeLanzarExcepcionSiNoSePuedeEliminarUsuarioConGrupos() {

        Usuario juan = new Usuario();
        juan.setId(1L);
        juan.setNombre("Juan");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(juan));

        doThrow(new RuntimeException("violación de integridad"))
                .when(usuarioRepository).delete(juan);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.eliminar(1L));

        assertEquals(
                "No se puede eliminar el usuario porque pertenece a uno o más grupos",
                ex.getMessage());
    }
}