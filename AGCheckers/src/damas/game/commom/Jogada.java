package damas.game.commom;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import damas.game.fsm.FiniteStateMachine;

/**
 *  
 *
 */
public class Jogada {

	/**
	 * Atributos
	 */
	private PecaCome[] peca;

	public static final int JOGADOR = 0;
	public static final int COMPUTADOR = 1;
	public static int JOGADOR_ATUAL;
	protected static boolean COMEU_PECA = false;
	protected static int ULTIMA_PECA_COMIDA = 0;
	protected static ARR_PECAS_COMIDAS arr_pecas_comidas;
	protected static int N_PECAS_COMIDAS = 0;

	private static char PEDRA_JOGADOR;
	private static char DAMA_JOGADOR;
	private static char PEDRA_ADVERSARIO;
	private static char DAMA_ADVERSARIO;
	private static char PECA_CAPTURADA;

	//----------------------------------------------------------------
	
	/**
	 * Define qual sera o jogador
	 * @param jogadorAtual
	 */
	public static void setJogador(int jogadorAtual) {
		if (jogadorAtual == Jogada.COMPUTADOR) {
			Jogada.PEDRA_JOGADOR = Pecas.PEDRA_COMPUTADOR;
			Jogada.DAMA_JOGADOR = Pecas.DAMA_COMPUTADOR;
			Jogada.PEDRA_ADVERSARIO = Pecas.PEDRA_JOGADOR;
			Jogada.DAMA_ADVERSARIO = Pecas.DAMA_JOGADOR;
			Jogada.PECA_CAPTURADA = Pecas.PECA_CAPTURADA_JOGADOR;

			JOGADOR_ATUAL = Jogada.COMPUTADOR;
		} else {
			Jogada.PEDRA_JOGADOR = Pecas.PEDRA_JOGADOR;
			Jogada.DAMA_JOGADOR = Pecas.DAMA_JOGADOR;
			Jogada.PEDRA_ADVERSARIO = Pecas.PEDRA_COMPUTADOR;
			Jogada.DAMA_ADVERSARIO = Pecas.DAMA_COMPUTADOR;
			Jogada.PECA_CAPTURADA = Pecas.PECA_CAPTURADA_COMPUTADOR;

			JOGADOR_ATUAL = Jogada.JOGADOR;
		}
	}
	
	/**
	 * Verifica a validade de uma jogada
	 * 
	 * @param de
	 * @param para
	 * @param tabuleiro
	 * 
	 * @return
	 */
	public boolean isJogadaValida(int de, int para, char[] tabuleiro) {

		COMEU_PECA = false;
		ULTIMA_PECA_COMIDA = 0;
		return this.isNovaJogadaValida(de, para, tabuleiro);
	}

	public boolean damaComeu(int de, int para, char[] tabuleiro) {
		int maior = Math.max(de, para);
		int menor = Math.min(de, para);
		int inc = (maior - menor)% 7 == 0 ? 7 : 9;
		if(de > para) { // esta abaixo
			int posicaoComivel = -1;
			for(int i = de - inc; i > para; i = i - inc) {
				if(tabuleiro[i] == Jogada.PEDRA_ADVERSARIO || tabuleiro[i] == Jogada.DAMA_ADVERSARIO) {
					posicaoComivel = i;
					i = para;
				}
			}
			if(posicaoComivel != -1) {
				boolean livre = true;
				for(int i = posicaoComivel - inc; i > para; i = i - inc) {
					if(tabuleiro[i] != Pecas.VAZIO) {
						livre = false;
						return false;
					}
				}
				if(livre) {
					return true;
				}
			} else {
				return false;
			}
		} else {
			int posicaoComivel = -1;
			for(int i = de + inc; i < para; i = i + inc) {
				if(tabuleiro[i] == Jogada.PEDRA_ADVERSARIO || tabuleiro[i] == Jogada.DAMA_ADVERSARIO) {
					posicaoComivel = i;
					i = para;
				}
			}
			if(posicaoComivel != -1) {
				boolean livre = true;
				for(int i = posicaoComivel + inc; i < para; i = i + inc) {
					if(tabuleiro[i] != Pecas.VAZIO) {
						livre = false;
						return false;
					}
				}
				if(livre) {
					return true;
				}
			} else {
				return false;
			}
		}		
		return false;
	}
	
