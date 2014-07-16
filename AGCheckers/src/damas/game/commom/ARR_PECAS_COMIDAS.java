package damas.game.commom;

public class ARR_PECAS_COMIDAS {
	/**
	 * Atributos
	 */
	int ARR_PECAS_COMIDAS[];
	int owner[];

	//------------------------------------------------------------------	
	
	/**
	 * Construtor
	 */
	public ARR_PECAS_COMIDAS(int n) {
		ARR_PECAS_COMIDAS = new int[n];
		owner = new int[n];
		for (int i = 0; i < n; i++)
			this.setOwner(i, -1);
	}

	//------------------------------------------------------------------
	
	/**
	 * Getters e Setters
	 */
	
	public int getARR_PECAS_COMIDAS(int i) { return ARR_PECAS_COMIDAS[i]; }
	public void setARR_PECAS_COMIDAS(int i, int peca_comida) { ARR_PECAS_COMIDAS[i] = peca_comida; }
	public int getOwner(int i) { return owner[i]; }
	public void setOwner(int i, int owner) { this.owner[i] = owner; }
}