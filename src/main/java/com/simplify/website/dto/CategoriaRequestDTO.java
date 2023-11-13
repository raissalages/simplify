package com.simplify.website.dto;

import java.util.List;


public record CategoriaRequestDTO(String nome, double limite, double valorTotalMensal, List<Integer> despesas){

}

