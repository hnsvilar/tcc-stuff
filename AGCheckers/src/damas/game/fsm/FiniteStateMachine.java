package damas.game.fsm;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import damas.bd.core.AtributosPartida;
import damas.bd.core.Configuracao;
import damas.data.ArqConfiguracao;
import damas.data.GerenciadorDificuldade;
import damas.data.LoggerTempo;
import damas.game.animacao.AnimacaoTransfer;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;
import damas.game.jogador.Jogador;
import damas.gui.InterfaceDamas;

public class FiniteStateMachine implements Runnable {

	private static LoggerTempo loggerTempo;

	// estados do jogo
	public static final int VEZ_JOGADOR = 1;
	public static final int VEZ_COMPUTADOR = 2;
	public static final int FIM_DE_JOGO = 3;
	private static int VEZ;

	private boolean CONTAR_LANCES_FIM_DE_JOGO;
	public static int LANCES_REALIZADOS = 0;
	private static int LANCES_DE_DAMAS_SEGUIDOS = 0;
	private static int LANCES_FIM_DE_JOGO = 0;

	public static final int TAMANHO_TABULEIRO = 64;
	public static final int[] LATERAL_ESQUERDA = { 8, 24, 40, 56 };
	public static final int[] LATERAL_DIREITA = { 7, 23, 39, 55 };

	public static final int[] LATERAL_SUPERIOR = { 1, 3, 5, 7 };
	public static final int[] LATERAL_INFERIOR = { 56, 58, 60, 62 };

	private AcaoComputador aComputador;
	private AcaoJogador aJogador;
	private Thread fsm;

	private No noRaiz;
	private No noAtual;
	private Individuo tabuleiroAuxiliarJogador = null;

	private long inicio;

	public static final String MSG_VEZ_JOGADOR = "YOUR TURN!";
	public static final String MSG_VEZ_COMPUTADOR = "WAIT...";
	public static String MSG_FIM_DE_JOGO;
	public static boolean IS_DEBUG;

	//------------------------------------------------------------------

	public FiniteStateMachine() {}

	//------------------------------------------------------------------

	public AcaoComputador getAcaoComputador() {
		return aComputador;
	}
	
	public void setNoAtual(No noAtual) {
		this.noAtual = noAtual;
	}

	public int estadoInicial(int iniciante, Object obj){
		if (iniciante == -1) {
			((InterfaceDamas)obj).alteraNomeUsuario();
			iniciante = ((InterfaceDamas)obj).escolhaUsuario();
		} else {
			iniciante = iniciante == JOptionPane.YES_OPTION ? JOptionPane.NO_OPTION
					: JOptionPane.YES_OPTION;
		}
		if (iniciante == JOptionPane.CANCEL_OPTION || iniciante == JOptionPane.CLOSED_OPTION)
			System.exit(0);

		int participante = (iniciante == JOptionPane.YES_OPTION) ? FiniteStateMachine.VEZ_JOGADOR
				: FiniteStateMachine.VEZ_COMPUTADOR;

		FiniteStateMachine.VEZ = participante;
		ArqConfiguracao.setPrimeiroJogador(participante);
		ArqConfiguracao.getData().zera();
		
		if (participante == FiniteStateMachine.VEZ_JOGADOR) {
			Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
			Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
			Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
			Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
		} else {
			Pecas.PEDRA_JOGADOR = Pecas.getPRETA();
			Pecas.DAMA_JOGADOR = Pecas.getDAMA_PRETA();
			Pecas.PEDRA_COMPUTADOR = Pecas.getBRANCA();
			Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_BRANCA();
		}

		IS_DEBUG = false;
		CONTAR_LANCES_FIM_DE_JOGO = false;
		LANCES_FIM_DE_JOGO = 0;
		loggerTempo = new LoggerTempo();

		return iniciante;
	}

	public void setDebug(boolean debug, int owner, char[] tabuleiro) {
		FiniteStateMachine.IS_DEBUG = debug;

		if (debug) {
			//			this.tabuleiro = tabuleiro;
			//			aComputador.setTabuleiro(tabuleiro);
			//			aJogador = new AcaoJogador();
			//			aComputador.setTabuleiro(tabuleiro);
			//			FiniteStateMachine.LANCES_REALIZADOS = 1;
			//			this.update(0, 0, owner);

		}
	}

