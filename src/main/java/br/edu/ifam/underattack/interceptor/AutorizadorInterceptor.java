package br.com.caelum.vraptor.interceptor;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.annotations.Public;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.controller.LoginController;
import br.com.caelum.vraptor.controller.UsuarioLogado;

import javax.inject.Inject;

/**
 * Created by erika.silva on 21/04/2017.
 */
@Intercepts
public class AutorizadorInterceptor {

    @Inject private UsuarioLogado usuarioLogado;
    @Inject private Result result;

    @Accepts
    public boolean accepts(ControllerMethod method){
        return !method.containsAnnotation(Public.class);
    }

    @AroundCall
    public void intercepta(SimpleInterceptorStack stack){
        if(usuarioLogado.isLogado()) {
            stack.next();
        } else {
            result.redirectTo(LoginController.class).formulario();
        }
    }
}
