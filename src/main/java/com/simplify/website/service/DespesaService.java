package com.simplify.website.service;

import com.simplify.website.dto.CategoriaRequestDTO;
import com.simplify.website.dto.DespesaRequestDTO;
import com.simplify.website.model.Despesa;
import com.simplify.website.repository.CategoriaRepository;
import com.simplify.website.repository.DespesaRepository;
import com.simplify.website.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DespesaService {
    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void cadastrarDespesa(DespesaRequestDTO data, String email){

        despesaRepository.save(new Despesa(data.descricao(), data.valor(), data.data(), usuarioRepository.findByEmail(email), categoriaRepository.findById(data.categoria()).get()));

        List<Integer> idDespesas = categoriaRepository.findById(data.categoria()).get().getDespesas()
                .stream()
                .map(Despesa::getId)
                .collect(Collectors.toList());


        categoriaService.editarCategoria(data.categoria(), new CategoriaRequestDTO(
                categoriaRepository.findById(data.categoria()).get().getNome(),
                categoriaRepository.findById(data.categoria()).get().getUsuario().getId(),
                categoriaRepository.findById(data.categoria()).get().getLimite(),
                ((categoriaRepository.findById(data.categoria()).get().getValorTotalMensal()) + data.valor()) ,
                idDespesas
        ), data.valor());
    }

    public boolean validarNulo(DespesaRequestDTO data){
        return (!data.descricao().isBlank() && !(data.categoria() == null) || !(data.data() == null) && !(data.usuario() == null) && (!( data.valor() < 0)));
    }
}
