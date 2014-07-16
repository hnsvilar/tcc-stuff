package damas.game.arvore;

import java.util.ArrayList;

import damas.game.commom.Individuo;

public class No {
	private Individuo estado;
	private No pai;
	private int custo;
	private ArrayList<No> filhos;
	
	public No (No pai, Individuo estado) {
		filhos = new ArrayList<No>();
		this.pai = pai;
		this.estado = estado;
		if(pai == null) {
			custo = 0;
		} else {
			custo = pai.getCusto() + 1;
		}
	}
	
	public int getCusto() {
		return custo;
	}
	
	public No getPai() {
		return pai;
	}
	
	public Individuo getEstado() {
		return estado;
	}
	
	public ArrayList<No> getFilhos() {
		return filhos;
	}
	
	public void addFilho(No no) {
		filhos.add(no);
	}
	
	public String toString() {
		return custo + " " + estado.toString();
	}
}
