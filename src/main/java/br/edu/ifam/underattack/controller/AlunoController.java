package br.edu.ifam.underattack.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.edu.ifam.underattack.dto.AlunoLogadoDTO;
import br.edu.ifam.underattack.interceptor.AlunoInfo;
import br.edu.ifam.underattack.interceptor.annotations.Public;
import br.edu.ifam.underattack.dao.AlunoDao;
import br.edu.ifam.underattack.dao.FaseDao;
import br.edu.ifam.underattack.dao.IngredienteDao;
import br.edu.ifam.underattack.dao.NivelAlunoDao;
import br.edu.ifam.underattack.model.*;
import br.edu.ifam.underattack.util.JWTUtil;

import javax.inject.Inject;

/**
 * Created by erika.silva on 22/04/2017.
 */

@Controller
public class AlunoController {

    private final Result result;
    private final Validator validator;
    private final AlunoDao alunoDao;
    private final FaseDao faseDao;
    private final NivelAlunoDao nivelAlunoDao;
    private final IngredienteDao ingredienteDao;
    private final AlunoInfo alunoInfo;


    @Inject
    public AlunoController(Result result, Validator validator, AlunoDao alunoDao, FaseDao faseDao,
                           NivelAlunoDao nivelAlunoDao, IngredienteDao ingredienteDao, AlunoInfo alunoInfo) {
        this.result = result;
        this.validator = validator;
        this.alunoDao = alunoDao;
        this.faseDao = faseDao;
        this.nivelAlunoDao = nivelAlunoDao;
        this.ingredienteDao = ingredienteDao;
        this.alunoInfo = alunoInfo;
    }

    public AlunoController() {
        this(null, null, null, null, null, null, null);
    }

    @Post
    @Public
    @Consumes(value = "application/json")
    public void register(Aluno aluno) {
        validator.addIf(alunoDao.existeAlunoComLogin(aluno.getLogin()), new I18nMessage("error", "login.ja.utilizado"));
        validator.onErrorSendBadRequest();
        alunoDao.adiciona(aluno);
        ingredienteDao.associaIngrediente(aluno.getPocaoMagica());
        this.result.use(Results.json()).withoutRoot().from("sucesso").serialize();
    }

    @Post
    @Public
    @Consumes(value = "application/json")
    public void login(Aluno aluno) {
        Aluno alunoConsultado = this.alunoDao.consulta(aluno.getLogin(), aluno.getSenha());
        validator.addIf(alunoConsultado == null, new I18nMessage("login","login.invalido"));
        validator.onErrorSendBadRequest();

        String token = JWTUtil.generateToken(alunoConsultado.getLogin());

        AlunoLogadoDTO usuarioLogadoDTO = new AlunoLogadoDTO(aluno.getLogin(), token);

//        this.alunoInfo.login(alunoConsultado);

        this.result.use(Results.json()).withoutRoot().from(usuarioLogadoDTO).serialize();
    }

    @Get
    public void getUser(){
        result.use(Results.status()).ok();
    }
}
