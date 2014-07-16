package damas.gui.testes;

import java.util.HashMap;
import java.util.Set;

import damas.bd.core.AtributosPartida;
import damas.bd.core.Configuracao;
import damas.bd.core.Usuario;
import damas.data.ArqConfiguracao;
import damas.game.ag.AlgoritmoGenetico;
import damas.game.commom.Individuo;
import damas.game.commom.Jogada;
import damas.game.commom.Pecas;

public class TestandoConfiguracoes {
	public static void main(String[] args) {
		ArqConfiguracao.setJogador("Gabb");
		System.out.println(ArqConfiguracao.carregaArquivos());
		Usuario u = ArqConfiguracao.getUsuario();
		for(AtributosPartida a : u.getAtributosPartidas()) {
			System.out.println(a.toString());
		}
	}
	
	public static void mains(String[] args) {
		ArqConfiguracao.setJogador("Gabb");
		System.out.println(ArqConfiguracao.carregaArquivos());
		Pecas.PEDRA_JOGADOR = Pecas.VAZIO;
		Pecas.DAMA_JOGADOR = Pecas.VAZIO;
		Pecas.PEDRA_COMPUTADOR = Pecas.VAZIO;
		Pecas.DAMA_COMPUTADOR = Pecas.VAZIO;	
		char[] tab = TestandoAvaliacao.geraTab();
		Pecas.PEDRA_JOGADOR = Pecas.getBRANCA();
		Pecas.DAMA_JOGADOR = Pecas.getDAMA_BRANCA();
		Pecas.PEDRA_COMPUTADOR = Pecas.getPRETA();
		Pecas.DAMA_COMPUTADOR = Pecas.getDAMA_PRETA();
		
//		tab[23] = 'c';
//		tab[44] = 'j';
		tab[21] = 'c';
		tab[12] = 'c';
		tab[3] = '-';
		tab[30] = 'c';
		tab[35] = '-';
		tab[39] = 'j';
		tab[28] = 'j';
//		tab[] = '';
		TestandoAvaliacao.imprimeTabuleiro(tab);
		System.out.println("-----------");
		
		while(!ArqConfiguracao.isReady()) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(ArqConfiguracao.getTotalConfiguracoes());
		
		System.out.println(ArqConfiguracao.getConfiguracoesNegativas().size());
		System.out.println(ArqConfiguracao.getConfiguracoesPositivas().size());
//		System.out.println(ArqConfiguracao.getConfiguracoesNegativas().size()+ArqConfiguracao.getConfiguracoesPositivas().size());
//		
		System.out.println("Positivas: "+ArqConfiguracao.getQuantOcorrenciasPositivas(tab));
		double aux = ((double)(((double)ArqConfiguracao.getQuantOcorrenciasPositivas(tab))*100)/
				ArqConfiguracao.getTotalConfiguracoes());
		System.out.println(aux);
		
		System.out.println("Negativas: "+ArqConfiguracao.getQuantOcorrenciasNegativas(tab));
		aux = ((double)(((double)ArqConfiguracao.getQuantOcorrenciasNegativas(tab))*100)/
				ArqConfiguracao.getTotalConfiguracoes());
		System.out.println(aux);
		System.out.println("--------------------------------");
		
		AlgoritmoGenetico ag = new AlgoritmoGenetico(null);
		Individuo i = new Individuo(tab, 1, 2, Jogada.COMPUTADOR);
		System.out.println("avaliaPesoNovoNegativo "+ag.avaliaPesoNovoNegativo(i));
		System.out.println("avaliaPesoNovoPositivo "+ag.avaliaPesoNovoPositivo(i));
//		HashMap<Configuracao,Integer> a = ArqConfiguracao.getConfiguracoesNegativas();
//		Set<Configuracao> a2 = a.keySet();
//		for(Configuracao c : a2)
//			System.out.println(c.toString());
		
		
	}
}
