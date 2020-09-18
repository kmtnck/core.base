package it.alessandromodica.product.views;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.alessandromodica.product.common.config.SpringConfig;

/**
 * Classe listener che estende il classico ServletContextListener, che viene
 * chiamato dall'application server (Tomcat) al momento dell'avvio
 * dell'applicazione.
 * 
 * @author Alessandro
 *
 */
@Deprecated
public class AppViewListener implements ServletContextListener {

	private static final Logger log = Logger.getLogger(AppViewListener.class);

	
	public static AnnotationConfigApplicationContext context;

	/**
	 * Inizializza il contesto web del client sso
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		try {

			context = new AnnotationConfigApplicationContext(SpringConfig.class);
		} catch (Exception e) {
			// il log potrebbe non essere stato istanziato, pertanto si utilizza
			// il printstacktrace
			log.error(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		context.close();
	}

}
