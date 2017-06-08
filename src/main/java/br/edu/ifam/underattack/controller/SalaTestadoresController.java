package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dao.AlunoDao;
import br.edu.ifam.underattack.dao.DesafioDao;
import br.edu.ifam.underattack.dao.PocaoMagicaIngredienteDao;
import br.edu.ifam.underattack.dao.ValorDeEntradaDao;
import br.edu.ifam.underattack.dto.*;
import br.edu.ifam.underattack.model.*;
import br.edu.ifam.underattack.model.enums.*;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    private PocaoMagicaIngredienteDao pocaoIngredienteDao;

    @Inject
    public SalaTestadoresController(Result result, AlunoDao alunoDao,
                    DesafioDao desafioDao, ValorDeEntradaDao valorDeEntradaDao, Validator validator,
                    PocaoMagicaIngredienteDao pocaoIngredienteDao){
        this.result = result;
        this.alunoDao = alunoDao;
        this.desafioDao = desafioDao;
        this.valorDeEntradaDao = valorDeEntradaDao;
        this.validator = validator;
        this.pocaoIngredienteDao = pocaoIngredienteDao;
    }

    @Deprecated
    public SalaTestadoresController(){
        this(null, null, null, null, null, null);
    }


    @Get
    public void getPocaoMagicaItems(String login){
        Aluno alunoConsultado = this.alunoDao.consulta(login);
        PocaoMagica pocaoAluno = alunoConsultado.getPocaoMagica();
        this.result.use(Results.json()).from(pocaoAluno, "ingredientes")
                .include("pocaoIngredienteList", "pocaoIngredienteList.ingrediente").serialize();
    }

    @Post
    @Consumes(value = "application/json")
    public void reiniciaDesafio(String login){
        this.alunoDao.reiniciaDesafioAluno(login);
        this.result.use(Results.status()).ok();
    }

    @Post
    @Consumes(value = "application/json")
    public void iniciaDesafio(Long idDesafio, String login){
        try {
            AlunoRealizaDesafio alunoRealizaDesafio = this.alunoDao.desafiosDoAluno(login, idDesafio);
            if(alunoRealizaDesafio.getCerebrosDisponiveis() == 0){
                alunoRealizaDesafio.setCerebrosDisponiveis(3);
            }

            int totalValoresCorretos = this.valorDeEntradaDao.getTotalValoresCorretos();
            int valorEncontrados = this.alunoDao.getQuantidadeValoresEncontrados(login);
            if(totalValoresCorretos == valorEncontrados){
                this.result.use(Results.http()).body("Aluno já encontrou valores").setStatusCode(400);
                return;
            }
            /*boolean alunoEncontrouValores = this.alunoDao.alunoEncontrouValores(login);
            if(alunoEncontrouValores){
                this.result.use(Results.http()).body("Aluno já encontrou valores").setStatusCode(400);
                return;
            }*/
            Collections.shuffle(alunoRealizaDesafio.getDesafio().getPrograma().getValoresEntrada());
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

        Aluno aluno = this.alunoDao.encontraValoresDeEntrada(login, valoresSelecionados);
        this.result.use(Results.json()).from(aluno).exclude("senha").serialize();
    }

    @Get
    public void getValoresAluno(String login){
        List<ValorDeEntradaDTO> valoresValidos = new ArrayList<>();
        List<AlunoEncontraValorDeEntrada> valoresAluno = this.valorDeEntradaDao.getValoresAluno(login);
        for (AlunoEncontraValorDeEntrada alunoEncontraValorDeEntrada : valoresAluno) {
            ValorDeEntradaDTO valorDeEntrada = new ValorDeEntradaDTO();
            valorDeEntrada.setId(alunoEncontraValorDeEntrada.getValorDeEntrada().getId());
            valorDeEntrada.setDescricao(alunoEncontraValorDeEntrada.getValorDeEntrada().getDescricao());
            valorDeEntrada.setPontos(alunoEncontraValorDeEntrada.getValorDeEntrada().getDificuldade().getPontos());
            valoresValidos.add(valorDeEntrada);
        }
        this.result.use(Results.json()).from(valoresValidos, "valores").serialize();
    }

    @Get
    public void getClassesAluno(String login){
        List<AlunoEncontraClasseEquivalencia> alunoEncontraClasseEquivalencias = this.alunoDao.classesDoAluno(login);
        this.result.use(Results.json()).from(alunoEncontraClasseEquivalencias, "classesAluno")
                .include("classeEquivalencia", "classeEquivalencia.ingrediente", "classeEquivalencia.valorDeEntrada")
                .serialize();
    }

    @Post
    @Consumes(value = "application/json")
    public void testClasseEquivalencia(TestClasseEquivalenciaDTO testClasse){
        ValorDeEntrada valorEncontrado = this.valorDeEntradaDao.getValorDeEntrada(testClasse.getValorDeEntrada().getId());
        List<ClasseEquivalencia> classesEquivalencia = valorEncontrado.getClassesEquivalencia();
        List<AlunoEncontraClasseEquivalencia> classesAluno = testClasse.getClassesAluno();

        List<ClasseEquivalencia> classesRepetidas = new ArrayList<>();
        List<ClasseEquivalencia> classesEncontradas = new ArrayList<>();

        for (ClasseEquivalencia classeEquivalencia : classesEquivalencia) {
            if(testClasse.getEntradaAluno().matches(classeEquivalencia.getExpressaoRegular())){
                // verificar se ele já encontrou essa classe
                for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia : classesAluno) {
                    if(alunoEncontraClasseEquivalencia.getClasseEquivalencia().getId() == classeEquivalencia.getId()){
                        classesRepetidas.add(classeEquivalencia);
                    }
                }
                classesEncontradas.add(classeEquivalencia);
            }
        }

        if(((classesEncontradas.size() == classesRepetidas.size()) && !classesEncontradas.isEmpty()) ||
                classesEncontradas.size() == 0 ){
            AlunoRealizaDesafio alunoRealizaDesafio = this.alunoDao.desafiosDoAluno(testClasse.getLogin(), testClasse.getIdDesafio());
            alunoRealizaDesafio.setCerebrosDisponiveis(alunoRealizaDesafio.getCerebrosDisponiveis() - 1);

            if(alunoRealizaDesafio.getCerebrosDisponiveis() == 0){
                alunoRealizaDesafio.setCerebrosDisponiveis(3);
            }
            String mensagem = classesRepetidas.size() == classesEncontradas.size() && !classesEncontradas.isEmpty() ?
                    "Zombugs não gostam de quem repete classes" : "Nenhuma classe encontrada";
            this.result.include("errorMessage", mensagem).use(Results.http()).body(mensagem).setStatusCode(400);
            return;
        }

        List<Long> idsClassesDeEquivalenciaAluno = new ArrayList<>();
        // Pego as classes de equivalencia do aluno
        for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia : classesAluno) {
            idsClassesDeEquivalenciaAluno.add(alunoEncontraClasseEquivalencia.getClasseEquivalencia().getId());
        }


        List<AlunoEncontraClasseEquivalencia> alunoEncontrouClasses = new ArrayList<>();
        for (ClasseEquivalencia classesEncontrada : classesEncontradas) {
            if(!idsClassesDeEquivalenciaAluno.contains(classesEncontrada.getId())){
                AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia = new AlunoEncontraClasseEquivalencia();
                alunoEncontraClasseEquivalencia.setClasseEquivalencia(classesEncontrada);
                alunoEncontraClasseEquivalencia.setEntradaAluno(testClasse.getEntradaAluno());
                alunoEncontraClasseEquivalencia.setAluno(alunoDao.consulta(testClasse.getLogin()));
                alunoEncontrouClasses.add(alunoEncontraClasseEquivalencia);
            }
        }
        AlunoClasseDTO alunoClasseDTO = new AlunoClasseDTO();
        alunoClasseDTO.setAlunoEncontraClasseEquivalencia(alunoEncontrouClasses);
        int pontosAcumulados = 0;
        for (ClasseEquivalencia classesEncontrada : classesEncontradas) {
            pontosAcumulados += classesEncontrada.getPontos();
        }
        alunoClasseDTO.setPontos(pontosAcumulados);
        this.result.use(Results.json()).from(alunoClasseDTO, "testResult")
                .include("alunoEncontraClasseEquivalencia", "alunoEncontraClasseEquivalencia.classeEquivalencia",
                        "alunoEncontraClasseEquivalencia.classeEquivalencia.valorDeEntrada",
                        "alunoEncontraClasseEquivalencia.classeEquivalencia.ingrediente").serialize();
    }

    public void updateQuantidadeCerebros(String login, Long idDesafio) {
        AlunoRealizaDesafio alunoRealizaDesafio = this.alunoDao.desafiosDoAluno(login, idDesafio);
        alunoRealizaDesafio.setCerebrosDisponiveis(alunoRealizaDesafio.getCerebrosDisponiveis() - 1);
        this.result.use(Results.http()).setStatusCode(500);
    }

    @Post
    @Consumes(value = "application/json")
    public void finalizaDesafio(String login, Long idDesafio, List<AlunoEncontraClasseEquivalencia> alunoClasses){
        // Pego os ids das classes que o aluno já encontrou
        List<Long> idsClassesJaEncontradas = new ArrayList<>();
        List<AlunoEncontraClasseEquivalencia> classesJaEncontradas = this.alunoDao.classesDoAluno(login);
        for (AlunoEncontraClasseEquivalencia classesJaEncontrada : classesJaEncontradas) {
            idsClassesJaEncontradas.add(classesJaEncontrada.getClasseEquivalencia().getId());
        }

        // Já encontrou todas as classes!
        if(classesJaEncontradas.size() == alunoClasses.size()){
            ResultadoDesafioDto resultadoDesafio = getResultadoDesafioDto(this.alunoDao.consulta(login), alunoDao.desafiosDoAluno(login, idDesafio));
            this.result.use(Results.json()).from(resultadoDesafio, "resultadoDesafio").serialize();
            return; // Só continua se o aluno ainda não encontrou todas as classes de equivalência
        }

        // Salva as classes encontradas
        for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia : alunoClasses) {
            // Se o aluno ainda não encontrou a classe, salva!
            if(!idsClassesJaEncontradas.contains(alunoEncontraClasseEquivalencia.getClasseEquivalencia().getId())){
                this.alunoDao.encontraClassesEquivalencia(login, alunoEncontraClasseEquivalencia.getEntradaAluno(),
                        alunoEncontraClasseEquivalencia.getClasseEquivalencia());
            }
        }
        // Termina o registro


        //Calcula variáveis do aluno, como pontos, quantidade de bugs encontrados e objetivos concluídos
        int bugsEncontrados = 0;
        Aluno aluno = this.alunoDao.consulta(login);
        int totalPontos = 0;
        for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia : aluno.getAlunoEncontraClasseEquivalencia()) {
            totalPontos += alunoEncontraClasseEquivalencia.getClasseEquivalencia().getPontos();
            if (alunoEncontraClasseEquivalencia.getClasseEquivalencia().hasItemPocao()){
                for (PocaoMagicaIngrediente pocaoMagicaIngrediente : aluno.getPocaoMagica().getPocaoIngredienteList()) {
                    if(pocaoMagicaIngrediente.getIngrediente().getId()
                            .equals(alunoEncontraClasseEquivalencia.getClasseEquivalencia().getIngrediente().getId())){
                        pocaoMagicaIngrediente.setSituacaoIngrediente(SituacaoIngrediente.ENCONTRADO);
                        AlunoParticipaFase salaTestadores = aluno.getAlunoParticipaFase().get(0);
                        salaTestadores.setSituacaoFase(SituacaoFase.ESPERA);
                        this.pocaoIngredienteDao.atualiza(pocaoMagicaIngrediente);
                        break;
                    }
                }
            }
            if (alunoEncontraClasseEquivalencia.getClasseEquivalencia().getBugExistente() != null) {
                bugsEncontrados++;
            }
        }
        aluno.setPontos(aluno.getPontos() + totalPontos);
        int quantidadeBugsFase = aluno.getAlunoParticipaFase().get(0).getFase().getQuantidadeBugsFase();
        if(quantidadeBugsFase == bugsEncontrados){
            Set<FaseObjetivo> objetivos = aluno.getAlunoParticipaFase().get(0).getFaseObjetivo();
            for (FaseObjetivo objetivo : objetivos) {
                if(objetivo.getObjetivo().getTipoObjetivo().equals(TipoObjetivo.ZOMBUGS)){
                    objetivo.setRealizado(true);
                    break;
                }
            }
        }
        AlunoRealizaDesafio alunoRealizaDesafio = alunoDao.desafiosDoAluno(login, idDesafio);
        alunoRealizaDesafio.getAluno().getAlunoParticipaFase().get(0).setBugsEncontrados(bugsEncontrados);
        if(classesJaEncontradas.size() == alunoClasses.size()){
            alunoRealizaDesafio.setSituacaoDesafio(SituacaoDesafio.CONCLUIDO);
        }
        alunoRealizaDesafio.setDesempenho(alunoRealizaDesafio.calcularDesempenho());

        this.alunoDao.atualiza(aluno);
        this.alunoDao.atualizaDesafioAluno(alunoRealizaDesafio);

        ResultadoDesafioDto resultadoDesafio = getResultadoDesafioDto(aluno, alunoRealizaDesafio);
        this.result.use(Results.json()).from(resultadoDesafio, "resultadoDesafio").serialize();
    }

    private ResultadoDesafioDto getResultadoDesafioDto(Aluno aluno, AlunoRealizaDesafio alunoRealizaDesafio) {
        // Valores pro retorno
        int total = aluno.getAlunoParticipaFase().get(0).getFase().getQuantidadeBugsFase();
        int totalBugsEncontrados = aluno.getAlunoParticipaFase().get(0).getBugsEncontrados();
        String desempenho = alunoRealizaDesafio.getDesempenho().getDescricao();
        double totalEstrelas = alunoRealizaDesafio.getDesempenho().getTotalEstrelas();


        ResultadoDesafioDto resultadoDesafio = new ResultadoDesafioDto();
        resultadoDesafio.setDesempenho(desempenho);
        resultadoDesafio.setTotalBugsFase(total);
        resultadoDesafio.setTotalBugsEncontrados(totalBugsEncontrados);
        resultadoDesafio.setTotalEstrelas(totalEstrelas);
        resultadoDesafio.setPontosAluno(aluno.getPontos());
        return resultadoDesafio;
    }

    @Post
    @Consumes(value = "application/json")
    public void checkIfUnblocked(String login){
        Aluno alunoConsultado = this.alunoDao.consulta(login);
        List<PocaoMagicaIngrediente> ingredientesAluno = alunoConsultado.getPocaoMagica().getPocaoIngredienteList();
        List<PocaoMagicaIngrediente> ingredientesEncontrados = new ArrayList<PocaoMagicaIngrediente>();
        for (PocaoMagicaIngrediente ingredienteAluno : ingredientesAluno) {
            if(ingredienteAluno.getSituacaoIngrediente().equals(SituacaoIngrediente.ENCONTRADO)){
                ingredientesEncontrados.add(ingredienteAluno);
            }
        }
        if (ingredientesAluno.size() == ingredientesEncontrados.size()
                && alunoConsultado.getAlunoParticipaFase().get(1).getSituacaoFase().equals(SituacaoFase.BLOQUEADA)){
            alunoConsultado.getAlunoParticipaFase().get(1).setSituacaoFase(SituacaoFase.ESPERA);

            AlunoParticipaFase salaTestadores = alunoConsultado.getAlunoParticipaFase().get(0);
            FaseObjetivo objetivoRealizado = salaTestadores.getObjetivoRealizado(TipoObjetivo.POCAO);
            objetivoRealizado.setRealizado(true);
            alunoConsultado.setPontos(alunoConsultado.getPontos() + objetivoRealizado.getObjetivo().getPontos());
            this.alunoDao.atualiza(alunoConsultado);

            this.result.use(Results.json()).from(true, "desbloqueou").serialize();

        } else {
            this.result.use(Results.json()).from(false, "desbloqueou").serialize();
        }
    }

    private int somaPontosAluno(List<AlunoEncontraClasseEquivalencia> classesAluno){
        int totalPontos = 0;
        for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia : classesAluno) {
            totalPontos += alunoEncontraClasseEquivalencia.getClasseEquivalencia().getPontos();
        }
        return totalPontos;

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
