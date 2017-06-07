package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dao.DesafioDao;
import br.edu.ifam.underattack.dao.FaseDao;
import br.edu.ifam.underattack.dao.IngredienteDao;
import br.edu.ifam.underattack.interceptor.annotations.Public;
import br.edu.ifam.underattack.model.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by erika.silva on 04/06/2017.
 */
@Controller
public class FaseController {

    private final Result result;

    private final FaseDao faseDao;

    private final DesafioDao desafioDao;

    @Inject
    public FaseController(Result result, FaseDao faseDao, DesafioDao desafioDao){
        this.result = result;
        this.faseDao = faseDao;
        this.desafioDao = desafioDao;
    }

    @Deprecated
    public FaseController(){
        this(null, null, null);
    }


    @Get
    public void fasesFromAluno(String login){
        List<AlunoParticipaFase> alunoParticipaFase = this.faseDao.listFasesFromAluno(login);
        this.result.use(Results.json()).from(alunoParticipaFase, "fasesAluno")
                .include("fase", "faseObjetivo", "fase.desafios", "faseObjetivo.objetivo").serialize();
    }

    @Get
    public void getDesafio(Long idDesafio){
        Desafio desafio = this.desafioDao.getDesafio(idDesafio);
        this.result.use(Results.json()).from(desafio, "desafio").include("programa", "alunoRealizaDesafio").serialize();
    }

}
