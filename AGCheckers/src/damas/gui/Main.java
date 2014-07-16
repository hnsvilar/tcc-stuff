	package damas.gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import damas.data.ArqConfiguracao;

public class Main implements ActionListener{

	private InterfaceDamas in;
	private RestartButton restart;
	private Icon logo;
	private int ultimaOpcao;
	private JMenuItem menuItemNome, menuItemNivel;
	
	public Main(){	
		this.restart = new RestartButton("/img/gifs/POWER_OFF.gif",
				"/img/gifs/POWER_ON.gif", new Rectangle(852, 42, 25, 25));
		this.restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Main.this.restart();
			}

		});
		menuItemNome = new JMenuItem("Mudar usuário", KeyEvent.VK_T);
		menuItemNivel = new JMenuItem("Mudar nível", KeyEvent.VK_N);
		menuItemNivel.addActionListener(this);
		menuItemNome.addActionListener(this);
		in = new InterfaceDamas(-1, restart,menuItemNome,menuItemNivel);		
		logo = in.getLogo().getDisabledIcon(); 
		ultimaOpcao = in.getUltimaOpcao();
		in.repaintGamePaneBacground();
	}
	
	public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
        if(source.getText().equals("Mudar usuário")) {
        	in.alteraNomeUsuario();
        	restart();      	
        } else if(source.getText().equals("Mudar nível")) {
        	in.setNivel();
        }
    }
	
	public void restart() {
		
		Object[] opcoes = { "Sim", "Não" };
		// identifica quem comeca jogando.
		int op = JOptionPane.showOptionDialog(null, "Deseja iniciar um novo jogo?\n**" +
				"As peças de cada jogador serão invertidas",
				"Restart", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, this.logo,
				opcoes, opcoes[1]);

		if (op == JOptionPane.YES_OPTION) {
			in.dispose();
			in.discard();
			
			in = new InterfaceDamas(ultimaOpcao, this.restart, menuItemNome, menuItemNivel);
			ultimaOpcao = in.getUltimaOpcao();
		}
		
		ArqConfiguracao.carregaArquivos();

		//in.repaintGamePaneBacground();
		//in.repaint();
	}
	
	
	public static void main(String[] args) {
		new Main();
	}
}

