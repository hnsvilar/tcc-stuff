package damas.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import damas.data.ArqConfiguracao;
import damas.data.GerenciadorLog;
import damas.game.animacao.AnimacaoTabuleiro;
import damas.game.animacao.AnimacaoTransfer;
import damas.game.arvore.No;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;
import damas.game.fsm.FiniteStateMachine;
import damas.game.commom.Individuo;

public class InterfaceDamas extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -272107790039643022L;
	private JLabel lbStatus;
	private JLabel lbPecasComputador;
	private JLabel lbPecasJogador;
	private JTextArea txtTabuleiro;
	private BackgroundSample sampleLogo;

	private AnimacaoTransfer animacaoTransfer;
	private AnimacaoTabuleiro animacaoTabuleiro;
	private JScrollPane scrollPane;

	private FiniteStateMachine fsm;
	private Component restartButton;
	private int ultimaOpcao;
	private ImagePanel gamePane;

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItemNome;
	private JMenuItem menuItemNivel;
	
	public InterfaceDamas(int ultimaOpcao, Component restartButton, 
			JMenuItem menuItemNome, JMenuItem menuItemNivel) {		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("Opções");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		
		this.menuItemNome = menuItemNome;
		this.menuItemNivel = menuItemNivel;
		//a group of JMenuItems		
		menu.add(menuItemNome);		
		menu.add(menuItemNivel);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		this.ultimaOpcao = ultimaOpcao;
		this.restartButton = restartButton;
		sampleLogo = new BackgroundSample("/img/gifs/LOGO.gif", new Rectangle(
				0, 0, 40, 40));
		
		fsm = new FiniteStateMachine();
		ultimaOpcao = fsm.estadoInicial(ultimaOpcao, this);
		this.ultimaOpcao = ultimaOpcao;

		animacaoTabuleiro = null;
		animacaoTabuleiro = new AnimacaoTabuleiro(new Rectangle(51, 105, 480, 480));
		animacaoTransfer = null;
		animacaoTransfer = new AnimacaoTransfer() {
			private static final long serialVersionUID = -3235606383347822609L;

			@Override
			public boolean validaJogadaExterna(int origem, int destino) {
				return fsm.validaMovimentoExterno(origem, destino,
						InterfaceDamas.this);
			}

			@Override
			public void triggerUpdate(int origem, int destino) {
				// TODO Aqui chama Update
				fsm.update(origem, destino, FiniteStateMachine.getEstado());
				this.showMundo();
			}

			@Override
			public void showMundo() {
				No no = fsm.getNoAtual();
				if(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_COMPUTADOR
						&& no.getEstado().getComeu()) {
					char[] tabAuxiliar = new char[no.getEstado().getTabuleiro().length];
					System.arraycopy(no.getPai().getEstado().getTabuleiro(), 0, 
							tabAuxiliar, 0, no.getEstado().getTabuleiro().length);
					ArrayList<Integer> posicoesIntermediarias = no.getEstado().getPosicoesIntermediarias();
					ArrayList<Integer> pecasComidas = no.getEstado().getPecasComidas();
					char tipoPeca = tabAuxiliar[posicoesIntermediarias.get(0)];
					
					int limpar = posicoesIntermediarias.get(0);
							
					for(int i = 1; i < posicoesIntermediarias.size(); i++) {
						tabAuxiliar[limpar] = Pecas.VAZIO;
						tabAuxiliar[posicoesIntermediarias.get(i)] = tipoPeca;
						tabAuxiliar[pecasComidas.get(i-1)] = Pecas.PECA_CAPTURADA_JOGADOR;
						animacaoTabuleiro
						.populaPecas(
								tabAuxiliar,
								animacaoTransfer,
								(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR));
						InterfaceDamas.this.updateScrenn();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				animacaoTabuleiro
						.populaPecas(
								no.getEstado().getTabuleiro(),
								animacaoTransfer,
								(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR));
				InterfaceDamas.this.updateScrenn();
			}

			@Override
			public void alertaJogadaInvalida() {
				// TODO Auto-generated method stub
				fsm.msgInvalida(InterfaceDamas.this);
			}

		};

		fsm.init(animacaoTransfer);
		this.init();
		this.printPopulacao();
		fsm.start();
	}

	
	
	public void setNivel() {
		String nivel = null;
		if(fsm != null)
			nivel = String.valueOf(fsm.getAcaoComputador().getNiveis());
		nivel = JOptionPane.showInputDialog(this,"Digite o número de níveis que a árvore " +
					"deve percorrer.",fsm.getAcaoComputador().getNiveis());
		fsm.getAcaoComputador().setNiveis(Integer.parseInt(nivel));
	}
	
	public String getUltimaConfiguracaoTabuleiro() {
		String auxiliar = txtTabuleiro.getText();
		//"----------------------------------------------------------------------"
		return auxiliar;
	}
	
	public void discard(){
		fsm.stop();
	}
	public int getUltimaOpcao() {
		return this.ultimaOpcao;
	}

	public void repaintGamePaneBacground() {
		gamePane.paintImmediately(gamePane.getBounds());
	}

	private void updateScrenn() {
		this.printPopulacao();

		if (fsm.getMsgStatus().equals(FiniteStateMachine.MSG_VEZ_JOGADOR))
			lbStatus.setBounds(new Rectangle(650, 28, 300, 40));
		else if (fsm.getMsgStatus().equals(
				FiniteStateMachine.MSG_VEZ_COMPUTADOR))
			lbStatus.setBounds(new Rectangle(690, 28, 300, 40));
		else if (fsm.getMsgStatus().equals(FiniteStateMachine.MSG_FIM_DE_JOGO)) {
//			GerenciadorLog l = new GerenciadorLog();
//			l.grava(txtTabuleiro.getText(), GerenciadorLog.Tipo.RESULTADO);
			lbStatus.setBounds(new Rectangle(680, 28, 300, 40));
			lbStatus.setFont(new Font("Times New Roman", Font.BOLD, 16));
		}

		lbPecasJogador.setText(fsm.getNumeroPecasJogador());
		lbPecasComputador.setText(fsm.getNumeroPecasComputador());
		lbStatus.setText(fsm.getMsgStatus());
	}
	
	public void alteraNomeUsuario() {
		String nome = null;
		if(ArqConfiguracao.getUsuario() != null)
			nome = ArqConfiguracao.getUsuario().getNome();
		else 
			nome = "Gabb";
		nome = JOptionPane.showInputDialog(this,"Digite seu nome de usuário",nome);
		ArqConfiguracao.setJogador(nome);
	}

	public int escolhaUsuario() {		
		Object[] opcoes = { "Sim", "Não", "Sair" };
		// identifica quem comeca jogando.		
		return JOptionPane.showOptionDialog(this, "Deseja iniciar jogando?",
				"Bem Vindo!", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, sampleLogo.getDisabledIcon(),
				opcoes, opcoes[0]);
	}

	/**
	 * inicializa a interface.
	 */
	private void init() {

//		this.dispose();
//		this.setUndecorated(true);
		
		lbPecasComputador = new JLabel();
		lbPecasComputador.setBounds(new Rectangle(500, 58, 300, 30));
		lbPecasComputador.setForeground(Color.WHITE);
		lbPecasComputador.setText("12");
		lbPecasComputador.setFont(new Font("Verdana", Font.BOLD, 20));

		lbPecasJogador = new JLabel();
		lbPecasJogador.setBounds(new Rectangle(500, 16, 300, 30));
		lbPecasJogador.setForeground(Color.WHITE);
		lbPecasJogador.setText("12");
		lbPecasJogador.setFont(new Font("Verdana", Font.BOLD, 20));

		lbStatus = new JLabel();
		lbStatus.setBounds(new Rectangle(20, 10, 300, 20));
		lbStatus.setForeground(Color.WHITE);
		lbStatus.setFont(new Font("Times New Roman", Font.BOLD, 26));
		lbStatus.setText(fsm.getMsgStatus());
		lbStatus
				.setBounds(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR ? new Rectangle(
						650, 28, 300, 40)
						: new Rectangle(690, 28, 300, 40));

		animacaoTabuleiro
				.populaPecas(
						fsm.getMundo(),
						animacaoTransfer,
						FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR);

		Jogada
				.setJogador(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR ? Jogada.JOGADOR
						: Jogada.COMPUTADOR);
		String imgPathJ;
		String imgPathC;

		if (FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR) {
			imgPathJ = "/img/pngs/DAMA_BRANCA_CURSOR.png";
			imgPathC = "/img/pngs/DAMA_PRETA_CURSOR.png";
		} else {
			imgPathJ = "/img/pngs/DAMA_PRETA_CURSOR.png";
			imgPathC = "/img/pngs/DAMA_BRANCA_CURSOR.png";
			;
		}

		BackgroundSample sampleJ = new BackgroundSample(imgPathJ,
				new Rectangle(425, 7, 40, 40));
		BackgroundSample sampleC = new BackgroundSample(imgPathC,
				new Rectangle(425, 49, 40, 40));

		gamePane = new ImagePanel("/img/background/BACKGROUND.eng.png",
				new Rectangle(0, 0, 958, 639));
		gamePane.setSize(958, 639);
		gamePane.setLayout(null);
		gamePane.add(lbStatus);
		gamePane.add(sampleJ);
		gamePane.add(sampleC);
		gamePane.add(lbPecasComputador);
		gamePane.add(lbPecasJogador);
		gamePane.add(this.restartButton);

		Image logo = ((ImageIcon) sampleLogo.getDisabledIcon()).getImage();
		this.setIconImage(logo);
		this.setLayout(null);
		gamePane.add(animacaoTabuleiro);
		gamePane.add(this.areaTexto(new Rectangle(585, 106, 320, 480)));
		this.add(gamePane);
		if (!this.isUndecorated())
			this.setSize(964, 670);
		else
			this.setSize(958, 639);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2
				- this.getSize().width / 2, Toolkit.getDefaultToolkit()
				.getScreenSize().height
				/ 2 - this.getSize().height / 2);
		this.setResizable(false);
		this.setTitle("G.A. Checkers - v.1.5.");
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				fsm.stop();
				System.gc();
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				fsm.stop();
				System.gc();
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {

			}

			@Override
			public void windowIconified(WindowEvent arg0) {

			}

			@Override
			public void windowOpened(WindowEvent arg0) {

			}

		});
		this.setVisible(true);
	}

	/**
	 * inicializa a area de texto que exibe o tabuleiro
	 * 
	 * @param rec
	 */
	private JScrollPane areaTexto(Rectangle rec) {
		txtTabuleiro = new JTextArea();
		txtTabuleiro.setEditable(false);
		txtTabuleiro.setAutoscrolls(true);

		scrollPane = new JScrollPane(txtTabuleiro);

		scrollPane.setBounds(rec);
		scrollPane.setBorder(null);

		return scrollPane;

	}

	public JLabel getLogo() {
		return this.sampleLogo;
	}

	/**
	 * imprime a populacao do tabuleiro n.
	 */
	private void printPopulacao() {

		int posicao = 0;

		char[] tab = fsm.getMundo();

		Calendar cal = new GregorianCalendar();

		int hour24 = cal.get(Calendar.HOUR_OF_DAY); // 0..23
		int min = cal.get(Calendar.MINUTE); // 0..59
		int sec = cal.get(Calendar.SECOND); // 0..59
		int ms = cal.get(Calendar.MILLISECOND); // 0..999

		String hora = (hour24 < 10 ? "0" + String.valueOf(hour24) : String
				.valueOf(hour24))
				+ ":"
				+ (min < 10 ? "0" + String.valueOf(min) : String.valueOf(min))
				+ ":"
				+ (sec < 10 ? "0" + String.valueOf(sec) : String.valueOf(sec))
				+ ":" + ms;

		txtTabuleiro.append("Game at " + hora + "\n");
		for (int i = 0; i < 8; i++) {
			txtTabuleiro.append("\t");
			for (int j = 0; j < 8; j++) {
				txtTabuleiro.append(String.valueOf(tab[posicao]) + "   ");
				posicao++;
			}
			txtTabuleiro.append("\n");
		}
		txtTabuleiro.append("\n------------------------"
				+ "----------------------------------------------\n");
//		System.out.print("aki? ");
//		ArqConfiguracao.setUltimaConfiguracao(ArqConfiguracao.tabuleiroToString(tab));
		ArqConfiguracao.setUltimaConfiguracao(tab);
//		System.out.println(ArqConfiguracao.getUltimaConfiguracao());

		txtTabuleiro.setCaretPosition(txtTabuleiro.getDocument().getLength());
		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// scrollPane.getVerticalScrollBar().setValue(
		// scrollPane.getHeight()
		// * (FiniteStateMachine.LANCES_REALIZADOS + 1));
		// System.out.println("OK");
		//
		
		// }
		// });
	}
}

class BackgroundSample extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9082369731975855421L;

	public BackgroundSample(String path, Rectangle bounds) {
		Image img = this.getToolkit().getImage(
				this.getClass().getResource(path));
		img = img.getScaledInstance(bounds.width, bounds.height,
				Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(img);
		this.setIcon(icon);
		this.setDisabledIcon(icon);
		this.setEnabled(false);
		this.setFocusable(false);
		this.setBounds(bounds);

	}
}