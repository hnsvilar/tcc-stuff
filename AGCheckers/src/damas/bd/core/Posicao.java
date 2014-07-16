package damas.bd.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="posicao")
public class Posicao {
		
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
 
	@Column(name = "posicao")
	private int posicao;
	
	@Column(name = "peca")
	private char peca;
	
	@ManyToMany//(fetch = FetchType.EAGER)
	@JoinTable
	(
		name = "posicao_configuracao",
		joinColumns = @JoinColumn(name = "id_posicao_fk"),
		inverseJoinColumns = @JoinColumn(name = "id_configuracao_fk")
	)
	private List<Configuracao> configuracoes;
	 

	@Id
	@GeneratedValue
	@Column(name="id_posicao")
	private Integer id;
	
	public Posicao(int p, char v) {
		this.posicao = p;
		this.peca = v;
		this.configuracoes = new ArrayList<Configuracao>();
	}
	
	public Posicao() {this.configuracoes = new ArrayList<Configuracao>();}
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
	
	public List<Configuracao> getConfiguracoes() {
		return configuracoes;
	}
	
	public void setConfiguracoes(List<Configuracao> configuracoes) {
		this.configuracoes = configuracoes;
	}
	
	public char getPeca() {
		return peca;
	}
	
	public void setPeca(char peca) {
		this.peca = peca;
	}
	
	public int getPosicao() {
		return posicao;
	}
	
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	
	
	
	public void addConfiguracao(Configuracao u) {
		configuracoes.add(u);
	}
	
	public String toString() {
		String s = posicao+":"+peca;
		return s;
	}
	
}
