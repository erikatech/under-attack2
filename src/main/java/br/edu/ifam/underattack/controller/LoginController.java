package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.edu.ifam.underattack.dao.UsuarioDao;
import br.edu.ifam.underattack.model.Usuario;

/**
 * Created by erika.silva on 22/04/2017.
 */

@Path("/usuario")
@Controller
public class UsuarioController {

    private final Result result;
    private final Validator validator;
    private final UsuarioDao dao;

    public UsuarioController(Result result, Validator validator, UsuarioDao dao){
        this.result = result;
        this.validator = validator;
        this.dao = dao;
    }

    public UsuarioController(){
        this(null, null, null);
    }

    @Post
    public void autentica(Usuario usuario){
        boolean existe = this.dao.existe(usuario);
        validator.addIf(!existe)


    }

}