	/**
	 * 
	 * @param trans
	 */
	public void init(AnimacaoTransfer trans) {

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

		Individuo tabuleiroAtual = new Individuo(tabuleiro, 12, 12, VEZ);
		noRaiz = new No(null, tabuleiroAtual);
		noAtual = noRaiz;
		aComputador = new AcaoComputador(tabuleiro, trans, 2);
		aJogador = new AcaoJogador();
		if(GerenciadorDificuldade.getInstance() != null && 
				GerenciadorDificuldade.getInstance().precisaCalcular()) {
			GerenciadorDificuldade.getInstance().calculaDificuldade(ArqConfiguracao.getUsuario());
		}
		//carrega o arquivo de configuracao
		ArqConfiguracao.carregaArquivos();

	}

	/**
	 * Verifica se é empate
	 * @param totalPecasJogador
	 * @param totalPecasComputador
	 * @param tabuleiro
	 * @param owner
	 * @return
	 */
	public boolean isEmpate(Individuo individuo, int owner) {

		/*
		 * Empate: Após 20 lances sucessivos de damas, sem captura ou
		 * deslocamento de pedra, a partida é declarada empatada. - OK Finais
		 * de: 2 damas contra 2 damas; 2 damas contra uma; 2 damas contra uma
		 * dama e uma pedra; 1 dama contra uma dama 1 dama contra uma dama e uma
		 * pedra, são declarados empatados após 5 lances.
		 * 
		 */

		if (FiniteStateMachine.LANCES_DE_DAMAS_SEGUIDOS == 20) {
			FiniteStateMachine.MSG_FIM_DE_JOGO = this.getMsgEmpate();
			return true;
		}

		int damasJogador = aJogador.getNumeroDamasJogador(individuo.getTabuleiro());
		int damasComputador = aComputador.getNumeroDamasComputador(individuo.getTabuleiro());

		if (individuo.getNumPecasJogador() <= 2 && individuo.getNumPecasComputador() <= 2) {
			if (damasJogador == 1 || damasJogador == 2) {
				if (damasComputador > 0 && damasComputador <= 2) {
					if (CONTAR_LANCES_FIM_DE_JOGO) {
						LANCES_FIM_DE_JOGO++;
					}
					CONTAR_LANCES_FIM_DE_JOGO = true;
				}
			}

		}

		boolean result = (LANCES_FIM_DE_JOGO == 5 ? true : false);

		if (result) {
			FiniteStateMachine.MSG_FIM_DE_JOGO = this.getMsgEmpate();
		}

		return result;

	}

