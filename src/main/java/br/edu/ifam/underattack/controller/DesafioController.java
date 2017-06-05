package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dao.AlunoDao;
import br.edu.ifam.underattack.dao.DesafioDao;
import br.edu.ifam.underattack.dao.ValorDeEntradaDao;
import br.edu.ifam.underattack.model.*;
import br.edu.ifam.underattack.model.enums.IndicadorFase;
import br.edu.ifam.underattack.model.enums.SituacaoDesafio;
import br.edu.ifam.underattack.model.enums.TipoValorEntrada;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by erika.silva on 04/06/2017.
 */
@Controller
public class DesafioController {

    private final Result result;

    private final AlunoDao alunoDao;

    private final Validator validator;

    @Inject
    public DesafioController(Result result, AlunoDao alunoDao, Validator validator){
        this.result = result;
        this.alunoDao = alunoDao;
        this.validator = validator;

    }

    @Deprecated
    public DesafioController(){
        this(null, null, null);
    }


    @Get
    public void getDesafiosAluno(String login, Long idDesafio){
        boolean alunoEncontrouValores = this.alunoDao.alunoEncontrouValores(login);
        validator.addIf(!alunoEncontrouValores, new I18nMessage("aluno.valores","aluno.encontrou.valores"));
        validator.onErrorSendBadRequest();

        AlunoRealizaDesafio alunoRealizaDesafio = this.alunoDao.desafiosDoAluno(login, idDesafio);
        this.result.use(Results.json()).from(alunoRealizaDesafio, "alunoDesafio")
                .include("desafio", "desafio.programa", "desafio.programa.valoresEntrada")
                .exclude("desafio.programa.valoresEntrada.tipo").serialize();
    }
}
