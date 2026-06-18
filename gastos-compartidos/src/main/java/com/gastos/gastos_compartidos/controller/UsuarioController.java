package com.gastos.gastos_compartidos.controller;

import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public Usuario guardar(@Valid @RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {

        return usuarioService.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {

        usuarioService.eliminar(id);

        return "Usuario eliminado correctamente";
    }
}
