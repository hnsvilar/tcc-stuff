package damas.bd;

import damas.bd.dao.ConfiguracaoDAO;
import damas.bd.dao.UsuarioDAO;
import damas.bd.dao.PosicaoDAO;
import damas.bd.dao.AtributosDAO;

public aspect AspectoTransacao {
	pointcut transacaoOp() : execution (* ConfiguracaoDAO.*(..)) || 
		(execution (* UsuarioDAO.*(..)) && !execution(* UsuarioDAO.getConfiguracoes(..))) || 
			execution (* PosicaoDAO.*(..))|| 
			execution (* AtributosDAO.*(..));
	 

	Object around() : transacaoOp()  {
		TransactionManager t = TransactionManager.getInstance();
		t.begin();
		try {
			Object o = proceed();
			t.commit();
			return o;
		} catch (Throwable ex) {
			t.rollback();
			System.err.println(ex.getMessage());
		}
		return null;
	}
}
