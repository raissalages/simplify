package com.simplify.website.service;

import com.simplify.website.model.Usuario;
import com.simplify.website.model.UsuarioAutenticado;
import com.simplify.website.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AutenticacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticarUsuario(String email, String senha, HttpSession session)  {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            System.out.println("AutenticacaoService.autenticarUsuario(): " + session.getId());
            session.setAttribute("usuarioAutenticado", new UsuarioAutenticado(usuario.getEmail()));
            System.out.println("Autenticadooo");

            return usuario;

        }
        System.out.println("nao rolou");
        return null;
    }

}
