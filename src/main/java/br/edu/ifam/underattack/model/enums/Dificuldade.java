package br.edu.ifam.underattack.model.enums;


/**
 * @author erika
 *
 */
public enum Dificuldade {
	
	FACIL("Fácil") {
		@Override
		public int getPontos() {
			return 10;
		}
	},
	
	NORMAL("Normal") {
		@Override
		public int getPontos() {
			return 25;
		}
	},
	
	DIFICIL("Difícil") {
		@Override
		public int getPontos() {
			return 40;
		}
	},
	
	CHUCK_NORRIS("Chuck Norris") {
		@Override
		public int getPontos() {
			return 65;
		}
	};
	
	private String descricao;
	
	public abstract int getPontos();
	
	private Dificuldade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
