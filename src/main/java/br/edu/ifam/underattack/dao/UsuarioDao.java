package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;


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
	
	public boolean existe(Usuario usuario) {
		return !em.createQuery("select u from Usuario u where u.login = "
			+ ":login and u.senha = :senha", Usuario.class)
			.setParameter("login", usuario.getLogin())
			.setParameter("senha", usuario.getSenha())
			.getResultList().isEmpty();
	}

	public void salva(Usuario usuario) {
		em.persist(usuario);
	}

}
