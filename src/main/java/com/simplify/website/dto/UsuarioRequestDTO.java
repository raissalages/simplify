package com.simplify.website.dto;

import java.util.Date;
import java.util.List;

public record UsuarioRequestDTO(String nomeCompleto, String email, String senha,  String genero, Date dataNascimento, List<Integer> categorias, List<Integer> despesas) {
}
