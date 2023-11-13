package com.simplify.website.model;

import com.simplify.website.dto.DespesaRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="despesa")
@Table(name="despesa")
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String descricao;
    @Column
    private double valor;
    @Column
    private Date data;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Despesa(DespesaRequestDTO data) {
        this.descricao = data.descricao();
        this.valor = data.valor();
        this.usuario = new Usuario();

        this.usuario.setId(data.usuario());
        this.categoria = new Categoria(); // Certifique-se de inicializar a categoria também, se necessário.

        this.categoria.setId(data.categoria());
    }

    public Despesa(String descricao, double valor, Date data, Usuario usuario, Categoria categoria) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.usuario = usuario;
        this.categoria = categoria;
    }
}