	public boolean isNovaJogadaValida(int de, int para, char[] tabuleiro) {
		if (de == para)
			return false;
		// verifica se a peca eh do jogador.
		if (tabuleiro[de] != Jogada.PEDRA_JOGADOR
				&& tabuleiro[de] != Jogada.DAMA_JOGADOR)
			return false;

		// verifica se a casa de destino é um espaço vazio.
		if (tabuleiro[para] != Pecas.VAZIO)
			return false;
		
		//verifica se podia ter comido uma peca
		//se podia comer, retorna falso;
		boolean podiaComer = false;
		int i = 0;
		
		ArrayList<Integer> pecasComidas = new ArrayList<Integer>();
		while(!podiaComer && i < tabuleiro.length) {
			if(tabuleiro[i] == Jogada.PEDRA_JOGADOR || tabuleiro[i] == Jogada.DAMA_JOGADOR) {
				podiaComer = Movimentacao.podeComer(tabuleiro, i, Jogada.JOGADOR);
			} else if(tabuleiro[i] == PECA_CAPTURADA) {
				pecasComidas.add(i);
			}
			i++;
		}
		if(podiaComer == true) {			
			if(tabuleiro[de] == Jogada.PEDRA_JOGADOR) {
				int inc = (Math.max(de, para)) - (Math.min(de, para));
				if(inc == 7 || inc == 9) {
					return false;
				}
			} else if (tabuleiro[de] == Jogada.DAMA_JOGADOR) {
				if(!damaComeu(de, para, tabuleiro)) {
					return false;
				}
			}
		}
		
		
		if (tabuleiro[de] == Jogada.PEDRA_JOGADOR) {			
			return this.isJogadaValidaPeca(de, para, tabuleiro, this
					.isLateral(de));
		} else if (tabuleiro[de] == Jogada.DAMA_JOGADOR)
			return this.isJogadaValidaDama(de, para, tabuleiro);

		return false;
	}

	/**
	 * Valida o movimento de uma PECA COMUM DO JOGADOR(não dama).
	 * 
	 * @param de
	 * @param para
	 * @param tabuleiro
	 * @param isLateral
	 * 
	 * @return
	 */
	public boolean isJogadaValidaPeca(int de, int para, char[] tabuleiro,
			Boolean isLateral) {		
		int novaPosicao = Math.max(de, para) - Math.min(de, para);
		// nao é lateral
		if (Jogada.PEDRA_JOGADOR == Pecas.PEDRA_COMPUTADOR) {
			if (isLateral == null) {
				if (de < para)
					return this.isAvancoValidoPeca(de, para, tabuleiro);
				else
					return this.isRetornoValidoPeca(de, para, tabuleiro);

			} else if (isLateral.booleanValue()) {
				// eh a lateral esquerda.
				if (de < para) {
					if (novaPosicao != 9 && novaPosicao != 18)
						return false;
					else
						return this.isAvancoValidoPeca(de, para, tabuleiro);
				} else {
					if (novaPosicao != 14)
						return false;
					else
						return this.isRetornoValidoPeca(de, para, tabuleiro);
				}

			} else {
				// eh a lateral da direita.
				if (de < para) {
					if (novaPosicao != 7 && novaPosicao != 14)
						return false;
					else
						return this.isAvancoValidoPeca(de, para, tabuleiro);
				} else {
					if (novaPosicao != 18)
						return false;
					else
						return this.isRetornoValidoPeca(de, para, tabuleiro);
				}
			}
		} else {
			if (isLateral == null) {
				if (de > para)
					return this.isAvancoValidoPeca(de, para, tabuleiro);
				else
					return this.isRetornoValidoPeca(de, para, tabuleiro);

			} else if (isLateral.booleanValue()) {
				// eh a lateral da esquerda.
				if (de > para) {
					if (novaPosicao != 7 && novaPosicao != 14)
						return false;
					else
						return this.isAvancoValidoPeca(de, para, tabuleiro);
				} else {
					if (novaPosicao != 18)
						return false;
					else
						return this.isRetornoValidoPeca(de, para, tabuleiro);
				}
			} else {
				// eh a lateral direita.
				if (de > para) {
					if (novaPosicao != 9 && novaPosicao != 18)
						return false;
					else
						return this.isAvancoValidoPeca(de, para, tabuleiro);
				} else {
					if (novaPosicao != 14)
						return false;
					else
						return this.isRetornoValidoPeca(de, para, tabuleiro);
				}
			}
		}
	}

