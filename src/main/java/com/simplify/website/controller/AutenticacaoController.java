package com.simplify.website.controller;
/*
import com.simplify.website.service.UsuarioService;
import com.simplify.website.model.Usuario;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/autenticar")
    public ResponseEntity<String> autenticarUsuario(
            @RequestParam String email,
            @RequestParam String senha
    ) {
        Usuario usuario = usuarioService.autenticarUsuario(email, senha);

        if (usuario != null) {
            return new ResponseEntity<>("Login bem-sucedido!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
        }
    }
}
*/