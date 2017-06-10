package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.*;
import br.edu.ifam.underattack.model.enums.SituacaoDesafio;
import br.edu.ifam.underattack.model.enums.SituacaoFase;
import br.edu.ifam.underattack.model.enums.TipoValorEntrada;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.*;


public class AlunoDao {

	private final EntityManager em;

	private final FaseDao faseDao;

	private final NivelAlunoDao nivelAlunoDao;

	private final ValorDeEntradaDao valorDeEntradaDao;

	@Inject
	public AlunoDao(EntityManager em, FaseDao faseDao, NivelAlunoDao nivelAlunoDao, ValorDeEntradaDao valorDeEntradaDao) {
		this.em = em;
		this.faseDao = faseDao;
		this.nivelAlunoDao = nivelAlunoDao;
		this.valorDeEntradaDao = valorDeEntradaDao;
	}
	
	@Deprecated
	public AlunoDao() {
		this(null, null, null, null); // para uso do CDI
	}

	public void adiciona(Aluno aluno) {
		List<Fase> todasAsFases = faseDao.listAll();
		Fase salaTestadores = todasAsFases.get(0);
		Fase salaDesenvolvedores = todasAsFases.get(1);

		// Cada fase é associada ao aluno, assumindo as características iniciais
		AlunoParticipaFase alunoParticipaSalaTestadores = new AlunoParticipaFase();
		alunoParticipaSalaTestadores.setAluno(aluno);
		alunoParticipaSalaTestadores.setFase(salaTestadores);
		alunoParticipaSalaTestadores.setHistoriaExibida(false);
		alunoParticipaSalaTestadores.setSituacaoFase(SituacaoFase.ESPERA);
		alunoParticipaSalaTestadores.setFaseObjetivo(this.buildFaseObjetivo(1L, alunoParticipaSalaTestadores));

		// Configurando a sala dos desenvolvedores
		AlunoParticipaFase alunoParticipaSalaDesenvolvedores = new AlunoParticipaFase();
		alunoParticipaSalaDesenvolvedores.setAluno(aluno);
		alunoParticipaSalaDesenvolvedores.setFase(salaDesenvolvedores);
		alunoParticipaSalaDesenvolvedores.setHistoriaExibida(false);
		alunoParticipaSalaDesenvolvedores.setSituacaoFase(SituacaoFase.BLOQUEADA);
		alunoParticipaSalaDesenvolvedores.setFaseObjetivo(this.buildFaseObjetivo(2L, alunoParticipaSalaDesenvolvedores));

		List<AlunoParticipaFase> alunoParticipaFase = Arrays.asList(alunoParticipaSalaTestadores, alunoParticipaSalaDesenvolvedores);
		aluno.setAlunoParticipaFase(alunoParticipaFase);
		aluno.setPocaoMagica(new PocaoMagica());
		aluno.setNivelAluno(nivelAlunoDao.consulta(1L));

		this.em.persist(aluno);
	}

	private Set<FaseObjetivo> buildFaseObjetivo(Long faseId, AlunoParticipaFase alunoParticipaFase){
		Set<FaseObjetivo> faseObjetivosFinal = new HashSet<>();
		List<Objetivo> objetivoList = this.faseDao.listObjetivos(faseId);
		for (Objetivo objetivo : objetivoList) {
			FaseObjetivo obj = new FaseObjetivo();
			obj.setAlunoFase(alunoParticipaFase);
			obj.setObjetivo(objetivo);
			obj.setRealizado(false);
			faseObjetivosFinal.add(obj);
		}
		return faseObjetivosFinal;

	}

	public Aluno consulta(Long id) {
		TypedQuery<Aluno> query = this.em.createQuery(
				"select a from Aluno a where a.id =:id", Aluno.class);
		query.setParameter("id", id);
		try {
			query.getSingleResult();
		} catch(NoResultException e){
			e.printStackTrace();
			throw new NoResultException("Login ou senha inválidos");
		}

		return query.getSingleResult();
	}

	public Aluno consulta(String login){
		TypedQuery<Aluno> query = this.em.createQuery(
				"select a from Aluno a where a.login =:login", Aluno.class);
		query.setParameter("login", login);
		try {
			query.getSingleResult();
		} catch(NoResultException e){
			e.printStackTrace();
			throw new NoResultException("Login ou senha inválidos");
		}
		return query.getSingleResult();
	}

