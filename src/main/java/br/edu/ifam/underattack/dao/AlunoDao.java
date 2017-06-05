package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.*;
import br.edu.ifam.underattack.model.enums.SituacaoFase;
import br.edu.ifam.underattack.model.enums.TipoValorEntrada;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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


    public void encontraValoresDeEntrada(String login, List<ValorDeEntrada> valoresSelecionados) {
		final Aluno aluno = this.consulta(login);
		for (ValorDeEntrada valorSelecionado : valoresSelecionados) {
			ValorDeEntrada valorEncontrado = this.valorDeEntradaDao.getValorDeEntrada(valorSelecionado.getId());
			if(valorEncontrado.getTipo().equals(TipoValorEntrada.CORRETO)){
				AlunoEncontraValorDeEntrada alunoValor = new AlunoEncontraValorDeEntrada();
				alunoValor.setAluno(aluno);
				alunoValor.setValorDeEntrada(valorEncontrado);
				this.em.persist(alunoValor);
			}
		}
	}

	public boolean alunoEncontrouValores(String login) {
		TypedQuery<AlunoEncontraValorDeEntrada> query = this.em.createQuery("select av " +
				"from AlunoEncontraValorDeEntrada av where av.aluno.login=:login", AlunoEncontraValorDeEntrada.class);
		query.setParameter("login", login);
		List<AlunoEncontraValorDeEntrada> resultList = query.getResultList();
		return resultList.size() != 0;
	}
}
