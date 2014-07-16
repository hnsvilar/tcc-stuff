package damas.gui.testes;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import damas.game.ag.AlgoritmoGenetico;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;
import damas.game.fsm.AcaoComputador;

public class TestandoMutaPopulacaoCasoDama {
	public static void main(String[] args) {
		Pecas.PEDRA_JOGADOR = Pecas.VAZIO;
		Pecas.DAMA_JOGADOR = Pecas.VAZIO;
		Pecas.PEDRA_COMPUTADOR = Pecas.VAZIO;
		Pecas.DAMA_COMPUTADOR = Pecas.VAZIO;	
		char[] tab = TestandoAvaliacao.geraTab();
		Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
		Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
		Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
		Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
		AlgoritmoGenetico ag = new AlgoritmoGenetico();
		tab[21] = Pecas.PEDRA_JOGADOR;
		tab[39] = Pecas.PEDRA_JOGADOR;
		tab[42] = Pecas.PEDRA_JOGADOR;
		tab[44] = Pecas.PEDRA_JOGADOR;
		tab[46] = Pecas.PEDRA_JOGADOR;
		tab[3] = Pecas.PEDRA_COMPUTADOR;
		tab[7] = Pecas.PEDRA_COMPUTADOR;
		//tab[12] = Pecas.PEDRA_COMPUTADOR;
		tab[17] = Pecas.PEDRA_COMPUTADOR;
		Individuo pai = new Individuo(tab, 4, 4, Jogada.JOGADOR);
		TestandoAvaliacao.imprimeTabuleiro(pai.getTabuleiro());
		System.out.println("------");
		No atual = new No(null, pai);
		AcaoComputador ac = new AcaoComputador(null, null, 2);
		No filho = ac.getProximaJogada(atual);
		TestandoAvaliacao.imprimeTabuleiro(filho.getEstado().getTabuleiro());
//		pai.setPecaMudada(12);
//		ArrayList<Individuo> a = new ArrayList<Individuo>();
//		a.add(pai);
//		ag.setPopulacao(a);
//		ag.mutaPopulacao(Jogada.COMPUTADOR);
//		TestandoAvaliacao.imprimeTabuleiro(pai.getTabuleiro());
//		
//		for(int i =0; i < 2; i++) {
//			System.out.println(i);
//		}
	}
}
