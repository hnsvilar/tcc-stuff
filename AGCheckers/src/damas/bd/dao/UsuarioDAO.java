package damas.bd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import damas.bd.TransactionManager;
import damas.bd.core.AtributosPartida;
import damas.bd.core.Configuracao;
import damas.bd.core.Posicao;
import damas.bd.core.Usuario;

public class UsuarioDAO {  	
	
	public void insertTeste(Usuario u) {		
		Session sessao = TransactionManager.getSession();
		sessao.save(u);  
	}
	
	public void insertUsuario(Usuario u) {
		//Verifica se existe
		Session sessao = TransactionManager.getSession(); 
		Criteria crit = sessao.createCriteria(Usuario.class).add(Restrictions.like("nome", u.getNome()));
		Usuario aux = (Usuario) crit.uniqueResult();
		if(aux == null) {			
			sessao.save(u);  
		}
	} 
	
	public void updateUsuario(Usuario u){  
		Session sessao = TransactionManager.getSession();
		sessao.update(u);  		
	} 

	public Usuario retrieveUsuario(Integer pk){  
		Session sessao = TransactionManager.getSession(); 
		Usuario u = (Usuario)sessao.load(Usuario.class, pk);
		u.setConfiguracoes(getConfiguracoes(u.getNome()));
		return u;  
	}  
	
	public Usuario retrieveUsuarioByName(String nome){ 		
		Session sessao = TransactionManager.getSession(); 
		Criteria crit = sessao.createCriteria(Usuario.class).add(Restrictions.like("nome", nome));
		Usuario u = (Usuario) crit.uniqueResult();
		u.getAtributosPartidas();
		ArrayList<Configuracao> confs = getConfiguracoes(nome);
		u.setConfiguracoes(confs);
		for(Configuracao c : confs) {
			c.addUsuario(u);
		}
		return u;  
	}  

	@SuppressWarnings("deprecation")
	public ArrayList<Configuracao> getConfiguracoes(String usuario) {
		Session sessao = TransactionManager.getSession();		
		PreparedStatement preparedStatement;
		ResultSet rs;		
		
		ArrayList<Configuracao> array = new ArrayList<Configuracao>();		

		try {
			String getConfiguracao = "SELECT id_conf, resultado  FROM usuario_configuracao as uc, configuracao as c, usuario as u " +
					"where uc.id_configuracao_fk = c.id_conf and uc.id_usuario_fk = u.id_usuario  and u.nome = '"+usuario+"'";
			preparedStatement = sessao.connection().prepareStatement(getConfiguracao);
			rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				Configuracao c = new Configuracao();
				c.setId(rs.getInt(1));
				String getPosicao = "SELECT id_posicao, posicao, peca " +
						"FROM posicao as p, posicao_configuracao as pc " +
						"WHERE p.id_posicao = pc.id_posicao_fk and pc.id_configuracao_fk = '"+rs.getInt(1)+"'";
				preparedStatement = sessao.connection().prepareStatement(getPosicao);
				ResultSet rs2 = preparedStatement.executeQuery();
				while(rs2.next()) {
					Posicao p = new Posicao(rs2.getInt(2), rs2.getString(3).charAt(0));
					p.setId(rs2.getInt(1));
					c.addPosicao(p);
					p.addConfiguracao(c);
				}
				c.setResultado(rs.getInt(2));
				array.add(c);
			}			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;  
	}
		
	public ArrayList<Usuario> getListUsuario(){  
		Session sessao = TransactionManager.getSession(); 
		Criteria crit = sessao.createCriteria(Usuario.class);
		@SuppressWarnings("unchecked")
		ArrayList<Usuario> u = (ArrayList<Usuario>) crit.list();
		return u;  
	} 

	public void deleteUsuario(Usuario u){  
		if(u != null) {
			Session sessao = TransactionManager.getSession(); 
			sessao.delete(u);  
		}
	}  
}
