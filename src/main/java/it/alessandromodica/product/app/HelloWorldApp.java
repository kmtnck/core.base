package it.alessandromodica.product.app;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.BusinessException;

@Component
public class HelloWorldApp extends MainApplication {

	/**
	 * Metodo di inizializzazione del contesto applicativo. Inizializza il log4j e
	 * crea il manager session factory hibernate per accerere allo storage.
	 * 
	 * Questo metodo potrebbe inizializzare tutto cio che potrebbe servire ad una
	 * applicazione piu complessa.
	 * 
	 * @throws BusinessException
	 */
	public static void InitApp() throws BusinessException {

		TITOLO_APP = "Una nuova app che fa cose";

		InitApp("appjpa-mysql");

	}

}
