package br.edu.ifam.underattack.observers;

import br.com.caelum.vraptor.events.VRaptorInitialized;
import br.edu.ifam.underattack.dao.UsuarioDao;
import br.edu.ifam.underattack.model.Professor;
import br.edu.ifam.underattack.util.JPAUtil;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;

public class InitialDataObserver {

	public void createProfessor(@Observes VRaptorInitialized event) {
		EntityManager em = JPAUtil.criaEntityManager();
		em.getTransaction().begin();
		em.persist(new Professor("jucimar", "123"));
		em.getTransaction().commit();
		em.close();
	}
}