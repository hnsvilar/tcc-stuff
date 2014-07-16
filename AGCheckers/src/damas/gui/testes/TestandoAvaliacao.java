package damas.gui.testes;

import java.util.ArrayList;

import damas.game.ag.AlgoritmoGenetico;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;
import damas.game.fsm.FiniteStateMachine;

public class TestandoAvaliacao {
	private static final int AVANCO_SIMPLES = 1;
	/*
	 * identifica o movimento realizado por um aliado que resulta em 
	 * sua captura por um adversário.
	 */
	private static final int JOGADA_RESULTA_CAPTURA = -5;
	/*
	 * identifica que a permanência de um aliado em sua posição original
	 * (literalmente quando a peça representada pelo indivíduo está em 
	 * sua origem) resulta em sua captura.
	 */
	private static final int NAO_JOGADA_RESULTA_CAPTURA = 2;
	/*
	 * identifica jogada de um aliado que resulta em capturar um adversário
	 */
	private static final int CAPTURA_UMA_PECA = 3;
	
	
	public static char[] geraTab() {
//		if (participante == FiniteStateMachine.VEZ_JOGADOR) {
//			Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
//			Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
//			Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
//			Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
//		} else {
//			Pecas.PEDRA_JOGADOR = Pecas.getPRETA();
//			Pecas.DAMA_JOGADOR = Pecas.getDAMA_PRETA();
//			Pecas.PEDRA_COMPUTADOR = Pecas.getBRANCA();
//			Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_BRANCA();
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
	
	public static void imprimeTabuleiro(char[] tab) {
		int aux = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(tab[aux] + " ");
				aux++;
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		testeAvaliaDiagonalOrigem();
	}
	
	public static void testeAvaliaDiagonalOrigem() {
		Pecas.PEDRA_JOGADOR = Pecas.VAZIO;
		Pecas.DAMA_JOGADOR = Pecas.VAZIO;
		Pecas.PEDRA_COMPUTADOR = Pecas.VAZIO;
		Pecas.DAMA_COMPUTADOR = Pecas.VAZIO;	
		AlgoritmoGenetico ag = new AlgoritmoGenetico(null);
		imprimeTabuleiro(geraTab());
		System.out.println("-------------");
		//-----------------------------
		Individuo pai = new Individuo(geraTab(), 4, 2, Jogada.COMPUTADOR);
		Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
		Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
		Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
		Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
		pai.setPecaMudada(21);
		ArrayList<Integer> posicoes = new ArrayList<Integer>();
		posicoes.add(21);
		posicoes.add(30);
		pai.setComeu(false);
		char[] tabuleiroAntes = pai.getTabuleiro().clone(); 
		tabuleiroAntes[21] = Pecas.PEDRA_COMPUTADOR;
		tabuleiroAntes[12] = Pecas.PEDRA_COMPUTADOR;
		//tabuleiroAntes[3] = Pecas.PEDRA_JOGADOR;
		tabuleiroAntes[39] = Pecas.PEDRA_JOGADOR;
		//tabuleiroAntes[26] = Pecas.PEDRA_COMPUTADOR;
		tabuleiroAntes[42] = Pecas.PEDRA_JOGADOR;
		tabuleiroAntes[44] = Pecas.PEDRA_JOGADOR;
		tabuleiroAntes[46] = Pecas.PEDRA_JOGADOR;
		tabuleiroAntes[14] = Pecas.PEDRA_JOGADOR;
		pai.setPosicoesIntermediarias(posicoes);
		pai.setComeu(false);
		
		
//		
		char[] tabuleiroFilho1 = new char[tabuleiroAntes.length];
		System.arraycopy(tabuleiroAntes, 0, tabuleiroFilho1, 0, tabuleiroAntes.length);		
		tabuleiroFilho1[21] = Pecas.VAZIO;
		
		tabuleiroFilho1[30] = Pecas.PEDRA_COMPUTADOR;
		pai.setTabuleiro(tabuleiroFilho1);
		imprimeTabuleiro(tabuleiroAntes);
		System.out.println("---------------");
		imprimeTabuleiro(tabuleiroFilho1);
		System.out.println("--------------");
//		System.out.println("avaliaDiagonalOrigem "+ag.avaliaDiagonalOrigem(pai, Jogada.COMPUTADOR, tabuleiroAntes));
//		System.out.println("avaliaPecaOrigemSeraComida "+ag.avaliaPecaOrigemSeraComida(pai, Jogada.COMPUTADOR, tabuleiroAntes));
		System.out.println("avaliaDiagonalDestino "+ag.avaliaDiagonalDestino(pai, Jogada.COMPUTADOR, tabuleiroAntes));
		
//		Individuo filho1 = new Individuo(tabuleiroFilho1, 2, numPecasC, jogadorAtual)
////		tab.setPecaMudada(17);
//		char[] tabuleiroAlterado = tab.getTabuleiro(); 
//		tabuleiroAlterado[12] = Pecas.VAZIO;
//		tabuleiroAlterado[21] = Pecas.VAZIO;
//		tabuleiroAlterado[19] = Pecas.PEDRA_COMPUTADOR;
//		tabuleiroAlterado[23] = Pecas.VAZIO;
//		tabuleiroAlterado[30] = Pecas.DAMA_JOGADOR;
//		tab.setTabuleiro(tabuleiroAlterado);
//		tabuleiroAlterado = tab.getTabuleiro().clone();
//		tabuleiroAlterado[19] = Pecas.VAZIO;
//		tabuleiroAlterado[12] = Pecas.PEDRA_COMPUTADOR;
//		System.out.println("Primeiro tabuleiro");
//		imprimeTabuleiro(tabuleiroAlterado);
//		System.out.println("-----------------");
//		System.out.println("Segundo tabuleiro");
//		imprimeTabuleiro(tab);
//		System.out.println("-----------------");
		
//		System.out.println(avaliaDiagonalDestino(tab, Jogada.COMPUTADOR, tabuleiroAlterado));
	}
	
	
	
//	public static void testeAvaliaPecaOrigemSeraComida() {
//		Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
//		Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
//		Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
//		Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
//		//-----------------------------
//		Individuo tab = new Individuo(geraTab(), 12, 12, Jogada.COMPUTADOR);
//		tab.setPecaMudada(12);
//		ArrayList<Integer> posicoes = new ArrayList<Integer>();
//		posicoes.add(12);
//		posicoes.add(19);
//		tab.setPosicoesIntermediarias(posicoes);
//		tab.setComeu(false);
////		tab.setPecaMudada(17);
//		char[] tabuleiroAlterado = tab.getTabuleiro(); 
//		tabuleiroAlterado[12] = Pecas.VAZIO;
//		tabuleiroAlterado[21] = Pecas.VAZIO;
//		tabuleiroAlterado[19] = Pecas.PEDRA_COMPUTADOR;
//		tabuleiroAlterado[23] = Pecas.VAZIO;
//		tabuleiroAlterado[30] = Pecas.DAMA_JOGADOR;
//		tab.setTabuleiro(tabuleiroAlterado);
//		tabuleiroAlterado = tab.getTabuleiro().clone();
//		tabuleiroAlterado[19] = Pecas.VAZIO;
//		tabuleiroAlterado[12] = Pecas.PEDRA_COMPUTADOR;
//		System.out.println("Primeiro tabuleiro");
//		imprimeTabuleiro(tabuleiroAlterado);
//		System.out.println("-----------------");
//		System.out.println("Segundo tabuleiro");
//		imprimeTabuleiro(tab);
//		System.out.println("-----------------");
//		
//		
//		System.out.println(avaliaPecaOrigemSeraComida(tab, Jogada.COMPUTADOR, tabuleiroAlterado));
//	}
	
	
}
