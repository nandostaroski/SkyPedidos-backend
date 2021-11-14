package com.st4r.sky.services;

import com.st4r.sky.domain.Cidade;
import com.st4r.sky.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repository;

    public List<Cidade> findCidades(Integer estadoId) {
        return repository.findCidades(estadoId);
    }
}
