package com.simplify.website.controller;

import com.simplify.website.service.CategoriaService;
import com.simplify.website.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.simplify.website.dto.*;
import com.simplify.website.model.*;
import com.simplify.website.repository.*;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /* Endpoint de teste, para recuperar todas as despesas, independente da autenticação.
     * Utilize-o para validar tudo no backend
     */
    @GetMapping
    public List<CategoriaResponseDTO> recuperaCategorias(){
        return categoriaRepository.findAll().stream().map(CategoriaResponseDTO::new).toList();
    }

    @GetMapping("/listar-categorias")
    public List<CategoriaResponseDTO> listarCategorias(HttpSession session) {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            Integer idUsuario = usuarioRepository.findByEmail(usuarioAutenticado.getEmail()).getId();
            return categoriaRepository.findByUsuarioId(idUsuario)
                    .stream()
                    .map(CategoriaResponseDTO::new)
                    .toList();
        } else {
            return Collections.emptyList();
        }


    }

    @GetMapping("{id}")
    public Categoria buscaPorId(@PathVariable Integer id){
        return categoriaRepository.findById(id).get();
    }

    @PostMapping
    public void salvarCategoria(@RequestBody CategoriaRequestDTO data, HttpSession session){

        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            try {
                categoriaService.cadastrarCategoria(data, usuarioAutenticado.getEmail());

            }
            catch(EntityNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    @PutMapping("/{id}")
    public void editarCategoria(@PathVariable Integer id, @RequestBody CategoriaRequestDTO data){
        Categoria categoria = new Categoria(data);
        categoria.setId(id);
        if(categoriaService.validarNulo(data))
            categoriaRepository.save(categoria);

    }

    @DeleteMapping("/{id}")
    public void deletarCategoria(@PathVariable Integer id){
        categoriaRepository.deleteById(id);
    }
    @PatchMapping("/salvar/{id}")
    public void alterarCategoriaPatch(@PathVariable Integer id, @RequestBody CategoriaRequestDTO data, HttpSession session) {
        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado != null) {
            try {
                Categoria categoria = categoriaRepository.findById(id).orElseThrow(EntityNotFoundException::new);

                if (data.nome() != null) {
                    categoria.setNome(data.nome());
                }

                if (data.valorTotalMensal() != null) {
                    categoria.setValorTotalMensal(data.valorTotalMensal());
                }

                    categoriaRepository.save(categoria);
                } catch(EntityNotFoundException e){
                    e.printStackTrace();
                }
            } else{
                System.out.println("Usuario nao autenticado");
            }
        }

    @DeleteMapping("/categorias")
    public ResponseEntity<?> deletarTodasCategorias() {
        try {
            List<Categoria> categorias = categoriaRepository.findAll();

            for (Categoria categoria : categorias) {
                if (categoria.getDespesas() != null) {
                    List<Despesa> despesas = despesaRepository.findAll();
                    for(Despesa despesa : despesas){
                        categoria.getDespesas().remove(despesa);
                        despesaRepository.save(despesa);
                    }
                }

                if (categoria.getUsuario() != null) {
                    categoria.getUsuario().getDespesas().remove(categoria);
                    usuarioRepository.save(categoria.getUsuario());
                }

                categoriaRepository.delete(categoria);
            }

            return ResponseEntity.ok("Todas as categorias foram deletadas com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar todas as despesas.");
        }
    }

}
