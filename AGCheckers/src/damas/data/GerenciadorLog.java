package damas.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GerenciadorLog {
	
	public static enum Tipo {
		RESULTADO, DADOS, TEMPO_KING, TEMPO
	}	
		
	BufferedWriter out;
	private static Logger logger1 = null;
	private static Logger logger2 = null;
	private static Logger loggerTempo = null;
	private FileHandler fh1;
	private FileHandler fh2;
	private FileHandler fh3;
	public GerenciadorLog(String usuario) {
		if(logger1 == null) {
			logger1 = Logger.getLogger("gerenciadorLog");  			
			try {
				fh1 = new FileHandler("Log/dados-"+usuario+".log", true);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger1.addHandler(fh1);
			logger1.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh1.setFormatter(formatter);		
		}
		if(logger2 == null) {
			logger2 = Logger.getLogger("Resultados");  			
			try {
				fh2 = new FileHandler("Log/resultados-"+usuario+".log", true);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger2.addHandler(fh2);
			logger2.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh2.setFormatter(formatter);
		}
		if(loggerTempo == null) {
			loggerTempo = Logger.getLogger("Resultados");  			
			try {
				fh3 = new FileHandler("LogTempo/LoggingTempo-"+usuario+".log", true);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loggerTempo.addHandler(fh3);
			loggerTempo.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh3.setFormatter(formatter);
		}
	}
	
	public void grava(String str, String usuario, Tipo tipo) throws IOException {
		if(logger1 != null && logger2 != null && loggerTempo != null) {
			 // This block configure the logger with handler and formatter
			  if(tipo == Tipo.DADOS) {
//			      // the following statement is used to log any messages   
			      logger1.log(Level.INFO,str);
				  out = new BufferedWriter(new FileWriter("Log/dados-"+usuario+".txt",true));
				  out.write(str + "\n");
//				  System.out.println(str);
				  out.close();
			  }
			  else if (tipo == Tipo.RESULTADO) {
			      // the following statement is used to log any messages   
			      logger2.log(Level.INFO,str);
				  out = new BufferedWriter(new FileWriter("Log/resultados-"+usuario+".txt",true));
				  out.write(str + "\n");
//				  System.out.println(str);
				  out.close();
			  } else if(tipo == Tipo.TEMPO) {
				  loggerTempo.log(Level.INFO,str);
//				  out = new BufferedWriter(new FileWriter("Log/resultados-"+usuario+".txt",true));
//				  out.write(str + "\n");
//				  System.out.println(str);
//				  out.close();
			  }
		}
	}

	public void gravaJogadaRei(String str) {
		
	}
}
