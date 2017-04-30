package br.edu.ifam.underattack.model.enums;


/**
 * @author erika
 *
 */
public enum Desempenho {
	
	RUIM("Ruim") {
		@Override
		public double getTotalEstrelas() {
			return 0;
		}
	},
	
	NORMAL("Normal") {
		@Override
		public double getTotalEstrelas() {
			return 1;
		}
	},
	
	BOM("Bom") {
		@Override
		public double getTotalEstrelas() {
			return 2.5;
		}
	},
	
	OTIMO("Ótimo") {
		@Override
		public double getTotalEstrelas() {
			return 3;
		}
	};
	
	private String descricao;
	
	public abstract double getTotalEstrelas();
	
	private Desempenho(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