	/**
	 * Valida o avanco de uma PECA COMUM DO JOGADOR(não dama).
	 * 
	 * @param de
	 * @param para
	 * @param tabuleiro
	 * 
	 * @return
	 */
	public boolean isAvancoValidoPeca(int de, int para, char[] tabuleiro) {
		int novaPosicao = Math.max(de, para) - Math.min(de, para);
		// esta "subindo" no tabuleiro <baixo -> topo>
		if (novaPosicao == 7 || novaPosicao == 9)
			return true;
		else if (novaPosicao == 14 || novaPosicao == 18) {
			boolean jaComeu = false;
			// se avançou duas fileiras verifica se a anterior era uma
			// peca adversario.
			if (Jogada.PEDRA_JOGADOR == Pecas.PEDRA_COMPUTADOR) {
				char casaPulada = tabuleiro[para - (novaPosicao / 2)];

				if (casaPulada != Jogada.PEDRA_ADVERSARIO// Pecas.PECA_COMPUTADOR
						&& casaPulada != Jogada.DAMA_ADVERSARIO)// Pecas.DAMA_COMPUTADOR)
					return false;

				COMEU_PECA = true;
				ULTIMA_PECA_COMIDA = para - (novaPosicao / 2);

			} else {
				char casaPulada = tabuleiro[para + (novaPosicao / 2)];

				if (casaPulada != Jogada.PEDRA_ADVERSARIO// Pecas.PECA_COMPUTADOR
						&& casaPulada != Jogada.DAMA_ADVERSARIO)// Pecas.DAMA_COMPUTADOR)
					return false;

				COMEU_PECA = true;
				ULTIMA_PECA_COMIDA = para + (novaPosicao / 2);
			}

			// valida apenas se no caminho ateh a peca nao
			// consta uma casa onde houve uma peca comida.
			for (int i = 0; i < N_PECAS_COMIDAS; i++)
				jaComeu = arr_pecas_comidas.getARR_PECAS_COMIDAS(i) == para
						&& arr_pecas_comidas.getOwner(i) == Jogada.JOGADOR_ATUAL ? true
						: jaComeu;

			if (jaComeu) {
				COMEU_PECA = false;
				ULTIMA_PECA_COMIDA = 0;
				return false;
			} else
				return true;
		} else
			return false;
	}

	/**
	 * Valida o retorno de uma PECA COMUM DO JOGADOR(não dama). Ocorre apenas no
	 * caso de uma peca do jogador comer uma peca do adversario.
	 * 
	 * @param de
	 * @param para
	 * @param tabuleiro
	 * 
	 * @return
	 */
	public boolean isRetornoValidoPeca(int de, int para, char[] tabuleiro) {
		int intervalo = Math.max(de, para) - Math.min(de, para);

		boolean jaComeu = false;
		if (intervalo == 14 || intervalo == 18) {
			if (Jogada.PEDRA_JOGADOR == Pecas.PEDRA_COMPUTADOR) {
				// se avançou duas fileiras verifica se a anterior era uma
				// peca adversario.
				char casaPulada = tabuleiro[para + (intervalo / 2)];

				if (casaPulada != Jogada.PEDRA_ADVERSARIO// PECA_COMPUTADOR
						&& casaPulada != Jogada.DAMA_ADVERSARIO)// DAMA_COMPUTADOR)
					return false;

				COMEU_PECA = true;				
				ULTIMA_PECA_COMIDA = para + (intervalo / 2);
			} else {
				// se avançou duas fileiras verifica se a anterior era uma
				// peca adversario.
				char casaPulada = tabuleiro[para - (intervalo / 2)];

				if (casaPulada != Jogada.PEDRA_ADVERSARIO// PECA_COMPUTADOR
						&& casaPulada != Jogada.DAMA_ADVERSARIO)// DAMA_COMPUTADOR)
					return false;

				COMEU_PECA = true;
				ULTIMA_PECA_COMIDA = para - (intervalo / 2);
			}

			// valida apenas se no caminho ateh a peca nao
			// consta uma casa onde houve uma peca comida.
			for (int i = 0; i < N_PECAS_COMIDAS; i++)
				jaComeu = arr_pecas_comidas.getARR_PECAS_COMIDAS(i) == para
						&& arr_pecas_comidas.getOwner(i) == Jogada.JOGADOR_ATUAL ? true
						: jaComeu;

			if (jaComeu) {
				COMEU_PECA = false;
				ULTIMA_PECA_COMIDA = 0;
				return false;
			} else
				return true;
		} else
			return false;
	}

