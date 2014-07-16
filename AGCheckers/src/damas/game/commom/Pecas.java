package damas.game.commom;

/**
 * 
 * 
 *
 */
public class Pecas {

	/**
	 * Atributos
	 */
	public final static char VAZIO = '-'; // representa uma casa preta jogavel.
	public final static char PAREDE = '#'; // representa uma casa branca nao jogavel.
	public final static char PECA_CAPTURADA_COMPUTADOR = 'X';
	public final static char PECA_CAPTURADA_JOGADOR = 'Y';

	private final static char BRANCA = 'B';
	private final static char PRETA = 'P';
	private final static char DAMA_BRANCA = 'A';
	private final static char DAMA_PRETA = 'E';

	/* as pecas do jogador e do computador sao posteriormente definidas
	tal qual a escolha do usuario de comecar ou nao jogando*/
	public static char PEDRA_JOGADOR;
	public static char DAMA_JOGADOR;
	public static char PEDRA_COMPUTADOR;
	public static char DAMA_COMPUTADOR;

	//----------------------------------------------------------
	
	/**
	 * Getters
	 */
	public static char getBRANCA() { return BRANCA;	}
	public static char getPRETA() { return PRETA; }
	public static char getDAMA_BRANCA() { return DAMA_BRANCA; }
	public static char getDAMA_PRETA() { return DAMA_PRETA; }
}
