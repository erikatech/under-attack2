package br.edu.ifam.underattack.model.enums;

public enum TipoClasseEquivalencia {
	
	VALIDA ("Válida"),
	
	INVALIDA ("Inválida");
	
	private String label;
	
	private TipoClasseEquivalencia(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
