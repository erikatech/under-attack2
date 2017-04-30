package br.edu.ifam.underattack.model;

import br.edu.ifam.underattack.model.enums.Dificuldade;
import br.edu.ifam.underattack.model.enums.TipoValorEntrada;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "valor_de_entrada")
public class ValorDeEntrada implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1857835619928147477L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private TipoValorEntrada tipo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valorDeEntrada", fetch = FetchType.EAGER)
    private List<ClasseEquivalencia> classesEquivalencia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valorDeEntrada")
    private List<AlunoEncontraValorDeEntrada> alunoEncontraValorDeEntrada;

    @ManyToOne
    private Programa programa;

    @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade;

    public ValorDeEntrada() {
        this.classesEquivalencia = new ArrayList<ClasseEquivalencia>();
    }

    public ValorDeEntrada(String descricao, TipoValorEntrada tipo) {
        this();
        this.descricao = descricao;
        this.tipo = tipo;
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

    public TipoValorEntrada getTipo() {
        return tipo;
    }

    public void setTipo(TipoValorEntrada tipo) {
        this.tipo = tipo;
    }

    public List<ClasseEquivalencia> getClassesEquivalencia() {
        return classesEquivalencia;
    }

    public void setClassesEquivalencia(
            List<ClasseEquivalencia> classesEquivalencia) {
        this.classesEquivalencia = classesEquivalencia;
    }

    public List<AlunoEncontraValorDeEntrada> getAlunoEncontraValorDeEntrada() {
        return alunoEncontraValorDeEntrada;
    }

    public void setAlunoEncontraValorDeEntrada(
            List<AlunoEncontraValorDeEntrada> alunoEncontraValorDeEntrada) {
        this.alunoEncontraValorDeEntrada = alunoEncontraValorDeEntrada;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public boolean isValorEntradaCorreto() {
        return this.tipo.equals(TipoValorEntrada.CORRETO);
    }

    public void adicionarClasseEquivalencia(
            ClasseEquivalencia classeEquivalencia) {
        if (this.classesEquivalencia.contains(classeEquivalencia)) {
            throw new RuntimeException("Já existe!!");
        }

        if (null == classeEquivalencia) {
            throw new RuntimeException("Valor não pode ser nulo!!");
        }

        this.classesEquivalencia.add(classeEquivalencia);
        classeEquivalencia.setValorDeEntrada(this);
    }

    public int getPontos() {
        return dificuldade.getPontos();
    }

}
