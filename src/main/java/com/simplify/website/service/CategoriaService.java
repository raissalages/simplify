package com.simplify.website.service;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.CategoriaRepository;
import com.simplify.website.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private DespesaRepository despesaRepository;

    @Transactional
    public void editarCategoria(Integer id, CategoriaRequestDTO data, Double valor) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        categoria.setNome(data.nome());
        categoria.setLimite(data.limite());
        categoria.setValorTotalMensal(categoria.getValorTotalMensal() + valor);

        List<Integer> idDespesas = data.despesas();
        if (idDespesas != null && !idDespesas.isEmpty()) {
            List<Despesa> despesas = despesaRepository.findAllById(idDespesas);

            for (Despesa despesa : despesas) {
                despesa.setCategoria(categoria);
            }

            categoria.setDespesas(despesas);
            categoriaRepository.save(categoria);
        }
    }

    @Transactional
    public void removerDespesa(Integer id) {
        Despesa despesa = despesaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Categoria categoria = despesa.getCategoria();

        categoria.setValorTotalMensal(categoria.getValorTotalMensal() - despesa.getValor());
        categoria.getDespesas().remove(despesa);

        despesaRepository.deleteById(id);

        categoriaRepository.save(categoria);
    }

    public boolean validarNulo(CategoriaRequestDTO data){
        return (!data.nome().isBlank() && !data.nome().isEmpty()) || (!(data.limite() <= 0));
    }

    public List<String> listarNomesCategorias() {
        List<String> categorias = new ArrayList<>();

        for (Categoria categoria : categoriaRepository.findAll()
             ) {
            categorias.add(categoria.getNome());
        }
        return categorias;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

}