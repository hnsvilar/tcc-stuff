package damas.data;

import java.util.List;

import damas.bd.core.AtributosPartida;
import damas.bd.core.Configuracao;
import damas.bd.core.Usuario;

public class GerenciadorDificuldade {
	private static double dificuldade = -1;
	
	private static GerenciadorDificuldade instance = null;
	
	private double multiplicadorResultado = 0.4;
	private double multiplicadorPecas = 0.1;
	private double multiplicadorDamas = 0.3;
	private double multiplicadorLancesPorResultado = 0.2;
	
	public static int MAX_POR_ANALISE = 20;
	public static int MAX_RESULTADO = 100;
	public static int MIN_RESULTADO = 0;
	public static int QUANTIDADE_ANTES_CALCULAR_DIFICULDADE = 2;
	
	private static int contador = -1;
	
	//100 / quantidade de multiplicadores = 100 / 5 = 20.
	//sao 5 porque: 2*modResultado + 0.5*Pecas+ 1.5*Damas +1*lances
	
	private GerenciadorDificuldade() {}
	
	public static GerenciadorDificuldade getInstance() {
		if(instance == null)
			instance = new GerenciadorDificuldade();
		return instance;
	}
	
	public double getDificuldade() {
		return dificuldade;
	}
	
	/**
	 *	Funcao que calcula o nivel daquele jogador.
	 * Usa:
	 * 	Resultado
	 * 	NPecasAliadas
	 * NDamasAliadas
	 * NPecasInimigas
	 * NDamasInimigas
	 * numLances
	 * 
	 * Pontos:
	 * 1 ++ Quanto maior o numero de pecas e damas alidas, melhor
	 * 2 -- quanto maior o numero de pecas e damas inimigas, pior
	 * 3 ++ Se resultado for vitoria, melhor
	 * 4 ++ Quanto maior for a diferença entre as aliadas e as inimigas, para mais aliadas que inimigas, melhor. 
	 * 5 ++ Quanto maior o numero de lances, melhor
	 */
	public double calculaDificuldade(Usuario usuario) {
		double classificacao = 0.0;
		double fator = 100;
		if(usuario!= null) {
			double [] fatores = new double[usuario.getAtributosPartidas().size()];
			if(usuario.getAtributosPartidas().size() == 1) {
		        fatores[0] = fator;
		    } else {
		        for(int i = usuario.getAtributosPartidas().size() - 1; i > 0; i--) {
		            fator = fator/2;
		            fatores[i] = fator;
		            if(i == 1) {
		                fatores[0] = fator;
		            }
		        }
		    }
					
			classificacao = 
					calculaResultado(usuario.getAtributosPartidas(),fatores) +
					calculaPecas(usuario.getAtributosPartidas(),fatores) +
					calculaDamas(usuario.getAtributosPartidas(),fatores) +
					calculaLancesPorResultado(usuario.getAtributosPartidas(),fatores)
					;
			
			contador = GerenciadorDificuldade.QUANTIDADE_ANTES_CALCULAR_DIFICULDADE;
			
			usuario.setClassificacao(classificacao);
		}
		dificuldade = classificacao;
		return classificacao;
	}

	private double calculaResultado(List<AtributosPartida> atributos, double [] fatores) {
		double media = 0.0;  
		for(int i = 0; i < atributos.size(); i++) {
			if(atributos.get(i).getResultado() == Configuracao.VITORIA) {
				media += MAX_RESULTADO * fatores[i];
			} else if(atributos.get(i).getResultado() == Configuracao.DERROTA) {
				media += MIN_RESULTADO * fatores[i];
			} else if(atributos.get(i).getResultado() == Configuracao.EMPATE) {
				media += ((	MAX_RESULTADO + MIN_RESULTADO) / 2) * fatores[i];
			}
			
		}
			
		return media * multiplicadorResultado / MAX_RESULTADO;
	}
	
