package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.AlunoEncontraValorDeEntrada;
import br.edu.ifam.underattack.model.ValorDeEntrada;
import br.edu.ifam.underattack.model.enums.TipoValorEntrada;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;


public class ValorDeEntradaDao {

    private final EntityManager em;

    @Inject
    public ValorDeEntradaDao(EntityManager em) {
        this.em = em;
    }

    @Deprecated
    public ValorDeEntradaDao() {
        this(null); // para uso do CDI
    }

    public ValorDeEntrada getValorDeEntrada(Long id){
        return this.em.find(ValorDeEntrada.class, id);
    }

    public List<AlunoEncontraValorDeEntrada> getValoresAluno(String login){
        TypedQuery<AlunoEncontraValorDeEntrada> query = this.em.createQuery("select av from " +
                "AlunoEncontraValorDeEntrada av where av.aluno.login=:login", AlunoEncontraValorDeEntrada.class);
        query.setParameter("login", login);
        return query.getResultList();
    }

    public int getTotalValoresCorretos(Long idDesafio) {
        TypedQuery<ValorDeEntrada> query =
                this.em.createQuery("select v from ValorDeEntrada v where v.tipo =:tipo " +
                        "and v.programa.desafio.id=:idDesafio", ValorDeEntrada.class);
        query.setParameter("tipo", TipoValorEntrada.CORRETO);
        query.setParameter("idDesafio", idDesafio);
        return query.getResultList().size();


    }
}
