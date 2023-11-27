package com.simplify.website.controller;

import com.simplify.website.model.UsuarioAutenticado;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/")
    public String homepage(){
        return "index.html";
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }


    @GetMapping("/cadastro")
    public String cadastro(){
        return "cadastro.html";
    }

    @GetMapping("/paginainicial")
    public String paginaInicial(){
        return "paginainicial.html";
    }

    @GetMapping("/cadastrodespesa")
    public String cadastroDespesa(){
        return "cadastrodespesa.html";
    }

    @GetMapping("/listagemdespesas")
    public String listagemDespesas(){
        return "listagemdespesas.html";
    }

    @GetMapping("/cadastrocategoria")
    public String cadastroCategoria(){
        return "cadastrocategoria.html";
    }

    @GetMapping("/controleporcategoria")
    public String controlePorCategoria(){
        return "controleporcategoria.html";
    }

    @GetMapping("/perfil")
    public String perfil(){
        return "perfil.html";
    }

}
