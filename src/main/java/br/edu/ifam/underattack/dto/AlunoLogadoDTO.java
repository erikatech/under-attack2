package br.edu.ifam.underattack.dto;

/**
 * Created by erika.silva on 22/04/2017.
 */
public class AlunoLogadoDTO {

    private String login;

    private String token;

    private String nivel;

    private int pontos;

    public AlunoLogadoDTO(String login, String token, String nivel, int pontos) {
        this.login = login;
        this.token = token;
        this.nivel = nivel;
        this.pontos = pontos;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }

    public String getNivel() {
        return nivel;
    }
}
