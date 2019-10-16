package it.alessandromodica.product.persistence.interfaces;

import javax.persistence.EntityManager;

import it.alessandromodica.product.common.exceptions.RepositoryException;

public interface ICommand<T> {
	public void execute(T entity, EntityManager manager) throws RepositoryException;
}