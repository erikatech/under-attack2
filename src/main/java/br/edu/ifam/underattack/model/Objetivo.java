package br.edu.ifam.underattack.model;

import br.edu.ifam.underattack.model.enums.TipoObjetivo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "objetivo")
public class Objetivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6220832636921943288L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String descricao;

	private int pontos;

	@ManyToOne
	private Fase fase;

	@Enumerated(EnumType.STRING)
	private TipoObjetivo tipoObjetivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getPontos() {
		return pontos;
	}

	public void setPontos(int pontos) {
		this.pontos = pontos;
	}

	public Fase getFase() {
		return fase;
	}

	public void setFase(Fase fase) {
		this.fase = fase;
	}

	public TipoObjetivo getTipoObjetivo() {
		return tipoObjetivo;
	}

	public void setTipoObjetivo(TipoObjetivo tipoObjetivo) {
		this.tipoObjetivo = tipoObjetivo;
	}

}
