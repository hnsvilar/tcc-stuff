package damas.gui.testes;

import java.util.ArrayList;

import damas.game.ag.AlgoritmoGenetico;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Movimentacao;
import damas.game.commom.Pecas;
import damas.game.fsm.AcaoComputador;
import damas.game.fsm.AcaoJogador;
import damas.game.fsm.FiniteStateMachine;

public class TestandoValidarJogada {
	public static void main(String[] args) {
		TestandoValidarJogada t = new TestandoValidarJogada();
		
		t.teste2();
//		t.teste();
	}
	
	private void teste2() {
		AcaoJogador a = new AcaoJogador();
		int origem=5;
		int destino=23;
		Pecas.PEDRA_JOGADOR = Pecas.VAZIO;
		Pecas.DAMA_JOGADOR = Pecas.VAZIO;
		Pecas.PEDRA_COMPUTADOR = Pecas.VAZIO;
		Pecas.DAMA_COMPUTADOR = Pecas.VAZIO;	
		char[] tab = TestandoAvaliacao.geraTab();
		Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
		Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
		Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
		Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
		Jogada j = new Jogada();
		j.setJogador(Jogada.JOGADOR);
		tab[3] = Pecas.DAMA_JOGADOR;
		tab[12] = Pecas.PECA_CAPTURADA_COMPUTADOR;
		tab[30] = Pecas.PEDRA_COMPUTADOR;
//		tab[5] = Pecas.DAMA_JOGADOR;
//		tab[12] = Pecas.PECA_CAPTURADA_COMPUTADOR;
//		tab[26] = Pecas.PEDRA_COMPUTADOR;
		System.out.println(Movimentacao.getPosicoesPossiveisComer(tab, 5, Jogada.JOGADOR).toString());
	}
	
	
	private void teste() {
		AcaoJogador a = new AcaoJogador();
		int origem=5;
		int destino=23;
		Pecas.PEDRA_JOGADOR = Pecas.VAZIO;
		Pecas.DAMA_JOGADOR = Pecas.VAZIO;
		Pecas.PEDRA_COMPUTADOR = Pecas.VAZIO;
		Pecas.DAMA_COMPUTADOR = Pecas.VAZIO;	
		char[] tab = TestandoAvaliacao.geraTab();
		Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
		Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
		Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
		Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
		Jogada j = new Jogada();
		j.setJogador(Jogada.JOGADOR);
//		tab[1] = tab[8] = tab[39] = Pecas.PEDRA_COMPUTADOR;
//		tab[10] = tab[28] = tab[46] = tab[53] = tab[51] = tab[56] = 
//				tab[58] = tab[60] = Pecas.PEDRA_JOGADOR;
		tab[8] = tab[10] = tab[19] = tab[21] = tab[28] = tab[30] =
				Pecas.PEDRA_COMPUTADOR;
		tab[17] = Pecas.PEDRA_JOGADOR;
//		# - # - # - # - 
//		B # B # - # - # 
//		# P # B # B # - 
//		- # - # B # B # 
//		# - # B # B # P 
//		- # - # - # P # 
//		# - # - # B # P 
//		- # - # - # - # 
		TestandoAvaliacao.imprimeTabuleiro(tab);
		
		//System.out.println(a.isJogadaValida(origem, destino, tab, null));
		
		System.out.println("-----------");
		//System.out.println(Movimentacao.podeComer(tab, origem, Jogada.JOGADOR));
		FiniteStateMachine f = new FiniteStateMachine();
		Jogada.setJogador(Jogada.COMPUTADOR);
		f.init(null);
		f.estadoInicial(1, null);
		
		Individuo estado = new Individuo(tab, 4, 9, Jogada.JOGADOR);
		estado.setPecaMudada(17);
		ArrayList<Integer> posicoesIntermediarias = new ArrayList<Integer>();
		posicoesIntermediarias.add(24);
		posicoesIntermediarias.add(17);
		estado.setPosicoesIntermediarias(posicoesIntermediarias);
		estado.setComeu(false);
		estado.setAvaliacao(0);
		//Individuo estado = new Individuo(tab, 4, 9, Jogada.COMPUTADOR);
//		No pai = new No(null, estadoPai);
		
		No no = new No(null, estado);
		f.setNoAtual(no);
		System.out.println(f.fimDeJogo(true, FiniteStateMachine.VEZ_COMPUTADOR));
		
		AcaoComputador ac = new AcaoComputador(null, null,1);
		System.out.println(ac.isPossivelMover(tab));
		
		System.out.println("----------");
		
		No proxima = ac.getProximaJogada(no);
		TestandoAvaliacao.imprimeTabuleiro(proxima.getEstado().getTabuleiro());
		System.out.println(proxima.getEstado().getAvaliacao()+"\n");
		System.out.println("----------");System.out.println("----------");
		AlgoritmoGenetico ag = new AlgoritmoGenetico();
		ag.geraNovaPopulacao(no, Jogada.COMPUTADOR);
		for(No n : no.getFilhos()) { 
			TestandoAvaliacao.imprimeTabuleiro(n.getEstado().getTabuleiro());
			System.out.println("----------");
		}
		System.out.println(ag.terminaJogo(no.getFilhos()));
	}
}
