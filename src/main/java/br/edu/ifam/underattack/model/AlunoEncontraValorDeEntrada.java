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
@Table(name = "aluno_valor_de_entrada")
public class AlunoEncontraValorDeEntrada implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3615002693510085743L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "aluno_id")
	private Aluno aluno;

	@ManyToOne
	@JoinColumn(name = "valor_de_entrada_id")
	private ValorDeEntrada valorDeEntrada;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public ValorDeEntrada getValorDeEntrada() {
		return valorDeEntrada;
	}

	public void setValorDeEntrada(ValorDeEntrada valorDeEntrada) {
		this.valorDeEntrada = valorDeEntrada;
	}

}
