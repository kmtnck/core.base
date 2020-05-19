package it.alessandromodica.product.common.config;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import it.alessandromodica.product.app.HelloWorldApp;
import it.alessandromodica.product.common.exceptions.BusinessException;

@Configuration
@ComponentScan(basePackages="it.alessandromodica.product")
public class ConfigApp {

	private static final Logger log = Logger.getLogger(ConfigApp.class);

	/**
	 * Metodo per inizializzare i parametri di configurazione del client sso
	 * 
	 * Inizializza il logger Definisce i parametri di configurazione usati
	 * dall'applicazione
	 * @throws BusinessException 
	 * 
	 */
	public ConfigApp() throws BusinessException
	{
		HelloWorldApp.InitApp();
	}

	public static void initLog() throws FactoryConfigurationError {
		DOMConfigurator.configure(ConfigApp.class.getResource("log4j.xml"));
		log.info("Istanziato il logger");
	}
	
}
