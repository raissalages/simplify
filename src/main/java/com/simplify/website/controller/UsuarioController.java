package com.simplify.website.controller;

import com.simplify.website.Exception.EmailDuplicadoException;
import com.simplify.website.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;


import javax.persistence.EntityNotFoundException;
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
    private CategoriaRepository categoriaRepository;
    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponseDTO> recuperarUsuarios(){
        return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::new).toList();
    }

    @GetMapping("/perfil")
    public Usuario perfilUsuario(HttpSession session){
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            try {

                return usuarioRepository.findByEmail(usuarioAutenticado.getEmail());
            }
            catch(EntityNotFoundException e){
                e.printStackTrace();
            }
        }
        return null;
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

        List<Categoria> categorias = new ArrayList<>();
        // Verifica se a lista de despesas não é nula antes de iterar
        if (data.categorias() != null) {
            for(Integer id : data.categorias()){
                Categoria categoria = categoriaRepository.findById(id).orElse(null);

                // Verifica se a despesa encontrada não é nula antes de adicioná-la à lista
                if (categoria != null) {
                    categorias.add(categoria);
                }
            }
        }

        usuarioRepository.save(new Usuario(data.nomeCompleto(), data.email(), data.senha(), data.genero(), data.dataNascimento(), categorias, despesas));
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



    @PutMapping("/salvar/{id}")
    public void editarUsuario(@PathVariable Integer id, @RequestBody UsuarioRequestDTO data, HttpSession session){
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            try {
                Usuario usuario = usuarioRepository.findById(id).orElseThrow(EntityNotFoundException::new);
                System.out.println("Nome Completo: " + data.nomeCompleto());
                System.out.println("Email: " + data.email());
                System.out.println("Gênero: " + data.genero());
                System.out.println("Data de Nascimento: " + data.dataNascimento());
                System.out.println("Senha: " + data.senha());

                usuario.setGenero(data.genero());
                usuario.setNomeCompleto(data.nomeCompleto());
                usuario.setDataNascimento(data.dataNascimento());
                usuario.setEmail(data.email());
                usuario.setSenha(data.senha());
                usuarioRepository.save(usuario);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usuario nao autenticado");
        }
    }

    @PatchMapping("/salvar/{id}")
    public void alterarUsuarioPatch(@PathVariable Integer id, @RequestBody UsuarioRequestDTO data, HttpSession session) {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            try {
                Usuario usuario = usuarioRepository.findById(id).orElseThrow(EntityNotFoundException::new);

                if (data.genero() != null) {
                    usuario.setGenero(data.genero());
                }

                if (data.nomeCompleto() != null) {
                    usuario.setNomeCompleto(data.nomeCompleto());
                }

                if (data.dataNascimento() != null) {
                    usuario.setDataNascimento(data.dataNascimento());
                }

                if (data.email() != null) {
                    usuario.setEmail(data.email());
                }

                if (data.senha() != null) {
                    usuario.setSenha(data.senha());
                }

                usuarioRepository.save(usuario);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usuario nao autenticado");
        }
    }
    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Integer id){usuarioRepository.deleteById(id);}



    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
