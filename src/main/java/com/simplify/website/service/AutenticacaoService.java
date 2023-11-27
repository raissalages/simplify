package com.simplify.website.service;

import com.simplify.website.model.Usuario;
import com.simplify.website.model.UsuarioAutenticado;
import com.simplify.website.repository.UsuarioRepository;
import com.simplify.website.util.Criptografia;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AutenticacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
    * Recebe email e senha do login. Utiliza o algoritmo de criptografia para comparar a senha com a do usuário cujo
    * email está armazenado no banco. Caso o usuário exista (não nulo) e a senha seja a mesma, a sessão é criada e o
    * usuário é retornado.
    */
    public Usuario autenticarUsuario(String email, String senha, HttpSession session)  {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && usuario.getSenha().equals(Criptografia.encriptaSenha(senha))) {
            session.setAttribute("usuarioAutenticado", new UsuarioAutenticado(usuario.getEmail()));

            return usuario;

        }
        return null;
    }

}