	public boolean isJogadaValidaDama(int de, int para, char[] tabuleiro) {		
		int novaPosicao = Math.max(de, para) - Math.min(de, para);

		if (novaPosicao % 7 == 0) {
			return this.isCaminhoDamaValido(de, para, 7, tabuleiro);
		} else if (novaPosicao % 9 == 0) {
			return this.isCaminhoDamaValido(de, para, 9, tabuleiro);
		} else
			return false;
	}

	private boolean isCaminhoDamaValido(int de, int para, int intervalo,
			char[] tabuleiro) {

		int pecasAdversarias = 0;
		int menor = Math.min(de, para);
		int maior = Math.max(de, para);
		boolean jaComeu = false;
		for (int i = (tabuleiro[menor] != Pecas.VAZIO) ? menor + intervalo
				: menor; (tabuleiro[maior] != Pecas.VAZIO) ? i < maior
				: i <= maior; i += intervalo) {
			if (tabuleiro[i] == Jogada.PEDRA_ADVERSARIO// Pecas.PECA_COMPUTADOR
					|| tabuleiro[i] == Jogada.DAMA_ADVERSARIO) { // Pecas.DAMA_COMPUTADOR)
				pecasAdversarias++;
				ULTIMA_PECA_COMIDA = i;
				COMEU_PECA = true;
			}

			else if (tabuleiro[i] == Pecas.VAZIO) {
				// valida apenas se no caminho ateh a peca nao
				// consta uma casa onde houve uma peca comida.
				for (int j = 0; j < N_PECAS_COMIDAS; j++)
					jaComeu = arr_pecas_comidas.getARR_PECAS_COMIDAS(j) == i
					&& arr_pecas_comidas.getOwner(j) == Jogada.JOGADOR_ATUAL ? true
					: jaComeu;

			} else if (tabuleiro[i] == Jogada.PEDRA_JOGADOR
					|| tabuleiro[i] == Jogada.DAMA_JOGADOR)
				return false;
			else if (tabuleiro[i] == Jogada.PECA_CAPTURADA)
				return false;

		}

		if (pecasAdversarias > 1 || jaComeu) {
			ULTIMA_PECA_COMIDA = 0;
			COMEU_PECA = false;
			return false;
		}

		return true;
	}

	/**
	 * Determina se a origem da jogada encontra-se nas laterais.
	 * 
	 * Retorna true se for a lateral esquerda, false se for direita e null se
	 * nao for uma lateral.
	 * 
	 * @param de
	 * 
	 * @return
	 */
	public Boolean isLateral(int de) {

		for (int i = 0; i < FiniteStateMachine.LATERAL_DIREITA.length; i++) {
			if (de == FiniteStateMachine.LATERAL_ESQUERDA[i]) {
				return new Boolean(true);
			} else if (de == FiniteStateMachine.LATERAL_DIREITA[i]) {
				return new Boolean(false);
			}
		}

		return null;
	}

	public boolean haPecaAComer(int de, char[] tabuleiro) {
		peca = new PecaCome[1];

		peca[0] = this.pecaPodeComer(de, tabuleiro, true);

		if (peca[0] == null) {
			peca = null;
			return false;
		}

		return peca[0].getPodeComer();
	}

	public boolean haPecaAComer(char[] tabuleiro) {

		peca = new PecaCome[12];
		boolean haCaptura = false;
		PecaCome aux;
		arr_pecas_comidas = new ARR_PECAS_COMIDAS(12);
		N_PECAS_COMIDAS = 0;
		
		//Percorre o tabuleiro
		for (int i = 0, j = 0; i < tabuleiro.length; i++)
			//Se na posição i houver uma pedra ou uma dama do jogador
			if (tabuleiro[i] == Jogada.PEDRA_JOGADOR
					|| tabuleiro[i] == Jogada.DAMA_JOGADOR) {
				//verifica se pode comer
				aux = this.pecaPodeComer(i, tabuleiro, false);

				if (aux != null && aux.getPodeComer()) {
					peca[j] = aux;
					haCaptura = true;
					j++;

				}
			}

		if (haCaptura)
			return true;

		peca = null;
		return false;

	}

