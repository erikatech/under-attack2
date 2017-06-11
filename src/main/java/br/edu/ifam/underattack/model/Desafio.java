package br.edu.ifam.underattack.model;

import br.edu.ifam.underattack.model.enums.IndicadorFase;
import br.edu.ifam.underattack.model.enums.NivelDesafio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "desafio")
public class Desafio implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1822263825561055884L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NivelDesafio nivel;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Programa programa;

    @OneToMany(mappedBy = "desafio", fetch = FetchType.EAGER)
    private List<AlunoRealizaDesafio> alunoRealizaDesafio;

    private Integer top;

    private Integer leftPos;

    private String imagem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NivelDesafio getNivel() {
        return nivel;
    }

    public void setNivel(NivelDesafio nivel) {
        this.nivel = nivel;
    }

    public List<AlunoRealizaDesafio> getAlunoRealizaDesafio() {
        return alunoRealizaDesafio;
    }

    public void setAlunoRealizaDesafio(
            List<AlunoRealizaDesafio> alunoRealizaDesafio) {
        this.alunoRealizaDesafio = alunoRealizaDesafio;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getLeftPos() {
        return leftPos;
    }

    public void setLeftPos(Integer leftPos) {
        this.leftPos = leftPos;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getTotalTestesDesafio() {
        int totalTestesDoDesafio = 0;
        Set<ValorDeEntrada> valoresSemRepeticao = new HashSet<ValorDeEntrada>();
        for (ValorDeEntrada valorDeEntrada : this.programa.getValoresEntrada()) {
            valoresSemRepeticao.add(valorDeEntrada);
        }
        for (ValorDeEntrada valor : valoresSemRepeticao) {
            if (valor.isValorEntradaCorreto()) {
                totalTestesDoDesafio += valor.getClassesEquivalencia().size();
            }
        }
        return totalTestesDoDesafio;
    }

    public List<ClasseEquivalencia> getClassesEquivalencia() {
        Set<ValorDeEntrada> valoresCorretos = new HashSet<ValorDeEntrada>();
        List<ClasseEquivalencia> classesEquivalencia = new ArrayList<ClasseEquivalencia>();
        List<ValorDeEntrada> valoresEntrada = this.programa.getValoresEntrada();
        for (ValorDeEntrada valorDeEntrada : valoresEntrada) {
            if (valorDeEntrada.isValorEntradaCorreto()) {
                valoresCorretos.add(valorDeEntrada);
            }
        }

        for (ValorDeEntrada valor : valoresCorretos) {
            classesEquivalencia.addAll(valor.getClassesEquivalencia());
        }
        return classesEquivalencia;
    }

    public String getSituacaoTestadores(Long idAluno) {
        for (AlunoRealizaDesafio ad : alunoRealizaDesafio) {
            if (ad.getDesafio().getId().equals(this.getId())
                    && ad.getAluno().getId().equals(idAluno)
                    && ad.getIndicadorFase().equals(
                    IndicadorFase.FASE_TESTADORES)) {
                return ad.getSituacaoDesafio().getLabel();
            }
        }
        return null;
    }

    public String getSituacaoDesenvolvedores(Long idAluno) {
        for (AlunoRealizaDesafio ad : alunoRealizaDesafio) {
            if (ad.getDesafio().getId().equals(this.getId())
                    && ad.getAluno().getId().equals(idAluno)
                    && ad.getIndicadorFase().equals(
                    IndicadorFase.FASE_DESENVOLVEDORES)) {
                return ad.getSituacaoDesafio().getLabel();
            }
        }
        return null;
    }

}
