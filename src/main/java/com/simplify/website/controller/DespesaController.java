package com.simplify.website.controller;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;
import com.simplify.website.service.AutenticacaoService;
import com.simplify.website.service.*;
import com.simplify.website.service.SessaoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/despesa")
public class DespesaController {
    private SessaoService sessaoService;
    private DespesaService despesaService;

    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    public DespesaController(SessaoService sessaoService, DespesaService despesaService) {
        this.sessaoService = sessaoService;
        this.despesaService = despesaService;
    }
    @GetMapping
    public List<DespesaResponseDTO> recuperaDespesas(){
        return despesaRepository.findAll().stream().map(DespesaResponseDTO::new).toList();
    }
/*
    @GetMapping("/{id}")
    public List<DespesaResponseDTO> recuperaDespesasPorUsuario(@PathVariable Integer id) {
        return despesaRepository.findByUsuarioId(id).stream().map(DespesaResponseDTO::new).toList();
    }

    @GetMapping("/despesas")
    public List<DespesaResponseDTO> recuperaDespesasPorUsuario(HttpSession session) {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            Integer idUsuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail()).getId();
            return ResponseEntity.ok(despesaRepository.findByUsuarioId(idUsuario).stream().map(DespesaResponseDTO::new).toList());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
*/
    @GetMapping("/despesas")
    public List<DespesaResponseDTO> recuperaDespesasPorUsuario(HttpSession session) {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            Integer idUsuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail()).getId();
            return despesaRepository.findByUsuarioId(idUsuario)
                    .stream()
                    .map(DespesaResponseDTO::new)
                    .toList();
        } else {
            // Você pode tratar a falta de autenticação retornando uma lista vazia ou lançando uma exceção, conforme a necessidade
            return Collections.emptyList();
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> salvarDespesaa(@RequestBody DespesaRequestDTO data, HttpSession session){
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        //usuarioRepository.findByEmail(usuarioAutenticado.getEmail());

        if (usuarioAutenticado != null) {
            System.out.println("Antes de chamar despesaService.cadastrarDespesa");

            try {
                System.out.println("Dados recebidos: " + data);

                despesaService.cadastrarDespesa(data, usuarioAutenticado.getEmail());
                System.out.println("Depois de chamar despesaService.cadastrarDespesa");

                return ResponseEntity.ok("Despesa cadastrada com sucesso!");

            }catch (EntityNotFoundException e){
                e.printStackTrace();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");

    }
    @PostMapping
    public void salvarDespesa(@RequestBody DespesaRequestDTO data){
        try {

            if(data.categoria() != null) {
                despesaRepository.save(new Despesa(data.descricao(), data.valor(), data.data(), usuarioRepository.findById(data.usuario()).get(), categoriaRepository.findById(data.categoria()).get()));

                List<Integer> idDespesas = categoriaRepository.findById(data.categoria()).get().getDespesas()
                        .stream()
                        .map(Despesa::getId)
                        .collect(Collectors.toList());


                categoriaService.editarCategoria(data.categoria(), new CategoriaRequestDTO(
                        categoriaRepository.findById(data.categoria()).get().getNome(),
                        categoriaRepository.findById(data.categoria()).get().getLimite(),
                        ((categoriaRepository.findById(data.categoria()).get().getValorTotalMensal()) + data.valor()),
                        idDespesas
                ), data.valor());
            }else{
                despesaRepository.save(new Despesa(data.descricao(), data.valor(), data.data(), usuarioRepository.findById(data.usuario()).get(), null));

            }


        }catch (EntityNotFoundException e){
            e.printStackTrace();
        }
    }

    @PutMapping("/{id}")
    public void editarDespesa(@PathVariable Integer id, @RequestBody DespesaRequestDTO data){
        Despesa despesa = new Despesa(data);
        despesa.setId(id);
        despesaRepository.save(despesa);
    }

    @DeleteMapping("/{id}")
    public void deletarDespesa(@PathVariable Integer id, HttpSession session) throws AuthenticationException {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");
        if(usuarioAutenticado != null) {
            categoriaService.removerDespesa(id);
        }else{
            throw new AuthenticationException("Usuário não autenticado. Faça login.");
        }
    }

    @DeleteMapping("/despesas")
    public ResponseEntity<?> deletarTodasDespesas() {
        try {
            // Busca todas as despesas no repositório
            List<Despesa> despesas = despesaRepository.findAll();

            // Itera sobre as despesas e remove cada uma delas
            for (Despesa despesa : despesas) {
                // Remove a despesa da lista de despesas da categoria (se necessário)
                if (despesa.getCategoria() != null) {
                    despesa.getCategoria().getDespesas().remove(despesa);
                    categoriaRepository.save(despesa.getCategoria());
                }

                // Remove a despesa da lista de despesas do usuário (se necessário)
                if (despesa.getUsuario() != null) {
                    despesa.getUsuario().getDespesas().remove(despesa);
                    usuarioRepository.save(despesa.getUsuario());
                }

                // Deleta a despesa
                despesaRepository.delete(despesa);
            }

            return ResponseEntity.ok("Todas as despesas foram deletadas com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar todas as despesas.");
        }
    }
}
