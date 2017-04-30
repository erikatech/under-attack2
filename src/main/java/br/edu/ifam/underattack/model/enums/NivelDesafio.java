package br.edu.ifam.underattack.model.enums;

public enum NivelDesafio {

	FACIL("F�cil"),

	INTERMEDIARIO("Intermedi�rio"),

	DIFICIL("Dif�cil");

	private String label;

	private NivelDesafio(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
