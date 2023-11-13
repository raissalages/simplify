package com.simplify.website.dto;

import com.simplify.website.model.*;

import java.util.Date;
import java.util.List;

public record UsuarioResponseDTO (Integer id, String nomeCompleto,  String email, String senha, String genero, Date dataNascimento, List<DespesaResponseDTO> despesas){
    public UsuarioResponseDTO(Usuario usuario){
        this(usuario.getId(), usuario.getNomeCompleto(),usuario.getEmail(), usuario.getSenha(),  usuario.getGenero(), usuario.getDataNascimento(), usuario.getDespesas().stream().map(DespesaResponseDTO::new).toList());

    }

    private static List<DespesaResponseDTO> mapDespesas(List<Despesa> despesas) {
        return despesas.stream().map(DespesaResponseDTO::newWithoutUsuario).toList();
    }
}
