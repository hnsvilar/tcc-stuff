package damas.game.ag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import damas.data.ArqConfiguracao;
import damas.data.GerenciadorDificuldade;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Movimentacao;
import damas.game.commom.Pecas;
import damas.game.fsm.AcaoJogador;
import damas.game.fsm.FiniteStateMachine;
import damas.game.fsm.Par;
import damas.gui.testes.TestandoAvaliacao;

public class AlgoritmoGenetico extends Jogada {

	// populacao
	private ArrayList<Individuo> populacao;
	/*
	 * chama-se de avanço simples o ato de avançar e pôr-se aparentemente
	 *  em posição favorável em relação a um adversário para ataque em
	 *  uma outra rodada.
	 */
	private static final double AVANCO_SIMPLES = 1;
	/*
	 * identifica o movimento realizado por um aliado que resulta em 
	 * sua captura por um adversário.
	 */
	private static final double JOGADA_RESULTA_CAPTURA = -5;
	/*
	 * identifica que a permanência de um aliado em sua posição original
	 * (literalmente quando a peça representada pelo indivíduo está em 
	 * sua origem) resulta em sua captura.
	 */
	private static final double NAO_JOGADA_RESULTA_CAPTURA = 2;
	/*
	 * identifica jogada de um aliado que resulta em capturar um adversário
	 */
	private static final double CAPTURA_UMA_PECA = 3;
	
	//---------------------------------------------------------------------------
	
	public AlgoritmoGenetico() {
		populacao = new ArrayList<Individuo>();
	}
	
	public ArrayList<Individuo> getPopulacao() { return populacao; }
	public void setPopulacao(ArrayList<Individuo> populacao) {
		this.populacao = populacao;
	}
	
	public int terminaJogo(ArrayList<No> filhos) {
		int termina = -1;
		for(int i = 0; i < filhos.size(); i++) {
			AcaoJogador aj = new AcaoJogador();
//			if(!Movimentacao.podeMover(no.getEstado().getTabuleiro(), no.getEstado().getPecaMudada()) ||
//					!Movimentacao.podeComer(no.getEstado().getTabuleiro(), no.getEstado().getPecaMudada()))
//			System.out.println("i "+i + " quant "+filhos.get(i).getEstado().getNumPecasJogador());
//			TestandoAvaliacao.imprimeTabuleiro(filhos.get(i).getEstado().getTabuleiro());
			if(filhos.get(i).getEstado().getNumPecasJogador() == 0)
				return i;
			if(!aj.isPossivelMover(filhos.get(i).getEstado().getTabuleiro())) {
					termina = i;
					break;
			}
		}
		return termina;
	}
	
