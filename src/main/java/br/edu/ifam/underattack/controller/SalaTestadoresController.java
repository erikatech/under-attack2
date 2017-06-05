package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dao.*;
import br.edu.ifam.underattack.model.*;
import br.edu.ifam.underattack.model.enums.IndicadorFase;
import br.edu.ifam.underattack.model.enums.SituacaoDesafio;

import javax.inject.Inject;
import javax.persistence.NoResultException;

/**
 * Created by erika.silva on 04/06/2017.
 */
@Controller
public class SalaTestadoresController {

    private final Result result;

    private final AlunoDao alunoDao;

    private final DesafioDao desafioDao;

    @Inject
    public SalaTestadoresController(Result result, AlunoDao alunoDao, DesafioDao desafioDao){
        this.result = result;
        this.alunoDao = alunoDao;
        this.desafioDao = desafioDao;
    }

    @Deprecated
    public SalaTestadoresController(){
        this(null, null, null);
    }


    @Get
    public void getPocaoMagicaItems(String login){
        Aluno alunoConsultado = this.alunoDao.consulta(login);
        PocaoMagica pocaoAluno = alunoConsultado.getPocaoMagica();
        this.result.use(Results.json()).from(pocaoAluno, "ingredientes")
                .include("pocaoIngredienteList").serialize();
    }

    @Post
    @Consumes(value = "application/json")
    public void iniciaDesafio(Long idDesafio, String login){
        try {
            AlunoRealizaDesafio alunoRealizaDesafio = this.alunoDao.desafiosDoAluno(login, idDesafio);
            this.result.use(Results.json()).from(alunoRealizaDesafio, "alunoDesafio")
                    .include("desafio", "desafio.programa", "desafio.programa.valoresEntrada").serialize();
        } catch(NoResultException ex){
            Desafio desafio = this.desafioDao.getDesafio(idDesafio);
            Aluno aluno = this.alunoDao.consulta(login);
            AlunoRealizaDesafio alunoDesafio = new AlunoRealizaDesafio();
            alunoDesafio.setAluno(aluno);
            alunoDesafio.setDesafio(desafio);
            alunoDesafio.setCerebrosDisponiveis(3);
            alunoDesafio.setIndicadorFase(IndicadorFase.FASE_TESTADORES);
            alunoDesafio.setSituacaoDesafio(SituacaoDesafio.EM_ANDAMENTO);
            this.alunoDao.iniciaDesafio(alunoDesafio);
            this.result.use(Results.json()).from(alunoDesafio, "alunoDesafio")
                    .include("desafio", "desafio.programa", "desafio.programa.valoresEntrada").serialize();
        }
    }

}
