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
public class SalaTestadoresController {

    private final Result result;

    private final AlunoDao alunoDao;

    private final DesafioDao desafioDao;

    private final ValorDeEntradaDao valorDeEntradaDao;

    private final Validator validator;

    @Inject
    public SalaTestadoresController(Result result, AlunoDao alunoDao,
                    DesafioDao desafioDao, ValorDeEntradaDao valorDeEntradaDao, Validator validator){
        this.result = result;
        this.alunoDao = alunoDao;
        this.desafioDao = desafioDao;
        this.valorDeEntradaDao = valorDeEntradaDao;
        this.validator = validator;
    }

    @Deprecated
    public SalaTestadoresController(){
        this(null, null, null, null, null);
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

            boolean alunoEncontrouValores = this.alunoDao.alunoEncontrouValores(login);
            validator.addIf(alunoEncontrouValores, new I18nMessage("aluno.valores","aluno.encontrou.valores"));
            validator.onErrorSendBadRequest();

            Collections.shuffle(alunoRealizaDesafio.getDesafio().getPrograma().getValoresEntrada());
            if(alunoRealizaDesafio.getCerebrosDisponiveis() == 0){
                alunoRealizaDesafio.setCerebrosDisponiveis(3);
            }
            this.result.use(Results.json()).from(alunoRealizaDesafio, "alunoDesafio")
                    .include("desafio", "desafio.programa", "desafio.programa.valoresEntrada")
                    .exclude("desafio.programa.valoresEntrada.tipo").serialize();
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
            Collections.shuffle(alunoDesafio.getDesafio().getPrograma().getValoresEntrada());

            this.result.use(Results.json()).from(alunoDesafio, "alunoDesafio")
                    .include("desafio", "desafio.programa", "desafio.programa.valoresEntrada").serialize();
        }
    }

    @Get
    public void isValorValido(Long idValorDeEntrada, String login, Long desafioId){
        ValorDeEntrada valorDeEntrada = this.valorDeEntradaDao.getValorDeEntrada(idValorDeEntrada);
        if(valorDeEntrada.getTipo().equals(TipoValorEntrada.CORRETO)){
            this.result.use(Results.http())
                    .body(String.valueOf(valorDeEntrada.getDificuldade()
                            .getPontos()));
        } else {
            AlunoRealizaDesafio alunoRealizaDesafio = this.alunoDao.desafiosDoAluno(login, desafioId);
            alunoRealizaDesafio.setCerebrosDisponiveis(alunoRealizaDesafio.getCerebrosDisponiveis() - 1);
            this.result.include("errorMessage", "Valor Inválido").use(Results.http()).sendError(500);
        }
    }

    @Post
    @Consumes(value = "application/json")
    public void goToNextStage(String login, List<ValorDeEntrada> valoresSelecionados){
        boolean hasAtLeastOne = this.hasAtLeastOne(valoresSelecionados);
        validator.addIf(!hasAtLeastOne, new I18nMessage("valoresIncorretos","sem.valores.corretos"));
        validator.onErrorSendBadRequest();
        this.alunoDao.encontraValoresDeEntrada(login, valoresSelecionados);
        this.result.use(Results.status()).ok();
    }

    private boolean hasAtLeastOne(List<ValorDeEntrada> valoresSelecionados) {
        List<ValorDeEntrada> finalList = new ArrayList<>();
        for (ValorDeEntrada valorSelecionado : valoresSelecionados) {
            ValorDeEntrada valorDeEntrada = this.valorDeEntradaDao.getValorDeEntrada(valorSelecionado.getId());
            if (valorDeEntrada.getTipo().equals(TipoValorEntrada.CORRETO)){
                finalList.add(valorDeEntrada);
            }
        }
        return finalList.size() != 0;
    }

}
