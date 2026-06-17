package com.gastos.gastos_compartidos.service;

import com.gastos.gastos_compartidos.entity.Grupo;
import com.gastos.gastos_compartidos.entity.MiembroGrupo;
import com.gastos.gastos_compartidos.entity.Usuario;
import com.gastos.gastos_compartidos.repository.GrupoRepository;
import com.gastos.gastos_compartidos.repository.MiembroGrupoRepository;
import com.gastos.gastos_compartidos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MiembroGrupoService {

    private final MiembroGrupoRepository miembroGrupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;

    public MiembroGrupoService(
            MiembroGrupoRepository miembroGrupoRepository,
            UsuarioRepository usuarioRepository,
            GrupoRepository grupoRepository) {

        this.miembroGrupoRepository = miembroGrupoRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
    }

    public List<MiembroGrupo> listarTodos() {
        return miembroGrupoRepository.findAll();
    }

    public MiembroGrupo guardar(MiembroGrupo miembroGrupo) {
        return miembroGrupoRepository.save(miembroGrupo);
    }

    public MiembroGrupo unirseAGrupo(Long usuarioId, String codigoInvitacion) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Grupo grupo = grupoRepository.findByCodigoInvitacion(codigoInvitacion)
                .orElseThrow(() -> new RuntimeException("Código de invitación inválido"));

        boolean yaExiste = miembroGrupoRepository
                .existsByUsuarioIdAndGrupoId(usuarioId, grupo.getId());

        if (yaExiste) {
            throw new RuntimeException("El usuario ya pertenece al grupo");
        }

        MiembroGrupo miembro = new MiembroGrupo();
        miembro.setUsuario(usuario);
        miembro.setGrupo(grupo);
        miembro.setRol("MIEMBRO");
        miembro.setFechaIngreso(LocalDate.now());

        return miembroGrupoRepository.save(miembro);
    }

}
