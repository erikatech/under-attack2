package br.edu.ifam.underattack.model.enums;

public enum SituacaoDesafio {
	
	NAO_INICIADO ("A iniciar"),
	
	EM_ANDAMENTO("Em Andamento"),
	
	EM_TESTE("Aguardando Testes Automatizados"),
	
	CONCLUIDO("Concluído");
	
	
	private String label;
	
	private SituacaoDesafio(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
