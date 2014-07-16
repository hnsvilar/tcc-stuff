package damas.gui.testes;

import damas.game.fsm.FiniteStateMachine;

public class TestandoFimJogo {
	public static void main(String[] args) {
		FiniteStateMachine fsm = new FiniteStateMachine();
		System.out.println(fsm.fimDeJogo(true,  FiniteStateMachine.VEZ_COMPUTADOR));
	}
}
