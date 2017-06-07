package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.PocaoMagicaIngrediente;

import javax.inject.Inject;
import javax.persistence.EntityManager;


public class PocaoMagicaIngredienteDao {
    private final EntityManager em;

    @Inject
    public PocaoMagicaIngredienteDao(EntityManager em) {
        this.em = em;
    }

    @Deprecated
    public PocaoMagicaIngredienteDao() {
        this(null); // para uso do CDI
    }

    public void atualiza(PocaoMagicaIngrediente pocaoIngrediente) {
        this.em.merge(pocaoIngrediente);
    }
}
