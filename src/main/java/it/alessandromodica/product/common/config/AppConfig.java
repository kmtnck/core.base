package it.alessandromodica.product.common.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import it.alessandromodica.product.app.GoToBusiness;
import it.alessandromodica.product.app.MainApplication;
import it.alessandromodica.product.common.exceptions.BusinessException;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages="it.alessandromodica.product")
//@EnableWebMvc
public class AppConfig {

	/**
	 * Metodo per inizializzare i parametri di configurazione del client sso
	 * 
	 * Inizializza il logger Definisce i parametri di configurazione usati
	 * dall'applicazione
	 * @throws BusinessException 
	 * 
	 */
	public AppConfig() throws BusinessException
	{
		InitApp();
	}

	public static void InitApp() throws BusinessException {

		GoToBusiness.TITOLO_APP = "Una nuova app che fa cose";

		MainApplication.InitApp("appjpa-mysql");

	}
	
}
