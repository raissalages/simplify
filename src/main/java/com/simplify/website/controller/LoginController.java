package com.simplify.website.controller;

import com.google.protobuf.ServiceException;
import com.simplify.website.model.Usuario;
import com.simplify.website.model.UsuarioAutenticado;
import com.simplify.website.service.AutenticacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private HttpSession session;
/* voltar p esse se o retorno com responseentity nao funcionar
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session) {
        Usuario usuario = autenticacaoService.autenticarUsuario(email, senha);

        if (usuario != null) {
            // Armazenar o usuário na sessão
            session.setAttribute("usuarioAutenticado", new UsuarioAutenticado(usuario.getEmail()));
            return "redirect:/paginainicial"; // Redirecionar para a página principal após o login bem-sucedido
        } else {
            // Tratar falha de autenticação
            return "redirect:/login?error";
        }
    }
    */

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha, HttpSession session) {
        Usuario usuario = null;
        try {
            usuario = autenticacaoService.autenticarUsuario(email, senha, session);

            if (usuario != null) {
                // Armazenar o usuário na sessão
                session.setAttribute("usuarioAutenticado", new UsuarioAutenticado(usuario.getEmail()));

                // Retornar o ID da sessão como parte da resposta
                System.out.println(session.getId());
                return ResponseEntity.ok(session.getId());
            } else {
                // Tratar falha de autenticação
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
