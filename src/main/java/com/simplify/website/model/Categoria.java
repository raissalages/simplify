package com.simplify.website.model;


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
    @Column
    private String nome;
    @Column
    private double limite;
    @Column
    private double valorTotalMensal;
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Despesa> despesas;


    public Categoria(CategoriaRequestDTO data) {
        this.nome = data.nome();
        this.limite = data.limite();
        this.valorTotalMensal = data.valorTotalMensal();
        for(Integer despesaId : data.despesas()){
            Despesa despesa = new Despesa();
            despesa.setId(despesaId);
        }
    }

    public Categoria(String nome, double limite, double valorTotalMensal, List<Despesa> despesas) {
        this.nome = nome;
        this.limite = limite;
        this.valorTotalMensal = valorTotalMensal;
        this.despesas = despesas;
    }
}