	public Aluno consulta(String login, String senha) {
		try {
			TypedQuery<Aluno> query = this.em
					.createQuery(
							"select a from Aluno a where (a.login =:login) and a.senha=:senha",
							Aluno.class);
			query.setParameter("login", login);
			query.setParameter("senha", senha);
			Aluno aluno = query.getSingleResult();
			aluno.getAlunoRealizaDesafio().size();
			return aluno;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean existeAlunoComLogin(String login) {
		Long count = em
				.createQuery("select count(a) from Aluno a where a.login = :login", Long.class)
				.setParameter("login", login)
				.getSingleResult();
		return count > 0;
	}

	public AlunoRealizaDesafio desafiosDoAluno(String login, Long idDesafio) throws NoResultException{
		try {
			TypedQuery<AlunoRealizaDesafio> query = this.em.createQuery("select ad from AlunoRealizaDesafio ad " +
					"where ad.aluno.login=:login and ad.desafio.id=:idDesafio", AlunoRealizaDesafio.class);
			query.setParameter("login", login);
			query.setParameter("idDesafio", idDesafio);
			return query.getSingleResult();
		} catch(NoResultException ex){
			throw new NoResultException("Nenhum resultado");
		}
	}

	public void iniciaDesafio(AlunoRealizaDesafio alunoDesafio) {
		this.em.persist(alunoDesafio);
	}


    public Aluno encontraValoresDeEntrada(String login, List<ValorDeEntrada> valoresSelecionados) {
		final Aluno aluno = this.consulta(login);
		List<AlunoEncontraValorDeEntrada> valoresAluno = this.valorDeEntradaDao.getValoresAluno(login);
		List<Long> idsValoresAluno = new ArrayList<>();

		for (AlunoEncontraValorDeEntrada alunoEncontraValorDeEntrada : valoresAluno) {
			idsValoresAluno.add(alunoEncontraValorDeEntrada.getValorDeEntrada().getId());
		}

		for (ValorDeEntrada valorSelecionado : valoresSelecionados) {
			ValorDeEntrada valorEncontrado = this.valorDeEntradaDao.getValorDeEntrada(valorSelecionado.getId());
			if(valorEncontrado.getTipo().equals(TipoValorEntrada.CORRETO) &&
					!idsValoresAluno.contains(valorEncontrado.getId())){
				AlunoEncontraValorDeEntrada alunoValor = new AlunoEncontraValorDeEntrada();
				alunoValor.setAluno(aluno);
				alunoValor.setValorDeEntrada(valorEncontrado);
				aluno.setPontos(aluno.getPontos() + valorEncontrado.getDificuldade().getPontos());
				this.em.persist(alunoValor);
			}
		}
		this.atualiza(aluno);
		return aluno;
	}


	public void encontraClassesEquivalencia(String login, String entradaAluno, ClasseEquivalencia classeEquivalencia) {
		final Aluno aluno = this.consulta(login);
		AlunoEncontraClasseEquivalencia alunoEncontraClasseEquivalencia = new AlunoEncontraClasseEquivalencia();
		alunoEncontraClasseEquivalencia.setAluno(aluno);
		alunoEncontraClasseEquivalencia.setEntradaAluno(entradaAluno);
		alunoEncontraClasseEquivalencia.setClasseEquivalencia(classeEquivalencia);
		this.em.persist(alunoEncontraClasseEquivalencia);
	}

	public List<AlunoEncontraClasseEquivalencia> classesDoAluno(String login, Long desafioId) {
		TypedQuery<AlunoEncontraClasseEquivalencia> query =
				this.em.createQuery("select ac from AlunoEncontraClasseEquivalencia ac where ac.aluno.login=:login" +
								" and ac.classeEquivalencia.valorDeEntrada.programa.desafio.id=:idDesafio",
						AlunoEncontraClasseEquivalencia.class);
		query.setParameter("login", login);
		query.setParameter("idDesafio", desafioId);
		return query.getResultList();
	}

	public List<Long> idsClassesDoDesafioAtual(String login, Long desafioId) {
		TypedQuery<Long> query =
				this.em.createQuery("select ac.classeEquivalencia.id from AlunoEncontraClasseEquivalencia ac where ac.aluno.login =:login " +
				"and ac.classeEquivalencia.valorDeEntrada.programa.desafio.id =:desafioId", Long.class);
		query.setParameter("login", login);
		query.setParameter("desafioId", desafioId);
		return query.getResultList();
	}

	public List<ClasseEquivalencia> classesDoDesafioAtual(String login, Long desafioId) {
		TypedQuery<ClasseEquivalencia> query =
				this.em.createQuery("select ac.classeEquivalencia from AlunoEncontraClasseEquivalencia ac where ac.aluno.login =:login " +
						"and ac.classeEquivalencia.valorDeEntrada.programa.desafio.id =:desafioId", ClasseEquivalencia.class);
		query.setParameter("login", login);
		query.setParameter("desafioId", desafioId);
		return query.getResultList();
	}

	public boolean alunoEncontrouValores(String login) {
		TypedQuery<AlunoEncontraValorDeEntrada> query = this.em.createQuery("select av " +
				"from AlunoEncontraValorDeEntrada av where av.aluno.login=:login", AlunoEncontraValorDeEntrada.class);
		query.setParameter("login", login);
		List<AlunoEncontraValorDeEntrada> resultList = query.getResultList();
		return resultList.size() != 0;
	}

	public int getQuantidadeValoresEncontrados(String login, Long idDesafio) {
		TypedQuery<AlunoEncontraValorDeEntrada> query = this.em.createQuery("select av " +
				"from AlunoEncontraValorDeEntrada av where av.aluno.login=:login " +
				"and av.valorDeEntrada.programa.desafio.id=:idDesafio", AlunoEncontraValorDeEntrada.class);
		query.setParameter("login", login);
		query.setParameter("idDesafio", idDesafio);
		List<AlunoEncontraValorDeEntrada> resultList = query.getResultList();
		return resultList.size();
	}

	public void atualiza(Aluno aluno){
		this.em.merge(aluno);
	}

	public void atualizaDesafioAluno(AlunoRealizaDesafio alunoDesafio){
		this.em.merge(alunoDesafio);
	}

	public void reiniciaDesafioAluno(String login) {
		Aluno alunoConsultado = this.consulta(login);
		AlunoRealizaDesafio alunoRealizaDesafio = alunoConsultado.getAlunoRealizaDesafio().get(0);
		alunoRealizaDesafio.setCerebrosDisponiveis(3);
		alunoRealizaDesafio.setSituacaoDesafio(SituacaoDesafio.EM_ANDAMENTO);
		this.atualiza(alunoConsultado);
	}
}