	/**
	 * 
	 * Num de Damas		V se Aliadas | V se Inimigas
	 * 0-1					0		 | 100
	 * 2-3					25		 | 75
	 * 4-6					50		 | 50
	 * 7-9					75		 | 25
	 * 10-12				100		 | 0
	 */
	private double calculaDamas(List<AtributosPartida> atributos, double [] fatores ) {
		double resultadoAliadas = 0.0;
		double resultadoInimigas = 0.0;
		double[] valores = new double[5];
		for(int i = 0; i < 5; i++) {
			valores[i] = 25 * i;
		}
		
		//Calcula valor das aliadas e das inimigas
		for(int i = 0; i < atributos.size(); i++) {
			if(atributos.get(i).getNumDamasAliadas() < 2) {
				resultadoAliadas += (valores[0] * fatores[i]);
			} else if(atributos.get(i).getNumDamasAliadas() < 4) {
				resultadoAliadas += valores[1] * fatores[i];
			} else if(atributos.get(i).getNumDamasAliadas() < 7) {
				resultadoAliadas += valores[2] * fatores[i];
			} else if(atributos.get(i).getNumDamasAliadas() < 10) {
				resultadoAliadas += valores[3] * fatores[i];
			} else { 
				resultadoAliadas += valores[4] * fatores[i];
			}
			
			if(atributos.get(i).getNumDamasInimigas() < 2) {
				resultadoInimigas += (valores[4] * fatores[i]);
			} else if(atributos.get(i).getNumDamasInimigas() < 4) {
				resultadoInimigas += (valores[3] * fatores[i]);
			} else if(atributos.get(i).getNumDamasInimigas() < 7) {
				resultadoInimigas += (valores[2] * fatores[i]);
			} else if(atributos.get(i).getNumDamasInimigas() < 10) {
				resultadoInimigas += (valores[1] * fatores[i]);
			} else { 
				resultadoInimigas += (valores[0] * fatores[i]);
			}
		} 
		
		return (resultadoAliadas + resultadoInimigas) * multiplicadorDamas / (2 * MAX_RESULTADO);
	}

	private double calculaPecas(List<AtributosPartida> atributos, double [] fatores ) {
		double resultadoAliadas = 0.0;
		double resultadoInimigas = 0.0;
		double[] valores = new double[5];
		for(int i = 0; i < 5; i++) {
			valores[i] = 25 * i;
		}
		
		//Calcula valor das aliadas e das inimigas
		for(int i = 0; i < atributos.size(); i++) {
			if(atributos.get(i).getNumPecasAliadas() < 2) {
				resultadoAliadas += (valores[0] * fatores[i]);
			} else if(atributos.get(i).getNumPecasAliadas() < 4) {
				resultadoAliadas += valores[1] * fatores[i];
			} else if(atributos.get(i).getNumPecasAliadas() < 7) {
				resultadoAliadas += valores[2] * fatores[i];
			} else if(atributos.get(i).getNumPecasAliadas() < 10) {
				resultadoAliadas += valores[3] * fatores[i];
			} else { 
				resultadoAliadas += valores[4] * fatores[i];
			}
			
			if(atributos.get(i).getNumPecasInimigas() < 2) {
				resultadoInimigas += (valores[4] * fatores[i]);
			} else if(atributos.get(i).getNumPecasInimigas() < 4) {
				resultadoInimigas += (valores[3] * fatores[i]);
			} else if(atributos.get(i).getNumPecasInimigas() < 7) {
				resultadoInimigas += (valores[2] * fatores[i]);
			} else if(atributos.get(i).getNumPecasInimigas() < 10) {
				resultadoInimigas += (valores[1] * fatores[i]);
			} else { 
				resultadoInimigas += (valores[0] * fatores[i]);
			}
		}

		return (resultadoAliadas + resultadoInimigas) * multiplicadorPecas / (MAX_RESULTADO * 2);
	}

