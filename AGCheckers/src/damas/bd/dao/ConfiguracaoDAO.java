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
import damas.bd.core.Configuracao;
import damas.bd.core.HibernateUtility;
import damas.bd.core.Posicao;
import damas.bd.core.Usuario;

public class ConfiguracaoDAO {  

	public void insertConfiguracao(Configuracao c){  
		Session sessao = TransactionManager.getSession();
		sessao.save(c); 		  
	}

	public void insertConfiguracao(int resultado, Usuario u){  
		Configuracao c = new Configuracao(resultado);
		c.addUsuario(u);
		Session sessao = TransactionManager.getSession();
		sessao.save(c);  		
	}

	public void updateConfiguracao(Configuracao c){  
		Session sessao = TransactionManager.getSession();
		sessao.update(c);  		
	}  

	public Configuracao retrieveConfiguracao(Integer pk){  
		Session sessao = TransactionManager.getSession();
		Configuracao c = (Configuracao)sessao.load(Configuracao.class, pk);  
		return c;  
	}  

	public Configuracao retrieveByAntes(String antes){  
		Session sessao = HibernateUtility.getSession();		
		Criteria crit = sessao.createCriteria(Usuario.class).add(Restrictions.like("antes", antes));
		Configuracao u = (Configuracao) crit.uniqueResult();
		return u;  
	} 

	public ArrayList<Configuracao> getListConfiguracao(){  
		Session sessao = HibernateUtility.getSession();		
		Criteria crit = sessao.createCriteria(Usuario.class);
		@SuppressWarnings("unchecked")
		ArrayList<Configuracao> u = (ArrayList<Configuracao>) crit.list();
		return u;  
	}

	public ArrayList<Configuracao> getListByAntes(String antes){  
		Session sessao = HibernateUtility.getSession();		
		Criteria crit = sessao.createCriteria(Usuario.class).add(Restrictions.like("antes", antes));
		@SuppressWarnings("unchecked")
		ArrayList<Configuracao> u = (ArrayList<Configuracao>) crit.list();
		return u;  
	} 

	@SuppressWarnings("deprecation")
	public ArrayList<Configuracao> getListByUsuario(String usuario) {
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

	@SuppressWarnings("deprecation")
	public ArrayList<Configuracao> getListWithPosicaoByUsuario(String usuario) {
		Session sessao = TransactionManager.getSession();		
		PreparedStatement preparedStatement;
		try {
			preparedStatement = sessao.connection().prepareStatement("SELECT id_usuario, nome, vitorias, derrotas, id_conf, " +
					"resultado, id_posicao, posicao, peca FROM usuario_configuracao as uc, configuracao as c, usuario as u, " +
					"posicao_configuracao as pc, posicao as p where uc.id_configuracao_fk = c.id_conf and uc.id_usuario_fk = " +
					"u.id_usuario and pc.id_configuracao_fk = c.id_conf and pc.id_posicao_fk = p.id_posicao and u.nome = '"+ usuario +"'");

			ResultSet rs = preparedStatement.executeQuery();			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Configuracao> u = null;
		return u;  
	}

	public void deleteConfiguracao(Configuracao c){  
		Session sessao = TransactionManager.getSession();		
		sessao.delete(c);  
	}  
	
}
