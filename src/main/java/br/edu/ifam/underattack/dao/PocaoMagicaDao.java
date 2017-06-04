package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.PocaoMagica;

import javax.inject.Inject;
import javax.persistence.EntityManager;


public class PocaoMagicaDao {
	private final EntityManager em;

	@Inject
	public PocaoMagicaDao(EntityManager em) {
		this.em = em;
	}

	@Deprecated
	public PocaoMagicaDao() {
		this(null); // para uso do CDI
	}
	
	public void atualiza(PocaoMagica pocaoMagica){
		this.em.merge(pocaoMagica);
	}
}
