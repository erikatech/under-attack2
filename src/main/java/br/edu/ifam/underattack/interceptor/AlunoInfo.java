package br.edu.ifam.underattack.interceptor;

import br.edu.ifam.underattack.model.Aluno;
import br.edu.ifam.underattack.model.AlunoEncontraClasseEquivalencia;
import br.edu.ifam.underattack.model.AlunoEncontraValorDeEntrada;
import br.edu.ifam.underattack.model.AlunoRealizaDesafio;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class AlunoInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1208836393147202429L;

	private Aluno aluno;

	private AlunoRealizaDesafio alunoDesafio;

	private List<AlunoEncontraClasseEquivalencia> classesEncontradas;

	private List<AlunoEncontraValorDeEntrada> valoresEncontrados;

	private boolean mudouDeNivel;

	private boolean desbloqueouFaseDesenvolvedores;

	private boolean fimJogo;
	
//	private List<RetornoTesteTO> resultadosTeste;
	

	public Aluno getAluno() {
		return aluno;
	}

	public AlunoRealizaDesafio getAlunoDesafio() {
		return alunoDesafio;
	}

	public List<AlunoEncontraValorDeEntrada> getValoresEncontrados() {
		return valoresEncontrados;
	}

	public List<AlunoEncontraClasseEquivalencia> getClassesEncontradas() {
		return classesEncontradas;
	}

	public void login(Aluno aluno) {
		this.aluno = aluno;
	}

	public void logout() {
		this.aluno = null;
		this.alunoDesafio = null;
		this.classesEncontradas = null;
		this.desbloqueouFaseDesenvolvedores = false;
		this.fimJogo = false;
		this.mudouDeNivel = false;
	}
	
	public void iniciaValorEntrada(AlunoRealizaDesafio alunoDesafio) {
		this.alunoDesafio = alunoDesafio;
		this.alunoDesafio.getAluno().setAlunoEncontraValorDeEntrada(
				new ArrayList<AlunoEncontraValorDeEntrada>());
	}

	public void iniciaClassesEquivalencia(AlunoRealizaDesafio alunoDesafio) {
		this.alunoDesafio = alunoDesafio;
		this.alunoDesafio.getAluno().setAlunoEncontraClasseEquivalencia(
				new ArrayList<AlunoEncontraClasseEquivalencia>());
	}

	public void conclui() {
		this.alunoDesafio = null;

	}

	public void incluiValoresEntrada(
			List<AlunoEncontraValorDeEntrada> valoresEncontrados) {
		this.valoresEncontrados = valoresEncontrados;
	}

	public void reinicarValoresEncontrados() {
		this.alunoDesafio.getAluno().setAlunoEncontraValorDeEntrada(
				new ArrayList<AlunoEncontraValorDeEntrada>());
	}

	public void addClasseEquivalencia(
			AlunoEncontraClasseEquivalencia classeAluno) {
		this.classesEncontradas.add(classeAluno);
	}

	public void reinicarClassesEquivalencia() {
		this.classesEncontradas = new ArrayList<AlunoEncontraClasseEquivalencia>();
	}

	public void iniciaClasses(AlunoRealizaDesafio alunoDesafio) {
		this.alunoDesafio = alunoDesafio;
	}

	public boolean isLogado() {
		return this.aluno != null;
	}
	
//	public void reiniciaFaseDesenvolvedores(){
//		this.resultadosTeste.clear();
//	}

	public boolean getMudouDeNivel() {
		return mudouDeNivel;
	}

	public void setMudouDeNivel(boolean mudouDeNivel) {
		this.mudouDeNivel = mudouDeNivel;
	}

	public boolean getDesbloqueouFaseDesenvolvedores() {
		return desbloqueouFaseDesenvolvedores;
	}

	public void setDesbloqueouFaseDesenvolvedores(
			boolean desbloqueouFaseDesenvolvedores) {
		this.desbloqueouFaseDesenvolvedores = desbloqueouFaseDesenvolvedores;
	}

	/*public void setListaResultados(List<RetornoTesteTO> resultadosTeste) {
		this.resultadosTeste = resultadosTeste;
	}
	
	public List<RetornoTesteTO> getResultadosTeste() {
		return resultadosTeste;
	}
*/
	public void setFimJogo(boolean fimJogo) {
		this.fimJogo = fimJogo;
	}
	
	public boolean getFimJogo() {
		return fimJogo;
	}
}
