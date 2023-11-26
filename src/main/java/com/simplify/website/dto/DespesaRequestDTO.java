package com.simplify.website.dto;

import com.simplify.website.model.Usuario;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.Optional;

public record DespesaRequestDTO( String descricao,
                                 @Positive double valor,
                                Date data,
                                 Integer usuario,
                                 Integer categoria) {
}
