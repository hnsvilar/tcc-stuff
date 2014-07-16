package damas.gui.testes;

import java.util.ArrayList;
import java.util.Random;

import damas.bd.core.Configuracao;
import damas.bd.core.Posicao;
import damas.bd.core.Usuario;
import damas.bd.dao.FacadeDAO;
import damas.game.ag.AlgoritmoGenetico;
import damas.game.arvore.Estado;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Movimentacao;
import damas.game.commom.Pecas;
import damas.game.fsm.FiniteStateMachine;

public class TestandoMain {
	public static char[] geraTab() {
//		if (participante == FiniteStateMachine.VEZ_JOGADOR) {
//			Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
//			Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
//			Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
//			Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
//		} else {
			Pecas.PEDRA_JOGADOR = Pecas.getPRETA();
			Pecas.DAMA_JOGADOR = Pecas.getDAMA_PRETA();
			Pecas.PEDRA_COMPUTADOR = Pecas.getBRANCA();
			Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_BRANCA();
//		}
		
		char [] tabuleiro = new char[64];
		int posicao = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						tabuleiro[posicao] = Pecas.PAREDE;
					} else {
						if (i < 3) {
							tabuleiro[posicao] = Pecas.PEDRA_COMPUTADOR;
						} else if (i > 4) {
							tabuleiro[posicao] = Pecas.PEDRA_JOGADOR;
						} else {
							tabuleiro[posicao] = Pecas.VAZIO;
						}
					}
				} else {
					if (j % 2 == 0) {
						if (i < 3) {
							tabuleiro[posicao] = Pecas.PEDRA_COMPUTADOR;
						} else if (i > 4) {
							tabuleiro[posicao] = Pecas.PEDRA_JOGADOR;
						} else {
							tabuleiro[posicao] = Pecas.VAZIO;
						}
					} else {
						tabuleiro[posicao] = Pecas.PAREDE;
					}
				}
				posicao++;

			}
		}
				
		return tabuleiro;
	}
	
	public static void imprimeTabuleiro(Individuo tab) {
		int aux = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(tab.getTabuleiro()[aux] + " ");
				aux++;
			}
			System.out.println();
		}
	}
	public static ArrayList<Integer> getPosicoesPossiveis(Individuo i) {
		ArrayList<Integer> posicoesPossiveis = new ArrayList<Integer>();		
		posicoesPossiveis = Movimentacao.getPosicoesPossiveisComer(i.getTabuleiro(),i.getPecaMudada());
		i.setComeu(false);
		if(posicoesPossiveis.size() != 0) {
			i.setComeu(true);
		} else {
			System.out.println("era pra entrar aki");
			posicoesPossiveis = Movimentacao.getPosicoesPossiveisMover(i.getTabuleiro(), i.getPecaMudada());
		}
		System.out.println("COnsegui: "+posicoesPossiveis.size());
		return posicoesPossiveis;
	}
	
	public static ArrayList<Individuo> mutaPopulacao(ArrayList<Individuo> populacao) {
		for(Individuo indie : populacao) {
			
			ArrayList<Integer> posicoesPossiveis = getPosicoesPossiveis(indie);
			//Se comeu
			if(indie.getComeu()) {
				System.out.println("comeu");
				ArrayList<Integer> caminhos = new ArrayList<Integer>();
				int auxiliarInicial = indie.getPecaMudada();
				do {
					int random;
					Random randomGenerator = new Random();
					random = randomGenerator.nextInt(posicoesPossiveis.size());
					char tab[] = indie.getTabuleiro();
					char aux = tab[indie.getPecaMudada()];
					tab[indie.getPecaMudada()] = Pecas.VAZIO;
					tab[posicoesPossiveis.get(random)] = aux;
					indie.setTabuleiro(tab);
					caminhos.add(indie.getPecaMudada());
					posicoesPossiveis = getPosicoesPossiveis(indie);
					indie.setPecaMudada(posicoesPossiveis.get(random));
				}while(indie.getComeu());
				indie.setComeu(true);	
				indie.setPecaMudada(auxiliarInicial);
			} else {
				//Se não comeu
				System.out.println("nao comeu");
				int random;
				Random randomGenerator = new Random();
				System.out.println(posicoesPossiveis.size());
				random = randomGenerator.nextInt(posicoesPossiveis.size());
				char tab[] = indie.getTabuleiro();
				char aux = tab[indie.getPecaMudada()];
				tab[indie.getPecaMudada()] = Pecas.VAZIO;
				tab[posicoesPossiveis.get(random)] = aux;
				indie.setTabuleiro(tab);
				ArrayList<Integer> caminhos = new ArrayList<Integer>();
				caminhos.add(indie.getPecaMudada());
				indie.setPosicoesIntermediarias(caminhos);
			}			
			
			
		}
		return populacao;
	}
	
	public static void main(String[] args) {
		ArrayList<String> a = new ArrayList<String>();
		a.add("um");
		a.add("dois");
		ArrayList<String> b = new ArrayList<String>();
		b.add("tres");
		b.add("quatro");
		b.add("cinco");
		a.addAll(b);
		a.add("seis");
		System.out.println(a);
	}
	
	public static void maein(String[] args) {
		Individuo tab = new Individuo(geraTab(), 12, 12, Jogada.COMPUTADOR);
//		tab.setPecaMudada(17);
		char[] tabuleiroAlterado = tab.getTabuleiro(); 
		tabuleiroAlterado[30] = 'P';
		tabuleiroAlterado[46] = 'P';
		tabuleiroAlterado[53] = Pecas.VAZIO;
		System.out.println("Primeiro tabuleiro");
		imprimeTabuleiro(tab);
		System.out.println("-----------------");
	
		ArrayList<Individuo> p;// = Movimentacao.getPosicoesPossiveisComer(tab.getTabuleiro(), 19);
//		System.out.println(p.toString());
		AlgoritmoGenetico ag = new AlgoritmoGenetico(new No(null, tab));
		No n = new No(null, tab);
		ag.geraNovaPopulacao();
		p = new ArrayList<Individuo>();
		
		for(Individuo i : ag.getPopulacao()) {
			if(i.getPecaMudada()==21) {
				System.out.println("HEIN");
				p.add(i);
			}
		}
		ag.setPopulacao(p);
		System.out.println("IMRPIMINDO");
		for(Individuo i : p) {
			imprimeTabuleiro(i);
		}
		ag.mutaPopulacao();
		p = ag.getPopulacao();
		
		for(Individuo i : p) {
			System.out.println("----------------- "+ p.indexOf(i) + " ---------------");
			imprimeTabuleiro(i);
		}
		
		
//		System.out.println("MUDANDO");
//		char[] aux = p.get(0).getTabuleiro();
//		aux[30] = 'B';
//		aux[21] = ' ';
//		p.get(0).setTabuleiro(aux);
//		for(Individuo i : p) {
//			System.out.println("----------------- "+ p.indexOf(i) + " ---------------");
//			imprimeTabuleiro(i);
//		}
//		p.add(tab);
		
	}
	
	public static void maidn(String[] args) {
		
		Individuo tab = new Individuo(geraTab(), 12, 12, Jogada.COMPUTADOR);
//		char[] tabuleiro mudado = new 
		System.out.println("Primeiro tabuleiro");
		imprimeTabuleiro(tab);
		No raiz = new No(null, tab);
		
		AlgoritmoGenetico ag = new AlgoritmoGenetico(raiz);
		ag.geraNovaPopulacao();
		ArrayList<Individuo> p1 = ag.getPopulacao();
		for(Individuo i : p1) {
			System.out.println(i.getPecaMudada());
			imprimeTabuleiro(i);
			System.out.println("-----------");
		}
		System.out.println("\n --------------------------- \n MUTAR --------------------------- \n");
		ag.mutaPopulacao();
		p1 = ag.getPopulacao();
		for(Individuo i : p1) {
			imprimeTabuleiro(i);
			System.out.println("-----------");
		}
//		ArrayList<Individuo> filhos = tab.getSucessores();
//		ArrayList<Integer> posicoesPossiveis = new ArrayList<Integer>();
//		System.out.println(filhos.size() + " filhos gerados!\n");
//		for(int i = 0; i < filhos.size(); i++) {
//			System.out.println("Filho " + i + " movimentou "+filhos.get(i).getPecaMudada());
//			posicoesPossiveis = Movimentacao.getPosicoesPossiveisComer(filhos.get(i).getTabuleiro(),filhos.get(i).getPecaMudada());
//			System.out.println(posicoesPossiveis.toString());
//			break;
////			imprimeTabuleiro((Individuo) filhos.get(i));
//		}
		
		
//		if(Movimentacao.podeComer(i.getTabuleiro(), i.getPecaMudada())) {				
		
		
		
		
//		FacadeDAO f = new FacadeDAO();
//		Usuario u = f.retrieveUsuario(2);
//		System.out.println(u.getConfiguracoes());
//		//System.out.println(f.getListByUsuario("Gabb").toString());
//		//Usuario u = f.retrieveUsuarioByName("Gabb");
//		//u.setConfiguracoes(f.getListConfiguracao());
//		//u.getConfiguracoes();
//		//System.out.println(u.getConfiguracoes());
//		//f.teste("Gabb");
//		
////		Usuario a = new Usuario();
////		a.setDerrotas(20);
////		a.setVitorias(1);
////		a.setNome("AUX");
////		//f.updateUsuario(a);
////		
////		Posicao aux = new Posicao(0,'X');
////		f.insertPosicao(aux);
////		
////		Configuracao co = new Configuracao();
////		co.setResultado(1);
////		
////		f.insertConfiguracao(co);
////		
////		co.addPosicao(aux);
////		aux.addConfiguracao(co);
////		f.insertPosicao(aux);
//		//a.getConfiguracoes()
	}
}
