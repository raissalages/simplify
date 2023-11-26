package com.simplify.website.model;


import com.simplify.website.dto.UsuarioRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="usuario")
@Table(name="usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String nomeCompleto;
    @Column(unique=true)
    private String email;
    @Column
    private String senha;
    @Column
    private String genero;
    @Column
    private Date dataNascimento;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Despesa> despesas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Categoria> categorias;
    public Usuario(UsuarioRequestDTO data){
        this.nomeCompleto = data.nomeCompleto();
        this.email = data.email();
        this.senha = data.senha();
        this.genero = data.genero();
        this.dataNascimento = data.dataNascimento();
        for(Integer despesaId : data.despesas()){
            Despesa despesa = new Despesa();
            despesa.setId(despesaId);
        }
    }

    public Usuario(String nomeCompleto, String email, String senha, String genero, Date dataNascimento, List<Despesa> despesas){
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.despesas = despesas;
    }

    public Usuario(String nomeCompleto, String email, String senha, String genero, Date dataNascimento){
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", genero='" + genero + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", despesas=" + (despesas != null ? despesas.size() + " despesas" : "null") +
                '}';
    }

}
