package br.edu.ifam.underattack.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fase_objetivo")
public class FaseObjetivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8803773811401908892L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "alunoFase_id")
	private AlunoParticipaFase alunoFase;

	@ManyToOne
	@JoinColumn(name = "objetivo_id")
	private Objetivo objetivo;

	private boolean realizado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AlunoParticipaFase getAlunoFase() {
		return alunoFase;
	}

	public void setAlunoFase(AlunoParticipaFase alunoFase) {
		this.alunoFase = alunoFase;
	}

	public Objetivo getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Objetivo objetivo) {
		this.objetivo = objetivo;
	}

	public boolean isRealizado() {
		return realizado;
	}

	public void setRealizado(boolean realizado) {
		this.realizado = realizado;
	}

}
