package com.simplify.website.service;

import com.simplify.website.Exception.EmailDuplicadoException;
import com.simplify.website.dto.UsuarioRequestDTO;
import com.simplify.website.model.Categoria;
import com.simplify.website.model.Despesa;
import com.simplify.website.model.Usuario;
import com.simplify.website.repository.CategoriaRepository;
import com.simplify.website.repository.DespesaRepository;
import com.simplify.website.repository.UsuarioRepository;
import com.simplify.website.util.Criptografia;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private AutenticacaoService autenticacaoService;

    public Usuario autenticarUsuario(String email, String senha, HttpSession session) {
        try {
            Usuario usuario =  autenticacaoService.autenticarUsuario(email,senha, session);

            System.out.println("Autenticacao retorna nulo (teste): " + usuario);
            return autenticacaoService.autenticarUsuario(email,senha, session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    public void cadastrarNovoUsuario(UsuarioRequestDTO usuario) throws EmailDuplicadoException {
        if (usuarioRepository.findByEmail(usuario.email()) != null) {
            throw new EmailDuplicadoException("O e-mail já está em uso");
        }
        else {
            List<Despesa> despesas = new ArrayList<>();
            // Verifica se a lista de despesas não é nula antes de iterar
            if (usuario.despesas() != null) {
                for(Integer id : usuario.despesas()){
                    Despesa despesa = despesaRepository.findById(id).orElse(null);

                    // Verifica se a despesa encontrada não é nula antes de adicioná-la à lista
                    if (despesa != null) {
                        despesas.add(despesa);
                    }
                }
            }

            List<Categoria> categorias = new ArrayList<>();
            // Verifica se a lista de despesas não é nula antes de iterar
            if (usuario.categorias() != null) {
                for(Integer id : usuario.categorias()){
                    Categoria categoria = categoriaRepository.findById(id).orElse(null);

                    // Verifica se a despesa encontrada não é nula antes de adicioná-la à lista
                    if (categoria != null) {
                        categorias.add(categoria);
                    }
                }
            }
            usuarioRepository.save(new Usuario(usuario.nomeCompleto(), usuario.email(), Criptografia.encriptaSenha(usuario.senha()), usuario.genero(), usuario.dataNascimento(), categorias, despesas));
        }
    }

}
