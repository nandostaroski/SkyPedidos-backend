package com.st4r.sky.dto;

import com.st4r.sky.domain.Cidade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class CidadeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;

    public CidadeDTO(Cidade obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
    }
}
