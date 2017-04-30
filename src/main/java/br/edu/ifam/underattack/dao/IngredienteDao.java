package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.Ingrediente;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;


public class IngredienteDao {

	private final EntityManager em;

	@Inject
	public IngredienteDao(EntityManager em) {
		this.em = em;
	}

	@Deprecated
	public IngredienteDao() {
		this(null); // para uso do CDI
	}
	
	public List<Ingrediente> listAll() {
		return em.createQuery("select i from Ingrediente i", Ingrediente.class).getResultList();
	}

}
