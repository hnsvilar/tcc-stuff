package damas.game.fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import damas.data.ArqConfiguracao;
import damas.data.GerenciadorDificuldade;
import damas.game.ag.AlgoritmoGenetico;
import damas.game.animacao.AnimacaoTransfer;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Movimentacao;
import damas.game.commom.PecaCome;
import damas.game.commom.Pecas;
import damas.game.jogador.Jogador;
import damas.gui.testes.TestandoAvaliacao;

public class AcaoComputador {

	private AlgoritmoGenetico ag;
	private boolean comeUmaPeca;
	private AnimacaoTransfer trans;
	private char ULTIMA_PECA_JOGADA = Pecas.PEDRA_COMPUTADOR;
	private int niveis;
	private static GerenciadorDificuldade gerenciadorDificuldade = null;

	public AcaoComputador(char[] tabuleiro, AnimacaoTransfer trans, int niveis) {
		ag = new AlgoritmoGenetico();
		this.trans = trans;
		comeUmaPeca = false;
		this.niveis = niveis;
		GerenciadorDificuldade.getInstance();
	}

	public static GerenciadorDificuldade getGerenciadorDificuldade() {
		return gerenciadorDificuldade;
	}

	public void setNiveis(int niveis) {
		this.niveis = niveis;
	}	
	
	public int getNiveis() {
		return niveis;
	}
	
	
	