	public No geraNovaJogada(No noAtual, int niveis) {
//		System.out.println("\n\n");
//		System.out.println("\nVENDO PROXIMA");
		int contador = 1;
		int vezAtual = Jogada.COMPUTADOR;
		geraNovaPopulacao(noAtual, Jogada.COMPUTADOR);		
		ArrayList<No> fronteira = noAtual.getFilhos();
		vezAtual = Jogada.JOGADOR;
		No proximoMovimento = null;
		if(noAtual.getEstado().getNumPecasJogador() <2) {
			System.out.println("tabuleiro");
			TestandoAvaliacao.imprimeTabuleiro(noAtual.getEstado().getTabuleiro());
		}
//		System.out.println("PROCURANDO NOVO PARA");
//		TestandoAvaliacao.imprimeTabuleiro(noAtual.getEstado().getTabuleiro());
		int podeTerminarJogo = terminaJogo(fronteira);
		if(podeTerminarJogo != -1) {
			avaliaIndividuo(fronteira.get(podeTerminarJogo).getEstado(), noAtual, Jogada.COMPUTADOR);
			ArqConfiguracao.getData().somaSelecionados(fronteira.get(podeTerminarJogo).getEstado().getAvaliacao());
			return fronteira.get(podeTerminarJogo);
		}
		if(niveis == 1) {
			ArrayList<No> podemComer = new ArrayList<No>();			

//			proximoMovimento = fronteira.get(0);	
//
//			avaliaIndividuo(proximoMovimento.getEstado(), proximoMovimento.getPai(), Jogada.COMPUTADOR);

			for(No no : fronteira) {
				if(no.getEstado().getComeu()) {
					podemComer.add(no);
				}
				//verifica melhor filho, pois os filhos na fronteira sao movimentos do computador
				avaliaIndividuo(no.getEstado(), no.getPai(), Jogada.COMPUTADOR);
//				if(proximoMovimento.getEstado().getAvaliacao() < 
//						(no.getEstado()).getAvaliacao()) {
//					proximoMovimento = no;
//				}				
			}	
			Par[] medias;
			if(podemComer.size() > 0) {
				medias = new Par[podemComer.size()];	
				int contadorMedia = 0;
				for(int i = 0; i < fronteira.size(); i++) {
					if(podemComer.contains(fronteira.get(i))) {
						medias[contadorMedia] = 
								new Par(i, podemComer.get(i).getEstado().getAvaliacao());
						contadorMedia++;						
					}
				}			
			} else {
				medias = new Par[fronteira.size()];
				for(int i = 0; i < medias.length; i++) {
					medias[i] = new Par(i, podemComer.get(i).getEstado().getAvaliacao());
				}
			}
			
			quickSort(medias, 0,medias.length, Jogada.COMPUTADOR);
			proximoMovimento = null;
			Random randomGenerator = new Random();
			for(int i = 0; i < medias.length; i++) {
				if(randomGenerator.nextInt(GerenciadorDificuldade.MAX_RESULTADO+1)
						< GerenciadorDificuldade.getInstance().getPorcentagemAcerto() 
						 || i == (medias.length-1)) {
					proximoMovimento = fronteira.get(medias[i].getIndice());
					break;
				}
			}
			
		} else {
			HashMap<Integer,ArrayList<No>> conjuntoFronteiras = 
					new HashMap<Integer, ArrayList<No>>();
			//gera os filhos dos primeiros filhos (aka, segundo nível da arvore)
			int [] deveExpandir = new int[fronteira.size()];
			for(int i = 0; i < fronteira.size(); i++) {
				AcaoJogador ajAux = new AcaoJogador();
				if(fronteira.get(i).getEstado().getNumPecasJogador()>0 || 
						!ajAux.isPossivelMover(fronteira.get(i).getEstado().getTabuleiro())) {
					deveExpandir[i] = 1;
					geraNovaPopulacao(fronteira.get(i), vezAtual);		
					conjuntoFronteiras.put(i, fronteira.get(i).getFilhos());
				} else {
					deveExpandir[i] = 0;
					ArrayList<No> aux = new ArrayList<No>();
					aux.add(fronteira.get(i));
					conjuntoFronteiras.put(i, aux);
				}
			}
			vezAtual = Jogada.COMPUTADOR;
			contador++;

			while(contador < niveis) {
//				System.out.println("entrou no while");
				//para cada no naquela array list de nos, gere todos os filhos e substitua.
				//faca isso para cada array list em conjuntoFronteiras
				for(int i = 0; i < fronteira.size();i++) {
//					System.out.println("VZ DA FRONTEIRA "+i);
					ArrayList<No> fronteiraAuxiliar = new ArrayList<No>();
					if(deveExpandir[i] == 1) {
//						System.out.println("deve expandir");
						//para cada filho do no atual, gere mais filhos
						if(noAtual.getEstado().getNumPecasJogador()>0) {
//							System.out.println("numJogador>0");
							for(No no : conjuntoFronteiras.get(i)) {							
								geraNovaPopulacao(no,vezAtual);
								fronteiraAuxiliar.addAll(no.getFilhos());
							}
						} else {
//							System.out.println("num jogador MENOR 0");
							fronteiraAuxiliar.add(noAtual);
						}
					} else {
//						System.out.println("NAO deve expandir");
						fronteiraAuxiliar = conjuntoFronteiras.get(i);
					}
					conjuntoFronteiras.put(i, fronteiraAuxiliar);
				}
				if(vezAtual == Jogada.JOGADOR) {				
					vezAtual = Jogada.JOGADOR;
				} else if(vezAtual == Jogada.COMPUTADOR) {			
					vezAtual = Jogada.JOGADOR;
				}

				contador++;
//				System.out.println("---------------");
			}
			
			System.out.println("Podem comer?");
			ArrayList<Integer> podemComer = new ArrayList<Integer>();
			for(int i = 0 ; i < fronteira.size(); i++) {
				if(fronteira.get(i).getEstado().getComeu()) {
					podemComer.add(i);
					System.out.println(i);
				}
			}
			System.out.println("------------");
			
			//calcula a avaliacao dos individuos e a media destes para cada array 
			//em colecaoFronteiras
			Par[] medias;
			if(podemComer.size() > 0)
				medias = new Par[podemComer.size()];
			else
				medias = new Par[fronteira.size()];
			int contadorMedia = 0;
			for(int i = 0; i < fronteira.size();i++) {
				double media = 0;
//				System.out.println(i + " -  TAMANHO "+conjuntoFronteiras.get(i).size());
				proximoMovimento = conjuntoFronteiras.get(i).get(0);	
				if(niveis % 2 != 0) {
					for(No no : conjuntoFronteiras.get(i)) {
						avaliaIndividuo( (no.getEstado()), no.getPai(), 
								Jogada.COMPUTADOR);						
						media = media + ( no.getEstado()).getAvaliacao();
					}
				} else {
					for(No no : conjuntoFronteiras.get(i)) {
						avaliaIndividuo( (no.getEstado()), no.getPai(), 
								Jogada.JOGADOR);						
						media = media + ( no.getEstado()).getAvaliacao();
					}
				}
				media = (double)(media /conjuntoFronteiras.get(i).size());
				if(podemComer.size() == 0){
					medias[contadorMedia] = new Par(i, media);
					contadorMedia++;
				} else {
					if(podemComer.contains(i)) {
						System.out.println("\t add "+i);
						medias[contadorMedia] = (new Par(i, media));
						contadorMedia++;
					}
				}
			}
//			System.out.println(vezAtual);
//			System.out.println("VEz jogador "+Jogada.JOGADOR);
//			for(int i = 0; i < medias.length; i++) {
//				System.out.println("media "+i + " "+medias[i].getMedia()+" idnice "+medias[i].getIndice());
//			}
//			System.out.println();
			//ordena em ordem decrescente
			quickSort(medias, 0,medias.length,(niveis%2==0? Jogada.JOGADOR: Jogada.COMPUTADOR));
//			for(int i = 0; i < medias.length; i++) {
//				System.out.println("media "+i + " "+medias[i].getMedia()+" idnice "+medias[i].getIndice());
//			}
			proximoMovimento = null;
			Random randomGenerator = new Random();
			for(int i = 0; i < medias.length; i++) {
				if(randomGenerator.nextInt(GerenciadorDificuldade.MAX_RESULTADO+1)
						< GerenciadorDificuldade.getInstance().getPorcentagemAcerto() 
						 || i == (medias.length-1)) {
					proximoMovimento = fronteira.get(medias[i].getIndice());
					break;
				}
			}
			if(proximoMovimento == null)
				proximoMovimento = fronteira.get(medias[medias.length-1].getIndice());

		}
		
		avaliaIndividuo(proximoMovimento.getEstado(), noAtual, Jogada.COMPUTADOR);
		ArqConfiguracao.getData().somaSelecionados(proximoMovimento.getEstado().getAvaliacao());
		return proximoMovimento;
		//		retorna pai
		//		de i <-0 a (niveis-1)
		//			n = n.getPai(0
		//		retorne n
		//		-------------------------------
		//		vez_PC
		//		gera filhos
		//		aumenta contador
		//		troca vez
		//	L1	se contador <= niveis
		//			se vez == jogador
		//				verifica melhor filho
		//			se vez == pc
		//				verifica pior filho
		//			retorna pai
		//				de i <-0 a (niveis-1)
		//					n = n.getPai(0
		//				retorne n
		//		senao
		//			se vez == jogador
		//				gerafilhos_jogador
		//				ve melhor filho / poda arvore
		//			se vez == pc
		//				geraFilhos
		//			aumenta contador
		//			GOTO L1
	}
	
