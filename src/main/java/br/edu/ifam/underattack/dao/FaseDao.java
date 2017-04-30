package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.Fase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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

}