	public boolean fimDeJogo(boolean updated, int owner) {

		//		int nPecasJogador = Integer.valueOf(aJogador.getTotalPecasJogador(tabuleiro)).intValue();
		//		int nPecasComputador = Integer.valueOf(aComputador.getNumeroPecasComputador()).intValue();

		int jogadorAtual = Jogada.getJOGADOR_ATUAL();
		Individuo aux;
		if(tabuleiroAuxiliarJogador == null) {
			aux = noAtual.getEstado();
		} else {
			aux = tabuleiroAuxiliarJogador;
		}

		if (aux.getNumPecasJogador() == 0) {
//			System.out.println("aux.getNumPecasJogador() == 0");
			FiniteStateMachine.MSG_FIM_DE_JOGO = this.getMsgVitoriaComputador();
			ArqConfiguracao.getData().setResultado("Computador");
			ArqConfiguracao.gravarLog();
			return true;
		} else if (aux.getNumPecasComputador() == 0) {
//			System.out.println("aux.getNumPecasComputador() == 0");
			FiniteStateMachine.MSG_FIM_DE_JOGO = this.getMsgVitoriaJogador();
			ArqConfiguracao.getData().setResultado("Usuario");
//			
			ArqConfiguracao.gravarLog();
			return true;
		}
		if (owner == VEZ_COMPUTADOR) {
			// se pc nao puder mover jogador vence
			Jogada.setJogador(Jogada.COMPUTADOR);
//			System.out.println("vez computador ");
			if (updated && !aComputador.isPossivelMover(aux.getTabuleiro())) {
				FiniteStateMachine.MSG_FIM_DE_JOGO = this
						.getMsgVitoriaJogador();
				ArqConfiguracao.getData().setResultado("Usuario");
				ArqConfiguracao.gravarLog();
				return true;
			}
			if (updated) {
				// deve-se avaliar a ultima peca jogada pelo adversario.
				char p = aJogador.getULTIMA_PECA_JOGADA();

				if (p != Pecas.DAMA_JOGADOR || aux.getComeu() || aJogador.isHouveCaptura()) {
					FiniteStateMachine.LANCES_DE_DAMAS_SEGUIDOS = 0;
				} else {
					FiniteStateMachine.LANCES_DE_DAMAS_SEGUIDOS++;
				}
			}
		} else if (owner == VEZ_JOGADOR) {
			Jogada.setJogador(Jogador.JOGADOR);
			// se jogador nao pode mover pc vence
			if (updated && !aJogador.isPossivelMover(aux.getTabuleiro())) {
				FiniteStateMachine.MSG_FIM_DE_JOGO = this
						.getMsgVitoriaComputador();
				ArqConfiguracao.getData().setResultado("Computador");
				ArqConfiguracao.gravarLog();
				return true;
			}
			if (updated) {
				// deve-se avaliar a ultima peca jogada pelo adversario.
				char p = aComputador.getULTIMA_PECA_JOGADA();

				if (p != Pecas.DAMA_COMPUTADOR || aux.getComeu()) {
					FiniteStateMachine.LANCES_DE_DAMAS_SEGUIDOS = 0;
				} else {
					FiniteStateMachine.LANCES_DE_DAMAS_SEGUIDOS++;
				}

			}
		}

		if (this.isEmpate(aux, owner)) {
			ArqConfiguracao.getData().setResultado("Empate");
			ArqConfiguracao.gravarLog();
			return true;
		}

		Jogada.setJogador(jogadorAtual);
		return false;
	}

	private String getMsgVitoriaJogador() {
		return "<html><p align = \"center\">GAME OVER,<br>YOU WIN!!</br></p></html>";
	}

	private String getMsgVitoriaComputador() {
		return "<html><p align = \"center\">GAME OVER,<br>YOU LOSE!!</br></p></html>";
	}

	private String getMsgEmpate() {
		return "<html><p align = \"center\">GAME OVER,<br>DRAW!!</br></p></html>";
	}

	public String getMsgStatus() {

		switch (FiniteStateMachine.VEZ) {
		case FiniteStateMachine.VEZ_JOGADOR:
			return FiniteStateMachine.MSG_VEZ_JOGADOR;

		case FiniteStateMachine.VEZ_COMPUTADOR:
			return FiniteStateMachine.MSG_VEZ_COMPUTADOR;

		case FiniteStateMachine.FIM_DE_JOGO:
			return FiniteStateMachine.MSG_FIM_DE_JOGO;
		}

		return null;
	}

	public boolean validaMovimentoExterno(int origem, int destino, Component parent) {
		if(tabuleiroAuxiliarJogador == null)
			return aJogador.isJogadaValida(origem, destino, noAtual.getEstado().getTabuleiro(), parent);
		else
			return aJogador.isJogadaValida(origem, destino, tabuleiroAuxiliarJogador.getTabuleiro(), parent);
	}

	private No getNovoNo(No pai, Individuo individuo, int destino) { 
		No auxiliar = new No(pai, new Individuo(
				individuo.getTabuleiro().clone(), 
				individuo.getNumPecasJogador(), 
				individuo.getNumPecasComputador(), 
				individuo.getJogadorAtual()));
		auxiliar.getEstado().setAvaliacao(individuo.getAvaliacao());
		auxiliar.getEstado().setComeu(individuo.getComeu());
		auxiliar.getEstado().setPecaMudada(individuo.getPecaMudada());
		auxiliar.getEstado().setPosicoesIntermediarias(individuo.getPosicoesIntermediarias());
		auxiliar.getEstado().setPecasComidas(individuo.getPecasComidas());
		this.aJogador.doAcaoPromocao(destino, auxiliar.getEstado().getTabuleiro());
		return auxiliar;
	}

