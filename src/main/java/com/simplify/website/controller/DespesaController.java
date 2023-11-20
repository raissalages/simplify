package com.simplify.website.controller;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;
import com.simplify.website.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@SessionAttributes("usuarioAutenticado")
@RequestMapping("/despesa")
public class DespesaController {
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

    @GetMapping("/{id}")
    public List<DespesaResponseDTO> recuperaDespesasPorUsuario(@PathVariable Integer id) {
        return despesaRepository.findByUsuarioId(id).stream().map(DespesaResponseDTO::new).toList();
    }


    @PostMapping
    public void salvarDespesa(@RequestBody DespesaRequestDTO data){
        try {
            despesaRepository.save(new Despesa(data.descricao(), data.valor(), data.data(), usuarioRepository.findById(data.usuario()).get(), categoriaRepository.findById(data.categoria()).get()));

            List<Integer> idDespesas = categoriaRepository.findById(data.categoria()).get().getDespesas()
                    .stream()
                    .map(Despesa::getId)
                    .collect(Collectors.toList());


            categoriaService.editarCategoria(data.categoria(), new CategoriaRequestDTO(
                        categoriaRepository.findById(data.categoria()).get().getNome(),
                        categoriaRepository.findById(data.categoria()).get().getLimite(),
                        ((categoriaRepository.findById(data.categoria()).get().getValorTotalMensal()) + data.valor()) ,
                        idDespesas
                    ), data.valor());


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
    public void deletarDespesa(@PathVariable Integer id){
        categoriaService.removerDespesa(id);
    }
}
