package com.st4r.sky.services;

import com.st4r.sky.domain.Categoria;
import com.st4r.sky.dto.CategoriaDTO;
import com.st4r.sky.repositories.CategoriaRepository;
import com.st4r.sky.services.exceptions.DataIntegrityException;
import com.st4r.sky.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria find(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado. ID:" + id + ", Tipo:" + Categoria.class.getName()));
    }

    public Categoria insert(Categoria obj) {
        obj.setId(null);
        return repository.save(obj);
    }

    public Categoria update(Categoria obj) {
        Categoria newObj = find(obj.getId());
        updateData(newObj, obj);
        return repository.save(newObj);
    }

    private void updateData(Categoria newObj, Categoria obj) {
        newObj.setNome(obj.getNome());
    }

    public void delete(Integer id) {
        find(id);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma Categoria que possui produtos.");
        }
    }

    public List<Categoria> findAll() {

        return repository.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linesPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);

        return repository.findAll(pageRequest);
    }

    public Categoria fromDTO(CategoriaDTO objDto) {
        return new Categoria(objDto.getId(), objDto.getNome());
    }
}
