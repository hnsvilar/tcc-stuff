package damas.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


import damas.bd.core.AtributosPartida;
import damas.bd.core.Configuracao;
import damas.bd.core.Posicao;
import damas.bd.core.Usuario;
import damas.bd.dao.FacadeDAO;
import damas.bd.dao.UsuarioDAO;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;
import damas.game.fsm.AcaoComputador;

public class ArqConfiguracao {	
	
	public static boolean IS_READY = false;
	
	private static String jogador = null;
	private static Usuario usuario = null;
	private static char[] tabCopia = null;
	
	private static int totalNegativas = -1;
	private static int totalPositivas = -1;
	//private static HashMap<String,Integer> configuracoes = null;
	private static HashMap<Configuracao, Integer> configuracoesNegativas = null;
	private static HashMap<Configuracao,Integer> configuracoesPositivas = null;
	
	private static ConfiguracaoLocal data = null;
	private static int primeiroJogador;
	
	public static Usuario getUsuario() {
		return usuario;
	}
	
	public ArqConfiguracao() {
		data = ConfiguracaoLocal.getInstance();
	}
	
	public static ConfiguracaoLocal getData() {
		if(data == null)
			data = ConfiguracaoLocal.getInstance();
		return data;
	}
	
	public static HashMap<Configuracao, Integer> getConfiguracoesNegativas() {
		return configuracoesNegativas;
	}
	
	public static HashMap<Configuracao, Integer> getConfiguracoesPositivas() {
		return configuracoesPositivas;
	}
	
	/**
	 * Carrega o usuário do banco de dados;
	 * @param jogador
	 */
	public static void setJogador(String jogador) {
		ArqConfiguracao.jogador = jogador;
		UsuarioDAO u = new UsuarioDAO();
		usuario = u.retrieveUsuarioByName(jogador);
		if(usuario == null) {
			usuario = new Usuario();
			usuario.setDerrotas(0);
			usuario.setConfiguracoes(new ArrayList<Configuracao>());
			usuario.setVitorias(0);
			usuario.setNome(jogador);
			usuario.setAtributosPartidas(new ArrayList<AtributosPartida>());
			u.insertUsuario(usuario);
		}
		System.out.println();
		System.out.println("---------------------\n");
		System.out.println(usuario);
		System.out.println("Configuracoes: "+usuario.getConfiguracoes().toString());
		System.out.println("---------------------\n");
	}
	
	public static String getUltimaConfiguracao() {
		return tabuleiroToString(tabCopia);
	}
	
	public static void setUltimaConfiguracao(char[] tab) {
		if(tabCopia == null) {
			tabCopia = tab.clone();
		}
	}
	
