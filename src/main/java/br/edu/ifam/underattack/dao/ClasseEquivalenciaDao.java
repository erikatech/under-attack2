package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.ClasseEquivalencia;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class ClasseEquivalenciaDao {

    private final EntityManager em;

    @Inject
    public ClasseEquivalenciaDao(EntityManager em) {
        this.em = em;
    }

    @Deprecated
    public ClasseEquivalenciaDao() {
        this(null); // para uso do CDI
    }

    public List<ClasseEquivalencia> getClassesDesafioAtual(Long idDesafio){
        TypedQuery<ClasseEquivalencia> query =
                this.em.createQuery("select c from ClasseEquivalencia  c " +
                        "where c.valorDeEntrada.programa.desafio.id=:idDesafio", ClasseEquivalencia.class);

        query.setParameter("idDesafio", idDesafio);
        return query.getResultList();
    }

    public ClasseEquivalencia consulta(Long id) {
        return this.em.find(ClasseEquivalencia.class, id);

    }
}
