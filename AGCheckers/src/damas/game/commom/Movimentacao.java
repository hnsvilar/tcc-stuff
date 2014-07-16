package damas.game.commom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Movimentacao {
	public static final int pedraAdversario = 0;
	public static final int damaAdversario = 1;
	public static final int pedraComputador = 2;
	public static final int damaComputador = 3;
	
	public static boolean lateralSuperiorContem(int i) {
		if(i == 1 || i == 3 || i == 5 || i == 7) {
			return true;
		}
		return false;
	}
	
	public static boolean lateralEsquerdaContem(int i) {
		if(i == 8 || i == 24 || i == 40 || i == 48) {
			return true;
		}
		return false;
	}
	
	public static boolean lateralInferiorContem(int i) {
		if(i == 56 || i == 58 || i == 60 || i == 62) {
			return true;
		}
		return false;
	}
	
	public static boolean lateralDireitaContem(int i) {
		if(i == 7 || i == 23 || i == 39 || i == 55) {
			return true;
		}
		return false;
	}
	
	
	public static HashMap<Integer, Character> getPecas(int vez) {
		HashMap<Integer, Character> pecas = new HashMap<Integer, Character>();
		if(vez == Jogada.JOGADOR) {
			pecas.put(pedraAdversario, Pecas.PEDRA_COMPUTADOR);
			pecas.put(damaAdversario,Pecas.DAMA_COMPUTADOR);
			pecas.put(pedraComputador,Pecas.PEDRA_JOGADOR);
			pecas.put(damaComputador,Pecas.DAMA_JOGADOR);
		} else {
			pecas.put(pedraAdversario,Pecas.PEDRA_JOGADOR);
			pecas.put(damaAdversario,Pecas.DAMA_JOGADOR);
			pecas.put(pedraComputador,Pecas.PEDRA_COMPUTADOR);
			pecas.put(damaComputador, Pecas.DAMA_COMPUTADOR);
		}
		
		return pecas;		
	}
	
	public static boolean podeMover(char[] tabuleiro, int indice) {
		if(podeMoverEsquerdaCima(tabuleiro, indice) || 
				podeMoverDireitaCima(tabuleiro, indice) ||
				podeMoverEsquerdaBaixo(tabuleiro, indice) || 
				podeMoverDireitaBaixo(tabuleiro, indice))
			return true;
		else
			return false;
	}
	
	public static boolean podeMoverDireitaBaixo(char[] tabuleiro, int indice) {
//		System.out.println("podeMoverDireitaBaixo indice + 9 " + (indice + 9) + " tabuleiro[indice + 9] " + tabuleiro[indice + 9]);
		if(tabuleiro[indice] == Pecas.PEDRA_COMPUTADOR || tabuleiro[indice] == Pecas.DAMA_JOGADOR
				|| tabuleiro[indice] == Pecas.DAMA_COMPUTADOR) {
			if(indice + 9 < tabuleiro.length && tabuleiro[indice + 9] == Pecas.VAZIO)
				return true;
		}
		return false;
	}

	public static boolean podeMoverEsquerdaBaixo(char[] tabuleiro, int indice) {
//		System.out.println("podeMoverEsquerdaBaixo indice + 7 " + (indice + 7) + " tabuleiro[indice + 7] " + tabuleiro[indice + 7]);
		if(tabuleiro[indice] == Pecas.PEDRA_COMPUTADOR || tabuleiro[indice] == Pecas.DAMA_JOGADOR
				|| tabuleiro[indice] == Pecas.DAMA_COMPUTADOR) {
			if(indice + 7 < tabuleiro.length && tabuleiro[indice + 7] == Pecas.VAZIO)
				return true;
		}
		return false;
	}

	public static boolean podeMoverDireitaCima(char[] tabuleiro, int indice) {
		if(tabuleiro[indice] == Pecas.PEDRA_JOGADOR || tabuleiro[indice] == Pecas.DAMA_JOGADOR
				|| tabuleiro[indice] == Pecas.DAMA_COMPUTADOR) {
			if(indice - 7 >= 0 && tabuleiro[indice - 7] == Pecas.VAZIO)
				return true;
		}
		return false;
	}
	
	public static boolean podeMoverEsquerdaCima(char[] tabuleiro, int indice) {
		if(tabuleiro[indice] == Pecas.DAMA_COMPUTADOR || tabuleiro[indice] == Pecas.DAMA_JOGADOR ||
				tabuleiro[indice] == Pecas.PEDRA_JOGADOR) {
			if(indice - 9 >= 0 && tabuleiro[indice - 9] == Pecas.VAZIO)
				return true;
			}
		return false;
	}
	
	//-----------------------------------------------------------------
	
	public static boolean podeComer(char[] tabuleiro, int indice, int vez) {
		if(podeComerDireitaBaixo(tabuleiro, indice, vez) ||
				podeComerEsquerdaBaixo(tabuleiro, indice, vez) ||
				podeComerDireitaCima(tabuleiro, indice, vez) ||
				podeComerEsquerdaCima(tabuleiro, indice, vez))
			return true;
		return false;
	}
	
	public static boolean podeComerDireitaBaixo(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if(indice + 18 < tabuleiro.length &&
					(tabuleiro[indice + 9] == pecas.get(damaAdversario) || 
					tabuleiro[indice + 9] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice + 18] == Pecas.VAZIO) {
				return true;
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice + 9; i < tabuleiro.length; i = i + 9) {
				if(tabuleiro[i] == pecas.get(damaComputador) || tabuleiro[i] == pecas.get(pedraComputador)) {
					return false;
				} else if(tabuleiro[i] == pecas.get(damaAdversario) || tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i+9 < tabuleiro.length && tabuleiro[i+9] == Pecas.VAZIO)
						return true;
					 else {
							return false;
						}
				}
			}
		}
		return false;
	}
	
	public static boolean podeComerEsquerdaCima(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if((indice - 18) >= 0 &&
					(tabuleiro[indice - 9] == pecas.get(damaAdversario) || 
					tabuleiro[indice - 9] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice - 18] == Pecas.VAZIO) {
				return true;
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice - 9; i >= 0; i = i - 9) {
				if(tabuleiro[i] == pecas.get(damaComputador) || tabuleiro[i] == pecas.get(pedraComputador)) {
					return false;
				} else if(tabuleiro[i] == pecas.get(damaAdversario) || tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i-9 >= 0  && tabuleiro[i-9] == Pecas.VAZIO)
						return true;
					 else {
							return false;
						}
				}
			}
		}
		return false;
	}
	
	public static boolean podeComerDireitaCima(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if(indice - 14 >= 0 &&
					(tabuleiro[indice - 7] == pecas.get(damaAdversario) ||
					tabuleiro[indice - 7] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice - 14] == Pecas.VAZIO) {
				return true;
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice - 7; i >= 0; i = i - 7) {
				if(tabuleiro[i] == pecas.get(damaComputador) || 
						tabuleiro[i] == pecas.get(pedraComputador)) {
					return false;
				} else if(tabuleiro[i] == pecas.get(damaAdversario) || tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i-7 >= 0  && tabuleiro[i-7] == Pecas.VAZIO)
						return true;
					 else {
							return false;
						}
				}
			}
		}
		return false;
	}

	public static boolean podeComerEsquerdaBaixo(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if(indice + 14 < tabuleiro.length &&
					(tabuleiro[indice + 7] == pecas.get(damaAdversario) || 
					tabuleiro[indice + 7] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice + 14] == Pecas.VAZIO) {
				return true;
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice + 7; i < tabuleiro.length; i = i + 7) {
				if(tabuleiro[i] == pecas.get(damaComputador) || 
						tabuleiro[i] == pecas.get(pedraComputador)) {
					return false;
				} else if(tabuleiro[i] == pecas.get(damaAdversario) || 
						tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i+7 < tabuleiro.length && tabuleiro[i+7] == Pecas.VAZIO) {						
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}

	//----------------------------------------------------------
	
	public static ArrayList<Integer> getPosicoesPossiveisMover(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		ArrayList<Integer> posicoes = new ArrayList<Integer>();
//		System.out.println("A peca eh "+tabuleiro[indice]);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
//			System.out.println("Pedra");
			if(podeMoverDireitaBaixo(tabuleiro, indice))
				posicoes.add(indice+9);
			if(podeMoverDireitaCima(tabuleiro, indice))
				posicoes.add(indice-7);
			if(podeMoverEsquerdaBaixo(tabuleiro, indice))
				posicoes.add(indice+7);
			if(podeMoverEsquerdaCima(tabuleiro, indice))
				posicoes.add(indice-9);
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
//			System.out.println("Dama");
			//Direita Cima
			for(int i = indice-7; i >= 0; i=i-7) {
				if(tabuleiro[i] == Pecas.VAZIO) {
					posicoes.add(i);
				} else {
					break;
				}
			}
			//Esquerda Cima
			for(int i = indice-9; i >= 0; i=i-9) {
				if(tabuleiro[i] == Pecas.VAZIO) {
					posicoes.add(i);
				} else {
					break;
				}
			}
			//Direita Baixo
			for(int i = indice+9; i < tabuleiro.length; i=i+9) {
				if(tabuleiro[i] == Pecas.VAZIO) {
					posicoes.add(i);
				} else {
					break;
				}
			}
			//Esquerda Baixo
			for(int i = indice+7; i < tabuleiro.length; i=i+7) {
				if(tabuleiro[i] == Pecas.VAZIO) {
					posicoes.add(i);
				} else {
					break;
				}
			}			
		}
		return posicoes;
	}
	
	public static ArrayList<Integer> getPosicoesPossiveisComer(char[] tabuleiro, int indice, int vez) {
		ArrayList<Integer> posicoes = new ArrayList<Integer>();
		int aux = -1;
		aux = getComerDireitaBaixo(tabuleiro,indice, vez);
//		System.out.println("????" +aux);
		if(aux != -1)
			posicoes.add(aux);
		
		aux = getComerEsquerdaBaixo(tabuleiro,indice,vez);
		if(aux != -1)
			posicoes.add(aux);
		
		aux = getComerDireitaCima(tabuleiro,indice,vez);
		if(aux != -1)
			posicoes.add(aux);
		
		aux = getComerEsquerdaCima(tabuleiro,indice,vez);
		if(aux != -1)
			posicoes.add(aux);
		
		if(posicoes.size() > 0) {
			System.out.println("ENCONTREI "+posicoes.size());
			for(int i = 0; i < posicoes.size(); i++) {
				System.out.print(posicoes.get(i)+"\t");
			}
			System.out.println();
		}
		return posicoes;
	}
	
	public static ArrayList<Integer> getPosicoesPossiveisComer(char[] tabuleiro, int indice, 
			ArrayList<Integer> pecasComidas, int vez) {
		ArrayList<Integer> posicoes = getPosicoesPossiveisComer(tabuleiro, indice, vez);
		ArrayList<Integer> posicoesARetornar = new ArrayList<Integer>();
		for(Integer i : posicoes) {
			if(!pecasComidas.contains(i)) {
				posicoesARetornar.add(i);
			}
		}
		return posicoesARetornar;
	}
	
	public static int getComerDireitaBaixo(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if(indice + 18 < tabuleiro.length &&
					(tabuleiro[indice + 9] == pecas.get(damaAdversario) || 
					tabuleiro[indice + 9] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice + 18] == Pecas.VAZIO) {
				return (indice + 9);
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice + 9; i < tabuleiro.length; i = i + 9) {
				if(tabuleiro[i] != pecas.get(damaAdversario) && 
						tabuleiro[i] != pecas.get(pedraAdversario) && 
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaAdversario) || 
						tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i+9 < tabuleiro.length && tabuleiro[i+9] == Pecas.VAZIO){
						return i;
					} else {
						return -1;
					}
				} else if(tabuleiro[i] == pecas.get(pedraComputador) ||
						tabuleiro[i] == pecas.get(damaComputador)) {
					return -1;
				}
			}
		} else if(tabuleiro[indice] == pecas.get(pedraAdversario)) {
			if(indice + 18 < tabuleiro.length &&
					(tabuleiro[indice + 9] == pecas.get(damaComputador) || 
					tabuleiro[indice + 9] == pecas.get(pedraComputador)) && 
					tabuleiro[indice + 18] == Pecas.VAZIO) {
				return (indice + 9);
			}
		} else if(tabuleiro[indice] == pecas.get(damaAdversario)) {
			for(int i = indice + 9; i < tabuleiro.length; i = i + 9) {
				if(tabuleiro[i] != pecas.get(damaComputador) && 
						tabuleiro[i] != pecas.get(pedraComputador) && 
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaComputador) || tabuleiro[i] == pecas.get(pedraComputador)) {
					if(i+9 < tabuleiro.length && tabuleiro[i+9] == Pecas.VAZIO){
						return i;
					} else {
						return -1;
					}
				} else if(tabuleiro[i] == pecas.get(pedraAdversario) ||
						tabuleiro[i] == pecas.get(damaAdversario)) {
					return -1;
				}
			}
		}
		return -1;
	}
	
	public static int getComerEsquerdaCima(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if(indice - 18 >= 0 &&
					(tabuleiro[indice - 9] == pecas.get(damaAdversario) || 
					tabuleiro[indice - 9] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice - 18] == Pecas.VAZIO) {
				return indice - 9;
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice - 9; i >= 0; i = i - 9) {
				if(tabuleiro[i] != pecas.get(damaAdversario) && 
						tabuleiro[i] != pecas.get(pedraAdversario) && 
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaAdversario) || tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i-9 >= 0  && tabuleiro[i-9] == Pecas.VAZIO) {
						return i;
					} else {
						return -1;
					}
				}else if(tabuleiro[i] == pecas.get(pedraComputador) ||
						tabuleiro[i] == pecas.get(damaComputador)) {
					return -1;
				}
			}
		} else if(tabuleiro[indice] == pecas.get(pedraAdversario)) {
			if(indice - 18 >= 0 &&
					(tabuleiro[indice - 9] == pecas.get(damaComputador) || 
					tabuleiro[indice - 9] == pecas.get(pedraComputador)) && 
					tabuleiro[indice - 18] == Pecas.VAZIO) {
				return indice - 9;
			}
		} else if(tabuleiro[indice] == pecas.get(damaAdversario)) {
			for(int i = indice - 9; i >= 0; i = i - 9) {
				if(tabuleiro[i] != pecas.get(damaComputador) && 
						tabuleiro[i] != pecas.get(pedraComputador) && 
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaComputador) || tabuleiro[i] == pecas.get(pedraComputador)) {
					if(i-9 >= 0  && tabuleiro[i-9] == Pecas.VAZIO) {
						return i;
					} else {
						return -1;
					}
				} else if(tabuleiro[i] == pecas.get(pedraAdversario) ||
						tabuleiro[i] == pecas.get(damaAdversario)) {
					return -1;
				}
			}
		}
		return -1;
	}
	
	public static int getComerDireitaCima(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if(indice - 14 >= 0 &&
					(tabuleiro[indice - 7] == pecas.get(damaAdversario) || 
					tabuleiro[indice - 7] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice - 14] == Pecas.VAZIO) {
				return indice-7;
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice - 7; i >= 0; i = i - 7) {
				if(tabuleiro[i] != pecas.get(damaAdversario) && 
						tabuleiro[i] != pecas.get(pedraAdversario) && 
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaAdversario) || tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i-7 >= 0  && tabuleiro[i-7] == Pecas.VAZIO) {
						return i;
					} else {
						return -1;
					}
				}else if(tabuleiro[i] == pecas.get(pedraComputador) ||
						tabuleiro[i] == pecas.get(damaComputador)) {
					return -1;
				}
			}
		} else if(tabuleiro[indice] == pecas.get(pedraAdversario)) {
			if(indice - 14 >= 0 &&
					(tabuleiro[indice - 7] == pecas.get(damaComputador) || 
					tabuleiro[indice - 7] == pecas.get(pedraComputador)) && 
					tabuleiro[indice - 14] == Pecas.VAZIO) {
				return indice-7;
			}
		} else if(tabuleiro[indice] == pecas.get(damaAdversario)) {
			for(int i = indice - 7; i >= 0; i = i - 7) {
				if(tabuleiro[i] != pecas.get(damaComputador) && 
						tabuleiro[i] != pecas.get(pedraComputador) &&
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaComputador) || tabuleiro[i] == pecas.get(pedraComputador)) {
					if(i-7 >= 0  && tabuleiro[i-7] == Pecas.VAZIO) {
						return i;
					} else {
						return -1;
					}
				}else if(tabuleiro[i] == pecas.get(pedraAdversario) ||
						tabuleiro[i] == pecas.get(damaAdversario)) {
					return -1;
				}
			}
		}
		return -1;
	}

	public static int getComerEsquerdaBaixo(char[] tabuleiro, int indice, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[indice] == pecas.get(pedraComputador)) {
			if(indice + 14 < tabuleiro.length &&
					(tabuleiro[indice + 7] == pecas.get(damaAdversario) || 
					tabuleiro[indice + 7] == pecas.get(pedraAdversario)) && 
					tabuleiro[indice + 14] == Pecas.VAZIO) {
				return indice+7;
			}
		} else if(tabuleiro[indice] == pecas.get(damaComputador)) {
			for(int i = indice + 7; i < tabuleiro.length; i = i + 7) {
				if(tabuleiro[i] != pecas.get(damaAdversario) && 
						tabuleiro[i] != pecas.get(pedraAdversario) && 
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaAdversario) || tabuleiro[i] == pecas.get(pedraAdversario)) {
					if(i+7 < tabuleiro.length && tabuleiro[i+7] == Pecas.VAZIO) {
						return i;
					} else {
						return -1;
					}
				} else if(tabuleiro[i] == pecas.get(pedraComputador) ||
						tabuleiro[i] == pecas.get(damaComputador)) {
					return -1;
				}
			}
		} else if(tabuleiro[indice] == pecas.get(pedraAdversario)) {
			if(indice + 14 < tabuleiro.length &&
					(tabuleiro[indice + 7] == pecas.get(damaComputador) || 
					tabuleiro[indice + 7] == pecas.get(pedraComputador)) && 
					tabuleiro[indice + 14] == Pecas.VAZIO) {
				return indice+7;
			}
		} else if(tabuleiro[indice] == pecas.get(damaAdversario)) {
			for(int i = indice + 7; i < tabuleiro.length; i = i + 7) {
				if(tabuleiro[i] != pecas.get(damaComputador) && 
						tabuleiro[i] != pecas.get(pedraComputador) && 
						tabuleiro[i] != Pecas.VAZIO) {
					return -1;
				}
				if(tabuleiro[i] == pecas.get(damaComputador) || tabuleiro[i] == pecas.get(pedraComputador)) {
					if(i+7 < tabuleiro.length && tabuleiro[i+7] == Pecas.VAZIO) {
						return i;
					} else {
						return -1;
					}
				} else if(tabuleiro[i] == pecas.get(pedraAdversario) ||
						tabuleiro[i] == pecas.get(damaAdversario)) {
					return -1;
				}
			}
		}
		return -1;
	}

	public static int descobrePosicaoAposComer(int pecaMudada, int pecaComida, char[] tabuleiro, int vez) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[pecaMudada] == pecas.get(pedraComputador) || 
				tabuleiro[pecaMudada] == pecas.get(pedraAdversario)) {
			if(pecaComida < pecaMudada) { //Esta acima
				if((pecaMudada-pecaComida)%9 == 0) {//Esta a esquerda
					return (pecaMudada-18);
				} else {//esta a direita
					return (pecaMudada-14);
				}
			} else {//esta abaixo
				if((pecaMudada-pecaComida)%7 == 0) { //esta a esquerda
					return (pecaMudada+14);
				} else {
					return (pecaMudada+18);
				}
			}
		} else if(tabuleiro[pecaMudada] == pecas.get(damaComputador) || 
				tabuleiro[pecaMudada] == pecas.get(damaAdversario)) {
			Random randomGenerator = new Random();
			ArrayList<Integer> posicoes = new ArrayList<Integer>();
			if(pecaComida < pecaMudada) { //Esta acima
				boolean pode = true;
				if((pecaMudada-pecaComida)%9 == 0) {//Esta a esquerda
					for(int i = pecaComida - 9; i >= 0 && pode; i = i - 9) {
						if(tabuleiro[i] != Pecas.VAZIO)
							pode = false;
						else
							posicoes.add(i);
					}
				} else {//esta a direita
					for(int i = pecaComida - 7; i >= 0 && pode; i = i - 7) {
						if(tabuleiro[i] != Pecas.VAZIO)
							pode = false;
						else
							posicoes.add(i);
					}
				}
			} else {//esta abaixo
				boolean pode = true;
				if((pecaMudada-pecaComida)%7 == 0) { //esta a esquerda					
					for(int i = pecaComida + 7; i < tabuleiro.length && pode; i = i + 7) {						
						if(tabuleiro[i] != Pecas.VAZIO)
							pode = false;
						else
							posicoes.add(i);
					}
				} else {
					for(int i = pecaComida + 9; i < tabuleiro.length && pode; i = i + 9) {
						if(tabuleiro[i] != Pecas.VAZIO)
							pode = false;
						else
							posicoes.add(i);
					}
				}
			}
			if(posicoes.size() > 0) {
				int random = randomGenerator.nextInt(posicoes.size());
				return posicoes.get(random);
			} else {
				return -1;
			}
		}
		return -1;
	}
	
	public static int descobrePosicaoAposComer(int pecaMudada, int pecaComida, char[] tabuleiro, int vez, 
			ArrayList<Integer> pecasComidas) {
		HashMap<Integer,Character> pecas = getPecas(vez);
		if(tabuleiro[pecaMudada] == pecas.get(pedraComputador) || tabuleiro[pecaMudada] == pecas.get(pedraAdversario)) {
			if(pecaComida < pecaMudada) { //Esta acima
				if((pecaMudada-pecaComida)%9 == 0) {//Esta a esquerda
					return (pecaMudada-18);
				} else {//esta a direita
					return (pecaMudada-14);
				}
			} else {//esta abaixo
				if((pecaMudada-pecaComida)%7 == 0) { //esta a esquerda
					return (pecaMudada+14);
				} else {
					return (pecaMudada+18);
				}
			}
		} else if(tabuleiro[pecaMudada] == pecas.get(damaComputador) ||
				tabuleiro[pecaMudada] == pecas.get(damaAdversario)) {
			Random randomGenerator = new Random();
			ArrayList<Integer> posicoes = new ArrayList<Integer>();
			boolean pode = true;
			if(pecaComida < pecaMudada) { //Esta acima
				if((pecaMudada-pecaComida)%9 == 0) {//Esta a esquerda
					for(int i = pecaComida - 9; i >= 0 && pode; i = i - 9) {
						if(!pecasComidas.contains(i)) {
							if(tabuleiro[i] != Pecas.VAZIO)
								pode = false;
							else
								posicoes.add(i);
						}
					}
				} else {//esta a direita
					for(int i = pecaComida - 7; i >= 0 && pode; i = i - 7) {
						if(!pecasComidas.contains(i)) {
							if(tabuleiro[i] != Pecas.VAZIO)
								pode = false;
							else
								posicoes.add(i);
						}
					}
				}
			} else {//esta abaixo
				if((pecaMudada-pecaComida)%7 == 0) { //esta a esquerda
					for(int i = pecaComida + 7; i < tabuleiro.length && pode; i = i + 7) {
						if(!pecasComidas.contains(i)) {
							if(tabuleiro[i] != Pecas.VAZIO)
								pode = false;
							else
								posicoes.add(i);
						}
					}
				} else {
					for(int i = pecaComida + 9; i < tabuleiro.length && pode; i = i + 9) {
						if(!pecasComidas.contains(i)) {
							if(tabuleiro[i] != Pecas.VAZIO)
								pode = false;
							else
								posicoes.add(i);
						}
					}
				}
			}
			
			if(posicoes.size() > 0) {
				int random = randomGenerator.nextInt(posicoes.size());
				return posicoes.get(random);
			} else {
				return -1;
			}
		}
		return -1;
	}

	private static boolean estaNaDiagonal(int pecaMudada, int posicao, int inc) {
		int maior = Math.max(pecaMudada, posicao);
		int menor = Math.min(pecaMudada, posicao);
		int inc2 = maior-menor;
		if(inc2 % inc == 0)
			return true;
		else
			return false;
	}

	
}
