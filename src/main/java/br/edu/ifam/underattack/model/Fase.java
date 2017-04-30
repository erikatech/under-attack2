package br.edu.ifam.underattack.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "fase")
public class Fase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6373024255164208314L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	@Lob
	private String descricao;

	private String nomeImagem;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "fase_desafio", joinColumns = { @JoinColumn(name = "fase_id") }, inverseJoinColumns = { @JoinColumn(name = "desafio_id") })
	private List<Desafio> desafios;

	@OneToMany(mappedBy = "fase", cascade = CascadeType.ALL)
	private Set<Objetivo> objetivos;

	public Fase() {
		this.desafios = new ArrayList<Desafio>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeImagem() {
		return nomeImagem;
	}

	public void setNomeImagem(String pathToImage) {
		this.nomeImagem = pathToImage;
	}

	public List<Desafio> getDesafios() {
		return desafios;
	}

	public void setDesafios(List<Desafio> desafios) {
		this.desafios = desafios;
	}

	public Set<Objetivo> getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(Set<Objetivo> objetivos) {
		this.objetivos = objetivos;
	}

	public void adicionarDesafio(Desafio desafio) {
		this.desafios.add(desafio);
	}

	public int getQuantidadeCasosTeste() {
		int total = 0;
		for (Desafio desafio : desafios) {
			total += desafio.getTotalTestesDesafio();
		}
		return total;
	}

	public int getQuantidadeBugsFase() {
		int total = 0;
		for (Desafio desafio : this.desafios) {
			List<ClasseEquivalencia> classesEquivalencia = desafio
					.getClassesEquivalencia();
			for (ClasseEquivalencia classeEquivalencia : classesEquivalencia) {
				if (classeEquivalencia.getBugExistente()) {
					total++;
				}
			}
		}
		return total;
	}

}
