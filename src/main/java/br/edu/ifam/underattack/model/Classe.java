package br.edu.ifam.underattack.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "classe")
public class Classe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2856575460981132989L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Lob
	private String codigo;

	@ManyToOne
	private Programa programa;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

}
