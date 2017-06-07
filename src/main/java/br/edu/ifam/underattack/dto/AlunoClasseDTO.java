package br.edu.ifam.underattack.dto;

import br.edu.ifam.underattack.model.AlunoEncontraClasseEquivalencia;
import br.edu.ifam.underattack.model.ClasseEquivalencia;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erika.silva on 22/04/2017.
 */
public class AlunoClasseDTO implements Serializable {

    private static final long serialVersionUID = -935666588633608512L;

    private List<AlunoEncontraClasseEquivalencia> alunoEncontraClasseEquivalencia;

    private int pontos;

    public List<AlunoEncontraClasseEquivalencia> getAlunoEncontraClasseEquivalencia() {
        return alunoEncontraClasseEquivalencia;
    }

    public void setAlunoEncontraClasseEquivalencia(List<AlunoEncontraClasseEquivalencia> alunoEncontraClasseEquivalencia) {
        this.alunoEncontraClasseEquivalencia = alunoEncontraClasseEquivalencia;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}
