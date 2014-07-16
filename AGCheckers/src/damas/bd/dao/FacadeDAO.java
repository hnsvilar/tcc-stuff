package damas.bd.dao;

import java.util.ArrayList;

import damas.bd.core.AtributosPartida;
import damas.bd.core.Configuracao;
import damas.bd.core.Posicao;
import damas.bd.core.Usuario;

public class FacadeDAO {
	
	//usuario
	public void insertUsuario(Usuario u){  
		UsuarioDAO ud = new UsuarioDAO();
		ud.insertUsuario(u); 
	} 
	
	public void updateUsuario(Usuario u){  
		UsuarioDAO ud = new UsuarioDAO();
		ud.updateUsuario(u);  
	} 

	public Usuario retrieveUsuario(Integer pk){  
		UsuarioDAO ud = new UsuarioDAO();
		return ud.retrieveUsuario(pk);    
	}  
	
	public Usuario retrieveUsuarioByName(String nome){  
		UsuarioDAO ud = new UsuarioDAO();
		return ud.retrieveUsuarioByName(nome);  
	}  

	public ArrayList<Usuario> getListUsuario(){  
		UsuarioDAO ud = new UsuarioDAO();
		return ud.getListUsuario();
	} 

	public void deleteUsuario(Usuario u){  
		UsuarioDAO ud = new UsuarioDAO();
		ud.deleteUsuario(u);  
	}
	
	public void deleteUsuarioByName(String nome){
		UsuarioDAO ud = new UsuarioDAO();
		ud.deleteUsuario(ud.retrieveUsuarioByName(nome));  
	}
	
	//configuracao
	public void insertConfiguracao(Configuracao c){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();
		cd.insertConfiguracao(c);
	}  
	
	public void updateConfiguracao(Configuracao c){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();
		cd.updateConfiguracao(c);
	}  

	public Configuracao retrieveConfiguracao(Integer pk){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();  
		return cd.retrieveConfiguracao(pk);  
	}  
	
	public Configuracao retrieveByAntes(String antes){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();  
		return cd.retrieveByAntes(antes);  
	} 
	
	public ArrayList<Configuracao> getListConfiguracao(){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();
		return cd.getListConfiguracao();  
	}
	
	public ArrayList<Configuracao> getListByAntes(String antes){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();
		return cd.getListByAntes(antes);  
	} 
	
	public ArrayList<Configuracao> getListByUsuario(String usuario){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();
		return cd.getListByUsuario(usuario);  
	} 

	public void deleteConfiguracao(Configuracao c){  
		ConfiguracaoDAO cd = new ConfiguracaoDAO();
		cd.deleteConfiguracao(c);
	}  
	
	//POSICAO
	
	public void insertPosicao(Posicao c){  
		PosicaoDAO dao = new PosicaoDAO();
		dao.insertPosicao(c); 
	}
	
	public void insertPosicao(int posicao, char valor){  
		PosicaoDAO dao = new PosicaoDAO();
		dao.insertPosicao(posicao,valor); 
	}
	
	public void updatePosicao(Posicao c){  
		PosicaoDAO dao = new PosicaoDAO();
		dao.updatePosicao(c);
	}  

	public Posicao retrievePosicao(Integer pk){  
		PosicaoDAO dao = new PosicaoDAO();
		return dao.retrievePosicao(pk);  
	}  
	
	public Posicao retrievePosicaoByPosicao(int posicao){  
		PosicaoDAO dao = new PosicaoDAO();
		return dao.retrievePosicaoByPosicao(posicao);  
	} 
	
	public ArrayList<Posicao> getListPosicao(){  
		PosicaoDAO dao = new PosicaoDAO();
		return dao.getListPosicao();  
	}
	
	public void deletePosicao(Posicao c){
		PosicaoDAO dao = new PosicaoDAO();
		dao.deletePosicao(c);
	}
	
	//atributo
		public void insertAtributo(AtributosPartida u){  
			AtributosDAO ud = new AtributosDAO();
			ud.insertAtributo(u); 
		} 
		
		public void updateAtributo(AtributosPartida u){  
			AtributosDAO ud = new AtributosDAO();
			ud.updateAtributosPartida(u);  
		} 

		public void deleteAtributo(AtributosPartida u){  
			AtributosDAO ud = new AtributosDAO();
			ud.deleteAtributosPartida(u);  
		}
}

