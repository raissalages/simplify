package com.simplify.website.dto;

import com.simplify.website.model.Usuario;

import java.util.Date;

public record DespesaRequestDTO(String descricao, double valor, Date data, Integer usuario, Integer categoria) {
}
