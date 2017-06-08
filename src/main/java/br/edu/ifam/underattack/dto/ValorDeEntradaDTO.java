package br.edu.ifam.underattack.dto;

/**
 * Created by erika.silva on 08/06/2017.
 */
public class ValorDeEntradaDTO {

    private Long id;

    private String descricao;

    private int pontos;

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

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}
