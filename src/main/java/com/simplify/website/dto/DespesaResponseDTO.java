package com.simplify.website.dto;

import com.simplify.website.model.*;

import java.util.Date;

public record DespesaResponseDTO (Integer id, String descricao, double valor, Date data, Integer usuario, Integer categoria){
    public DespesaResponseDTO(Despesa despesa){
        this(despesa.getId(), despesa.getDescricao(), despesa.getValor(), despesa.getData(),
               despesa.getUsuario().getId(), despesa.getCategoria().getId());
    }

    public static DespesaResponseDTO newWithoutUsuario(Despesa despesa) {
        return new DespesaResponseDTO(
                despesa.getId(),
                despesa.getDescricao(),
                despesa.getValor(),
                despesa.getData(),
                null,  // Define como null para evitar referência cíclica
                despesa.getCategoria().getId()
        );
    }
}
