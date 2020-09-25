package it.alessandromodica.product.callback;

import javax.persistence.EntityManager;

import it.alessandromodica.product.persistence.exceptions.RepositoryException;

/**
 * Callback comune in cui poter eseguire set di istruzioni in transazione
 * 
 * @author Alessandro
 *
 */
public abstract class CallbackCommon {

	/*
	 * public void setUtenteCorrente(BOUtente utenteCorrente) { this.utenteCorrente
	 * = utenteCorrente; }
	 */

	public void persistNoTransaction(EntityManager em) throws RepositoryException {
	}

}
