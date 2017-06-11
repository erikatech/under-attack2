package br.edu.ifam.underattack.observers;

import br.com.caelum.vraptor.events.VRaptorInitialized;
import br.edu.ifam.underattack.model.Fase;
import br.edu.ifam.underattack.model.Ingrediente;
import br.edu.ifam.underattack.model.NivelAluno;
import br.edu.ifam.underattack.model.Objetivo;
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
		cogumelo.setDescricao("Código");
		cogumelo.setNomeImagem("card");

		ingredientes.addAll(Arrays.asList(cogumelo));
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
		testadores.setDescricao("Os Gambsters invadiram a fabrica de software espalhando zombugs por " +
				"todos os códigos e aprisionando os desenvolvedores em uma das salas. Encontre os Zombugs e " +
				"desvende o código para libertar os desenvolvedores desta enrascada.");
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
		desenvolvedores.setDescricao("'Os Gambsters aprontaram novamente! Eles serviram aos desenvolvedores café " +
				"batizado com um poderoso sedativo. Sua missão agora é de construir os testes automatizados que irão " +
				"energizar a máquina de sons que poderá acordar os desenvolvedores. Vamos lá!'");
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
}