package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dao.*;
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

    private ClasseEquivalenciaDao classeEquivalenciaDao;

    @Inject
    public SalaTestadoresController(Result result, AlunoDao alunoDao,
                    DesafioDao desafioDao, ValorDeEntradaDao valorDeEntradaDao, Validator validator,
                    PocaoMagicaIngredienteDao pocaoIngredienteDao, ClasseEquivalenciaDao classeEquivalenciaDao){
        this.result = result;
        this.alunoDao = alunoDao;
        this.desafioDao = desafioDao;
        this.valorDeEntradaDao = valorDeEntradaDao;
        this.validator = validator;
        this.pocaoIngredienteDao = pocaoIngredienteDao;
        this.classeEquivalenciaDao = classeEquivalenciaDao;
    }

    @Deprecated
    public SalaTestadoresController(){
        this(null, null, null, null,
                null, null, null);
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

            int totalValoresCorretos = this.valorDeEntradaDao.getTotalValoresCorretos(idDesafio);

            int valorEncontrados = this.alunoDao.getQuantidadeValoresEncontrados(login, idDesafio);
            if(totalValoresCorretos == valorEncontrados){
                this.result.use(Results.http()).body("Aluno já encontrou valores").setStatusCode(400);
                return;
            }

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
    public void getValoresAluno(String login, Long idDesafio){
        List<ValorDeEntradaDTO> valoresValidos = new ArrayList<>();
        List<AlunoEncontraValorDeEntrada> valoresAluno = this.valorDeEntradaDao.getValoresAluno(login);

        for (AlunoEncontraValorDeEntrada alunoEncontraValorDeEntrada : valoresAluno) {
            if(alunoEncontraValorDeEntrada.getValorDeEntrada().getPrograma().getDesafio().getId().equals(idDesafio)){
                ValorDeEntradaDTO valorDeEntrada = new ValorDeEntradaDTO();
                valorDeEntrada.setId(alunoEncontraValorDeEntrada.getValorDeEntrada().getId());
                valorDeEntrada.setDescricao(alunoEncontraValorDeEntrada.getValorDeEntrada().getDescricao());
                valorDeEntrada.setPontos(alunoEncontraValorDeEntrada.getValorDeEntrada().getDificuldade().getPontos());
                valoresValidos.add(valorDeEntrada);
            }
        }
        this.result.use(Results.json()).from(valoresValidos, "valores").serialize();
    }

    @Get
    public void getClassesAluno(String login, Long idDesafio){
        List<AlunoEncontraClasseEquivalencia> alunoEncontraClasseEquivalencias =
                this.alunoDao.classesDoAluno(login, idDesafio);
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
        //Pegar os ids das classes que o aluno já encontrou pro desafio atual!
        List<Long> classesJaEncontradas = this.alunoDao.idsClassesDoDesafioAtual(login, idDesafio);
        if(classesJaEncontradas.size() == alunoClasses.size()){
            int bugsEncontradosNoDesafioAtual = 0;
            Aluno alunoConsultado = this.alunoDao.consulta(login);
            for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia :
                    alunoConsultado.getAlunoEncontraClasseEquivalencia()) {
                ClasseEquivalencia classeEquivalencia =
                        this.classeEquivalenciaDao.consulta(alunoEncontraClasseEquivalencia.getClasseEquivalencia().getId());
                if(classeEquivalencia.getValorDeEntrada().getPrograma().getDesafio().getId().equals(idDesafio)){
                    if(classeEquivalencia.getBugExistente() != null){
                        bugsEncontradosNoDesafioAtual++;
                    }
                }
            }
            ResultadoDesafioDto resultadoDesafio = getResultadoDesafioDto(this.alunoDao.consulta(login),
                    alunoDao.desafiosDoAluno(login, idDesafio), bugsEncontradosNoDesafioAtual);
            this.result.use(Results.json()).from(resultadoDesafio, "resultadoDesafio").serialize();
            return; // Só continua se o aluno ainda não encontrou todas as classes de equivalência
        }

        // Salva as classes encontradas
        for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia : alunoClasses) {
            // Se o aluno ainda não encontrou a classe, salva!
            if(!classesJaEncontradas.contains(alunoEncontraClasseEquivalencia.getClasseEquivalencia().getId())){
                this.alunoDao.encontraClassesEquivalencia(login, alunoEncontraClasseEquivalencia.getEntradaAluno(),
                        alunoEncontraClasseEquivalencia.getClasseEquivalencia());
            }
        }
        // Termina o registro


        Aluno aluno = this.alunoDao.consulta(login);
        int bugsEncontradosNoDesafioAtual = 0;
        int totalPontos = 0;
        for (AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia : aluno.getAlunoEncontraClasseEquivalencia()) {
            ClasseEquivalencia classeEquivalencia =
                    this.classeEquivalenciaDao.consulta(alunoEncontraClasseEquivalencia.getClasseEquivalencia().getId());
            // se a classe de equivalencia pertence ao desafio  atual...
            if(classeEquivalencia.getValorDeEntrada().getPrograma().getDesafio().getId().equals(idDesafio)){
                totalPontos += classeEquivalencia.getPontos();
                if(classeEquivalencia.hasItemPocao()){
                    for (PocaoMagicaIngrediente pocaoMagicaIngrediente : aluno.getPocaoMagica().getPocaoIngredienteList()) {
                        if(pocaoMagicaIngrediente.getIngrediente().getId()
                                .equals(classeEquivalencia.getIngrediente().getId())){
                            pocaoMagicaIngrediente.setSituacaoIngrediente(SituacaoIngrediente.ENCONTRADO);
                            AlunoParticipaFase salaTestadores = aluno.getAlunoParticipaFase().get(0);
                            salaTestadores.setSituacaoFase(SituacaoFase.ESPERA);
                            this.pocaoIngredienteDao.atualiza(pocaoMagicaIngrediente);
                            break;
                        }
                    }
                }
                if(classeEquivalencia.getBugExistente() != null){
                    bugsEncontradosNoDesafioAtual++;
                }

            }

        }
        /*List<ClasseEquivalencia> classesDoDesafio = classeEquivalenciaDao.getClassesDesafioAtual(idDesafio);
        // pega a quantidade de bugs do desafio atual
        int quantidadeBugsDesafio = 0;
        for (ClasseEquivalencia classeEquivalencia : classesDoDesafio) {
            if(classeEquivalencia.getBugExistente() != null){
                quantidadeBugsDesafio++;
            }
        }
*/
         AlunoParticipaFase salaTestadores = aluno.getAlunoParticipaFase().get(0);
        salaTestadores.setBugsEncontrados(salaTestadores.getBugsEncontrados() + bugsEncontradosNoDesafioAtual);
        alunoDao.atualiza(aluno);


        int quantidadeBugsFase = salaTestadores.getFase().getQuantidadeBugsFase();
        if(quantidadeBugsFase == salaTestadores.getBugsEncontrados()){
            Set<FaseObjetivo> objetivos = salaTestadores.getFaseObjetivo();
            for (FaseObjetivo objetivo : objetivos) {
                if(objetivo.getObjetivo().getTipoObjetivo().equals(TipoObjetivo.ZOMBUGS)){
                    totalPontos += objetivo.getObjetivo().getPontos();
                    objetivo.setRealizado(true);
                    break;
                }
            }
        }

        classesJaEncontradas = this.alunoDao.idsClassesDoDesafioAtual(login, idDesafio);
        AlunoRealizaDesafio alunoRealizaDesafio = alunoDao.desafiosDoAluno(login, idDesafio);
        // Se o aluno já encontrou todas as classes de equivalencia...
        if(classesJaEncontradas.size() == alunoRealizaDesafio.getDesafio().getClassesEquivalencia().size()){
            alunoRealizaDesafio.setSituacaoDesafio(SituacaoDesafio.CONCLUIDO);
        }
        alunoRealizaDesafio.setDesempenho(calculaDesempenho(login, idDesafio));
        this.alunoDao.atualiza(aluno);
        this.alunoDao.atualizaDesafioAluno(alunoRealizaDesafio);

        ResultadoDesafioDto resultadoDesafio = getResultadoDesafioDto(aluno, alunoRealizaDesafio, bugsEncontradosNoDesafioAtual);
        this.result.use(Results.json()).from(resultadoDesafio, "resultadoDesafio").serialize();
    }

    /**
     * Calcula o desempenho do aluno
     * @param login
     * @param idDesafio
     * @return
     */
    private Desempenho calculaDesempenho(String login, Long idDesafio){
        Desafio desafioAtual = this.desafioDao.getDesafio(idDesafio);
        int totalBugs = 0;
        int totalbugsEncontrados = 0;
        List<ClasseEquivalencia> todasAsClassesEquivalencia = new ArrayList<ClasseEquivalencia>();
        List<ValorDeEntrada> valoresEntradaDoDesafio = desafioAtual.getPrograma().getValoresEntrada();
        for (ValorDeEntrada valorDeEntrada : valoresEntradaDoDesafio) {
            if (valorDeEntrada.isValorEntradaCorreto()) {
                todasAsClassesEquivalencia.addAll(valorDeEntrada
                        .getClassesEquivalencia());
            }
        }
        for (ClasseEquivalencia classe : todasAsClassesEquivalencia) {
            if (classe.getBugExistente() != null) {
                totalBugs++;
            }
        }

        List<ClasseEquivalencia> classesDoDesafioAtual = alunoDao.classesDoDesafioAtual(login, idDesafio);
        for (ClasseEquivalencia classeEquivalencia : classesDoDesafioAtual) {
            if(classeEquivalencia.getBugExistente() != null){
                totalbugsEncontrados++;
            }
        }
        double desempenho = (totalbugsEncontrados * 100) / totalBugs;
        return getDesempenho(desempenho);
    }

    private Desempenho getDesempenho(double desempenho) {
        if (desempenho >= 0 && desempenho <= 25) {
            return Desempenho.RUIM;
        } else if (desempenho > 25 && desempenho <= 50) {
            return Desempenho.NORMAL;
        } else if (desempenho > 50 && desempenho <= 85) {
            return Desempenho.BOM;
        } else if (desempenho > 85) {
            return Desempenho.OTIMO;
        }
        return null;
    }





    private ResultadoDesafioDto getResultadoDesafioDto(Aluno aluno, AlunoRealizaDesafio alunoRealizaDesafio,
                               int bugsEncontradosNoDesafioAtual) {
        // Valores pro retorno
        AlunoParticipaFase alunoParticipaFase = aluno.getAlunoParticipaFase().get(0);
        int totalBugsDesafio = 0;
        for (ClasseEquivalencia classeEquivalencia : alunoRealizaDesafio.getDesafio().getClassesEquivalencia()) {
            if(classeEquivalencia.getBugExistente() != null){
                totalBugsDesafio++;
            }
        }
        String desempenho = alunoRealizaDesafio.getDesempenho().getDescricao();
        double totalEstrelas = alunoRealizaDesafio.getDesempenho().getTotalEstrelas();
        ResultadoDesafioDto resultadoDesafio = new ResultadoDesafioDto();
        resultadoDesafio.setDesempenho(desempenho);
        resultadoDesafio.setTotalBugsFase(totalBugsDesafio);
        resultadoDesafio.setTotalBugsEncontrados(bugsEncontradosNoDesafioAtual);
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

    public static void main(String[] args) {

        System.out.println("1111111111".matches("^([\\da-zA-Z]){0,9}$"));



    }
}
