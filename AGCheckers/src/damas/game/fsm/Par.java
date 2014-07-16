package damas.game.fsm;

public class Par {
	private double media;
	private int indice;
	
	public Par(int indice, double media) {
		this.indice = indice;
		this.media = media;
	}
	
	public int getIndice() {
		return indice;
	}
	
	public double getMedia() {
		return media;
	}
}
