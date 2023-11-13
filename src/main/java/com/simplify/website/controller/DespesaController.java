package com.simplify.website.controller;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping
    public List<DespesaResponseDTO> recuperaDespesas(){
        return despesaRepository.findAll().stream().map(DespesaResponseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public List<DespesaResponseDTO> recuperaDespesasPorUsuario(@PathVariable Integer id) {
        return despesaRepository.findByUsuarioId(id).stream().map(DespesaResponseDTO::new).toList();
    }
/*
    @PostMapping("/cadastrarDespesa")
    public String cadastrarDespesa(@ModelAttribute("despesa") Despesa despesa, Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioAutenticado");

        // Criar a despesa associada ao usuário
        despesa = new Despesa(despesa.getDescricao(), despesa.getValor(), usuario);

        // Salvar a despesa no banco de dados
        despesaService.salvarDespesa(despesa);

        // Redirecionar para a página de sucesso ou outra página apropriada
        return "redirect:/paginaSucesso";
    }*/
    @PostMapping
    public void salvarDespesa(@RequestBody DespesaRequestDTO data){
        try {
            despesaRepository.save(new Despesa(data.descricao(), data.valor(), data.data(), usuarioRepository.findById(data.usuario()).get(), categoriaRepository.findById(data.categoria()).get()));
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
        despesaRepository.deleteById(id);
    }
}
