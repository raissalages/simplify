package com.simplify.website.dto;

import com.simplify.website.model.*;

import java.util.List;

import static com.simplify.website.dto.UsuarioResponseDTO.mapDespesas;

public record CategoriaResponseDTO(Integer id, String nome, Integer usuario, double limite, double valorTotalMensal, List<DespesaResponseDTO> despesas){
    public CategoriaResponseDTO(Categoria categoria){
        this(categoria.getId(), categoria.getNome(), categoria.getUsuario().getId(), categoria.getLimite(), categoria.getValorTotalMensal(), categoria.getDespesas().stream().map(DespesaResponseDTO::new).toList());
    }

    public static CategoriaResponseDTO  newWithoutUsuario(Categoria categoria) {
        return new CategoriaResponseDTO(
          categoria.getId(),
          categoria.getNome(),
          null,
          categoria.getLimite(),
          categoria.getValorTotalMensal(),
          UsuarioResponseDTO.mapDespesas(categoria.getDespesas())
        );
    }
}