	@SuppressWarnings("deprecation")
	public void update(int origem, int destino, int estado) {
		boolean updated = true;
		switch (estado) {
		case FiniteStateMachine.VEZ_JOGADOR:
			Jogada.setJogador(Jogada.JOGADOR);
			if(tabuleiroAuxiliarJogador == null) {
				tabuleiroAuxiliarJogador = 
						new Individuo(noAtual.getEstado().getTabuleiro().clone(), 
								noAtual.getEstado().getNumPecasJogador(), 
								noAtual.getEstado().getNumPecasComputador(), 
								Jogada.JOGADOR);
				tabuleiroAuxiliarJogador.setPosicoesIntermediarias(new ArrayList<Integer>());
				tabuleiroAuxiliarJogador.setPecaMudada(origem);
				tabuleiroAuxiliarJogador.getPosicoesIntermediarias().add(origem);
			}
			tabuleiroAuxiliarJogador.getPosicoesIntermediarias().add(destino);

			char ultima_peca_jogada = tabuleiroAuxiliarJogador.getTabuleiro()[origem];
			this.aJogador.doAcaoMovimento(origem, destino,	
					ultima_peca_jogada, tabuleiroAuxiliarJogador);

			updated = this.aJogador.completouAcao(tabuleiroAuxiliarJogador);


			// VERIFICA SE O ADVERSARIO AINDA PODE REALIZAR JOGADAS
			if (this.fimDeJogo(updated, FiniteStateMachine.VEZ_COMPUTADOR)) {
				System.out.println("deu certo wtfv");
				setEstado(FIM_DE_JOGO);

				tabuleiroAuxiliarJogador.setTabuleiro(
						this.aJogador.limparCapturas(
								tabuleiroAuxiliarJogador.getTabuleiro().clone()));
				if(tabuleiroAuxiliarJogador.getComeu()) {
					ArqConfiguracao.salvaConfiguracao(
							tabuleiroAuxiliarJogador.getTabuleiro(), Jogador.JOGADOR);
				}


				noAtual = getNovoNo(noAtual, tabuleiroAuxiliarJogador, destino);

				tabuleiroAuxiliarJogador = null;

				aComputador.showAnimacao();
				System.out.println("Terminou");
				this.update(origem, destino, FiniteStateMachine.FIM_DE_JOGO);
			} else if (updated) {
				this.aJogador.doAcaoPromocao(destino, tabuleiroAuxiliarJogador.getTabuleiro());
				
				tabuleiroAuxiliarJogador.setTabuleiro(
						this.aJogador.limparCapturas(
								tabuleiroAuxiliarJogador.getTabuleiro().clone()));

				if(tabuleiroAuxiliarJogador.getComeu()) {
					ArqConfiguracao.salvaConfiguracao(tabuleiroAuxiliarJogador.getTabuleiro(), 
							Jogador.JOGADOR);
				}

				noAtual = getNovoNo(noAtual, tabuleiroAuxiliarJogador, destino);
				tabuleiroAuxiliarJogador = null;
				aComputador.showAnimacao();
				FiniteStateMachine.setEstado(FiniteStateMachine.VEZ_COMPUTADOR);
				LANCES_REALIZADOS++;
			}
			break;
			//-----------------------------------------------------------------------------
		case FiniteStateMachine.VEZ_COMPUTADOR:
			long t1 = System.currentTimeMillis();   

			// Realiza o movimento do computador				
			Jogada.setJogador(Jogador.COMPUTADOR);
			noAtual = this.aComputador.getProximaJogada(noAtual);

			this.aComputador.promoveDama(noAtual.getEstado().getPosicoesIntermediarias().get(
					noAtual.getEstado().getPosicoesIntermediarias().size()-1),
					noAtual.getEstado().getTabuleiro());

			aComputador.showAnimacao();

			LANCES_REALIZADOS++;

			if(noAtual.getEstado().getComeu()) {
				ArqConfiguracao.salvaConfiguracao(
						noAtual.getEstado().getTabuleiro(), Jogador.COMPUTADOR);
			}

			long t2 = System.currentTimeMillis();   
			loggerTempo.soma(t2-t1);
			FiniteStateMachine.setEstado(FiniteStateMachine.VEZ_JOGADOR);
			Jogada.setJogador(Jogada.JOGADOR);
			this.aJogador.doAcaoInicioCaptura(noAtual.getEstado().getTabuleiro());

			this.aguardar(500);
			aComputador.showAnimacao();
			// VERIFICA SE O ADVERSARIO AINDA PODE REALIZAR JOGADAS
			if (this.fimDeJogo(true, FiniteStateMachine.VEZ_JOGADOR)) {
				setEstado(FIM_DE_JOGO);
				this.update(origem, destino, FiniteStateMachine.FIM_DE_JOGO);
			}
			break;
			//-----------------------------------------------------------------------
		case FiniteStateMachine.FIM_DE_JOGO:
			FiniteStateMachine.setEstado(FiniteStateMachine.FIM_DE_JOGO);

			salvaConfiguracoes();
			tabuleiroAuxiliarJogador = null;
			this.aJogador.limparCapturas(noAtual.getEstado().getTabuleiro());
			aComputador.showAnimacao();
			fsm.stop();
			break;
		}
	}

