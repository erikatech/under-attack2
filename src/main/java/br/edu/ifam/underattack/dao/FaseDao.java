package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.AlunoParticipaFase;
import br.edu.ifam.underattack.model.Fase;
import br.edu.ifam.underattack.model.Objetivo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class FaseDao {

	private final EntityManager em;

	@Inject
	public FaseDao(EntityManager em) {
		this.em = em;
	}

	@Deprecated
	public FaseDao() {
		this(null); // para uso do CDI
	}
	
	public List<Fase> listAll() {
		return em.createQuery("select f from Fase f", Fase.class).getResultList();
	}

	public List<Objetivo> listObjetivos(Long idFase) {
		TypedQuery<Objetivo> query = this.em.createQuery("select o from Objetivo o where o.fase.id =:id",Objetivo.class);
		query.setParameter("id", idFase);
		return query.getResultList();
	}

	public void updateFase(Fase fase){
		this.em.merge(fase);
	}

	public List<AlunoParticipaFase> listFasesFromAluno(String login){
		TypedQuery<AlunoParticipaFase> query =
				this.em.createQuery("select a from AlunoParticipaFase a where a.aluno.login =:login",
						AlunoParticipaFase.class);
		query.setParameter("login", login);
		return query.getResultList();
	}
}
