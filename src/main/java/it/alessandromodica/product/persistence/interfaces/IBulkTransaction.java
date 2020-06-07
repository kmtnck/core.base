package it.alessandromodica.product.persistence.interfaces;

import it.alessandromodica.product.common.exceptions.RepositoryException;

public interface IBulkTransaction {
	
	public void persist() throws RepositoryException;

	//public void setEntities(BOUtente utente);
}

