package damas.bd.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="atributos")
public class AtributosPartida {
	private static final long serialVersionUID = 1L;
	@Id  
	@Column(name="id_atributo")	
	@GeneratedValue  
	private Integer id;
	
	@Column(name = "time_stamp")
	private String data;
	
	@Column(name = "resultado")
	private int resultado;
	
	@Column(name = "num_pecas_aliadas")
	private int numPecasAliadas;
	
	@Column(name = "num_damas_aliadas")
	private int numDamasAliadas;
	
	@Column(name = "num_pecas_inimigas")
	private int numPecasInimigas;
	
	@Column(name = "num_damas_inimigas")
	private int numDamasInimigas;
	
	@Column(name = "duracao")
	private long duracao;
	
	@ManyToOne  
    @JoinColumn(name="id_usuario_fk",updatable=true,insertable=true,nullable=false)  
    @Fetch(FetchMode.JOIN)  
    @Cascade(CascadeType.ALL)  
	private Usuario usuario;
	
	@Column(name = "quant_lances")
	private int quantLances;
	
	public AtributosPartida() {
		
	}
	
	public AtributosPartida(String data, int numDamasAliadas, int numPecasAliadas, int numDamasInimigas,
			int NumPecasInimigas, long duracao, int resultado, int quantLances) {
		this.data = data;
		this.numDamasAliadas = numDamasAliadas;
		this.numDamasInimigas = numDamasInimigas;
		this.numPecasAliadas = numPecasAliadas;
		this.numPecasInimigas = NumPecasInimigas;
		this.duracao = duracao;
		this.resultado = resultado;
		this.quantLances = quantLances;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getData() {
		return data;
	}
	
	public long getDuracao() {
		return duracao;
	}
	
	public int getNumDamasAliadas() {
		return numDamasAliadas;
	}
	
	public int getNumDamasInimigas() {
		return numDamasInimigas;
	}
	
	public int getNumPecasAliadas() {
		return numPecasAliadas;
	}
	
	public int getNumPecasInimigas() {
		return numPecasInimigas;
	}
	
	public int getResultado() {
		return resultado;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public int getQuantLances() {
		return quantLances;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public void setDuracao(long duracao) {
		this.duracao = duracao;
	}
	
	public void setNumDamasAliadas(int numDamasAliadas) {
		this.numDamasAliadas = numDamasAliadas;
	}
	
	public void setNumDamasInimigas(int numDamasInimigas) {
		this.numDamasInimigas = numDamasInimigas;
	}
	
	public void setNumPecasAliadas(int numPecasAliadas) {
		this.numPecasAliadas = numPecasAliadas;
	}
	
	public void setNumPecasInimigas(int numPecasInimigas) {
		this.numPecasInimigas = numPecasInimigas;
	}
	
	public void setResultado(int resultado) {
		this.resultado = resultado;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setQuantLances(int quantLances) {
		this.quantLances = quantLances;
	}
	
	public String toString() {
		return data+" DA "+numDamasAliadas+" PA "+numPecasAliadas+" DI "+numDamasInimigas+" PI "+
	numPecasInimigas + " R: "+resultado+" Dur: "+duracao + " QuantLances: "+quantLances;
	}

	public static String dateToString(Date d) {
		String data = d.getDay()+"/"+d.getMonth()+"/"+d.getYear()+";"
				+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
		return data;
	}
}
