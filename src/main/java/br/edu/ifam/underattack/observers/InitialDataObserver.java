package br.edu.ifam.underattack.observers;

import br.com.caelum.vraptor.events.VRaptorInitialized;
import br.edu.ifam.underattack.dao.UsuarioDao;
import br.edu.ifam.underattack.model.Professor;
import br.edu.ifam.underattack.util.JPAUtil;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class InitialDataObserver {

	public void createProfessor(@Observes VRaptorInitialized event) {
		EntityManager em = JPAUtil.criaEntityManager();
		String jpql = "select p from Professor p";
		List<Professor> professores = em.createQuery(jpql, Professor.class).getResultList();
		if (professores.isEmpty()){
			em.getTransaction().begin();
			em.persist(new Professor("Jucimar Brito", "jucimar", "123"));
			em.getTransaction().commit();
			em.close();
		}
	}
}