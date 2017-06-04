package br.edu.ifam.underattack.dao;

import br.edu.ifam.underattack.model.NivelAluno;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class NivelAlunoDao {

	private final EntityManager em;

	@Inject
	public NivelAlunoDao(EntityManager em) {
		this.em = em;
	}

	@Deprecated
	public NivelAlunoDao() {
		this(null); // para uso do CDI
	}

	public NivelAluno consulta(Long id) {
		TypedQuery<NivelAluno> query = this.em.createQuery(
				"select n from NivelAluno n where n.id =:id", NivelAluno.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
}
