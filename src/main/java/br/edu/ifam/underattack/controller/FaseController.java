package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dao.FaseDao;
import br.edu.ifam.underattack.dao.IngredienteDao;
import br.edu.ifam.underattack.interceptor.annotations.Public;
import br.edu.ifam.underattack.model.Aluno;
import br.edu.ifam.underattack.model.AlunoParticipaFase;
import br.edu.ifam.underattack.model.Fase;
import br.edu.ifam.underattack.model.Ingrediente;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by erika.silva on 04/06/2017.
 */
@Controller
public class FaseController {

    private final Result result;

    private final FaseDao faseDao;

    @Inject
    public FaseController(Result result, FaseDao faseDao){
        this.result = result;
        this.faseDao = faseDao;
    }

    @Deprecated
    public FaseController(){
        this(null, null);
    }


    @Get
    public void fasesFromAluno(String login){
        List<AlunoParticipaFase> alunoParticipaFase = this.faseDao.listFasesFromAluno(login);
        this.result.use(Results.json()).from(alunoParticipaFase, "fasesAluno")
                .include("fase", "faseObjetivo", "faseObjetivo.objetivo").serialize();
    }

}
