package com.simplify.website.repository;

import com.simplify.website.model.Despesa;
import com.simplify.website.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Integer> {
    List<Despesa> findByUsuarioId(Integer usuarioId);


}
