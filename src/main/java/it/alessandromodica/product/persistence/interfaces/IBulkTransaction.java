package it.alessandromodica.product.persistence.interfaces;

import java.util.List;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.model.bo.BOUtente;

public interface IBulkTransaction {
	
	public void persist() throws RepositoryException;

	@SuppressWarnings("rawtypes")
	public void setEntities(List obj, BOUtente utente);
}

