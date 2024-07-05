package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

import com.tallerwebi.dominio.excepcion.UsuarioNoActivado;
import com.tallerwebi.servicio.ServicioLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private final RepositorioUsuario repositorioUsuario;
    private final Email emailSender;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario, Email emailSender){
        this.repositorioUsuario = repositorioUsuario;
        this.emailSender = emailSender;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) throws UsuarioNoActivado {
        Usuario usuario = repositorioUsuario.buscarUsuario(email, password);
        if (usuario != null) {
            if (!usuario.isActivo()) {
                throw new UsuarioNoActivado();
            }
            return usuario;
        }
        return null;
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = repositorioUsuario.buscar(usuario.getEmail());
        if (usuarioEncontrado != null) {
            throw new UsuarioExistente();
        }
        usuario.setActivo(false);
        usuario.setRol("USER");
        usuario.setConfirmationToken(UUID.randomUUID().toString());
        repositorioUsuario.guardar(usuario);
        this.sendMail(usuario.getEmail(), usuario.getConfirmationToken());
    }

    @Override
    public void activarUsuario(String token) throws NoExisteUsuario {
        Usuario usuario = repositorioUsuario.buscarPorToken(token);
        if (usuario == null) {
            throw new NoExisteUsuario();
        }
        usuario.setActivo(true);
        repositorioUsuario.modificar(usuario);
    }

    private void sendMail(String email, String token) {
        String subject = "Confirmación de cuenta.";
        String confirmationUrl = "http://localhost:8080/turesto/confirmar?token=" + token;
        String text = "Para activar su cuenta, haga clic en el siguiente enlace: " + confirmationUrl;
        this.emailSender.sendSimpleMessage(email, subject, text);
    }

}

