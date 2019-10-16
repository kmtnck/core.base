package it.alessandromodica.product.persistence.interfaces;

import it.alessandromodica.product.common.exceptions.RepositoryException;

public interface IUnitOfWork {

	public void submit(IBulkTransaction instructions) throws RepositoryException;

	public void submitNoTransaction(IBulkTransaction instructions) throws RepositoryException;


}
