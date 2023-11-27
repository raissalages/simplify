package com.simplify.website.controller;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;
import com.simplify.website.service.*;
import jakarta.servlet.http.HttpSession;
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
    @Autowired
    private DespesaService despesaService;

    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping
    public List<DespesaResponseDTO> recuperaDespesas(){
        return despesaRepository.findAll().stream().map(DespesaResponseDTO::new).toList();
    }

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
            System.out.println("nem foi");
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    public DespesaResponseDTO recuperaDespesasPorUsuario(@PathVariable Integer id, HttpSession session) {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            Integer idUsuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail()).getId();
            System.out.println("id usuario:" + idUsuario);
            Despesa despesa = despesaRepository.findById(id).orElse(null);
            System.out.println("despesa:" + despesa.getId() );

            if (despesa != null && despesa.getUsuario().getId().equals(idUsuario)) {

                Categoria categoria = despesa.getCategoria();


                CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(categoria);

                DespesaResponseDTO despesaDTO = new DespesaResponseDTO(despesa.getId(), despesa.getDescricao(),
                        despesa.getValor(), despesa.getData(), idUsuario, despesa.getCategoria().getId());

                return despesaDTO;
            }
        }
        else{
            System.out.println("nao autenticou bleble");
        }
        return null;
        }


    @PostMapping("/cadastrar")
    public ResponseEntity<String> salvarDespesaa(@RequestBody DespesaRequestDTO data, HttpSession session){
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");


        if (usuarioAutenticado != null) {

            try {

                despesaService.cadastrarDespesa(data, usuarioAutenticado.getEmail());

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
                        categoriaRepository.findById(data.categoria()).get().getUsuario().getId(),
                        categoriaRepository.findById(data.categoria()).get().getLimite(),
                        ((categoriaRepository.findById(data.categoria()).get().getValorTotalMensal()) + data.valor()),
                        idDespesas
                ), data.valor());
            }else{
                throw new NullPointerException();
            }


        }catch (EntityNotFoundException e){
            e.printStackTrace();
        }
    }


    @PutMapping("/{id}")
    public void editarDespesa(@PathVariable Integer id, @RequestBody DespesaRequestDTO data, HttpSession session) throws AuthenticationException {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            Usuario usuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail());
            Despesa despesa = despesaRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            if (usuario != null) {
                Categoria categoriaAntiga = despesa.getCategoria();
                Categoria categoriaNova = (data.categoria() == null) ? categoriaAntiga : categoriaRepository.findById(data.categoria()).orElse(null);

                double diferencaValorCategoria = 0;

                // Se a categoria foi alterada
                if (categoriaNova != null && !categoriaNova.getId().equals(categoriaAntiga.getId())) {
                    // Atualiza a categoria antiga
                    categoriaAntiga.setValorTotalMensal(categoriaAntiga.getValorTotalMensal() - despesa.getValor());
                    categoriaRepository.save(categoriaAntiga);

                    // Atualiza a categoria nova
                    categoriaNova.setValorTotalMensal(categoriaNova.getValorTotalMensal() + data.valor());
                    categoriaRepository.save(categoriaNova);

                    diferencaValorCategoria = data.valor();
                } else if (diferencaValorCategoria != 0) {
                    // Apenas atualiza a categoria antiga se houver uma mudança no valor
                    categoriaAntiga.setValorTotalMensal(categoriaAntiga.getValorTotalMensal() + diferencaValorCategoria);
                    categoriaRepository.save(categoriaAntiga);
                }

                // Atualiza a despesa
                despesa.setValor(data.valor());
                despesa.setCategoria(categoriaNova);
                despesa.setData(data.data());
                despesa.setId(id);
                despesa.setUsuario(usuario);
                despesaRepository.save(despesa);
            } else {
                System.out.println("Usuário não encontrado");
            }
        } else {
            System.out.println("Usuário não autenticado");
        }
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
