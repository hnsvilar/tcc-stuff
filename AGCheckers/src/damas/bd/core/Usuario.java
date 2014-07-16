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
@Table(name="usuario")
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id  
	@Column(name="id_usuario")	
	@GeneratedValue  
	private Integer id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "vitorias")
	private Integer vitorias;
	
	@Column(name = "derrotas")
	private Integer derrotas;
	
	//@Fetch(FetchMode.JOIN)
	@ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(name = "usuario_configuracao", 
		joinColumns = @JoinColumn(name = "id_usuario_fk"), 
		inverseJoinColumns = @JoinColumn(name = "id_configuracao_fk"))
	private List<Configuracao> configuracoes;
	 
	@OneToMany(targetEntity=AtributosPartida.class, mappedBy = "usuario", fetch = FetchType.EAGER)  
	private List<AtributosPartida> atributosPartidas;
	
	@Transient
	private double classificacao = -1;
	
	/**
	 * @return Returns the matricula.
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param matricula The matricula to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return Returns the turmas.
	 */
	public List<Configuracao> getConfiguracoes() {
		return configuracoes;
	}
	
	public Usuario() {
		configuracoes = new ArrayList<Configuracao>();
	}
	/**
	 * @param turmas The turmas to set.
	 */
	public void setConfiguracoes(ArrayList<Configuracao> configuracoes) {
		this.configuracoes = configuracoes;
	}
	
//	public String toString() {
//		return this.getNome() + " " + this.getMatricula() + " " + this.getEndereco();
//	}
	
	public Integer getDerrotas() {
		return derrotas;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getVitorias() {
		return vitorias;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public List<AtributosPartida> getAtributosPartidas() {
		return atributosPartidas;
	}
	
	public void setDerrotas(Integer derrotas) {
		this.derrotas = derrotas;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setVitorias(Integer vitorias) {
		this.vitorias = vitorias;
	}
	
	public void setAtributosPartidas(ArrayList<AtributosPartida> arrayList) {
		this.atributosPartidas = arrayList;
	}
	
	public void addAtributo(AtributosPartida a) {
		this.atributosPartidas.add(a);
	}
	
	public void addConfiguracao(Configuracao c) {
		this.configuracoes.add(c);
	}
	
	public double getClassificacao() {
		return classificacao;
	}
	
	public void setClassificacao(double classificacao) {
		this.classificacao = classificacao;
	}
	
	public String toString() {
		return id +" - "+nome+" - Derrotas: "+derrotas+" - Vitorias: "+vitorias;
	}
}
 
