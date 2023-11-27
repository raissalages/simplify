package com.simplify.website.model;

public class UsuarioAutenticado {

    /*
    * Classe criada para armazenar o email do usuário na sessão a fim de autenticar outras ações pelo email armazenado.
    * A autenticação é feita por um serviço (AutenticacaoService).
    */
    private String email;

    public UsuarioAutenticado(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
