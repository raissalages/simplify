package com.simplify.website.dto;

import java.util.List;


public record CategoriaRequestDTO(String nome, Integer usuario, double limite, Double valorTotalMensal, List<Integer> despesas){

}

