package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dao.FaseDao;
import br.edu.ifam.underattack.dao.IngredienteDao;
import br.edu.ifam.underattack.interceptor.annotations.Public;
import br.edu.ifam.underattack.model.Fase;
import br.edu.ifam.underattack.model.Ingrediente;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by erika.silva on 30/04/2017.
 */
@Controller
public class AdminController {

    private final Result result;

    private final FaseDao faseDao;

    private final IngredienteDao ingredienteDao;

    @Inject
    public AdminController(Result result, FaseDao faseDao, IngredienteDao ingredienteDao){
        this.result = result;
        this.faseDao = faseDao;
        this.ingredienteDao = ingredienteDao;
    }

    @Deprecated
    public AdminController(){
        this(null, null, null);
    }


    @Get
    @Public
    public void listAll(){
        List<Fase> fases = this.faseDao.listAll();
        this.result.include("fases").use(Results.json()).from(fases).serialize();
    }

    @Get
    @Public
    public void listIngredientes(){
        List<Ingrediente> ingredientes = this.ingredienteDao.listAll();
        this.result.use(Results.json()).from(ingredientes, "ingredientes").serialize();
    }

    @Put
    @Consumes(value = "application/json")
    @Public
    public void updateFase(Fase fase){
        this.faseDao.updateFase(fase);
        this.result.include("message").use(Results.json())
                .from("Desafio salvo com sucesso").serialize();
    }


}
