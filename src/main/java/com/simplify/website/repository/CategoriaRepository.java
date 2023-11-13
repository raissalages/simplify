package com.simplify.website.repository;

import com.simplify.website.model.Categoria;
import com.simplify.website.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
