package damas.bd.core;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
//import org.hibernate.cfg.Configuration;

/**
 * @author Maurício
 *
 * Código desenvolvido por Maurício Linhares
 * 
 */
@SuppressWarnings("deprecation")
public class HibernateUtility {

    private static SessionFactory factory;
        
    static {
    	//Bloco estatico que inicializa o Hibernate
    	try {
    		
    		AnnotationConfiguration cfg = new AnnotationConfiguration();
    		cfg.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
    		cfg.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
    		cfg.setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/agcheckers?autoReconnect=true");
    		cfg.setProperty("hibernate.connection.username", "root");
    		cfg.setProperty("hibernate.connection.password", "123456");
    		cfg.addAnnotatedClass(Configuracao.class);
    		cfg.addAnnotatedClass(Usuario.class);
    		cfg.addAnnotatedClass(Posicao.class);
    		cfg.addAnnotatedClass(AtributosPartida.class);
//    		cfg.addAnnotatedClass(Professor.class);
//    		cfg.addAnnotatedClass(Turma.class);
//    		cfg.addAnnotatedClass(Curso.class);
//    		cfg.addAnnotatedClass(Disciplina.class);
    		
    		
    	factory = cfg.buildSessionFactory();
        	//.addResource("test/animals/orm.xml")
    	
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		factory = null;
    	}
    }
    
    public static SessionFactory getFactory() {
		return factory;
	}
    
    public static Session getSession() {
        //Retorna a sessão aberta
    	return factory.openSession();
        
    }
    
}