	public static void salvaConfiguracao(char[] tab, int vez) {		
//		System.err.println("Salvando2");
		if(jogador != null) {
			String salvar = "";
			char[] tabDepois = tab.clone();
//			System.out.println(tabuleiroToString(tab));
			for(int i = 0; i < tabDepois.length; i++) {
				if(tabCopia[i] != tabDepois[i]) {
					salvar += i +"," + tabCopia[i] + ";";
				}
			}
			
			try {
				BufferedWriter out;
				FacadeDAO facade = new FacadeDAO();
				Configuracao c = new Configuracao();
				
				for(int i = 0; i < tabDepois.length; i++) {
					String s = tabuleiroToString(tabCopia);
					if(tabCopia[i] != tabDepois[i]) {
						Posicao p = new Posicao(i, s.charAt(i));
						facade.insertPosicao(p);
						c.addPosicao(p);
					}
				}
				
				if(vez == Jogada.JOGADOR) {
					c.setResultado(Configuracao.DERROTA);
					out = new BufferedWriter(new FileWriter(jogador+"configuracoes.damas",true));
				} else {
					c.setResultado(Configuracao.VITORIA);
					out = new BufferedWriter(new FileWriter(jogador+"configuracoesvencidas.damas",true));
				}
				//--------------BD
//				System.out.println(salvar);			
				if(vez == Jogada.JOGADOR) { //significa que o jogador comeu a peca
					c.setResultado(Configuracao.DERROTA);
				} else {
					c.setResultado(Configuracao.VITORIA);
				}
				ArrayList<Usuario> array = new ArrayList<Usuario>();
				array.add(usuario);
				c.addUsuario(usuario);
				
				facade.insertConfiguracao(c);
				facade.updateUsuario(usuario);
				//-------------FIM BD
				out.write(tabuleiroToString(salvar) + "\n");
				tabCopia = null;
				tabDepois = null;
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static void salvaAtributo(AtributosPartida atributo) {		
//		System.err.println("Salvando2");
		if(jogador != null) {
			System.out.println("Salvando atributo");
			FacadeDAO facade = new FacadeDAO();
			atributo.setUsuario(usuario);
			usuario.addAtributo(atributo);
			
			facade.insertAtributo(atributo);
			facade.updateUsuario(usuario);
		}
	}
	
	public static int getTotalConfiguracoes() {
		return (configuracoesNegativas.size()+configuracoesPositivas.size());
	}
	
	public static int getTotalConfiguracoesComRepeticao() {
		int total = 0;
		Set<Configuracao> keys = configuracoesNegativas.keySet();
		for(Configuracao i : keys) {
			total += configuracoesNegativas.get(i);
		}
		keys = configuracoesPositivas.keySet();
		for(Configuracao i : keys) {
			total += configuracoesPositivas.get(i);
		}
		return total;
	}
	
	public static int getTotalNegativas() {
		return configuracoesNegativas.size();
	}
	
	public static int getTotalPositivas() {
		return configuracoesPositivas.size();
	}
	
	public static String tabuleiroToString(char[] tabuleiro) {
		String tabString = "";		
		for(int i = 0; i < tabuleiro.length; i++) {
			if(tabuleiro[i] == Pecas.PEDRA_JOGADOR) {
				tabString += 'j';
			} else if(tabuleiro[i] == Pecas.DAMA_JOGADOR) {
				tabString += 'J';
			} else if(tabuleiro[i] == Pecas.PEDRA_COMPUTADOR) {
				tabString += 'c';
			} else if(tabuleiro[i] == Pecas.DAMA_COMPUTADOR) {
				tabString += 'C';
			} else {
				tabString += tabuleiro[i];
			}					
		}
//		tabString += tabuleiro[i];
		return tabString;
	}
	
	public static String tabuleiroToString(String tabuleiro) {
		String tabString = "";		
		for(int i = 0; i < tabuleiro.length() -1; i++) {
			if(tabuleiro.charAt(i) == Pecas.PEDRA_JOGADOR) {
				tabString += 'j';
			} else if(tabuleiro.charAt(i) == Pecas.DAMA_JOGADOR) {
				tabString += 'J';
			} else if(tabuleiro.charAt(i) == Pecas.PEDRA_COMPUTADOR) {
				tabString += 'c';
			} else if(tabuleiro.charAt(i) == Pecas.DAMA_COMPUTADOR) {
				tabString += 'C';
			} else {
				tabString += tabuleiro.charAt(i);
			}					
		}
//		tabString += tabuleiro[i];
		return tabString;
	}
	
//	public static int getQuantOcorrencias (String antes, String depois) {
//		String chave = antes + ":" + depois;
//		if(configuracoes.containsKey(chave))
//			return configuracoes.get(chave);
//		return 0;
//	}
//	
	public static int getQuantOcorrenciasNegativas(char[] tab) {
		int quant = 0;
		if(ArqConfiguracao.isReady()) {			
			String s = tabuleiroToString(tab);
			Set<Configuracao> chaves = configuracoesNegativas.keySet(); 
			for(Configuracao c : chaves) {
				ArrayList<Posicao> posicao = (ArrayList<Posicao>) c.getPosicoes();
				boolean ehIgual = true;
				for(Posicao p : posicao) {
					if(s.charAt(p.getPosicao()) != p.getPeca()) {
						ehIgual = false;
						break;
					}	
				}				
				if(ehIgual) {
					quant += configuracoesNegativas.get(c);
//					for(Posicao p : posicao) {
//						System.out.print(s.charAt(p.getPosicao()) +" - "+ p.getPeca()+"\t");	
//					}
//					System.out.println();
				}
			}
		}
		return quant;		
	}
	
	public static int getQuantOcorrenciasPositivas(char[] tab) {
		int quant = 0;
		if(ArqConfiguracao.isReady()) {
			
			String s = tabuleiroToString(tab);
			Set<Configuracao> chaves = configuracoesPositivas.keySet(); 
			for(Configuracao c : chaves) {
				ArrayList<Posicao> posicao = (ArrayList<Posicao>) c.getPosicoes();
				boolean ehIgual = true;
//				System.out.println();
				for(Posicao p : posicao) {
					if(s.charAt(p.getPosicao()) != p.getPeca()) {
						ehIgual = false;
						break;
					}	
				}				
				if(ehIgual) {
					quant += configuracoesPositivas.get(c);
//					for(Posicao p : posicao) {
//						System.out.print(s.charAt(p.getPosicao()) +" - "+ p.getPeca()+"\t");	
//					}
//					System.out.println();
				}
			}
		}
		return quant;
	}
	
	public static boolean isReady() {
		if(usuario != null && usuario.getConfiguracoes() != null && 
				configuracoesNegativas != null && configuracoesPositivas != null && IS_READY) //ADICIONAR
			return true;
		return false;
	}

	
	public static void showConfiguracoesAnteriores(int tipo) {
//		if(tipo == DERROTA) {
//			Set<String> s = configuracoesNegativas.keySet();
//			System.out.println("Mostrando " + configuracoesNegativas.size() + ":");
//	//		Iterator it = s.iterator();
//	//		while(it.hasNext()) {
//	//			String aux = (String)it.next();
//	//			System.out.println(aux + " " + configuracoesAnteriores.get(aux));
//	//		}
//		} else if (tipo == VITORIA) {
//			Set<String> s = configuracoesPositivas.keySet();
//			System.out.println("Mostrando " + configuracoesPositivas.size() + ":");
//		} else {
//			Set<String> s = configuracoesNegativas.keySet();
//			System.out.println("Mostrando " + configuracoesNegativas.size() + ":");
//			s = configuracoesPositivas.keySet();
//			System.out.println("Mostrando " + configuracoesPositivas.size() + ":");
//		}
	}
	
	public static int carregaArquivos() {
		if(jogador != null) {
			IS_READY = false;
			if(data == null)
				data = ConfiguracaoLocal.getInstance();
			data.zera();
			System.out.println("Carregando Arquivos");			
			
			configuracoesNegativas = new HashMap<Configuracao, Integer>();
			configuracoesPositivas = new HashMap<Configuracao, Integer>();
			
			for(Configuracao c : usuario.getConfiguracoes()) {
				if(c.getResultado() == Configuracao.VITORIA) {
					if(configuracoesPositivas.containsKey(c)) {
						int aux = configuracoesPositivas.get(c);
						configuracoesPositivas.put(c,aux+1);
					} else
						configuracoesPositivas.put(c, 1);
				} else if(c.getResultado() == Configuracao.DERROTA) {
					if(configuracoesNegativas.containsKey(c)) {
						int aux = configuracoesNegativas.get(c);
						configuracoesNegativas.put(c,aux+1);
					} else
						configuracoesNegativas.put(c, 1);
				}
			}
			IS_READY = true;
			return configuracoesNegativas.size() + configuracoesPositivas.size();
		}
		return 0;
	}
	
//	public static int carregaArquivos() {
//		if(jogador != null) {
//			IS_READY = false;
//			if(data == null)
//				data = new ConfiguracaoLocal();
//			data.zera();
//			System.out.println("Carregando Arquivos");
//			int quant = 0;
//			try {
//				//1
//				BufferedReader in = new BufferedReader(new FileReader("configuracoes.damas"));
////				conf = new HashMap<Integer, String>();
//				
//				configuracoes = new HashMap<String, Integer>();
//				configuracoesAnteriores = new HashMap<String, Integer>();
//				tabCopia = null;
//				
//				while(in.ready()) {
//					String linha = in.readLine();
//					String s = "";
//					for(int i = 0; i < linha.length(); i++) {
//						switch(linha.charAt(i)) {
//							case 'J' : s += Pecas.DAMA_JOGADOR; break;
//							case 'j' : s += Pecas.PEDRA_JOGADOR; break;
//							case 'C' : s += Pecas.DAMA_COMPUTADOR; break;
//							case 'c' : s += Pecas.PEDRA_COMPUTADOR; break;
//							default : s += linha.charAt(i); break;
//						}
//					}
//					if(configuracoes.containsKey(s)) {
//						int aux = configuracoes.get(s);
//						configuracoes.put(s,aux+1);
//					} else {
//						configuracoes.put(s,1);
//					}
//										
//					if(configuracoesAnteriores.containsKey(s)) {
//						int aux = configuracoesAnteriores.get(s);
//						configuracoesAnteriores.put(s,aux+1);
//					} else {
//						configuracoesAnteriores.put(s,1);
//					}
//					
//					quant++;
//				}
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}	
//			totalNegativas = quant;
//			System.out.println("Total negativas: "+totalNegativas);
//			showConfiguracoesAnteriores(DERROTA);
//			//2
//			quant = 0;
//			try {
//				//1
//				BufferedReader in = new BufferedReader(new FileReader("configuracoesvencidas.damas"));
////				conf = new HashMap<Integer, String>();
//				
//				configuracoesPositivas = new HashMap<String, Integer>();
//				tabCopia = null;
//				
//				while(in.ready()) {
//					String linha = in.readLine();
//					String s = "";
//					for(int i = 0; i < linha.length(); i++) {
//						switch(linha.charAt(i)) {
//							case 'J' : s += Pecas.DAMA_JOGADOR; break;
//							case 'j' : s += Pecas.PEDRA_JOGADOR; break;
//							case 'C' : s += Pecas.DAMA_COMPUTADOR; break;
//							case 'c' : s += Pecas.PEDRA_COMPUTADOR; break;
//							default : s += linha.charAt(i); break;
//						}
//					}
//										
//					if(configuracoesPositivas.containsKey(s)) {
//						int aux = configuracoesPositivas.get(s);
//						configuracoesPositivas.put(s,aux+1);
//					} else {
//						configuracoesPositivas.put(s,1);
//					}
//					
//					quant++;
//				}
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}	
//			totalPositivas = quant;
//			System.out.println("Total positivas: "+totalPositivas);
//			
//			showConfiguracoesAnteriores(VITORIA);
//	//		Set<String> s = configuracoes.keySet();
//	//		System.out.println("Mostrando:");
//	//		Iterator it = s.iterator();
//	//		while(it.hasNext()) {
//	//			String aux = (String)it.next();
//	//			System.out.println(aux + " " + configuracoes.get(aux));
//	//			System.out.println(aux.equals("#B#B#B#BB#B#B#B##-#B#B#B-#-#-#-##-#H#-#HH#X#H#-##-#H#H#HH#H#H#H#:#B#B#B#BB#B#B#B##-#B#B#B-#-#-#-##-#H#-#HH#-#H#-##-#H#H#HH#H#H#H#"));
//	//		}
//			IS_READY = true;
//			return quant;
//		}
//		return 0;
//	}

	public static void gravarLog() {
		String linha = "";
		System.out.println("entrou aki");
		linha += "Configuracoes: " + getTotalConfiguracoesComRepeticao() + "\t" +
				"Configuracoes sem repeticao:" + getTotalConfiguracoes()  + "\t" +
				"Average evaluation of the chromosomes using only the weights " + data.getMediaSemEquacao() + "\t" +
				"Average evaluation of the chromosomes using Equation 1 " + data.getMediaComEquacao() + "\t" +
				"Average evaluation of the selected chromosomes " + data.getMediaSelecionados() + "\t" +
				"Resultado: " + data.getResultado() + "\t" +
				"Primeiro jogador: " + (ArqConfiguracao.primeiroJogador == 1? "Usuario" : "Computador") + "\t" +
				"Dificuldade: "+GerenciadorDificuldade.getInstance().getDificuldade();
//			"";
		GerenciadorLog g = new GerenciadorLog(usuario.getNome());
		try {
			g.grava(linha, usuario.getNome(), GerenciadorLog.Tipo.DADOS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("terminou gravacao em arq");
	}

	public static void setPrimeiroJogador(int participante) {
		primeiroJogador = participante;
	}
	
	
	
}
