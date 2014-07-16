package damas.bd.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * 
 * @author Gabb
 *
 */
@Entity
@Table(name="configuracao")
public class Configuracao implements Serializable{
	public static final int DERROTA = 0;
	public static final int VITORIA = 1;
	public static final int EMPATE = 2;
	
	private static final long serialVersionUID = 1L;
 	
	@Column(name="resultado")
	private int resultado;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable
	(
		name = "usuario_configuracao",
		joinColumns = @JoinColumn(name = "id_configuracao_fk"),
		inverseJoinColumns = @JoinColumn(name = "id_usuario_fk")
	)
	private List<Usuario> usuarios;
	
	//@Fetch(FetchMode.JOIN)
	@ManyToMany(cascade = CascadeType.ALL)//(fetch=FetchType.EAGER)
	@JoinTable
	(
		name = "posicao_configuracao",
		joinColumns = @JoinColumn(name = "id_configuracao_fk"),
		inverseJoinColumns = @JoinColumn(name = "id_posicao_fk")
	)
	private List<Posicao> posicoes;
	 

	@Id
	@GeneratedValue
	@Column(name="id_conf")
	private Integer id;
	
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}	
	
	public Configuracao() {
		usuarios = new ArrayList<Usuario>();
		posicoes = new ArrayList<Posicao>();
	}
	
	public Configuracao(int r) {
		this.resultado = r;
		usuarios = new ArrayList<Usuario>();
		posicoes = new ArrayList<Posicao>();
	}
	
	/**
	 * @return Returns the alunos.
	 */
	public ArrayList<Usuario> getUsuarios() {
		return (ArrayList<Usuario>) usuarios;
	}
	/**
	 * @param alunos The alunos to set.
	 */
	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public int getResultado() {
		return resultado;
	}
	
	public void setResultado(int resultado) {
		this.resultado = resultado;
	}
	
	public void addUsuario(Usuario u) {
		usuarios.add(u);
	}
	
	public List<Posicao> getPosicoes() {
		return posicoes;
	}
	
	public void setPosicoes(List<Posicao> posicoes) {
		this.posicoes = posicoes;
	}
	
	public void addPosicao(Posicao p) {
		posicoes.add(p);
	}
	
	public void addPosicao(int p, char v) {
		Posicao aux = new Posicao(p,v);
		posicoes.add(aux);
	}
	
	
	public String toString() {
		String s = "";
		for(Posicao p : posicoes) {
			s+= p.toString()+"; ";
		}
		return s;		
	}
}
