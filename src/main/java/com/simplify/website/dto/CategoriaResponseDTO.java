package com.simplify.website.dto;

import com.simplify.website.model.*;

import java.util.List;

public record CategoriaResponseDTO(Integer id, String nome, double limite, double valorTotalMensal, List<DespesaResponseDTO> despesas){
    public CategoriaResponseDTO(Categoria categoria){
        this(categoria.getId(), categoria.getNome(), categoria.getLimite(), categoria.getValorTotalMensal(), categoria.getDespesas().stream().map(DespesaResponseDTO::new).toList());
    }

}
