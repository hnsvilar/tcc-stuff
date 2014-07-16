package damas.gui.testes;

import java.util.ArrayList;
import java.util.Random;

import damas.bd.core.AtributosPartida;
import damas.bd.core.Configuracao;
import damas.bd.core.Usuario;
import damas.data.GerenciadorDificuldade;
import damas.data.GerenciadorLog;
import damas.data.GerenciadorLog.Tipo;
import damas.game.arvore.No;
import damas.game.commom.Individuo;
import damas.game.fsm.AcaoComputador;

public class TestandoCalculoAtributos {
	
	public static void main(String[] args) {
		double fator = 100;
		int contador = 10;
		double [] fatores = new double[contador];
		if(contador == 1) {
			fatores[0] = fator;
		} else if(contador == 2) {
			fatores[0] = fator*2/3;
			fatores[1] = fator/3;
		} else {
			for(int i = contador - 1; i > 0; i--) {
				double auxiliar =fator/3; 
				fator = fator*2/3;
				System.out.println(i + " - "+auxiliar);
				fatores[i] = auxiliar;
				if(i == 1) {	
					fatores[1] = fator/2;
					fatores[0] = fator/2;
//					System.out.println("1 - "+fatores[1]);
					System.out.println("0 - "+fatores[0]);
					
					
				}
			}
		}
		
		double aux = 0;
		for(int i = 0; i < contador; i++) {
			aux+=fatores[i];
			System.out.println(fatores[i]);
		}
		System.out.println("aux "+aux);
	}
	
	public static void masin(String[] args) {
		Usuario u = new Usuario();
		ArrayList<AtributosPartida> v = new ArrayList<AtributosPartida>();
		v.add(new AtributosPartida("10/01/2012", 12, 12, 0, 0, 12345, Configuracao.VITORIA, 40));
		v.add(new AtributosPartida("10/01/2012", 12, 12, 0, 0, 12345, Configuracao.VITORIA, 40));
		v.add(new AtributosPartida("10/01/2012", 12, 12, 0, 0, 12345, Configuracao.VITORIA, 40));
		v.add(new AtributosPartida("10/01/2012", 12, 12, 0, 0, 12345, Configuracao.VITORIA, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.EMPATE, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.EMPATE, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.EMPATE, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.EMPATE, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.DERROTA, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.DERROTA, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.DERROTA, 40));
//		v.add(new AtributosPartida("10/01/2012", 10, 10, 0, 0, 12345, Configuracao.DERROTA, 40));
		
		u.setAtributosPartidas(v);
		GerenciadorDificuldade ga = GerenciadorDificuldade.getInstance();
		System.out.println(ga.calculaDificuldade(u));
		
//		System.out.println();
//		
////		ArrayList<Integer> teste = new ArrayList<Integer>();
////		ArrayList<Integer> teste2 = new ArrayList<Integer>();
////		for(int i = 0; i < 10; i++) {
////			teste.add(i);
////		}
////		System.out.println(teste.toString());
////		teste2.addAll(teste.subList(0, 4));
////		System.out.println(teste2.toString());
//		
//		ArrayList<No> p = new ArrayList<No>();
//		Random randomGenerator = new Random();
//		
//		double random;
//		for(int i = 0; i < 100; i++) {
//			System.out.println(randomGenerator.nextInt(101));
//		}
//		for(int i = 0; i < 15; i++) {
//			char[] a = new char[1];
//			a[0] = (char) i;
//			Individuo in = new Individuo(a, 0,0,0);
//			random = Double.parseDouble(""+(randomGenerator.nextInt(1000)));
//			in.setAvaliacao(random);
//			p.add(new No(null, in));
//		}
//		
//		for(No i : p) {
//			System.out.print(i.getEstado().getAvaliacao() + ", ");
//		}
//		System.out.println();
//		System.out.println("---------\n");
//		
//		AcaoComputador ac = new AcaoComputador(null, null, 0);
//		No[] aux = new No[p.size()];
//		for(int i = 0; i < p.size(); i++) {
//			aux[i] = p.get(i);
//		}
//		ac.quick_sort(aux, 0,p.size());
//				
//		p.clear();
//		for(int i = 0; i < aux.length; i++) {
//			p.add(aux[i]);
//		}
//		
//		for(No i : p) {
//			System.out.print(i.getEstado().getAvaliacao() + ", ");
//		}
//		System.out.println();
		
	}
}
