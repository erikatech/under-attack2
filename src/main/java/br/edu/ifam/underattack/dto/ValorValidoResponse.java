package br.edu.ifam.underattack.dto;

import br.edu.ifam.underattack.model.ValorDeEntrada;

/**
 * Created by erika.silva on 08/06/2017.
 */
public class ValorValidoResponse {

    private boolean isValido;

    private int pontos;

    private ValorDeEntrada valorEntrada;

    public void setValido(boolean valido) {
        isValido = valido;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public void setValorEntrada(ValorDeEntrada valorEntrada) {
        this.valorEntrada = valorEntrada;
    }
}
