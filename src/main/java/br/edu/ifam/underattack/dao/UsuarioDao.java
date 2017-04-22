package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;


public class UsuarioDao {

	private final EntityManager em;

	@Inject
	public UsuarioDao(EntityManager em) {
		this.em = em;
	}
	
	@Deprecated
	public UsuarioDao() {
		this(null); // para uso do CDI
	}
	
	public List<Usuario> consulta(String login, String senha) {
		return em.createQuery("select u from Usuario u where u.login = "
			+ ":login and u.senha = :senha", Usuario.class)
			.setParameter("login", login)
			.setParameter("senha", senha).getResultList();
	}

}
