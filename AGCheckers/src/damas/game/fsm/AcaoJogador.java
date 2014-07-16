package damas.game.fsm;

import java.awt.Component;
import java.util.ArrayList;

import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Movimentacao;
import damas.game.commom.PecaCome;
import damas.game.commom.Pecas;
import damas.game.jogador.Jogador;

public class AcaoJogador {
	private Jogador jogador;
	private boolean comeUmaPeca;
	private char ULTIMA_PECA_JOGADA = Pecas.PEDRA_JOGADOR;

	public AcaoJogador() {
		jogador = new Jogador();
		comeUmaPeca = false;		
	}

	public char[] limparCapturas(char[] tabuleiro) {
		return jogador.limparCapturas(tabuleiro);
	}

	public boolean isJogadaValida(int origem, int destino, char[] tab,
			Component parent) {
		if (!jogador.isJogadaValida(origem, destino, tab)) {
			if (origem != destino) {
				jogador.alertaJogadaInvalida(parent);
			}
			return false;
		}
		return true;
	}

	public void msgJogadaInvalida(Component parent) {
		jogador.alertaJogadaInvalida(parent);
	}

	public void doAcaoPromocao(int ultimoLance, char[] tabuleiro) {

		// verifica se a jogada foi uma captura em sequencia
		this.comeUmaPeca = jogador.haPecaAComer(ultimoLance, tabuleiro);

		// senao passa a vez ao computador.
		if (!this.comeUmaPeca) {
			if (jogador.isPromoveDama(ultimoLance, tabuleiro)) {
				tabuleiro = jogador.promoveDama(ultimoLance, tabuleiro);
			}
		}
	}

	public void doAcaoInicioCaptura(char[] tabuleiro) {
		this.comeUmaPeca = jogador.haPecaAComer(tabuleiro);
	}

	public char[] doAcaoMovimento(int de, int para, char peca, Individuo individuo) {
		ULTIMA_PECA_JOGADA = individuo.getTabuleiro()[de];
		return jogador.realizaJogada(de, para, peca, individuo);
	}

	public ArrayList<Integer> getPosicoesComidas(char[] tabuleiro) {
		ArrayList<Integer> p = new ArrayList<Integer>();
		for(int i = 0; i < tabuleiro.length; i++) {
			if(tabuleiro[i] == Pecas.PECA_CAPTURADA_COMPUTADOR) {
				p.add(i);
			}
		}
		return p;
	}
	
	public boolean completouAcao(Individuo individuo) {
		if(!individuo.getComeu())
			return true;
		int ultimaPosicao = individuo.getPosicoesIntermediarias().get(
				individuo.getPosicoesIntermediarias().size() - 1);
		
		ArrayList<Integer> posicoesComidas = getPosicoesComidas(individuo.getTabuleiro());
		
		ArrayList<Integer> posicoesPodeComer = 
				Movimentacao.getPosicoesPossiveisComer(individuo.getTabuleiro(), ultimaPosicao, 
						posicoesComidas, Jogada.JOGADOR);
		if(posicoesPodeComer.size() > 0)
				return false;
		return true;//!comeUmaPeca;
	}

	public boolean isPossivelMover(char[] tabuleiro) { 
		if (jogador.getPeca() != null) {
			return true;
		}
		int nPecas = jogador.countPecasJogador(tabuleiro);
		if(nPecas == 0)
			return false;
		
		char[] tab = new char[tabuleiro.length];

		System.arraycopy(tabuleiro, 0, tab, 0, tabuleiro.length);
		tab = jogador.limparCapturas(tab);

		for(int i = 0; i < tab.length; i++) {
			if(tab[i] == Pecas.PEDRA_JOGADOR || tab[i] == Pecas.DAMA_JOGADOR) {
				if(Movimentacao.podeMover(tab, i) || Movimentacao.podeComer(tab, i, Jogada.JOGADOR)) {
					return true;
				}
			}
		}
		
//		Individuo individuos[] = new Individuo[nPecas];
//		for (int i = 0, j = 0; j < nPecas && i < tab.length; i++) {
//			if (tab[i] == Pecas.PEDRA_JOGADOR || tab[i] == Pecas.DAMA_JOGADOR) {
//				individuos[j] = new Individuo(new char[tab.length]);
//				individuos[j].setTabuleiro(tab);
//				individuos[j].setOrigemPeca(i);
//				individuos[j].setTipoPeca(tab[i]);
//				j++;
//			}
//		}
//		// identifica as diagonais
//		for (int i = 0; i < individuos.length; i++) {
//			this.tracaDiagonais(individuos[i], individuos[i].getOrigemPeca(),
//					tab);
//			individuos[i].setPodeMover(new boolean[individuos[i]
//					.getDestinosPossiveis().length]);
//		}
//		// Para cada peca, verifica para quais casas escuras eh permitido que
//		// ela se mova.
//		for (int i = 0; i < individuos.length; i++) {
//			int de = individuos[i].getOrigemPeca();
//			for (int j = 0; j < individuos[i].getDestinosPossiveis().length
//					&& individuos[i].getDestinosPossiveis()[j] != 0; j++) {
//				int para = individuos[i].getDestinosPossiveis()[j];
//				individuos[i].setPodeMover(j, jogador.isJogadaValida(de, para,
//						tab));
//			}
//		}

		

//		for (int i = 0; i < individuos.length; i++) {
//			// verifica se eh possivel mover esta peca.
//			boolean isPossivelMover = individuos[i].isPossivelMover();
//
//			// passa a ser verdadeiro se achar ao menos uma peca que o seja.
//			haPecaMover = (isPossivelMover || haPecaMover);
//		}
		return false;
	}

	private void tracaDiagonais(Individuo individuo, int de, char[] tabuleiro) {
		int[] quadrados = new int[15];
		int lances = 0;

		for (int j = 7; de - j > 0 && tabuleiro[de - j] != Pecas.PAREDE; lances++, j += 7) {
			quadrados[lances] = de - j;
		}
		for (int j = 7; de + j < tabuleiro.length
				&& tabuleiro[de + j] != Pecas.PAREDE; lances++, j += 7) {
			quadrados[lances] = de + j;
		}
		for (int j = 9; de - j > 0 && tabuleiro[de - j] != Pecas.PAREDE; lances++, j += 9) {
			quadrados[lances] = de - j;
		}
		for (int j = 9; de + j < tabuleiro.length
				&& tabuleiro[de + j] != Pecas.PAREDE; lances++, j += 9) {
			quadrados[lances] = de + j;

		}

	}

	public String getTotalPecasJogador(char[] tabuleiro) {
		int n = jogador.countPecasJogador(tabuleiro);
		return n < 10 ? "0" + String.valueOf(n) : String.valueOf(n);
	}

	public int getNumeroDamasJogador(char[] tabuleiro) {
		int n = 0;
		for (int i = 0; i < tabuleiro.length; i++) {
			if (tabuleiro[i] == Pecas.DAMA_JOGADOR) {
				n++;

			}
		}
		return n;
	}

	public char getULTIMA_PECA_JOGADA() {
		return ULTIMA_PECA_JOGADA;
	}

	public boolean isHouveCaptura() {
		return jogador.isHouveCaptura();
	}
}
