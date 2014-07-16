package damas.game.animacao;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JLabel;

import damas.game.commom.PecaCome;
import damas.game.commom.Pecas;

public class AnimacaoTabuleiro extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2539173938527132891L;
	private Image vazio;
	private Image parede;
	private Image pecaJogador;
	private Image pecaJogadorCome;
	private Image damaJogador;
	private Image damaJogadorCome;
	private Image pecaJogadorRollover;
	private Image damaJogadorRollover;
	private Image pecaComputador;
	private Image damaComputador;

	public AnimacaoTabuleiro(Rectangle rec) {
		this.setBackground(Color.BLACK);
		this.setLayout(new GridLayout(8, 8));
		int posicao = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				posicao++;
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						this.add(corCasa(Color.YELLOW, posicao));
					} else {
						this.add(corCasa(Color.RED, posicao));
					}
				} else {
					if (j % 2 == 0) {
						this.add(corCasa(Color.RED, posicao));
					} else {
						this.add(corCasa(Color.YELLOW, posicao));
					}
				}

			}

		}

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		vazio = toolkit.getImage(this.getClass().getResource(
				"/img/gifs/VAZIO.gif"));

		parede = toolkit.getImage(this.getClass().getResource(
				"/img/gifs/PAREDE.gif"));

		if (Pecas.PEDRA_JOGADOR == Pecas.getBRANCA()) {
			pecaJogador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/PEDRA_BRANCA.gif"));
			damaJogador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/DAMA_BRANCA.gif"));
			pecaJogadorRollover = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/PEDRA_BRANCA_MOVER.gif"));

			damaJogadorRollover = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/DAMA_BRANCA_MOVER.gif"));
			pecaJogadorCome = toolkit.getImage(this.getClass().getResource(
					"/img/animados/PEDRA_BRANCA_COMER.gif"));

			damaJogadorCome = toolkit.getImage(this.getClass().getResource(
					"/img/animados/DAMA_BRANCA_COMER.gif"));

			pecaComputador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/PEDRA_PRETA.gif"));

			damaComputador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/DAMA_PRETA.gif"));

		} else {
			pecaJogador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/PEDRA_PRETA.gif"));
			damaJogador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/DAMA_PRETA.gif"));
			pecaJogadorRollover = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/PEDRA_PRETA_MOVER.gif"));

			damaJogadorRollover = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/DAMA_PRETA_MOVER.gif"));

			pecaJogadorCome = toolkit.getImage(this.getClass().getResource(
					"/img/animados/PEDRA_PRETA_COMER.gif"));

			damaJogadorCome = toolkit.getImage(this.getClass().getResource(
					"/img/animados/DAMA_PRETA_COMER.gif"));

			pecaComputador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/PEDRA_BRANCA.gif"));

			damaComputador = toolkit.getImage(this.getClass().getResource(
					"/img/gifs/DAMA_BRANCA.gif"));
		}
		this.setBounds(rec);
		this.setVisible(true);
	}

	private AnimacaoPeca corCasa(Color cor, int i) {
		AnimacaoPeca b = new AnimacaoPeca();
		b.setBackground(cor);
		b.setBorderPainted(false);
		b.setFocusPainted(false);

		if (cor != Color.RED)
			b.setEnabled(false);
		b.setName(String.valueOf(i - 1));

		return b;

	}

	public void populaPecas(char[] tabuleiro, AnimacaoTransfer transfer,
			boolean isVezJogador) {

		AnimacaoPeca b;

		for (int i = 0; i < tabuleiro.length; i++) {
			b = (AnimacaoPeca) this.getComponent(i);
			b.setTransferHandler(null);
			// b.setImage(null);
			b.setRolloverImage(null);
			b.setRolloverEnabled(false);

			if (tabuleiro[i] == Pecas.PEDRA_COMPUTADOR) {
				b.setImage(pecaComputador);
				b.setRolloverEnabled(false);
				b.setEnabled(false);
			} else if (tabuleiro[i] == Pecas.PEDRA_JOGADOR) {
				b.setImage(pecaJogador);
				b.setRolloverEnabled(true);
				b.setRolloverImage(pecaJogadorRollover);
				b.setEnabled(isVezJogador);
				if (isVezJogador)
					b.setTransferHandler(transfer);
			} else if (tabuleiro[i] == Pecas.DAMA_COMPUTADOR) {
				b.setImage(damaComputador);
				b.setEnabled(false);
			} else if (tabuleiro[i] == Pecas.DAMA_JOGADOR) {
				b.setImage(damaJogador);
				b.setRolloverEnabled(true);
				b.setRolloverImage(damaJogadorRollover);
				b.setEnabled(isVezJogador);
				if (isVezJogador)
					b.setTransferHandler(transfer);

			} else if (tabuleiro[i] == Pecas.VAZIO) {
				b.setEnabled(true);
				b.setTransferHandler(transfer);
				b.setImage(vazio);

			} else if (tabuleiro[i] == Pecas.PECA_CAPTURADA_COMPUTADOR
					|| tabuleiro[i] == Pecas.PECA_CAPTURADA_JOGADOR)
				b.setGrayScaleImage(b.getIcon());
			else
				b.setImage(parede);

		}
	}
}