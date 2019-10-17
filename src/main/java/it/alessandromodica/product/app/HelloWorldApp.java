package it.alessandromodica.product.app;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.BusinessException;

@Component
public class HelloWorldApp extends MainApplication {

	/**
	 * E' un metodo a titolo di esempio su cui la convivenza tra esigenze legacy
	 * e attuali è sempre fattibile
	 * 
	 * Si vuole adattare un set di viste particolarmente complesse e delegare a
	 * hibernate la sola mappatura del resultset Con hdbsql configurato in
	 * modalità default, ad ogni creazione dello schema vengono create le viste
	 * mappate dalle entità opportune
	 * 
	 * Un test esemplifica questo flusso di innesto chiamato
	 * testHelloWorldUtentiConnessi
	 * 
	 * La MainApplication è il punto di partenza per implementare moduli e
	 * componenti personalizzati. Un esempio è il modulo consultazione, ma è
	 * solo un esempio.
	 * 
	 * @throws BusinessException
	 */

	public static void InitApp() throws BusinessException {

		TITOLO_APP = "Una nuova app che fa cose";
		
		InitApp("appjpa-mysql");

	}

}
