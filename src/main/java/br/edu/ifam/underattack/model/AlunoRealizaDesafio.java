package br.edu.ifam.underattack.model;

import br.edu.ifam.underattack.model.enums.Desempenho;
import br.edu.ifam.underattack.model.enums.IndicadorFase;
import br.edu.ifam.underattack.model.enums.SituacaoDesafio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluno_desafio")
public class AlunoRealizaDesafio implements Serializable {

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
	@JoinColumn(name = "desafio_id")
	private Desafio desafio;

	@Enumerated(EnumType.STRING)
	private SituacaoDesafio situacaoDesafio;

	private Integer cerebrosDisponiveis;

	@Enumerated(EnumType.STRING)
	private Desempenho desempenho;

	@Enumerated(EnumType.STRING)
	private IndicadorFase indicadorFase;

	private double totalEstrelas;

	private int totalTestesEscritos;

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

	public Desafio getDesafio() {
		return desafio;
	}

	public void setDesafio(Desafio desafio) {
		this.desafio = desafio;
	}

	public SituacaoDesafio getSituacaoDesafio() {
		return situacaoDesafio;
	}

	public void setSituacaoDesafio(SituacaoDesafio situacaoDesafio) {
		this.situacaoDesafio = situacaoDesafio;
	}

	public void setDesempenho(Desempenho desempenho) {
		this.desempenho = desempenho;
	}

	public Integer getCerebrosDisponiveis() {
		return cerebrosDisponiveis;
	}

	public void setCerebrosDisponiveis(Integer cerebrosDisponiveis) {
		this.cerebrosDisponiveis = cerebrosDisponiveis;
	}

	public Desempenho getDesempenho() {
		return this.desempenho;
	}

	public IndicadorFase getIndicadorFase() {
		return indicadorFase;
	}

	public void setIndicadorFase(IndicadorFase indicadorFase) {
		this.indicadorFase = indicadorFase;
	}

	public double getTotalEstrelas() {
		return totalEstrelas;
	}

	public void setTotalEstrelas(double totalEstrelas) {
		this.totalEstrelas = totalEstrelas;
	}

	public int getTotalTestesEscritos() {
		return totalTestesEscritos;
	}

	public void setTotalTestesEscritos(int totalTestesEscritos) {
		this.totalTestesEscritos = totalTestesEscritos;
	}

	public Desempenho calcularDesempenho() {
		int totalBugs = 0;
		int totalbugsEncontrados = 0;

		List<ClasseEquivalencia> todasAsClassesEquivalencia = new ArrayList<ClasseEquivalencia>();

		List<ValorDeEntrada> valoresEntradaDoDesafio = desafio.getPrograma().getValoresEntrada();

		for (ValorDeEntrada valorDeEntrada : valoresEntradaDoDesafio) {
			if (valorDeEntrada.isValorEntradaCorreto()) {
				todasAsClassesEquivalencia.addAll(valorDeEntrada
						.getClassesEquivalencia());
			}
		}

		for (ClasseEquivalencia classe : todasAsClassesEquivalencia) {
			if (classe.getBugExistente()) {
				totalBugs++;
			}
		}

		List<AlunoEncontraClasseEquivalencia> classesEncontradas = aluno
				.getAlunoEncontraClasseEquivalencia();
		for (AlunoEncontraClasseEquivalencia alunoClasse : classesEncontradas) {
			if (alunoClasse.getClasseEquivalencia().getBugExistente()) {
				totalbugsEncontrados++;
			}
		}

		double desempenho = (totalbugsEncontrados * 100) / totalBugs;

		return getDesempenho(desempenho);
	}

	public Desempenho calcularDesempenhoEscritaTestes(int totalTestesEscritos) {
		int totalTestes = 0;

		List<ValorDeEntrada> valoresEntradaDoDesafio = desafio.getPrograma().getValoresEntrada();

		for (ValorDeEntrada valorDeEntrada : valoresEntradaDoDesafio) {
			totalTestes += valorDeEntrada.getClassesEquivalencia().size();
		}
		double desempenho = (totalTestesEscritos * 100) / totalTestes;

		return getDesempenho(desempenho);
	}

	private Desempenho getDesempenho(double desempenho) {
		if (desempenho >= 0 && desempenho <= 25) {
			return Desempenho.RUIM;
		} else if (desempenho > 25 && desempenho <= 50) {
			return Desempenho.NORMAL;
		} else if (desempenho > 50 && desempenho <= 85) {
			return Desempenho.BOM;
		} else if (desempenho > 85) {
			return Desempenho.OTIMO;
		}
		return null;
	}
	
	public boolean getFaseDesenvolvedores(){
		return this.indicadorFase.equals(IndicadorFase.FASE_DESENVOLVEDORES);
	}

}
