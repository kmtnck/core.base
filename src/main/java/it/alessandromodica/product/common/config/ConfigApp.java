package it.alessandromodica.product.common.config;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="it.alessandromodica.product")
public class ConfigApp {

	private static final Logger log = Logger.getLogger(ConfigApp.class);

	/**
	 * Metodo per inizializzare i parametri di configurazione del client sso
	 * 
	 * Inizializza il logger Definisce i parametri di configurazione usati
	 * dall'applicazione
	 * 
	 */
	public ConfigApp()
	{
		DOMConfigurator.configure(ConfigApp.class.getResource("log4j.xml"));
		log.info("Istanziato il logger");
	}
}
