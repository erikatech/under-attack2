package br.edu.ifam.underattack.dto;

/**
 * Created by erika.silva on 22/04/2017.
 */
public class UsuarioLogadoDTO {

    private String login;

    private String token;

    public UsuarioLogadoDTO(String login, String token){
        this.login = login;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }
}
