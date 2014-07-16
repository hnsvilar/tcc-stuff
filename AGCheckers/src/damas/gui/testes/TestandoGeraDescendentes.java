package damas.gui.testes;

import java.util.ArrayList;

import damas.game.ag.AlgoritmoGenetico;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;
import damas.game.fsm.AcaoComputador;

public class TestandoGeraDescendentes {
	public static void main(String[] args) {
		testarGeraDescendentes();
	}
	
	public static void testarGeraDescendentes() { 
		Pecas.PEDRA_JOGADOR = Pecas.VAZIO;
		Pecas.DAMA_JOGADOR = Pecas.VAZIO;
		Pecas.PEDRA_COMPUTADOR = Pecas.VAZIO;
		Pecas.DAMA_COMPUTADOR = Pecas.VAZIO;	
		char[] tab = TestandoAvaliacao.geraTab();
		Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
		Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
		Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
		Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
		tab[12] = Pecas.DAMA_COMPUTADOR;
		tab[19] = Pecas.PEDRA_JOGADOR;
		tab[33] = Pecas.PEDRA_JOGADOR;
//		tab[21] = Pecas.PEDRA_JOGADOR;
//		tab[30] = Pecas.PEDRA_JOGADOR;
		
		AlgoritmoGenetico ag = new AlgoritmoGenetico();
		Individuo estado = new Individuo(tab, 12, 12, Jogada.JOGADOR);
		No pai = new No(null, estado);
		TestandoAvaliacao.imprimeTabuleiro(estado.getTabuleiro());
		System.out.println("--------------");
		System.out.println("--------------");
		ArrayList<Individuo> filhos = estado.getSucessores(Jogada.COMPUTADOR);
		ag.setPopulacao(filhos);
		ag.mutaPopulacao(Jogada.COMPUTADOR);
		for(int i = 0; i < filhos.size(); i++) {
			System.out.println("--------------");
			TestandoAvaliacao.imprimeTabuleiro(filhos.get(i).getTabuleiro());
			
		}
//		
//		No pai = new No(null, estado);
//		ag.geraNovaPopulacao(pai, Jogada.COMPUTADOR);
//		ArrayList<No> fronteira = pai.getFilhos();
//		System.out.println(fronteira.toString());
//		System.out.println("\n-----------\n");System.out.println("\n-----------\n");System.out.println("\n-----------\n");
//		for(No no : fronteira) {
//			ag.geraNovaPopulacao(no, Jogada.JOGADOR);
//			ArrayList<No> fronteira2 = no.getFilhos();
//			for(No no2 : fronteira2) {
//				System.out.println(no2.toString());
//			}
//			System.out.println("\n-----------\n");
//		}
		
	}
}