	private double calculaLancesPorResultado(List<AtributosPartida> atributos, double [] fatores) {
		double mediaDeLancesVitoria = 0.0;
		double mediaDeLancesDerrota = 0.0;
		int quantVitoria, quantDerrota;
		quantVitoria = quantDerrota = 0;
		for(AtributosPartida a : atributos) {
			if(a.getResultado() == Configuracao.VITORIA) {
				mediaDeLancesVitoria += a.getQuantLances();
				quantVitoria++;
			} else if(a.getResultado() == Configuracao.DERROTA) {
				mediaDeLancesDerrota += a.getQuantLances();
				quantDerrota++;
			}	
		}
		mediaDeLancesVitoria = mediaDeLancesVitoria / quantVitoria;
		mediaDeLancesDerrota = mediaDeLancesDerrota / quantDerrota;

		double resultado = 0.0;
		double auxiliar = 0;
		for(int i = 0; i < atributos.size(); i++) {
			double porcentagem = atributos.get(i).getQuantLances();
			if(atributos.get(i).getResultado() == Configuracao.VITORIA) {
				//negativa, se for mais rapida
				porcentagem = (porcentagem / mediaDeLancesVitoria) - 1;				
				if(porcentagem < -0.5) {
					auxiliar = MAX_RESULTADO * fatores[i];
				} else if (porcentagem > 0.5) {
					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) / 2) * fatores[i];
				} else {
					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) * 3 / 4) * fatores[i];
				}						
			} else if(atributos.get(i).getResultado() == Configuracao.DERROTA) {
				porcentagem = (porcentagem / mediaDeLancesDerrota) - 1;
				if(porcentagem < -0.5) {
					auxiliar = MIN_RESULTADO * fatores[i];
				} else if (porcentagem > 0.5) {
					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) /2) * fatores[i];
				} else {
					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) /4) * fatores[i];
				}
			} else {
				auxiliar = (MAX_RESULTADO + MIN_RESULTADO) / 2;
			}
			resultado += auxiliar;
		}
		return resultado * multiplicadorLancesPorResultado/MAX_RESULTADO;
	}
	
	
//	private double calculaLancesPorResultado(List<AtributosPartida> atributos, double [] fatores) {
//		double mediaDeLancesVitoria = 0.0;
//		double mediaDeLancesDerrota = 0.0;
//		int quantVitoria, quantDerrota;
//		quantVitoria = quantDerrota = 0;
//		for(AtributosPartida a : atributos) {
//			if(a.getResultado() == Configuracao.VITORIA) {
//				mediaDeLancesVitoria += a.getQuantLances();
//				quantVitoria++;
//			} else if(a.getResultado() == Configuracao.DERROTA) {
//				mediaDeLancesDerrota += a.getQuantLances();
//				quantDerrota++;
//			}	
//		}
//		mediaDeLancesVitoria = mediaDeLancesVitoria / quantVitoria;
//		mediaDeLancesDerrota = mediaDeLancesDerrota / quantDerrota;
//
//		double resultado = 0.0;
//		double auxiliar = 0;
//		for(int i = 0; i < atributos.size(); i++) {
//			double porcentagem = atributos.get(i).getQuantLances();
//			if(atributos.get(i).getResultado() == Configuracao.VITORIA) {
//				//negativa, se for mais rapida
//				porcentagem = (porcentagem / mediaDeLancesVitoria) - 1;				
//				if(porcentagem < -0.5) {
//					auxiliar = fatores[i];
//				} else if (porcentagem > 0.5) {
//					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) / 2) * fatores[i] / MAX_RESULTADO;
//				} else {
//					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) * 3 / 4) * fatores[i] / MAX_RESULTADO;
//				}						
//			} else if(atributos.get(i).getResultado() == Configuracao.DERROTA) {
//				porcentagem = (porcentagem / mediaDeLancesDerrota) - 1;
//				if(porcentagem < -0.5) {
//					auxiliar = MIN_RESULTADO * fatores[i] / MAX_RESULTADO;
//				} else if (porcentagem > 0.5) {
//					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) /2) * fatores[i] / MAX_RESULTADO;
//				} else {
//					auxiliar = ((MAX_RESULTADO + MIN_RESULTADO) /4) * fatores[i] / MAX_RESULTADO;
//				}
//			} else {
//				auxiliar = (MAX_RESULTADO + MIN_RESULTADO) /2;
//			}
//			resultado += auxiliar;
//		}
//		
//		return (resultado * MAX_POR_ANALISE / MAX_RESULTADO) * multiplicadorLancesPorResultado;
//	}
//	
	public boolean precisaCalcular() {
		contador--;
		if(contador <= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public double getPorcentagemAcerto() {
		if(dificuldade != -1) {
			return ((dificuldade * 10) / 100) + 90;
		}
		return -1;
	}
}
