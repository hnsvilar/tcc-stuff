package damas.game.jogador;

import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;
import damas.game.fsm.FiniteStateMachine;

public class Jogador extends Jogada {

	/**
	 * Construtor.
	 *
	 */
	private boolean houveCaptura = false;
	public Jogador() {}

	/**
	 * Realiza a jogado do jogador.
	 * 
	 * @param de
	 * @param para
	 * @param peca
	 * @param tabuleiro
	 * 
	 * @return
	 */
	public char[] realizaJogada(int de, int para, char peca, Individuo individuo) {
		char[] tabuleiro = individuo.getTabuleiro();
		if (!COMEU_PECA && ULTIMA_PECA_COMIDA == 0) {
			tabuleiro[de] = Pecas.VAZIO;
			tabuleiro[para] = peca;
			individuo.setComeu(false);
			houveCaptura = false;
		} else if (COMEU_PECA && ULTIMA_PECA_COMIDA != 0) {
			tabuleiro[de] = Pecas.VAZIO;
			tabuleiro[para] = peca;
			tabuleiro[ULTIMA_PECA_COMIDA] = Pecas.PECA_CAPTURADA_COMPUTADOR;
			individuo.getPecasComidas().add(ULTIMA_PECA_COMIDA);
			Jogada.pushARR_PECAS_COMIDAS(ULTIMA_PECA_COMIDA);
			N_PECAS_COMIDAS++;
			individuo.setComeu(true);
			individuo.setNumPecasComputador(individuo.getNumPecasComputador()-1);
			houveCaptura = true;
		}

		return tabuleiro;
	}
	/**
	 * Conta a quantidade de pecas do jogador
	 * */
	public int countPecasJogador(char[] tabuleiro) {

		int nPecas = 0;

		for (int i = 0; nPecas < 12 && i < tabuleiro.length; i++)
			if (tabuleiro[i] == Pecas.PEDRA_JOGADOR
					|| tabuleiro[i] == Pecas.DAMA_JOGADOR)
				nPecas++;

		return nPecas;
	}	
	
	public boolean isPromoveDama(int de, char [] tabuleiro){
		
		if (tabuleiro[de] == Pecas.PEDRA_JOGADOR){
			
			for (int i = 0; i < FiniteStateMachine.LATERAL_SUPERIOR.length; i++)
				if (de == FiniteStateMachine.LATERAL_SUPERIOR[i])
					return true;
			
		}
		
		return false;
	}
	
	public char[] promoveDama(int de, char[] tabuleiro){
		
		tabuleiro[de] = Pecas.DAMA_JOGADOR;
		return tabuleiro;
	}

	public boolean isHouveCaptura() {
		return houveCaptura;
	}
}
