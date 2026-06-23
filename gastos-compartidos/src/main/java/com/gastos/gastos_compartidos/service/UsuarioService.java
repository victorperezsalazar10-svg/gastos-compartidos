package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
    }

    public Usuario guardar(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException(
                    "Ya existe un usuario con ese correo");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuarioActualizado) {

        Usuario usuario = buscarPorId(id);

        if (!usuario.getEmail().equals(usuarioActualizado.getEmail())
                && usuarioRepository.existsByEmail(
                usuarioActualizado.getEmail())) {

            throw new RuntimeException(
                    "Ya existe un usuario con ese correo");
        }

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {

        Usuario usuario = buscarPorId(id);

        try {
            usuarioRepository.delete(usuario);
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se puede eliminar el usuario porque pertenece a uno o más grupos");
        }
    }
}