	public void quickSort(Par[] v, int ini, int fim, int tipo) {
		int meio;

		if(ini<fim){
			meio = partition(v,ini,fim,tipo);
			quickSort(v,ini,meio,tipo);
			quickSort(v,meio+1,fim,tipo);
		}
	}

	public static int partition(Par[] v, int ini, int fim, int tipo){
		Par pivo;
		int topo,i;
		pivo = v[ini];
		topo = ini;

		for(i=ini+1;i<fim;i++){
			if(tipo == Jogada.JOGADOR) {
				if(v[i].getMedia() < pivo.getMedia()){
					v[topo]=v[i];
					v[i]=v[topo+1];
					topo++;
				}
			} else {
				if(v[i].getMedia() > pivo.getMedia()){
					v[topo]=v[i];
					v[i]=v[topo+1];
					topo++;
				}
			}
		}
		v[topo]=pivo;
		return topo;
	}

	
	public void geraNovaPopulacao(No pai, int vez) {
		populacao = new ArrayList<Individuo>();
		populacao.addAll(pai.getEstado().getSucessores(vez));
		mutaPopulacao(vez);
		
		if(vez == Jogada.COMPUTADOR) {
			for(Individuo i : populacao) {
				No novoFilho = new No(pai, i);
				pai.addFilho(novoFilho);
			}
		} else if(vez == Jogada.JOGADOR) {
			Individuo melhor = null;
			if(populacao.size() > 0) {
				melhor = populacao.get(0);
				avaliaIndividuo(melhor,pai,Jogada.JOGADOR);
			}
			for(Individuo i : populacao) {
				avaliaIndividuo(i, pai, Jogada.JOGADOR);
				if(melhor.getAvaliacao() < i.getAvaliacao()) {
					melhor = i;
				}
				
			}
			No novoFilho = new No(pai, melhor);
			pai.addFilho(novoFilho);
		}
	}
	
