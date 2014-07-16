package damas.bd;

import org.hibernate.Session;
import org.hibernate.Transaction;

import damas.bd.core.HibernateUtility;

public class TransactionManager {
	private static TransactionManager instance = null;
	private static Session sessao = null;  
	private static Transaction transaction = null;
	
	private TransactionManager() {}
	
	public static TransactionManager getInstance() {
		if(instance == null) {
			instance = new TransactionManager();
		}
		return instance;
	}
	
	
	public static Transaction getTransaction() {
		return transaction;
	}
	
	public static Session getSession() {
		return sessao;
	}
	
	public void begin() {
		sessao = HibernateUtility.getSession();  
		transaction = sessao.beginTransaction();
	}
	
	public void commit() {
		sessao.flush();  
		transaction.commit(); 		
		sessao.close(); 
	}
	
	public void rollback() {
		sessao.flush();  
		transaction.rollback(); 		
		sessao.close(); 
	}
	
	
}
