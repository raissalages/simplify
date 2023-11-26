package com.simplify.website.controller;

import com.simplify.website.service.EmailDuplicadoException;
import com.simplify.website.service.UsuarioService;
import com.simplify.website.util.Jwt;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponseDTO> recuperarUsuarios(){
        return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::new).toList();
    }

    @PostMapping
    public void salvarUsuario(@RequestBody UsuarioRequestDTO data){
        List<Despesa> despesas = new ArrayList<>();
        // Verifica se a lista de despesas não é nula antes de iterar
        if (data.despesas() != null) {
            for(Integer id : data.despesas()){
                Despesa despesa = despesaRepository.findById(id).orElse(null);

                // Verifica se a despesa encontrada não é nula antes de adicioná-la à lista
                if (despesa != null) {
                    despesas.add(despesa);
                }
            }
        }

        usuarioRepository.save(new Usuario(data.nomeCompleto(), data.email(), data.senha(), data.genero(), data.dataNascimento(), despesas));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarNovoUsuario(@RequestBody UsuarioRequestDTO data){

        try {
            usuarioService.cadastrarNovoUsuario(data);
            return new ResponseEntity<>("Usuário registrado com sucesso", HttpStatus.CREATED);
        } catch (EmailDuplicadoException e) {
            return new ResponseEntity<>("E-mail já em uso", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticarUsuario(@RequestBody UsuarioLoginDTO data, HttpSession session) {
        // Recupere o usuário do banco de dados usando o serviço
        System.out.println(session.getId());
        Usuario usuario = usuarioService.autenticarUsuario(data.email(), data.senha(), session);
        if (usuario != null) {
            //String token = Jwt.gerarToken(usuario);
            //HttpHeaders headers = new HttpHeaders();
            //headers.add("Authorization", "Bearer " + token);
            System.out.println("Usuario controller /login retornando token: " + session.getId());
            return new ResponseEntity<>(session.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
        }
    }



    @PutMapping("/{id}")
    public void editarUsuario(@PathVariable Integer id, @RequestBody UsuarioRequestDTO data){
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuarioRepository.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Integer id){usuarioRepository.deleteById(id);}
}
