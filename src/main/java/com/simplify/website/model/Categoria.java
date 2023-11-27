package com.simplify.website.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.simplify.website.dto.CategoriaRequestDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="categoria")
@Table(name="categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;
    @Column
    private String nome;
    @Column
    private double limite;
    @Column
    private Double valorTotalMensal;
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Despesa> despesas;


    public Categoria(CategoriaRequestDTO data) {
        this.nome = data.nome();
        this.usuario = new Usuario();
        usuario.setId(data.usuario());
        this.limite = data.limite();
        this.valorTotalMensal = data.valorTotalMensal();
        for(Integer despesaId : data.despesas()){
            Despesa despesa = new Despesa();
            despesa.setId(despesaId);
        }
    }

    public Categoria(String nome, Usuario usuario, double limite, Double valorTotalMensal, List<Despesa> despesas) {
        this.nome = nome;
        this.usuario = usuario;
        this.limite = limite;
        this.valorTotalMensal = valorTotalMensal;
        this.despesas = despesas;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getId() : "null") +
                ", nome='" + nome + '\'' +
                ", limite=" + limite +
                ", valorTotalMensal=" + valorTotalMensal +
                ", despesas=" + (despesas != null ? despesas.size() + " despesas" : "null") +
                '}';
    }

}
