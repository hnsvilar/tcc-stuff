package damas.gui.testes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import damas.game.animacao.AnimacaoTabuleiro;
import damas.game.animacao.AnimacaoTransfer;
import damas.game.commom.Jogada;
import damas.game.fsm.FiniteStateMachine;
import damas.gui.ImagePanel;

public class InterfaceEstudoUmCaso extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -272107790039643022L;
	private JLabel lbStatus;
	private JLabel lbPecasComputador;
	private JLabel lbPecasJogador;
	private JTextArea txtTabuleiro;
	private BackgroundSample sampleLogo;

	// private char[] tabuleiro;
	// private AlgoritmoGenetico ag;
	// private Jogador jogador;
	private AnimacaoTransfer transicao;
	private AnimacaoTabuleiro graficoTabuleiro;
	private JScrollPane scrollPane;

	private FiniteStateMachine fsm;

	public InterfaceEstudoUmCaso() {

		int opcao = this.escolhaUsuario();

		if (opcao == JOptionPane.CANCEL_OPTION
				|| opcao == JOptionPane.CLOSED_OPTION)
			System.exit(0);
	

		char[] tabTeste = new char[] {
//				'#',   'A',   '#',   '-',   '#',   'A',   '#',   '-',   
//				'P',   '#',   '-',   '#',   '-',   '#',   '-',   '#',   
//				#   P   #   -   #   B   #   -   
//				-   #   B   #   -   #   -   #   
//				#   P   #   B   #   -   #   -   
//				-   #   B   #   B   #   -   #   
//				#   -   #   -   #   -   #   B   
//				-   #   -   #   B   #   -   #
				'#', 'E', '#', '-', '#', 'E', '#', '-',// # P # P # P # P
				'A', '#', '-', '#', '-', '#', '-', '#',// P # P # - # P #
				'#', '-', '#', '-', '#', '-', '#', '-',// # P # P # P # P
				'A', '#', '-', '#', '-', '#', '-', '#',// - # - # P # - #
				'#', '-', '#', '-', '#', '-', '#', '-',// # - # B # B # -
				'-', '#', '-', '#', '-', '#', '-', '#',// B # B # - # B #
				'#', '-', '#', '-', '#', '-', '#', 'E',// # B # B # - # B
				'-', '#', '-', '#', 'A', '#', '-', '#'
				};// B # B # B # B #
		

//		fsm = new FiniteStateMachine(
//				opcao == JOptionPane.YES_OPTION ? FiniteStateMachine.VEZ_JOGADOR
//						: FiniteStateMachine.VEZ_COMPUTADOR);
		fsm = new FiniteStateMachine();
		opcao = fsm.estadoInicial(opcao == JOptionPane.YES_OPTION ? FiniteStateMachine.VEZ_JOGADOR
				: FiniteStateMachine.VEZ_COMPUTADOR, this);
		graficoTabuleiro = new AnimacaoTabuleiro(new Rectangle(0, 0, 600, 600));
		transicao = new AnimacaoTransfer() {
			private static final long serialVersionUID = -3235606383347822609L;

			// private int ultimaPosicao = -1;

			@Override
			public boolean validaJogadaExterna(int origem, int destino) {
				return fsm.validaMovimentoExterno(origem, destino,
						InterfaceEstudoUmCaso.this);
			}

			@Override
			public void triggerUpdate(int origem, int destino) {
				fsm.update(origem, destino, FiniteStateMachine.getEstado());
				this.showMundo();
			}

			@Override
			public void showMundo() {
				graficoTabuleiro
						.populaPecas(
								fsm.getMundo(),
								transicao,
								(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR),
								fsm.getAtacante());
				InterfaceEstudoUmCaso.this.updateScrenn();

			}

			@Override
			public void alertaJogadaInvalida() {
				// TODO Auto-generated method stub
				
			}

		};

		fsm.init(transicao);
		fsm.setDebug(true,  FiniteStateMachine.VEZ_COMPUTADOR, tabTeste);
		this.init();
		this.printPopulacao();
		fsm.start();

	}

	private void updateScrenn() {
		this.printPopulacao();
		// if(fsm.getEstado() == FiniteStateMachine.FIM_DE_JOGO){
		//			
		// return;
		// }

		lbStatus = new JLabel();
		lbStatus.setForeground(Color.WHITE);
		lbPecasJogador = new JLabel();
		lbPecasComputador = new JLabel();
		if (fsm.getMsgStatus().equals(FiniteStateMachine.MSG_VEZ_JOGADOR))
			lbStatus.setBounds(new Rectangle(245, 52, 300, 40));
		else if (fsm.getMsgStatus().equals(FiniteStateMachine.MSG_VEZ_COMPUTADOR))
			lbStatus.setBounds(new Rectangle(230, 52, 300, 40));	
		else if (fsm.getMsgStatus().equals(FiniteStateMachine.MSG_FIM_DE_JOGO)){
			lbStatus.setBounds(new Rectangle(245, 53, 300, 40));
			lbStatus.setFont(new Font("Arial Bold", Font.BOLD, 13));
//			fsm.stop();
		}
		lbStatus.setText(fsm.getMsgStatus());
		lbPecasJogador.setText(fsm.getNumeroPecasJogador());
		lbPecasComputador.setText(fsm.getNumeroPecasComputador());
		System.gc();
		// lbStatus.setText(text);

		// InterfaceTempDamas.this.printPopulacao();
		// }
		// lbStatus.setText(Rodada.IS_VEZ_JOGADOR ? "SUA VEZ!"
		// : "AGUARDE...");
		// lbStatus.setBounds(Rodada.IS_VEZ_JOGADOR ? new Rectangle(245,
		// 52, 300, 40) : new Rectangle(230, 52, 300, 40));
		// int valuePC = ag.countPecasPC();
		// int valueJogador = jogador.countPecasJogador(tabuleiro);
		// lbPecasPC.setText(valuePC < 10 ? "0" + String.valueOf(valuePC)
		// : String.valueOf(valuePC));
		//
		// lbPecasJogador.setText(valueJogador < 10 ? "0"
		// + String.valueOf(valueJogador) : String
		// .valueOf(valueJogador));
		// System.gc();
	}

	private int escolhaUsuario() {
		sampleLogo = new BackgroundSample("/img/gifs/LOGO.gif", new Rectangle(
				0, 0, 40, 40));
		Object[] opcoes = { "Sim", "Não", "Sair" };
		// identifica quem comeca jogando.
		return JOptionPane.showOptionDialog(this, "Deseja iniciar jogando?",
				"Bem Vindo!", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, sampleLogo.getDisabledIcon(),
				opcoes, opcoes[0]);
	}

	// private void encerraPartida(boolean isVitoriaJogador) {
	// graficoTabuleiro.populaPecas(tabuleiro, transicao, false, null);
	//
	// String texto = "<html><p align = \"center\">FIM DE JOGO,<br>";
	//
	// texto += isVitoriaJogador ? "VOCÊ VENCEU!!" : "VOCÊ PERDEU!!";
	//
	// texto += "</br></p></html>";
	// lbStatus.setBounds(new Rectangle(245, 53, 300, 40));
	// lbStatus.setFont(new Font("Arial Bold", Font.BOLD, 13));
	// lbStatus.setText(texto);
	// ag.stop(); // o metodo esta sendo chamado pelo ag. encerrando o ag
	// // antes,
	// // encerra a execucao do metodo.
	// }

	/**
	 * inicializa a interface.
	 */
	private void init() {

		lbPecasComputador = new JLabel();
//		lbPecasComputador.setBounds(new Rectangle(461, 10, 300, 20));
//		lbPecasComputador.setForeground(Color.WHITE);
//		lbPecasComputador.setText("12");
//		lbPecasComputador.setFont(new Font("Arial Bold", Font.PLAIN, 26));
//
		lbPecasJogador = new JLabel();
//		lbPecasJogador.setBounds(new Rectangle(108, 10, 300, 20));
//		lbPecasJogador.setForeground(Color.WHITE);
//		lbPecasJogador.setText("12");
//		lbPecasJogador.setFont(new Font("Arial Bold", Font.PLAIN, 26));
//
		lbStatus = new JLabel();
//		lbStatus.setBounds(new Rectangle(20, 10, 300, 20));
//		lbStatus.setForeground(Color.WHITE);
//		lbStatus.setFont(new Font("Arial Bold", Font.PLAIN, 24));
//		lbStatus
//				.setText(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR ? "SUA VEZ!"
//						: "AGUARDE...");
//		lbStatus
//				.setBounds(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR ? new Rectangle(
//						245, 52, 300, 40)
//						: new Rectangle(230, 52, 300, 40));

//		graficoTabuleiro
//				.populaPecas(fsm.getMundo(), transicao, FiniteStateMachine
//						.getEstado() == FiniteStateMachine.VEZ_JOGADOR, null);

		Jogada
				.setJogador(FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR ? Jogada.JOGADOR
						: Jogada.COMPUTADOR);
		// :Jogada.setJogador(Jogada.COMPUTADOR);
		String imgPathJ;
		String imgPathC;

		if (FiniteStateMachine.getEstado() == FiniteStateMachine.VEZ_JOGADOR) {
			imgPathJ = "/img/pngs/PECA_BRANCA_CURSOR.png";
			imgPathC = "/img/pngs/PECA_PRETA_CURSOR.png";
		} else {
			imgPathJ = "/img/pngs/PECA_PRETA_CURSOR.png";
			imgPathC = "/img/pngs/PECA_BRANCA_CURSOR.png";
			;
		}

		BackgroundSample sampleJ = new BackgroundSample(imgPathJ,
				new Rectangle(124, 40, 50, 50));
		BackgroundSample sampleC = new BackgroundSample(imgPathC,
				new Rectangle(425, 40, 50, 50));

		ImagePanel lowPane = new ImagePanel("/img/background/BACKGROUND.png",
				new Rectangle(0, 600, 600, 100));
		lowPane.setLayout(null);
		lowPane.setSize(600, 100);
		lowPane.add(lbStatus);
		lowPane.setBackground(Color.BLACK);
		lowPane.add(sampleJ);
		lowPane.add(sampleC);
		lowPane.add(lbPecasComputador);
		lowPane.add(lbPecasJogador);

		Image logo = ((ImageIcon) sampleLogo.getDisabledIcon()).getImage();
		this.setIconImage(logo);
		this.setLayout(null);
		this.add(graficoTabuleiro);
		this.add(lowPane);
		this.add(this.areaTexto(new Rectangle(600, 0, 390, 700)));
		this.setSize(995, 726);
		this.setLocation(150, 0);
		this.setResizable(false);
		this.setTitle("Damas através de A.G. - v.1.0. - Leonardo Filipe");
		this.setVisible(true);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {

			}

			@Override
			public void windowClosed(WindowEvent arg0) {

//				fsm.stop();
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}

			@Override
			public void windowClosing(WindowEvent arg0) {

//				fsm.stop();
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

		return scrollPane;

	}

	/**
	 * imprime a populacao do tabuleiro n.
	 */
	private void printPopulacao() {

		int posicao = 0;

		if (txtTabuleiro == null)
			return;
		char[] tab = fsm.getMundo();
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

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollPane.getVerticalScrollBar().setValue(
						scrollPane.getWidth()
								* FiniteStateMachine.LANCES_REALIZADOS);

			}
		});
	}

	public static void main(String[] args) {
		new InterfaceEstudoUmCaso();
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