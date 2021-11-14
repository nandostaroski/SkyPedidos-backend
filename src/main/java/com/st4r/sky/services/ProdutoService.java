package com.st4r.sky.services;

import com.st4r.sky.domain.Categoria;
import com.st4r.sky.domain.Produto;
import com.st4r.sky.repositories.CategoriaRepository;
import com.st4r.sky.repositories.ProdutoRepository;
import com.st4r.sky.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Produto find(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado. ID:" + id + ", Tipo:" + Produto.class.getName()));
    }

    public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
        List<Categoria> categorias = categoriaRepository.findAllById(ids);

        return repository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
    }

}