	public No getProximaJogada(No noAtual) {
		return ag.geraNovaJogada(noAtual,niveis);
	}

//	public No getProximaJogada(No noAtual) {
////		System.out.println("\n\n");
////		System.out.println("\nVENDO PROXIMA");
//		int contador = 1;
//		int vezAtual = Jogada.COMPUTADOR;
//		ag.geraNovaPopulacao(noAtual, Jogada.COMPUTADOR);		
//		ArrayList<No> fronteira = noAtual.getFilhos();
//		vezAtual = Jogada.JOGADOR;
//		No proximoMovimento = null;
//		System.out.println("PROCURANDO NOVO PARA");
//		TestandoAvaliacao.imprimeTabuleiro(noAtual.getEstado().getTabuleiro());
//		int podeTerminarJogo = terminaJogo(noAtual.getFilhos());
//		if(podeTerminarJogo != -1) {
//			return fronteira.get(podeTerminarJogo);
//		}
//		if(niveis == 1) {
//			ArrayList<No> podemComer = new ArrayList<No>();			
//
//			proximoMovimento = fronteira.get(0);	
//
//			ag.avaliaIndividuo(proximoMovimento.getEstado(), proximoMovimento.getPai(), Jogada.COMPUTADOR);
//
//			for(No no : fronteira) {
//				if(no.getEstado().getComeu())
//					podemComer.add(no);
//
//				//verifica melhor filho, pois os filhos na fronteira sao movimentos do computador
//				ag.avaliaIndividuo(no.getEstado(), no.getPai(), Jogada.COMPUTADOR);
//				if(proximoMovimento.getEstado().getAvaliacao() < 
//						(no.getEstado()).getAvaliacao()) {
//					proximoMovimento = no;
//				}				
//			}	
//
//			if(podemComer.size() > 0)
//				proximoMovimento = podemComer.get(0);
//			for(No no:podemComer){
//				if(proximoMovimento.getEstado().getAvaliacao() < 
//						(no.getEstado()).getAvaliacao()) {
//					proximoMovimento = no;
//				}
//			}
//		} else {
//			HashMap<Integer,ArrayList<No>> conjuntoFronteiras = 
//					new HashMap<Integer, ArrayList<No>>();
//			//gera os filhos dos primeiros filhos (aka, segundo nível da arvore)
//			int [] deveExpandir = new int[fronteira.size()];
//			for(int i = 0; i < fronteira.size(); i++) {
//				if(fronteira.get(i).getEstado().getNumPecasJogador()>0) {
//					deveExpandir[i] = 1;
//					ag.geraNovaPopulacao(fronteira.get(i), vezAtual);		
//					conjuntoFronteiras.put(i, fronteira.get(i).getFilhos());
//				}else {
//					deveExpandir[i] = 0;
//					ArrayList<No> aux = new ArrayList<No>();
//					aux.add(fronteira.get(i));
//					conjuntoFronteiras.put(i, aux);
//				}
//			}
//			vezAtual = Jogada.COMPUTADOR;
//			contador++;
//
//			while(contador < niveis) {
////				System.out.println("entrou no while");
//				//para cada no naquela array list de nos, gere todos os filhos e substitua.
//				//faca isso para cada array list em conjuntoFronteiras
//				for(int i = 0; i < fronteira.size();i++) {
////					System.out.println("VZ DA FRONTEIRA "+i);
//					ArrayList<No> fronteiraAuxiliar = new ArrayList<No>();
//					if(deveExpandir[i] == 1) {
////						System.out.println("deve expandir");
//						//para cada filho do no atual, gere mais filhos
//						if(noAtual.getEstado().getNumPecasJogador()>0) {
////							System.out.println("numJogador>0");
//							for(No no : conjuntoFronteiras.get(i)) {							
//								ag.geraNovaPopulacao(no,vezAtual);
//								fronteiraAuxiliar.addAll(no.getFilhos());
//							}
//						} else {
////							System.out.println("num jogador MENOR 0");
//							fronteiraAuxiliar.add(noAtual);
//						}
//					} else {
////						System.out.println("NAO deve expandir");
//						fronteiraAuxiliar = conjuntoFronteiras.get(i);
//					}
//					conjuntoFronteiras.put(i, fronteiraAuxiliar);
//				}
//				if(vezAtual == Jogada.JOGADOR) {				
//					vezAtual = Jogada.JOGADOR;
//				} else if(vezAtual == Jogada.COMPUTADOR) {			
//					vezAtual = Jogada.JOGADOR;
//				}
//
//				contador++;
////				System.out.println("---------------");
//			}
//			
////			System.out.println("Podem comer?");
//			ArrayList<Integer> podemComer = new ArrayList<Integer>();
//			for(int i = 0 ; i < fronteira.size(); i++) {
//				if(fronteira.get(i).getEstado().getComeu()) {
//					podemComer.add(i);
////					System.out.println(i);
//				}
//			}
////			System.out.println("------------");
//			
//			//calcula a avaliacao dos individuos e a media destes para cada array 
//			//em colecaoFronteiras
//			Par[] medias;
//			if(podemComer.size() > 0)
//				medias = new Par[podemComer.size()];
//			else
//				medias = new Par[fronteira.size()];
//			int contadorMedia = 0;
//			for(int i = 0; i < fronteira.size();i++) {
//				double media = 0;
////				System.out.println(i + " -  TAMANHO "+conjuntoFronteiras.get(i).size());
//				proximoMovimento = conjuntoFronteiras.get(i).get(0);	
//				if(niveis % 2 != 0) {
//					for(No no : conjuntoFronteiras.get(i)) {
//						ag.avaliaIndividuo( (no.getEstado()), no.getPai(), 
//								Jogada.COMPUTADOR);						
//						media = media + ( no.getEstado()).getAvaliacao();
//					}
//				} else {
//					for(No no : conjuntoFronteiras.get(i)) {
//						ag.avaliaIndividuo( (no.getEstado()), no.getPai(), 
//								Jogada.JOGADOR);						
//						media = media + ( no.getEstado()).getAvaliacao();
//					}
//				}
//				media = (double)(media /conjuntoFronteiras.get(i).size());
//				if(podemComer.size() == 0){
//					medias[contadorMedia] = new Par(i, media);
//					contadorMedia++;
//				} else {
//					if(podemComer.contains(i)) {
////						System.out.println("\t add "+i);
//						medias[contadorMedia] = (new Par(i, media));
//						contadorMedia++;
//					}
//				}
//			}
////			System.out.println(vezAtual);
////			System.out.println("VEz jogador "+Jogada.JOGADOR);
////			for(int i = 0; i < medias.length; i++) {
////				System.out.println("media "+i + " "+medias[i].getMedia()+" idnice "+medias[i].getIndice());
////			}
////			System.out.println();
//			//ordena em ordem decrescente
//			quickSort(medias, 0,medias.length,(niveis%2==0? Jogada.JOGADOR: Jogada.COMPUTADOR));
////			for(int i = 0; i < medias.length; i++) {
////				System.out.println("media "+i + " "+medias[i].getMedia()+" idnice "+medias[i].getIndice());
////			}
//			proximoMovimento = null;
//			Random randomGenerator = new Random();
//			for(int i = 0; i < medias.length; i++) {
//				if(randomGenerator.nextInt(GerenciadorAtributos.MAX_RESULTADO+1)
//						> gerenciadorDificuldade.getPorcentagemAcerto()) {
//					proximoMovimento = fronteira.get(medias[i].getIndice());
//					break;
//				}
//			}
//			if(proximoMovimento == null)
//				proximoMovimento = fronteira.get(medias[medias.length-1].getIndice());
//
//		}
//		return proximoMovimento;
//		//		retorna pai
//		//		de i <-0 a (niveis-1)
//		//			n = n.getPai(0
//		//		retorne n
//		//		-------------------------------
//		//		vez_PC
//		//		gera filhos
//		//		aumenta contador
//		//		troca vez
//		//	L1	se contador <= niveis
//		//			se vez == jogador
//		//				verifica melhor filho
//		//			se vez == pc
//		//				verifica pior filho
//		//			retorna pai
//		//				de i <-0 a (niveis-1)
//		//					n = n.getPai(0
//		//				retorne n
//		//		senao
//		//			se vez == jogador
//		//				gerafilhos_jogador
//		//				ve melhor filho / poda arvore
//		//			se vez == pc
//		//				geraFilhos
//		//			aumenta contador
//		//			GOTO L1
//	}
	
	
	public PecaCome[] getAtacante() {

		return ag != null ? ag.getPeca() : null;
	}

