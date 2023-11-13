package com.simplify.website.controller;

import com.simplify.website.service.UsuarioService;
import com.simplify.website.util.Jwt;
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
    public void cadastrarNovoUsuario(@RequestBody UsuarioRequestDTO data){
        usuarioRepository.save(new Usuario(data.nomeCompleto(),  data.email(), data.senha(),data.genero(), data.dataNascimento()));

    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticarUsuario(@RequestBody UsuarioLoginDTO data) {
        // Recupere o usuário do banco de dados usando o serviço
        Usuario usuario = usuarioService.autenticarUsuario(data.email(), data.senha());
        if (usuario != null) {
            String token = Jwt.gerarToken(usuario);
            // Inclua o token na resposta
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return new ResponseEntity<>("Login bem-sucedido!", HttpStatus.OK);
        } else {
            // Credenciais inválidas
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
