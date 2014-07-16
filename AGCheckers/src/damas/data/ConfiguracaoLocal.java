package damas.data;

import java.util.ArrayList;

import javax.print.attribute.standard.Media;

public class ConfiguracaoLocal {
	private static  int vitorias;
	private static  int derrotas;
	private static  double somaAdaptacaoSemEquacao = 0.0;
	private static  double somaAdaptacaoComEquacao = 0.0;
	private static  double somaAdaptacaoSelecionados = 0.0;
	private static  int quantAdaptacaoSemEquacao = 0;
	private  static int quantAdaptacaoComEquacao = 0;
	private  static int quantAdaptacaoSelecionados = 0;
	
	private static String resultado;
	private static ConfiguracaoLocal instance = null;
	
	private ConfiguracaoLocal() {
		vitorias = derrotas = 0;
		zera();
	}	
	
	public static ConfiguracaoLocal getInstance() {
		if(instance == null) {
			instance = new ConfiguracaoLocal();
		}
		return instance;
	}
	
	public void setResultado(String resultado) {
		ConfiguracaoLocal.resultado = resultado;
	}
	
	public String getResultado() {
		return resultado;
	}
	
	public void addVitoria() {
		vitorias++;
	}
	
	public void addDerrota() {
		derrotas--;
	}
	
	public void zera() {
		somaAdaptacaoComEquacao = 0.0;
		somaAdaptacaoSelecionados = 0.0;
		somaAdaptacaoSemEquacao = 0.0;
		quantAdaptacaoComEquacao = 0;
		quantAdaptacaoSelecionados = 0;
		quantAdaptacaoSemEquacao = 0;
	}
	
	public void somaComEquacao(double v) {
		somaAdaptacaoComEquacao = somaAdaptacaoComEquacao + v;
		quantAdaptacaoComEquacao++;
	}
	
	public void somaSemEquacao(double v) {
		somaAdaptacaoSemEquacao = somaAdaptacaoSemEquacao + v;
		quantAdaptacaoSemEquacao++;
	}
	
	public void somaSelecionados(double v) {
		somaAdaptacaoSelecionados = somaAdaptacaoSelecionados + v;
		quantAdaptacaoSelecionados++;
	}
	
	public int getDerrotas() {
		return derrotas;
	}
	
	public int getVitorias() {
		return vitorias;
	}
	
	public int getQuantAdaptacaoComEquacao() {
		return quantAdaptacaoComEquacao;
	}
	
	public int getQuantAdaptacaoSelecionados() {
		return quantAdaptacaoSelecionados;
	}
	
	public int getQuantAdaptacaoSemEquacao() {
		return quantAdaptacaoSemEquacao;
	}
	
	public double getSomaAdaptacaoComEquacao() {
		return somaAdaptacaoComEquacao;
	}
	
	public double getSomaAdaptacaoSelecionados() {
		return somaAdaptacaoSelecionados;
	}
	
	public double getSomaAdaptacaoSemEquacao() {
		return somaAdaptacaoSemEquacao;
	}

	public double getMediaSemEquacao() {
		double d = (double)(somaAdaptacaoSemEquacao / quantAdaptacaoSemEquacao);
		return d;
	}
	
	public double getMediaComEquacao() {
		double d = (double)(somaAdaptacaoComEquacao / quantAdaptacaoComEquacao);
		System.out.println("soma adaptacao" + somaAdaptacaoComEquacao);
		System.out.println(quantAdaptacaoComEquacao);
		return d;
	}
	
	public double getMediaSelecionados() {
		double d = (double)(somaAdaptacaoSelecionados / quantAdaptacaoSelecionados);
		return d;
	}
	
}
