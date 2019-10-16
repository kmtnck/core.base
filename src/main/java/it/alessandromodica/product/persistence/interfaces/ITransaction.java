package it.alessandromodica.product.persistence.interfaces;

import javax.persistence.EntityManager;

import it.alessandromodica.product.common.exceptions.RepositoryException;

public interface ITransaction {
	public void execute(EntityManager manager) throws RepositoryException;
}