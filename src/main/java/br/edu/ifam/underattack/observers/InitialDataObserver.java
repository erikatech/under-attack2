package br.edu.ifam.underattack.observers;

import br.com.caelum.vraptor.events.VRaptorInitialized;
import br.edu.ifam.underattack.model.*;
import br.edu.ifam.underattack.util.JPAUtil;

import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class InitialDataObserver {


	/**
	 * Esse é um exemplo simples de observer do CDI com VRaptor 4
	 *
	 * Ele é utilizado para inserir um usuário e alguns produtos
	 * sempre que a app é startada, pois estamos usando um banco
	 * em memória. Você pode ler mais a respeito de observers em:
	 *
	 * http://www.vraptor.org/pt/docs/eventos/*/

	public void prepareIngredientes(@Observes VRaptorInitialized event) {
		EntityManager em = JPAUtil.criaEntityManager();
		TypedQuery<Ingrediente> query = em.createQuery(
				"select i from Ingrediente i", Ingrediente.class);
		List<Ingrediente> ingredientesToRemove = query.getResultList();

		if(ingredientesToRemove.isEmpty()){
			List<Ingrediente> ingredientesToInsert = this.criaIngredientes();
			for(Ingrediente ingrediente : ingredientesToInsert){
				em.getTransaction().begin();
				em.persist(ingrediente);
				em.getTransaction().commit();
			}
		}
		em.close();
	}

	private List<Ingrediente> criaIngredientes() {
		List<Ingrediente> ingredientes = new ArrayList<>();
		Ingrediente cogumelo = new Ingrediente();
		cogumelo.setDescricao("Mucho loco cogumelo");
		cogumelo.setNomeImagem("cogumelo");

		Ingrediente cabeloGoku = new Ingrediente();
		cabeloGoku.setDescricao("Fio de cabelo do Goku");
		cabeloGoku.setNomeImagem("cabelo-goku");

		Ingrediente espada = new Ingrediente();
		espada.setDescricao("Espada");
		espada.setNomeImagem("espada");

		ingredientes.addAll(Arrays.asList(cogumelo, cabeloGoku, espada));
		return ingredientes;
	}

	public void prepareFases(@Observes VRaptorInitialized event) {
		EntityManager em = JPAUtil.criaEntityManager();
		TypedQuery<Fase> query = em.createQuery(
				"select f from Fase f", Fase.class);
		List<Fase> fases = query.getResultList();

		if(fases.isEmpty()){
			List<Fase> fasesToInsert = this.criaFases();

			for(Fase fase : fasesToInsert){
				em.getTransaction().begin();
				em.persist(fase);
				for(Objetivo objetivo : fase.getObjetivos()){
					objetivo.setFase(fase);
				}
				em.getTransaction().commit();
			}
		}
		em.close();
	}

	private List<Fase> criaFases() {
		Fase testadores = new Fase();
		testadores.setTitulo("Sala dos Testadores");
		testadores.setDescricao("A fábrica foi invadida por procriadores de bugs zumbis" +
				" disfarçados de desenvolvedores. Os procriadores infestaram trechos de códigos" +
				" de bugs zumbis e aprisionaram os verdadeiros desenvolvedores em uma das " +
				"salas da fábrica, impedindo-os de aniquilar os bugs");
		testadores.setNomeImagem("gambster.png");

		Objetivo encontrarItens = new Objetivo();
		encontrarItens.setDescricao("Encontrar os itens da poção");
		encontrarItens.setPontos(200);


		Objetivo aniquilarZombugs = new Objetivo();
		aniquilarZombugs.setDescricao("Encontrar todos os zombugs");
		aniquilarZombugs.setPontos(350);

		testadores.setObjetivos(new HashSet<Objetivo>(Arrays.asList(encontrarItens, aniquilarZombugs)));

		Fase desenvolvedores = new Fase();
		desenvolvedores.setTitulo("Sala dos Desenvolvedores");
		desenvolvedores.setDescricao("Parabéns! Os verdadeiros desenvolvedores agora estão livres graças a sua ajuda." +
				" Mas... espere um momento. Tem algo errado com eles! Parece que os gambsters serviram café batizado " +
				"um poderoso sedativo aos desenvolvedores, que os deixarão inconscientes por dias, meses e porque não, " +
				"AAAAAAAAANOOOOOOOOS! Sua missão agora é construir os testes automatizados que identifiquem os bugs zumbis" +
				" nos códigos. Boa sorte!");
		desenvolvedores.setNomeImagem("zombie-hand.png");

		Objetivo energizarMaquina = new Objetivo();
		energizarMaquina.setDescricao("Energizar máquina com testes");
		energizarMaquina.setPontos(200);

		desenvolvedores.setObjetivos(new HashSet<Objetivo>(Arrays.asList(energizarMaquina)));
		encontrarItens.setFase(desenvolvedores);

		return Arrays.asList(testadores, desenvolvedores);
	}

	public void prepareNiveis(@Observes VRaptorInitialized event) {
		EntityManager em = JPAUtil.criaEntityManager();
		TypedQuery<NivelAluno> query = em.createQuery(
				"select n from NivelAluno n", NivelAluno.class);
		List<NivelAluno> niveis = query.getResultList();

		if(niveis.isEmpty()){
			List<NivelAluno> niveisToInsert = this.criaNiveis();

			for(NivelAluno nivelAluno : niveisToInsert){
				em.getTransaction().begin();
				em.persist(nivelAluno);
				em.getTransaction().commit();
			}
		}
		em.close();
	}

	private List<NivelAluno> criaNiveis() {
		NivelAluno estagiario = new NivelAluno();
		estagiario.setDescricao("Aprendiz de exterminador");
		estagiario.setMinimo(0);
		estagiario.setMaximo(600);

		NivelAluno aniquiladorJunior = new NivelAluno();
		aniquiladorJunior.setDescricao("Aniquiliador Jr.");
		aniquiladorJunior.setMinimo(600);
		aniquiladorJunior.setMaximo(900);

		NivelAluno aniquiladorSenior = new NivelAluno();
		aniquiladorSenior.setDescricao("Aniquilador Sr.");
		aniquiladorSenior.setMinimo(900);
		aniquiladorSenior.setMaximo(1200);

		return Arrays.asList(estagiario, aniquiladorJunior, aniquiladorSenior);
	}


		/**
         *
         * @param event
         */
	public void createProfessor(@Observes VRaptorInitialized event) {
		EntityManager em = JPAUtil.criaEntityManager();
		String jpql = "select p from Professor p";
		List<Professor> professores = em.createQuery(jpql, Professor.class).getResultList();
		if (professores.isEmpty()){
			em.getTransaction().begin();
			em.persist(new Professor("Jucimar Brito", "jucimar", "123"));
			em.getTransaction().commit();
			em.close();
		}
	}
}