	private PecaCome pecaPodeComer(int de, char[] tabuleiro,
			boolean isCadeiaJogada) {

		PecaCome peca = new PecaCome(de, false);

		int novaJogada[];

		if (!COMEU_PECA && ULTIMA_PECA_COMIDA == 0 && isCadeiaJogada)
			return null;

		if (tabuleiro[de] == Jogada.PEDRA_JOGADOR) {
			// so pode mover novamente se uma PECA estiver sendo comida
			novaJogada = new int[] { de - 18, de - 14, de + 14, de + 18 };
			peca.setDestinosPermitidos(new int[novaJogada.length]);
			// boolean jaComeuPeca = false;
			for (int i = 0, j = 0; i < novaJogada.length; i++) {

				if ((novaJogada[i] > 0 && novaJogada[i] < 64)
						&& this.isNovaJogada(de, novaJogada[i], tabuleiro)) {

					peca.setPodeComer(true);
					peca.setTipoPeca(tabuleiro[de]);
					peca.setDestinosPermitidos(j, novaJogada[i]);
					j++;

				}
			}
			return peca;
		} else if (tabuleiro[de] == Jogada.DAMA_JOGADOR) {

			novaJogada = new int[15];

			int quadrado = 0;
			for (int i = 7; de - i > 0 && tabuleiro[de - i] != Pecas.PAREDE; quadrado++, i += 7)
				novaJogada[quadrado] = de - i;
			for (int i = 7; de + i < 64 && tabuleiro[de + i] != Pecas.PAREDE; quadrado++, i += 7)
				novaJogada[quadrado] = de + i;
			for (int i = 9; de - i > 0 && tabuleiro[de - i] != Pecas.PAREDE; quadrado++, i += 9)
				novaJogada[quadrado] = de - i;
			for (int i = 9; de + i < 64 && tabuleiro[de + i] != Pecas.PAREDE; quadrado++, i += 9)
				novaJogada[quadrado] = de + i;

			peca.setDestinosPermitidos(new int[quadrado]);
			for (int i = 0, j = 0; novaJogada[i] != 0; i++) {
				if (this.isNovaJogada(de, novaJogada[i], tabuleiro)) {

					peca.setPodeComer(true);
					peca.setTipoPeca(tabuleiro[de]);
					peca.setDestinosPermitidos(j, novaJogada[i]);
					j++;

				}

			}

			return peca;

		}

		return null;
	}

	public PecaCome[] getPeca() {
		return peca;
	}

	private boolean isNovaJogada(int de, int para, char[] tabuleiro) {

		int novaPosicao = Math.max(de, para) - Math.min(de, para);

		if (novaPosicao == 7 || novaPosicao == 9)
			return false;

		return this.isJogadaValida(de, para, tabuleiro) && COMEU_PECA
				&& ULTIMA_PECA_COMIDA != 0;
	}	
	
	public char[] limparCapturas(char[] tabuleiro) {
		if(JOGADOR_ATUAL == COMPUTADOR) {			
			for (int i = 0; i < tabuleiro.length; i++)
				if (tabuleiro[i] == Pecas.PECA_CAPTURADA_COMPUTADOR){
					i = tabuleiro.length;
				}
			
			
		}
		for (int i = 0; i < tabuleiro.length; i++) {
			if (tabuleiro[i] == Pecas.PECA_CAPTURADA_COMPUTADOR
					|| tabuleiro[i] == Pecas.PECA_CAPTURADA_JOGADOR)
				tabuleiro[i] = Pecas.VAZIO;
		}
		
//		if(JOGADOR_ATUAL == COMPUTADOR && pcPerdeu)
//			ArqConfiguracao.salvaConfiguracao(confAntes,ArqConfiguracao.tabuleiroToString(tabuleiro));	
		
		return tabuleiro;
	}

	/**
	 * Avisa o jogador quando ele tenta efetuar uma jogada nao permitida.
	 * 
	 * @param parent
	 */
	public void alertaJogadaInvalida(Component parent) {
		JOptionPane.showMessageDialog(parent, "Esta jogada não é permitida!",
				"Aviso!", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void pushARR_PECAS_COMIDAS(int posicao) {
		arr_pecas_comidas.setARR_PECAS_COMIDAS(N_PECAS_COMIDAS, posicao);
		arr_pecas_comidas.setOwner(N_PECAS_COMIDAS, Jogada.JOGADOR_ATUAL);
	}

	public static void incN_PECAS_COMIDAS() {
		N_PECAS_COMIDAS++;
	}

	public static int getJOGADOR_ATUAL() {
		return JOGADOR_ATUAL;
	}
	
	public static int getULTIMA_PECA_COMIDA() {
		return ULTIMA_PECA_COMIDA;
	}

}