	public char getULTIMA_PECA_JOGADA() {
		return ULTIMA_PECA_JOGADA;
	}

	public boolean completouAcao() {
		return !comeUmaPeca;
	}

	public char[] buscaAcao(char[] tabuleiro) {

		return tabuleiro;
	}

	public void showAnimacao() {
		trans.showMundo();
	}

	public int getNumeroDamasComputador(char[] tabuleiro) {
		int n = 0;
		for (int i = 0; i < tabuleiro.length; i++) {
			if (tabuleiro[i] == Pecas.DAMA_COMPUTADOR) {
				n++;

			}
		}
		return n;
	}

	public boolean isPossivelMover(char[] tabuleiro) {
		boolean possivel = false;
		for(int i = 0; i < tabuleiro.length; i++) {
			if(tabuleiro[i] == Pecas.PEDRA_COMPUTADOR || tabuleiro[i] == Pecas.DAMA_COMPUTADOR) {
				if(Movimentacao.podeMover(tabuleiro, i) || 
						Movimentacao.podeComer(tabuleiro, i, Jogada.COMPUTADOR)) {
					return true;
				}
			}
		}
		return possivel;
	}

	public void promoveDama(int destino, char[] tabuleiro) {
		for (int i = 0; i < FiniteStateMachine.LATERAL_INFERIOR.length; i++) {
			if (FiniteStateMachine.LATERAL_INFERIOR[i] == destino
					&& tabuleiro[destino] == Pecas.PEDRA_COMPUTADOR) {
				tabuleiro[destino] = Pecas.DAMA_COMPUTADOR;

			}
		}
	}
}