	private void salvaConfiguracoes() {
		loggerTempo.salvaLog(ArqConfiguracao.getUsuario().getNome());
		
		long fim = System.currentTimeMillis();
		long duracao = fim - inicio;
		ArqConfiguracao.carregaArquivos();
		Date d = new Date();
		String tempo = AtributosPartida.dateToString(d);
		
		int resultado;
		if(ArqConfiguracao.getData().getResultado().equals("Empate")) { 
			resultado = Configuracao.EMPATE;
		} else if(ArqConfiguracao.getData().getResultado().equals("Computador")) {
			resultado = Configuracao.DERROTA;
		} else {
			resultado = Configuracao.VITORIA;
		}
		
		AtributosPartida atributo = new AtributosPartida(tempo,
				getQuant(noAtual.getEstado().getTabuleiro(),Pecas.DAMA_COMPUTADOR), 
				getQuant(noAtual.getEstado().getTabuleiro(),Pecas.PEDRA_COMPUTADOR),
				getQuant(noAtual.getEstado().getTabuleiro(),Pecas.DAMA_JOGADOR), 
				getQuant(noAtual.getEstado().getTabuleiro(),Pecas.PEDRA_JOGADOR),
				duracao, resultado, LANCES_REALIZADOS);
		ArqConfiguracao.salvaAtributo(atributo);
	}
	
	private int getQuant(char[] tabuleiro, char procurado) {
		int quant = 0;
		for(int i = 0; i < tabuleiro.length; i++) {
			if(tabuleiro[i] == procurado)
				quant++;
		}
		return quant;
	}

	private static void setEstado(int estado) {
		FiniteStateMachine.VEZ = estado;
	}

	public static int getEstado() {
		return FiniteStateMachine.VEZ;
	}

	public No getNoAtual() {
		if(tabuleiroAuxiliarJogador == null)
			return noAtual;
		else
			return new No(noAtual, tabuleiroAuxiliarJogador);
	}

	public char[] getMundo() {
		return noAtual.getEstado().getTabuleiro();
	}

	public String getNumeroPecasJogador() {
		int n = noAtual.getEstado().getNumPecasJogador();
		return n < 10 ? "0"+ n : ""+n;//aJogador.getTotalPecasJogador(tabuleiroAtual.getTabuleiro());
	}

	public String getNumeroPecasComputador() {
		int n = noAtual.getEstado().getNumPecasComputador();
		return n < 10 ? "0"+ n : ""+n;//aComputador.getNumeroPecasComputador();
	}

	public void msgInvalida(Component parent) {
		aJogador.msgJogadaInvalida(parent);
	}

	/**
	 * Suspende o Thread por um dado periodo.
	 * 
	 * @param tempo
	 */
	private void aguardar(long tempo) {
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		fsm = new Thread(this);
		inicio = System.currentTimeMillis();
		fsm.start();
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		fsm.stop();
	}

	@Override
	public void run() {
		while (true) {
			if (VEZ == VEZ_COMPUTADOR) {
				this.update(0, 0, FiniteStateMachine.VEZ_COMPUTADOR);
			} else { 
				this.aguardar(500);
			}
		}

	}
}



