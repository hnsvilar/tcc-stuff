package damas.game.commom;

/**
 * 
 * 
 *
 */
public class PecaCome {

	int origemPeca;
	boolean podeComer;
	char tipoPeca;
	int[] destinosPermitidos;

	//----------------------------------------------------------
	
	/**
	 * Construtores
	 */
	public PecaCome(){}
	
	public PecaCome(int origemPeca, boolean podeComer) {
		this.origemPeca = origemPeca;
		this.podeComer = podeComer;
	}

	//----------------------------------------------------------
	
	/**
	 * Getters e Setters
	 */
	public int getOrigemPeca() { return origemPeca;	}
	public void setOrigemPeca(int origemPeca) {	this.origemPeca = origemPeca; }
	public boolean getPodeComer() {	return podeComer; }
	public void setPodeComer(boolean podeComer) { this.podeComer = podeComer; }
	public char getTipoPeca() { return tipoPeca; }
	public void setTipoPeca(char tipoPeca) { this.tipoPeca = tipoPeca; }
	public int[] getDestinosPermitidos() { return destinosPermitidos; }
	public void setDestinosPermitidos(int[] destinosPermitidos) {
		this.destinosPermitidos = new int[destinosPermitidos.length];
		System.arraycopy(destinosPermitidos, 0, this.destinosPermitidos, 0, destinosPermitidos.length);
	}
	public void setDestinosPermitidos(int i, int destino) {	this.destinosPermitidos[i] = destino; }
}
