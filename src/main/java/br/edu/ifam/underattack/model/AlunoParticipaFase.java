package br.edu.ifam.underattack.model;

import br.edu.ifam.underattack.model.enums.SituacaoDesafio;
import br.edu.ifam.underattack.model.enums.SituacaoFase;
import br.edu.ifam.underattack.model.enums.TipoObjetivo;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "aluno_fase")
public class AlunoParticipaFase implements Serializable {

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
	@JoinColumn(name = "fase_id")
	private Fase fase;

	@Enumerated(EnumType.STRING)
	private SituacaoFase situacaoFase;

	private Boolean historiaExibida;

	@OneToMany(mappedBy = "alunoFase", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<FaseObjetivo> faseObjetivo;

	private int bugsEncontrados;

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

	public Fase getFase() {
		return fase;
	}

	public void setFase(Fase fase) {
		this.fase = fase;
	}

	public SituacaoFase getSituacaoFase() {
		return situacaoFase;
	}

	public void setSituacaoFase(SituacaoFase situacaoFase) {
		this.situacaoFase = situacaoFase;
	}

	public Boolean getHistoriaExibida() {
		return historiaExibida;
	}

	public void setHistoriaExibida(Boolean historiaExibida) {
		this.historiaExibida = historiaExibida;
	}

	public Set<FaseObjetivo> getFaseObjetivo() {
		return faseObjetivo;
	}

	public void setFaseObjetivo(Set<FaseObjetivo> faseObjetivo) {
		this.faseObjetivo = faseObjetivo;
	}

	public int getBugsEncontrados() {
		return bugsEncontrados;
	}

	public void setBugsEncontrados(int bugsEncontrados) {
		this.bugsEncontrados = bugsEncontrados;
	}

	public boolean isBloqueada() {
		return this.situacaoFase.equals(SituacaoFase.BLOQUEADA);
	}

	public FaseObjetivo getObjetivoRealizado(TipoObjetivo tipoObjetivo) {
		for (FaseObjetivo faseObjetivo : this.faseObjetivo) {
			if (faseObjetivo.getObjetivo().getTipoObjetivo()
					.equals(tipoObjetivo)) {
				return faseObjetivo;
			}
		}
		return null;
	}

	public double getProgresso() {
		int totalDesafiosFase = fase.getDesafios().size();
		int desafiosFinalizados = 0;
		for (AlunoRealizaDesafio alunoDesafio : aluno.getAlunoRealizaDesafio()) {
			if (alunoDesafio.getSituacaoDesafio().equals(
					SituacaoDesafio.CONCLUIDO)) {
				desafiosFinalizados++;
			}
		}
		return (desafiosFinalizados * 100) / totalDesafiosFase;
	}

	public String getProgressoTestes() {
		Set<AlunoRealizaDesafio> alunoRealizaDesafioSemRepetir = new HashSet<AlunoRealizaDesafio>();
		List<AlunoRealizaDesafio> alunoRealizaDesafio = this.aluno
				.getAlunoRealizaDesafio();

		for (AlunoRealizaDesafio ar : alunoRealizaDesafio) {
			alunoRealizaDesafioSemRepetir.add(ar);
		}

		int testesConstruidos = 0;
		for (AlunoRealizaDesafio alunoDesafio : alunoRealizaDesafioSemRepetir) {
			if (alunoDesafio.getFaseDesenvolvedores()) {
				testesConstruidos += alunoDesafio.getTotalTestesEscritos();
			}
		}
		DecimalFormat format = new DecimalFormat("0.#");
		return format.format((testesConstruidos * 100)
				/ this.getQuantidadeCasosTeste());
	}

	public int getQuantidadeCasosTeste() {
		return this.fase.getQuantidadeCasosTeste();
	}

}
