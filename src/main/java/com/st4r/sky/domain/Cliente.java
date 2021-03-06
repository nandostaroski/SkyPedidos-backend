package com.st4r.sky.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.st4r.sky.domain.enums.Perfil;
import com.st4r.sky.domain.enums.TipoCliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter @Setter @NoArgsConstructor
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERFIL")
    private final Set<Integer> perfis = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    @Column(unique = true)
    private String email;
    private String CpfOuCnpj;
    private Integer tipo;
    @JsonIgnore
    private String senha;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "TELEFONE")
    private Set<String> telefones = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipoCliente, String senha) {
        super();
        this.id = id;
        this.nome = nome;
        this.email = email;
        CpfOuCnpj = cpfOuCnpj;
        this.tipo = (tipoCliente == null) ? null : tipoCliente.getCod();
        this.senha = senha;
        addPerfil(Perfil.CLIENTE);
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
    }

    public void addPerfil(Perfil perfil) {
        perfis.add(perfil.getCod());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cliente other = (Cliente) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

}
