package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.Desafio;
import br.edu.ifam.underattack.model.Fase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;


public class DesafioDao {

    private final EntityManager em;

    @Inject
    public DesafioDao(EntityManager em) {
        this.em = em;
    }

    @Deprecated
    public DesafioDao() {
        this(null); // para uso do CDI
    }

    public List<Desafio> listAll() {
        return em.createQuery("select d from Desafio d", Desafio.class).getResultList();
    }

    public void add(Desafio desafio){
        this.em.persist(desafio);
    }

}
