package com.simplify.website.repository;

import com.simplify.website.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
   Usuario findByEmailAndSenha(String email, String senha);

   Usuario findByEmail(String email);
}
