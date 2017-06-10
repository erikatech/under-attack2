package br.edu.ifam.underattack.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "programa")
public class Programa implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1604332821695542568L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Lob
    private String descricao;

    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL)
    private List<ValorDeEntrada> valoresEntrada;

    @OneToMany(mappedBy = "programa")
    private List<Classe> classes;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Desafio desafio;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Programa() {
        this.classes = new ArrayList<Classe>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Classe> getClasses() {
        return classes;
    }

    public void setClasses(List<Classe> classes) {
        this.classes = classes;
    }

    public List<ValorDeEntrada> getValoresEntrada() {
        return this.valoresEntrada;
    }

    public void setValoresEntrada(List<ValorDeEntrada> valoresEntrada) {
        this.valoresEntrada = valoresEntrada;
        for (ValorDeEntrada valorDeEntrada : valoresEntrada) {
            valorDeEntrada.setPrograma(this);
        }
    }

    public void adicionarValorEntrada(ValorDeEntrada valorEntrada) {
        if (this.valoresEntrada.contains(valorEntrada)) {
            throw new RuntimeException("Elemento já existe na lista");
        }

        if (null == valorEntrada) {
            throw new RuntimeException("Elemento não pode ser nulo");
        }

        this.valoresEntrada.add(valorEntrada);
        valorEntrada.setPrograma(this);
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }
}
