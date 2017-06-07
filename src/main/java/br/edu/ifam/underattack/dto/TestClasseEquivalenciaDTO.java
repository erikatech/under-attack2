package br.edu.ifam.underattack.dto;

import br.edu.ifam.underattack.model.ValorDeEntrada;

import java.io.Serializable;

/**
 * Created by erika.silva on 22/04/2017.
 */
public class TestClasseEquivalenciaDTO implements Serializable {

    private static final long serialVersionUID = -935666588633608512L;

    private String login;

    private String entradaAluno;

    private ValorDeEntrada valorDeEntrada;

    private Long idDesafio;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEntradaAluno() {
        return entradaAluno;
    }

    public void setEntradaAluno(String entradaAluno) {
        this.entradaAluno = entradaAluno;
    }

    public ValorDeEntrada getValorDeEntrada() {
        return valorDeEntrada;
    }

    public void setValorDeEntrada(ValorDeEntrada valorDeEntrada) {
        this.valorDeEntrada = valorDeEntrada;
    }

    public Long getIdDesafio() {
        return idDesafio;
    }

    public void setIdDesafio(Long idDesafio) {
        this.idDesafio = idDesafio;
    }
}
