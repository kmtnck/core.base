package it.alessandromodica.product.callback;

import javax.persistence.EntityManager;

import it.alessandromodica.product.app.GoToBusiness;
import it.alessandromodica.product.common.exceptions.RepositoryException;

/**
 * Callback comune in cui poter eseguire set di istruzioni in transazione
 * @author Alessandro
 *
 */
public abstract class CallbackCommon extends GoToBusiness{

	/*public void setUtenteCorrente(BOUtente utenteCorrente) {
		this.utenteCorrente = utenteCorrente;
	}*/
	
	public void persistNoTransaction(EntityManager em) throws RepositoryException {
	}

}
