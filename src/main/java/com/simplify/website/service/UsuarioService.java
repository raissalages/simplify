package com.simplify.website.service;

import com.simplify.website.model.Usuario;
import com.simplify.website.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    public Usuario autenticarUsuario(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha);
    }

}
