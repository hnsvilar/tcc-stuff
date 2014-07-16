package damas.game.commom;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Gabb
 *
 */
public class Individuo{
	private char[] tabuleiro;
	private int numPecasJogador;
	private int numPecasComputador;
	private int jogadorAtual;
	private double avaliacao;
	private ArrayList<Integer> posicoesIntermediarias = null;
	private int pecaMudada;
	private boolean comeu;
	private ArrayList<Integer> pecasComidas;
	
	public Individuo(char[] tabuleiro, int numPecasJ, int numPecasC, int jogadorAtual) {
		this.comeu = false;
		this.avaliacao = -1000;
		this.tabuleiro = tabuleiro;
		numPecasJogador = numPecasJ;
		numPecasComputador = numPecasC;
		this.jogadorAtual = jogadorAtual;
		pecasComidas = new ArrayList<Integer>();
	}
	
	public void setPosicoesIntermediarias(ArrayList<Integer> posicoesIntermediarias) { this.posicoesIntermediarias = posicoesIntermediarias; }
	public ArrayList<Integer> getPosicoesIntermediarias() {	return posicoesIntermediarias; }
	public char[] getTabuleiro() { return tabuleiro; }
	public int getNumPecasComputador() { return numPecasComputador; }
	public int getNumPecasJogador() { return numPecasJogador; }
	public void setTabuleiro(char[] tabuleiro) { this.tabuleiro = tabuleiro; }
	public void setPecaMudada(int pecaMudada) {	this.pecaMudada = pecaMudada; }
	public int getPecaMudada() { return pecaMudada; }
	public void setComeu(boolean comeu) { this.comeu = comeu; }
	public boolean getComeu() { return comeu;}
	public void setAvaliacao(double avaliacao) { this.avaliacao = avaliacao; }
	public double getAvaliacao() { return avaliacao; }
	public int getJogadorAtual() {
		return jogadorAtual;
	}
	public ArrayList<Integer> getPecasComidas() {
		return pecasComidas;
	}
	public void setNumPecasComputador(int numPecasComputador) {
		this.numPecasComputador = numPecasComputador;
	}
	public void setNumPecasJogador(int numPecasJogador) {
		this.numPecasJogador = numPecasJogador;
	}
	
	public double getCusto() {
		return 1;
	}

	public ArrayList<Individuo> getSucessores(int vez) {
		/*
		 * Verifica as pecas que podem mover
		 * para cada uma gera um tabuleiro
		 * para cada tabuleiro, chama ag para mover.
		 */
		ArrayList<Individuo> filhos = new ArrayList<Individuo>();
		HashMap<Integer,Character> pecas = Movimentacao.getPecas(vez);
		for(int i = 0; i < tabuleiro.length; i++) {
			if(tabuleiro[i] == pecas.get(Movimentacao.pedraComputador) || 
					tabuleiro[i] == pecas.get(Movimentacao.damaComputador)) { 
				if(Movimentacao.podeMover(tabuleiro, i) || Movimentacao.podeComer(tabuleiro, i, vez)) {
					char[] tabAux = new char[tabuleiro.length];
					System.arraycopy(tabuleiro, 0, tabAux, 0, tabuleiro.length);
					Individuo indi = new Individuo(tabAux, numPecasJogador, numPecasComputador, Jogada.JOGADOR);
					indi.setPecaMudada(i);
					filhos.add(indi);
				}
			}
		}
		return filhos;
	}
	
	public void moverPeca(int de, int para) {
		char peca = tabuleiro[de];
		tabuleiro[de] = Pecas.VAZIO;
		tabuleiro[para] = peca;
	}

	public void setPecasComidas(ArrayList<Integer> pecasComidas) {
		this.pecasComidas = pecasComidas;
	}

	public String toString() {
		return "Comeu: "+comeu+" JogadorAtual: "+jogadorAtual+" Avaliacao "+avaliacao+" NPC "+ numPecasComputador+
				" NPJ "+numPecasJogador+" PecaMudada: "+pecaMudada+" Caminho: "+posicoesIntermediarias.toString();
	}
}
//	/**
//	 * Atributos
//	 */	
//	private int origemPeca;
//	private int[] destinosPossiveis;
//	private char tipoPeca;
//	private char[] tabuleiro;
//	private boolean[] podeMover; // identifica para cada um dos possiveis
//	// destinos se eh possivel mover.
//	
//	//------------------------------------------------------------------
//	
//	/**
//	 * Construtor
//	 * @param tabuleiro
//	 */
//	public Individuo(char[] tabuleiro){
//		this.tabuleiro = tabuleiro;
//	}
//	
//	//------------------------------------------------------------------
//	
//	/**
//	 * Métodos
//	 */
//	
//	public int getOrigemPeca() {
//		return origemPeca;
//	}
//
//	public void setOrigemPeca(int posicaoPeca) {
//		this.origemPeca = posicaoPeca;
//	}
//
//	public char getTipoPeca() {
//		return tipoPeca;
//	}
//
//	public void setTipoPeca(char tipoPeca) {
//		this.tipoPeca = tipoPeca;
//	}
//
//	public char[] getTabuleiro() {
//		return tabuleiro;
//	}
//
//	public void setTabuleiro(char[] tabuleiro) {
//		System.arraycopy(tabuleiro, 0, Individuo.this.tabuleiro, 0,
//				tabuleiro.length);
//	}
//	
//	public int[] getDestinosPossiveis() {
//		return destinosPossiveis;
//	}
//
//	public void setDestinosPossiveis(int[] destinosPossiveis) {
//		this.destinosPossiveis = destinosPossiveis;
//	}
//
//	public void setDestinosPossiveis(int[] destinosPossiveis, int firstIndex,
//			int lastIndex) {
//		this.destinosPossiveis = new int[lastIndex];
//		System.arraycopy(destinosPossiveis, firstIndex, this.destinosPossiveis,
//				0, lastIndex);
//	}
//
//	public void setDestinosPossiveis(int i, int destino) {
//		this.destinosPossiveis[i] = destino;
//	}
//	
//	public boolean[] isPodeMover() {
//		return this.podeMover;
//	}
//
//	public boolean isPodeMover(int i) {
//		return this.podeMover[i];
//	}
//
//	public void setPodeMover(boolean[] podeMover) {
//		this.podeMover = podeMover;
//	}
//
//	public void setPodeMover(int i, boolean podeMover) {
//		this.podeMover[i] = podeMover;
//	}
//
//	public boolean isPossivelMover() {
//
//		boolean isPossivel = false;
//
//		for (int i = 0; i < this.podeMover.length; i++)
//			isPossivel = (isPossivel || this.podeMover[i]);
//
//		return isPossivel;
//	}
//	

