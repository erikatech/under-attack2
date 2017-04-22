package br.edu.ifam.underattack.interceptor;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.annotations.Public;
import br.edu.ifam.underattack.util.JWTUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by erika.silva on 21/04/2017.
 */
@Intercepts
public class AutorizadorInterceptor {

    private HttpServletRequest request;
    private Result result;

    /**
     * @deprecated Construtor utilizado apenas pelo CDI
     */
    public AutorizadorInterceptor() {
        this(null, null);
    }

    @Inject
    public AutorizadorInterceptor(HttpServletRequest request, Result result) {
        this.request = request;
        this.result = result;
    }
    @Accepts
    public boolean accepts(ControllerMethod method){
        return !method.containsAnnotation(Public.class);
    }

    @AroundCall
    public void intercepta(SimpleInterceptorStack stack){
        boolean isTokenValido = false;
        String token = request.getHeader("authorization");

        if (token != null) {
            String[] tokenArray = token.split(" ");
            String loginUsuario = tokenArray[0];
            String jwtToken = tokenArray[1];

            isTokenValido = JWTUtil.parseToken(jwtToken).getSubject().equals(loginUsuario);
        }

        if (isTokenValido) {
            stack.next();
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Não autorizado", "msg").serialize();
        }
    }
}
