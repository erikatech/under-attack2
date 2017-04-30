package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.annotations.Public;
import br.edu.ifam.underattack.dao.UsuarioDao;
import br.edu.ifam.underattack.dto.UsuarioLogadoDTO;
import br.edu.ifam.underattack.model.Usuario;
import br.edu.ifam.underattack.util.JWTUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by erika.silva on 22/04/2017.
 */

@Controller
public class LoginController {

    private final Result result;
    private final Validator validator;
    private final UsuarioDao dao;

    @Inject
    public LoginController(Result result, Validator validator, UsuarioDao dao){
        this.result = result;
        this.validator = validator;
        this.dao = dao;
    }

    public LoginController(){
        this(null, null, null);
    }

    @Post
    @Public
    @Consumes(value = "application/json")
    public void autentica(String login, String senha){
        List<Usuario> usuarios = this.dao.consulta(login, senha);
        validator.addIf(usuarios.isEmpty(), new I18nMessage("login","login.invalido"));
        validator.onErrorSendBadRequest();

        Usuario usuario = usuarios.get(0);
        String token = JWTUtil.generateToken(usuario.getLogin());

        UsuarioLogadoDTO usuarioLogadoDTO = new UsuarioLogadoDTO(login, token);
        this.result.use(Results.json()).withoutRoot().from(usuarioLogadoDTO).serialize();
    }

    @Get
    public void getUser(){
        result.use(Results.status()).ok();
    }
}
