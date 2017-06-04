package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.Ingrediente;
import br.edu.ifam.underattack.model.PocaoMagica;
import br.edu.ifam.underattack.model.PocaoMagicaIngrediente;
import br.edu.ifam.underattack.model.enums.SituacaoIngrediente;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


public class IngredienteDao {

	private final EntityManager em;

	private PocaoMagicaDao pocaoMagicaDao;

	@Inject
	public IngredienteDao(EntityManager em, PocaoMagicaDao pocaoMagicaDao) {
		this.em = em;
		this.pocaoMagicaDao = pocaoMagicaDao;
	}

	@Deprecated
	public IngredienteDao() {
		this(null, null); // para uso do CDI
	}
	
	public List<Ingrediente> listAll() {
		return em.createQuery("select i from Ingrediente i", Ingrediente.class).getResultList();
	}

	public void associaIngrediente(PocaoMagica pocaoMagica){
		List<PocaoMagicaIngrediente> pocaoMagicaIngredienteList = new ArrayList<PocaoMagicaIngrediente>();
		List<Ingrediente> ingredientes = this.listAll();
		for (Ingrediente ingrediente : ingredientes) {
			PocaoMagicaIngrediente pocaoIngrediente = new PocaoMagicaIngrediente();
			pocaoIngrediente.setIngrediente(ingrediente);
			pocaoIngrediente.setPocaoMagica(pocaoMagica);
			pocaoIngrediente
					.setSituacaoIngrediente(SituacaoIngrediente.ESCONDIDO);
			pocaoMagicaIngredienteList.add(pocaoIngrediente);
		}
		pocaoMagica.setPocaoIngredienteList(pocaoMagicaIngredienteList);
		pocaoMagicaDao.atualiza(pocaoMagica);
	}

}
