package com.simplify.website.controller;

import com.simplify.website.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponseDTO> recuperaCategorias(){
        return categoriaRepository.findAll().stream().map(CategoriaResponseDTO::new).toList();
    }

    @PostMapping
    public void salvarCategoria(@RequestBody CategoriaRequestDTO data){
        List<Despesa> despesas = new ArrayList<>();
        for(Integer id : data.despesas()){
            despesas.add(despesaRepository.findById(id).get());
        }
        if(categoriaService.validarNulo(data))
            categoriaRepository.save(new Categoria(data.nome(), data.limite(), data.valorTotalMensal(), despesas));

        }

    @PutMapping("/{id}")
    public void editarCategoria(@PathVariable Integer id, @RequestBody CategoriaRequestDTO data){
        Categoria categoria = new Categoria(data);
        categoria.setId(id);
        if(categoriaService.validarNulo(data))
            categoriaRepository.save(categoria);

    }

    @DeleteMapping("/{id}")
    public void deletarCategoria(@PathVariable Integer id){
        categoriaRepository.deleteById(id);
    }
}
