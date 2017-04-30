package br.edu.ifam.underattack.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ingrediente")
public class Ingrediente implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5835868436992139083L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private String nomeImagem;

    @OneToMany(mappedBy = "ingrediente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PocaoMagicaIngrediente> pocaoIngredienteList;

    public Ingrediente() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }

    public List<PocaoMagicaIngrediente> getPocaoIngredienteList() {
        return pocaoIngredienteList;
    }

    public void setPocaoIngredienteList(List<PocaoMagicaIngrediente> pocaoIngredienteList) {
        this.pocaoIngredienteList = pocaoIngredienteList;
    }
}
