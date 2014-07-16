package damas.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import damas.data.GerenciadorLog.Tipo;

public class LoggerTempo {
	private ArrayList<Long> tempos = null;
	
	public LoggerTempo() {
		tempos = new ArrayList<Long>();
	}
	
	public void soma(long l) {
		tempos.add(l);
	}
	
	public double getMedia() {
		double soma = 0;
		for(Long i : tempos) {
			soma += i;
		}
		soma = soma/tempos.size();
		return soma;
	}
	
	public void salvaLog(String nome) {
//		Logger logger = Logger.getLogger(LoggerTempo.class.getName());
//		logger.setLevel(Level.INFO);
//		FileHandler fileTxt;
//		try {
//			fileTxt = new FileHandler("LogTempo/LoggingTempo"+nome+".txt");
//			
//			// Create txt Formatter
//			SimpleFormatter formatterTxt = new SimpleFormatter();
//			fileTxt.setFormatter(formatterTxt);
//			logger.addHandler(fileTxt);
//			
//			logger.info(getMedia() + "\n");
//			
//			tempos = new ArrayList<Long>();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		GerenciadorLog g = new GerenciadorLog(nome);
		try {
			g.grava(String.valueOf(getMedia()), nome, Tipo.TEMPO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
