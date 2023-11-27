package com.simplify.website.service;

import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.CategoriaRepository;
import com.simplify.website.repository.DespesaRepository;
import com.simplify.website.repository.UsuarioRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    public void cadastrarCategoria(CategoriaRequestDTO data, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null) {
            // Verifica se a lista de despesas não é nula antes de iterar
            List<Despesa> despesas = new ArrayList<>();
            if (data.despesas() != null) {
                for (Integer idDespesa : data.despesas()) {
                    Despesa despesa = despesaRepository.findById(idDespesa).orElse(null);

                    // Verifica se a despesa encontrada não é nula antes de adicioná-la à lista
                    if (despesa != null) {
                        despesas.add(despesa);
                    }
                }
            }


            // Adiciona a nova categoria à lista de categorias do usuário
            categoriaRepository.save(new Categoria(data.nome(), usuarioRepository.findByEmail(email), data.limite(), data.valorTotalMensal(), despesas));
            //usuario.getCategorias().add(new Categoria(data.nome(), usuarioRepository.findByEmail(email), data.limite(), data.valorTotalMensal(), despesas));
            // Salva as alterações no banco de dados
            usuarioRepository.save(usuario);

        } else {
            // Lida com o caso em que o usuário não foi encontrado
            // Pode lançar uma exceção ou lidar de outra forma conforme necessário
            throw new EntityNotFoundException("Usuário não encontrado");
        }
    }
}
