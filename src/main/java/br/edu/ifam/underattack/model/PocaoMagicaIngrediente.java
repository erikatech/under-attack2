package br.edu.ifam.underattack.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.codec.binary.Base64;

import br.edu.ifam.underattack.model.enums.SituacaoIngrediente;

@Entity
@Table(name = "pocao_magica_ingrediente")
public class PocaoMagicaIngrediente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3615002693510085743L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "pocao_id")
	private PocaoMagica pocaoMagica;

	@ManyToOne
	@JoinColumn(name = "ingrediente_id")
	private Ingrediente ingrediente;

	@Enumerated(EnumType.STRING)
	private SituacaoIngrediente situacaoIngrediente;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PocaoMagica getPocaoMagica() {
		return pocaoMagica;
	}

	public void setPocaoMagica(PocaoMagica pocaoMagica) {
		this.pocaoMagica = pocaoMagica;
	}

	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}

	public SituacaoIngrediente getSituacaoIngrediente() {
		return situacaoIngrediente;
	}

	public void setSituacaoIngrediente(SituacaoIngrediente situacaoIngrediente) {
		this.situacaoIngrediente = situacaoIngrediente;
	}
}
