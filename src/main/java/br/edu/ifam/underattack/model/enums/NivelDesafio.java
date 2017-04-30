package br.edu.ifam.underattack.model.enums;

public enum NivelDesafio {

	FACIL("Fácil"),

	INTERMEDIARIO("Intermediário"),

	DIFICIL("Difícil");

	private String label;

	private NivelDesafio(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
