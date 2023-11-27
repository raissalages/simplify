package com.simplify.website.repository;

import com.simplify.website.model.Categoria;
import com.simplify.website.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByUsuarioId(Integer usuarioId);

}