	public static void imprimeTabuleiro(Individuo tab) {
		int aux = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				System.out.print(tab.getTabuleiro()[aux] + " ");
				aux++;
			}
			System.out.println();
		}
	}
	
	public ArrayList<Integer> getPosicoesPossiveis(Individuo i, ArrayList<Integer> pecasComidas, int vez) {
		ArrayList<Integer> posicoesPossiveis = new ArrayList<Integer>();		
		if(pecasComidas == null)
			posicoesPossiveis = Movimentacao.getPosicoesPossiveisComer(i.getTabuleiro(),i.getPecaMudada(), vez);
		else {
			posicoesPossiveis = 
					Movimentacao.getPosicoesPossiveisComer(i.getTabuleiro(),i.getPecaMudada(),
							pecasComidas, vez);
		}
		if(posicoesPossiveis.size() > 0) {
			i.setComeu(true);
		} else {
			i.setComeu(false);
//			System.out.println("Indo pegar movimentos possiveis "+i.getPecaMudada());
			posicoesPossiveis = Movimentacao.getPosicoesPossiveisMover(i.getTabuleiro(), 
					i.getPecaMudada(), vez);
		}
//		System.out.println("COnsegui: "+posicoesPossiveis.size());
		return posicoesPossiveis;
	}
	
	public void mutaPopulacao(int vez) {
		for(Individuo indie : populacao) {
			ArrayList<Integer> posicoesPossiveis = getPosicoesPossiveis(indie, null, vez);
			//Se comeu
			if(indie.getComeu()) {
				/**
				 * Detalhes se comeu:
				 * 1- Não limpa o tabuleiro até terminar
				 * 2- Não come a mesma peça
				 */					
				ArrayList<Integer> pecasComidas = new ArrayList<Integer>();
				ArrayList<Integer> caminhos = new ArrayList<Integer>();
				int auxiliarInicial = indie.getPecaMudada(); //posicao inicial do tabuleiro
				do {
					imprimeTabuleiro(indie);
					int random;
					Random randomGenerator = new Random();
					random = randomGenerator.nextInt(posicoesPossiveis.size());
					//posicoesPossiveis.get(random) <- peca que foi comida
					char tab[] = indie.getTabuleiro();
					int futuraPosicao = Movimentacao.descobrePosicaoAposComer(indie.getPecaMudada(),
							posicoesPossiveis.get(random), indie.getTabuleiro(), vez);
					
					tab[futuraPosicao] = tab[indie.getPecaMudada()];
					tab[indie.getPecaMudada()] = Pecas.VAZIO;
					pecasComidas.add(posicoesPossiveis.get(random));
					indie.setTabuleiro(tab);
					caminhos.add(indie.getPecaMudada());
					indie.setPecaMudada(futuraPosicao);
					posicoesPossiveis = getPosicoesPossiveis(indie,pecasComidas, vez);					
				} while(indie.getComeu());
				indie.setComeu(true);	
				caminhos.add(indie.getPecaMudada());
				indie.setPecaMudada(auxiliarInicial);
				indie.setPecasComidas(pecasComidas);
				indie.setPosicoesIntermediarias(caminhos);
				if(vez == Jogada.COMPUTADOR)
					indie.setNumPecasJogador(indie.getNumPecasJogador()-pecasComidas.size());
				else if(vez == Jogada.JOGADOR) 
					indie.setNumPecasJogador(indie.getNumPecasComputador()-pecasComidas.size());
				for(Integer i : pecasComidas) {
					indie.getTabuleiro()[i] = Pecas.VAZIO;
				}
			} else {
				//Se não comeu
				int random;
				Random randomGenerator = new Random();
				random = randomGenerator.nextInt(posicoesPossiveis.size());
				char tab[] = indie.getTabuleiro();
				char aux = tab[indie.getPecaMudada()];
				tab[indie.getPecaMudada()] = Pecas.VAZIO;
				tab[posicoesPossiveis.get(random)] = aux;
				indie.setTabuleiro(tab);
				ArrayList<Integer> caminhos = new ArrayList<Integer>();
				caminhos.add(indie.getPecaMudada());
				caminhos.add(posicoesPossiveis.get(random));
				indie.setPosicoesIntermediarias(caminhos);
			}				
		}
	}
	
	// ######################################################################################################
		
	/**
	 * Funcao que calcula a avaliacao do individuo
	 * @param individuo
	 */
	public void avaliaIndividuo(Individuo individuo, No pai, int vez) {
		/**
		 * SITUAÇÕES POSSIVEIS
		 * 
		 * Mover para junto de peca adversaria mantendo espaço livre nas imediações-- 
		 * Mover para frente de peca aliada ++ 
		 * Mover para quina e nao poder mover -- 
		 * Mover para frente ++ 
		 * impedir que peca seja comida++ 
		 * Comer peca ++;
		 * 
		 */
		
		char[] tabuleiro = pai.getEstado().getTabuleiro();
		double avaliacao = 0.0;

		avaliacao += this.avaliaPecaOrigemSeraComida(individuo, vez, tabuleiro.clone());
	
		avaliacao += this.avaliaDiagonalOrigem(individuo, vez, tabuleiro.clone());

		avaliacao += this.avaliaDiagonalDestino(individuo, vez, tabuleiro.clone());
		
		ArqConfiguracao.getData().somaSemEquacao(avaliacao);
		
		if(ArqConfiguracao.IS_READY == true) {
			if(vez == Jogada.COMPUTADOR) {
				avaliacao -= this.avaliaPesoNovoNegativo(individuo);
				avaliacao += this.avaliaPesoNovoPositivo(individuo);
			} else if(vez == Jogada.JOGADOR) {
				avaliacao += this.avaliaPesoNovoNegativo(individuo);
				avaliacao -= this.avaliaPesoNovoPositivo(individuo);
			}
		}
		
		ArqConfiguracao.getData().somaComEquacao(avaliacao);
		individuo.setAvaliacao(avaliacao);
	}
	
	/***************************************************************************
	 * Funcionalidade analoga para peca ou dama. Se houver peca atacante nas
	 * proximidades incentiva o movimento.
	 * 
	 * FUNCIONANDO =D
	 * 
	 * Este método avalia se a peça do computador representada por este indivíduo corre risco de
	 * ser capturada caso não seja movida do seu local de origem.
	 **************************************************************************/
	public double avaliaPecaOrigemSeraComida(Individuo individuo, int vez, char[] tabuleiroAnterior) {
		
		HashMap<Integer, Character> pecas = Movimentacao.getPecas(vez);
		double avaliacao = 0.0;
		char[] tab = tabuleiroAnterior;
		
		//Pega os destinos possiveis
		ArrayList<Integer> destinosPossiveis = getDestinosPossiveis(tabuleiroAnterior, individuo.getPecaMudada());
			
		for (int i = 0; i < destinosPossiveis.size() && destinosPossiveis.get(i) != 0; i++) {
			int posicao = destinosPossiveis.get(i);
			int maior = Math.max(posicao, individuo.getPecaMudada());
			int menor = Math.min(posicao, individuo.getPecaMudada());
			int inc = (maior - menor) % 7 == 0 ? 7 : 9;
			// nao eh interessante avaliar o tipo das pecas, apenas dos
			// adversarios

			if (tab[posicao] == pecas.get(Movimentacao.pedraAdversario)) {
				// se a peca esta adjacente
				if (maior - menor == inc) {
					// obtem o valor da casa "atras" da peca.
					int proximaCasa = (posicao == maior) ? individuo.getPecaMudada()
							- inc : individuo.getPecaMudada() + inc;
					if (this.isDominioTabuleiro(proximaCasa) && tab[proximaCasa] == Pecas.VAZIO) {
						// caracteriza cenario onde a peca eh comida.
						// peca eh incentivada a ser movida
						avaliacao += NAO_JOGADA_RESULTA_CAPTURA;
						avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, individuo.getPecaMudada(),
								posicao, tabuleiroAnterior.clone(), pecas, vez, NAO_JOGADA_RESULTA_CAPTURA);
						
					}
				}
			} else if (tab[posicao] == pecas.get(Movimentacao.damaAdversario)
					&& this.isDamaLivre(individuo.getPecaMudada(), posicao, inc, tab)) {
				avaliacao += NAO_JOGADA_RESULTA_CAPTURA;
				// obtem o valor da casa "atras" da peca.
				avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, individuo.getPecaMudada(),
						posicao, tabuleiroAnterior.clone(), pecas, vez, NAO_JOGADA_RESULTA_CAPTURA);
			}

		}
		return avaliacao;
	}
	
	/*****************************************************************************
	 * Avalia o cenario deixado pela peca a mover-se. Nao eh necessario avaliar
	 * o tipo da peca.
	 ****************************************************************************/
	public double avaliaDiagonalOrigem(Individuo individuo, int vez, char[] tabuleiroAntes) {
		char[] tab = individuo.getTabuleiro().clone();
		double avaliacao = 0.0;
		HashMap<Integer, Character> pecas = Movimentacao.getPecas(vez);
				
		// avalia a configuração da diagonal deixada.
		// Ex.: se moveu-se verticalmente da esq para dir, avalia a diagonal
		// dir esq da origem.
		int destino = individuo.getPosicoesIntermediarias().get(individuo.getPosicoesIntermediarias().size()-1);
		int origem = individuo.getPecaMudada(); 
		int maior = Math.max(destino, origem);
		int menor = Math.min(destino, origem);

		// determina a qual diagonal pertence o destino e atribui o inc a
		// diagonal oposta.
		int inc = ((maior - menor) % 7 == 0) ? 9 : 7;
		
		// avalia o que ocorre com aliados proximos a peca se ela for movida.
		ArrayList<Integer> destinosPossiveis = getDestinosPossiveis(tab, individuo.getPecaMudada());
		
		for (int i = 0; i < destinosPossiveis.size(); i++) {
			
			int quadrado = (destino != destinosPossiveis.get(i)) ? destinosPossiveis.get(i) : origem;			
			maior = Math.max(quadrado, origem);
			menor = Math.min(quadrado, origem);
			
			inc = ((maior - menor) % 9 == 0) ? 9 : 7;
			// avalia apenas akeles da diagonal oposta ao movimento da peca.
			// mesma digonal sera examinada apenas em avaliaDiagonalDestino
			// seria interessante avaliar apenas peças aliadas deixadas
			// em situaçoes desfavoraveis.

			// avaliar separadamente individuos antes e depois da peca.
			if (quadrado + inc == individuo.getPecaMudada()) {
				// mover uma peca resulta em um espaco vazio.
				// avalia se a peca movida era vizinha d uma aliada
				// e se esta esta eh vizinha de um adversario.
				// o que gera uma peca propria a ser comida.
				if (isDominioTabuleiro(quadrado - inc)
						&& tab[quadrado - inc] == pecas.get(Movimentacao.pedraAdversario)
						&& (tab[quadrado] == pecas.get(Movimentacao.pedraComputador) ||
						tab[quadrado] == pecas.get(Movimentacao.damaComputador))) {
					avaliacao += JOGADA_RESULTA_CAPTURA;
					avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, quadrado, quadrado - inc,
							tab.clone(), pecas, vez, JOGADA_RESULTA_CAPTURA);
				} else if (isDominioTabuleiro(quadrado - inc)
						&& tab[quadrado - inc] == pecas.get(Movimentacao.damaAdversario)
						&& (tab[quadrado] == pecas.get(Movimentacao.pedraComputador) ||
						tab[quadrado] == pecas.get(Movimentacao.damaComputador))
						&& isDamaLivre(quadrado, quadrado - inc, inc, tab)) {
					avaliacao += JOGADA_RESULTA_CAPTURA;
					avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, quadrado, quadrado - inc,
							tab.clone(), pecas, vez, JOGADA_RESULTA_CAPTURA);

				}
			} else if (quadrado - inc == individuo.getPecaMudada()) {
				// mover uma peca resulta em um espaco vazio.
				// avalia se a peca movida era vizinha d uma aliada
				// e se esta esta eh vizinha de um adversario.
				// o que gera uma peca propria a ser comida.
				if (isDominioTabuleiro(quadrado + inc)
						&& tab[quadrado + inc] == pecas.get(Movimentacao.pedraAdversario)
						&& (tab[quadrado] == pecas.get(Movimentacao.pedraComputador) ||
						tab[quadrado] == pecas.get(Movimentacao.damaComputador))) {
					
					avaliacao += JOGADA_RESULTA_CAPTURA;
					avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, quadrado, quadrado - inc,
							individuo.getTabuleiro().clone(), pecas, vez, JOGADA_RESULTA_CAPTURA);
				} else if (isDominioTabuleiro(quadrado + inc)
						&& tab[quadrado + inc] == pecas.get(Movimentacao.damaAdversario)
						&& this.isDamaLivre(quadrado, quadrado + inc, inc, tab)) {
					avaliacao += JOGADA_RESULTA_CAPTURA;
					avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, quadrado, quadrado - inc,
							individuo.getTabuleiro().clone(), pecas, vez, JOGADA_RESULTA_CAPTURA);
				}
			}
		}
		return avaliacao;
	}
	

	/*****************************************************************************
	 * Aqui peca e dama do computador devem ser avalaidas separadamente.
	 ****************************************************************************/
	public double avaliaDiagonalDestino(Individuo individuo, int vez, char[] tabuleiroAntes) {
		
		double avaliacao = 0.0;
		int destino = individuo.getPosicoesIntermediarias().get(individuo.getPosicoesIntermediarias().size() - 1);
		char[] tab = individuo.getTabuleiro();

		// traca as diagonais referentes a posicao da peca a partir de
		// seu destino
		ArrayList<Integer> diagonais = getDestinosPossiveis(individuo.getTabuleiro(), destino);

		//se não comeu
		if (!individuo.getComeu()) {
			int[] verificados = new int[diagonais.size()];
			int auxiliar = 0;
			//verifica todas as posicoes na diagonal
			for (int i = 0; i < diagonais.size(); i++) {
				// o quadrado atual sendo avaliado.
				int quadrado = diagonais.get(i);
				avaliacao += this.avaliaJogadaSemCapturaAdv(individuo,
						quadrado, destino, vez, tab.clone(), tabuleiroAntes.clone(),verificados);
				verificados[auxiliar] = diagonais.get(i);
				auxiliar++;
			}
		} else {			
			avaliacao += this.avaliaJogadaComCapturaAdv(individuo, individuo.getPecaMudada(), destino,
					individuo.getTabuleiro().clone(), tabuleiroAntes.clone(), vez);
		}

		return avaliacao;
	}
	
	private double avaliaJogadaComCapturaAdv(Individuo individuo, int origemAliado, int destinoAliado, 
			char[] tabuleiro, char[] tabuleiroAntes, int vez) {
		HashMap<Integer, Character> pecas = Movimentacao.getPecas(vez);
		
		double avaliacao = 0.0;
		
		//Verifica quantas pecas adversarias comeu
		avaliacao += this.avaliaQuantasPecasAdversariasCapturou(individuo, destinoAliado, 
				individuo.getTabuleiro().clone(), vez);
		
		int maior = Math.max(origemAliado, destinoAliado);
		int menor = Math.min(origemAliado, destinoAliado);

		int inc = maior - menor % 7 == 0 ? 7 : 9;

		//Verifica se a peca pode ser comida onde está agora
		// traca as diagonais referentes a posicao da peca a partir de
		// seu destino
		ArrayList<Integer> destinosPossiveis = getDestinosPossiveis(individuo.getTabuleiro(), destinoAliado);
		
		for (int i = 0; i < destinosPossiveis.size(); i++) {
			int quadrado = destinosPossiveis.get(i);
			// verifica se o sentido do movimento nao mudou.
			maior = Math.max(destinoAliado, quadrado);
			menor = Math.min(destinoAliado, quadrado);

			inc = ((maior - menor) % 7 == 0) ? 7 : 9;

			if (tabuleiro[destinoAliado] == pecas.get(Movimentacao.pedraComputador) /*&& quadrado != inicioAdv*/) {
				if (tabuleiro[quadrado] == pecas.get(Movimentacao.pedraAdversario)
						|| tabuleiro[quadrado] == pecas.get(Movimentacao.damaAdversario)) {
					if (maior - menor == inc && tabuleiro[quadrado] == pecas.get(Movimentacao.pedraAdversario)) {
						// casa atras do destinos
						int casaAdjacente = destinoAliado == maior ? destinoAliado + inc : destinoAliado - inc;
						
						if (this.isDominioTabuleiro(casaAdjacente)) {
							if (tabuleiro[casaAdjacente] == Pecas.VAZIO) {
								avaliacao += JOGADA_RESULTA_CAPTURA;
								avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, destinoAliado, quadrado, 
										tabuleiro.clone(), pecas, vez, -CAPTURA_UMA_PECA);
							}
						}
					}
				} else if (tabuleiro[quadrado] == pecas.get(Movimentacao.damaAdversario)) {
					if (this.isDamaLivre(destinoAliado, quadrado, inc, tabuleiro)) {
						// casa atras do destinos
						int casaAdjacente = destinoAliado == maior ? destinoAliado + inc : destinoAliado - inc;

						if (tabuleiro[casaAdjacente] == Pecas.VAZIO) {
							avaliacao += JOGADA_RESULTA_CAPTURA;
							avaliacao +=  avaliaQuantosAliadosSaoPerdidos(individuo, destinoAliado, quadrado, 
									tabuleiro.clone(), pecas, vez, -CAPTURA_UMA_PECA);
						}
					}
				} 
			} else if (tabuleiro[destinoAliado] == pecas.get(Movimentacao.damaComputador)) {
					if (tabuleiro[quadrado] == pecas.get(Movimentacao.pedraAdversario) && maior - menor == inc) {
						// casa atras do destinos
						int casaAdjacente = destinoAliado == maior ? destinoAliado + inc : destinoAliado - inc;
						
						if (this.isDominioTabuleiro(casaAdjacente) && tabuleiro[casaAdjacente] == Pecas.VAZIO) {
							avaliacao += JOGADA_RESULTA_CAPTURA;
							avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, destinoAliado,
									quadrado, tabuleiro.clone(), pecas, vez, -CAPTURA_UMA_PECA);
						}
					} else if (tabuleiro[quadrado] == pecas.get(Movimentacao.damaAdversario)) {
						if (this.isDamaLivre(destinoAliado, quadrado, inc, tabuleiro)) {
							int casaAdjacente = destinoAliado == maior ? destinoAliado + inc : destinoAliado - inc;
							
							if (tabuleiro[casaAdjacente] == Pecas.VAZIO) {
								avaliacao += JOGADA_RESULTA_CAPTURA;
								avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, destinoAliado,
										quadrado, tabuleiro.clone(), pecas, vez, -CAPTURA_UMA_PECA);
							}
						}
					}
				}
			}

		return avaliacao;
	}

	public double avaliaPesoNovoNegativo(Individuo individuo) {		
		double peso = ArqConfiguracao.getQuantOcorrenciasNegativas(individuo.getTabuleiro());
		double total = ArqConfiguracao.getTotalConfiguracoes();
		double resultado;
		if(total == 0)
			resultado = 0;
		else 
			resultado = peso * 100 / total;		
		return resultado;
	}	
	
	public double avaliaPesoNovoPositivo(Individuo individuo) {		
		double peso = ArqConfiguracao.getQuantOcorrenciasPositivas(individuo.getTabuleiro());
		double total = ArqConfiguracao.getTotalConfiguracoes();
		double resultado;
		if(total == 0)
			resultado = 0;
		else 
			resultado = peso * 100 / total;
		return resultado;
	}
	
	/**
	 * Conta quantos aliados são perdidos quando um adversario em (adversario) comeca a captura pela peca em (posicao)
	 * @return
	 */
	public double avaliaQuantosAliadosSaoPerdidos(Individuo individuo, int posicao, int adversario, 
			char[] tabuleiro, HashMap<Integer, Character> pecas, int vez, double naoJogadaResultaCaptura) {
		double avaliacao = 0.0;
		ArrayList<Integer> aliadosJaCapturados = new ArrayList<Integer>();			
		
		int maior = Math.max(posicao, adversario);
		int menor = Math.min(posicao, adversario);
		int inc = ((maior-menor) % 7 == 0) ? 7 : 9;
		int posAdvAtual = adversario;
		char pecaAdv = tabuleiro[adversario];
		int casaAdjacente = posicao == menor ? posicao - inc : posicao + inc;
		
		if(isDominioTabuleiro(casaAdjacente) && tabuleiro[casaAdjacente] == Pecas.VAZIO) {
			avaliacao += naoJogadaResultaCaptura;
			tabuleiro[posAdvAtual] = Pecas.VAZIO;
			posAdvAtual = casaAdjacente;
			tabuleiro[casaAdjacente] = pecaAdv;
			aliadosJaCapturados.add(posicao);	
					
			ArrayList<Integer> posicoesPossiveisComer = 
					Movimentacao.getPosicoesPossiveisComer(tabuleiro, posAdvAtual, aliadosJaCapturados,
							vez);
			while(posicoesPossiveisComer.size() > 0) {
				Random randomGenerator = new Random();
				int random = randomGenerator.nextInt(posicoesPossiveisComer.size());
				int destino = posicoesPossiveisComer.get(random);
				aliadosJaCapturados.add(destino);
				tabuleiro[posAdvAtual] = pecaAdv;
				int aux = posAdvAtual;
				posAdvAtual = Movimentacao.descobrePosicaoAposComer(posAdvAtual, destino, tabuleiro.clone(),
						vez, aliadosJaCapturados);
				tabuleiro[posAdvAtual] = pecaAdv;
				tabuleiro[aux] = Pecas.VAZIO;
				
				avaliacao += naoJogadaResultaCaptura;
				
				posicoesPossiveisComer = Movimentacao.getPosicoesPossiveisComer(tabuleiro, 
						posAdvAtual, aliadosJaCapturados, vez);
			}
		}
		return avaliacao;
	}	
	
	/**
	 * Este método irá então determinar quantos adversários, 
	 * além daquele que originou a chamada do método, é possível capturar
	 */
	private double avaliaQuantasPecasAdversariasCapturou(Individuo individuo, int destino, char[] tabuleiro, int vez) {		
		double avaliacao = 0.0;
		
		//para cada peca capturada, soma um.
		for(int i = 0; i < individuo.getPecasComidas().size(); i++) {
			avaliacao += AlgoritmoGenetico.CAPTURA_UMA_PECA;
		}
		
		//verifica se ao final a peca pode ser comida.
				
		return avaliacao;
	}
		
	/**
	 * Continua a avaliacao caso não tenha ocorrido captura de peca adversaria
	 */
	private double avaliaJogadaSemCapturaAdv(Individuo individuo, int quadrado,
			int destino, int vez, char[] tab, char[] tabuleiroAntes, int[] jaAnalisados) {
		
		HashMap<Integer, Character> pecas = Movimentacao.getPecas(vez);
		
		double avaliacao = 0.0;

		int maior = Math.max(destino, quadrado);
		int menor = Math.min(destino, quadrado);

		// determina o incremento do movimento em relacao a peca
		int inc = ((maior - menor) % 7 == 0) ? 7 : 9;

		//Verifica se é uma dama
		if (tab[quadrado] == pecas.get(Movimentacao.damaAdversario)) {
			//se existe uma dama ameacando a peca atualmente
			if (this.isDamaLivre(destino, quadrado, inc, tab)) {

				avaliacao += JOGADA_RESULTA_CAPTURA;
				//conta quantas pecas serao perdidas. Quadrado = onde a dama está, destino = onde o meu está
				avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, destino, quadrado, tab.clone(), 
						pecas, vez, JOGADA_RESULTA_CAPTURA);
			} else {
				// se a dama nao pode capturar esta peca.
				avaliacao += AVANCO_SIMPLES;
			}
		} else if (tab[quadrado] == pecas.get(Movimentacao.pedraAdversario)) {
			// se no quadrado tem uma pedra do adversario
			if (maior - menor == inc) { // se eh adjacente, tem perigo de comer.

				// obtem o valor da casa "atras" da peca.
				int proximaCasa = (quadrado == maior) ? destino - inc : destino
						+ inc;
				if(this.isDominioTabuleiro(proximaCasa)&&tab[proximaCasa] == Pecas.VAZIO) {
					avaliacao += JOGADA_RESULTA_CAPTURA;
					//conta quantas pecas serao perdidas. Quadrado = onde a dama está, destino = onde o meu está
					avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, destino, quadrado, tab.clone(), 
							pecas, vez, -CAPTURA_UMA_PECA);
				} else {
					avaliacao += AVANCO_SIMPLES;
				}
			}
		} else if (tab[quadrado] == pecas.get(Movimentacao.pedraComputador)
				|| tab[quadrado] == pecas.get(Movimentacao.damaComputador)) {
			if (maior - menor == inc) {// se eh adjacente
				// obtem o valor da casa "a frente" da peca.
				int casaAdjacente = (quadrado == maior) ? quadrado + inc
						: quadrado - inc;
				/**
				 * Se na minha frente ha uma peca e na frente dela um adversario
				 */
				if (isDominioTabuleiro(casaAdjacente) && (tab[casaAdjacente] == pecas.get(Movimentacao.pedraAdversario) || 
						tab[casaAdjacente] == pecas.get(Movimentacao.damaAdversario))) {
						avaliacao += NAO_JOGADA_RESULTA_CAPTURA;
						avaliacao -= avaliaQuantosAliadosSaoPerdidos(individuo, quadrado, casaAdjacente, 
								tabuleiroAntes.clone(), pecas, vez, NAO_JOGADA_RESULTA_CAPTURA);
				}
			}
			
		} else if (tab[quadrado] == Pecas.VAZIO) {
			if (maior - menor == inc) {
				// obtem o valor da casa "atras" da peca.
				int proximaCasa = (quadrado == maior) ? destino - inc : destino
						+ inc;
				
				if (isDominioTabuleiro(proximaCasa) && tab[proximaCasa] == pecas.get(Movimentacao.pedraAdversario)) {
					boolean novaPeca = true;
					for(int i = 0; i < jaAnalisados.length; i++) {
						if(jaAnalisados[i] == proximaCasa) {
							novaPeca = false;
						}
					}
					if (novaPeca) {

						avaliacao += JOGADA_RESULTA_CAPTURA;

						avaliacao += avaliaQuantosAliadosSaoPerdidos(individuo, destino, proximaCasa, 
								tabuleiroAntes.clone(), pecas, vez, -CAPTURA_UMA_PECA);
					}					
				} else {
					proximaCasa = (quadrado == maior) ? quadrado + inc : quadrado - inc;
					if (this.isDominioTabuleiro(proximaCasa)
							&& (tab[proximaCasa] == pecas.get(Movimentacao.pedraAdversario) ||
							tab[proximaCasa] == pecas.get(Movimentacao.damaAdversario))) {
						avaliacao += AVANCO_SIMPLES;
					}
				}
			}
		}

		return avaliacao;
	}

	public int getIndiceNovoDestino(int posicaoPeca, int value, char[] tabuleiro) {
		ArrayList<Integer> novosDestinos = getDestinosPossiveis(tabuleiro, posicaoPeca);
		for (int i = 0; i < novosDestinos.size(); i++)
			if (novosDestinos.get(i) == value)
				return i;

		return -1;
	}
	
	private boolean isDominioTabuleiro(int posicao) {
		return (posicao > 0 && posicao < FiniteStateMachine.TAMANHO_TABULEIRO) ? true : false;
	}

	private boolean isDamaLivre(int origem, int quadradoDama, int inc, char[] tabuleiro) {		
		
		int de = Math.min(origem, quadradoDama);
		int para = Math.max(origem, quadradoDama);
		
		if(tabuleiro[origem]!= Pecas.VAZIO) {
			//ve se a cada posterior está livre e pertence ao tabuleiro
			int casaAdjacente;
			if (quadradoDama < origem) {
				casaAdjacente = origem + inc;
			} else {
				casaAdjacente = origem - inc;
			}
			if (!isDominioTabuleiro(casaAdjacente)) {
				return false;
			} else if(tabuleiro[casaAdjacente] != Pecas.VAZIO) {
				return false;
			}
			
			for(int i = de+inc; i < tabuleiro.length && i < para; i += inc) {
				if (tabuleiro[i] != Pecas.VAZIO) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private ArrayList<Integer> getDestinosPossiveis(char[] tabuleiro, int de) {
		ArrayList<Integer> quadrados = new ArrayList<Integer>();

		for (int j = 7; de - j > 0 && tabuleiro[de - j] != Pecas.PAREDE; j += 7) {
			quadrados.add(de - j);
		}
		for (int j = 7; de + j < tabuleiro.length
				&& tabuleiro[de + j] != Pecas.PAREDE; j += 7) {
			quadrados.add(de + j);
		}
		for (int j = 9; de - j > 0 && tabuleiro[de - j] != Pecas.PAREDE; j += 9) {
			quadrados.add(de - j);
		}
		for (int j = 9; de + j < tabuleiro.length
				&& tabuleiro[de + j] != Pecas.PAREDE; j += 9) {
			quadrados.add(de + j);
		}
		return quadrados;
	}
	
	public char[] moverPecaCapturaParaAvaliacao(int de, int para,
			char tipoPecaAtacante, char[] tabuleiro) {

		tabuleiro[de] = Pecas.VAZIO;
		tabuleiro[para] = tipoPecaAtacante; // this.getTipoPeca();

		char pedra_capturada = '*';
		char peca_adv = '*';
		char dama_adv = '*';
		if (tipoPecaAtacante == Pecas.PEDRA_COMPUTADOR
				|| tipoPecaAtacante == Pecas.DAMA_COMPUTADOR) {
			pedra_capturada = Pecas.PECA_CAPTURADA_JOGADOR;
			peca_adv = Pecas.PEDRA_JOGADOR;
			dama_adv = Pecas.DAMA_JOGADOR;
		} else if (tipoPecaAtacante == Pecas.PEDRA_JOGADOR
				|| tipoPecaAtacante == Pecas.DAMA_JOGADOR) {
			pedra_capturada = Pecas.PECA_CAPTURADA_COMPUTADOR;
			peca_adv = Pecas.PEDRA_COMPUTADOR;
			dama_adv = Pecas.DAMA_COMPUTADOR;

		}
		int dis = Math.max(de, para) - Math.min(de, para);
		if (tipoPecaAtacante == Pecas.PEDRA_COMPUTADOR
				|| tipoPecaAtacante == Pecas.PEDRA_JOGADOR) {
			if (para > de
					&& (tabuleiro[para - (dis / 2)] == peca_adv || tabuleiro[para
							- (dis / 2)] == dama_adv)) {
				tabuleiro[para - (dis / 2)] = pedra_capturada;
			} else if (para < de
					&& (tabuleiro[para + (dis / 2)] == peca_adv || tabuleiro[para
							+ (dis / 2)] == dama_adv)) {
				tabuleiro[para + (dis / 2)] = pedra_capturada;
			}
		} else {
			// para dama criar um for de variacao inc, com inc valendo 7 ou
			// 9,
			// do destino a origem
			// definindo pontos diferentes de vazio como vazio.
			int inc = (Math.max(de, para) - Math.min(de, para)) % 7 == 0 ? 7
					: 9;
			int inicio = Math.min(de, para) + inc;
			int fim = Math.max(de, para);

			for (int i = inicio; i < fim; i += inc) {
				if (tabuleiro[i] != Pecas.VAZIO) {
					tabuleiro[i] = pedra_capturada;
				}
			}
		}

		return tabuleiro;
	}

	public boolean ehValido(No no) {
		if(no.getPai() == null) {
			return true;
		} else {
			if(no.getPai().getPai() == null) {
				return true;
			}
			else if(no.getPai().getPai().getEstado().equals(no.getEstado()))
				return false;
		}
		return true;
	}	
}
