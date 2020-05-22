package it.alessandromodica.product.persistence.interfaces;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.repo.AppRepository;

@Component
public interface IRepositoryCommands<T> {

	public AppRepository<T> setEntity(Class<T> classEntity);
	
	public void add(T obj) throws RepositoryException;

	public void delete(T obj) throws RepositoryException;

	public void deleteAll() throws RepositoryException;

	public void deleteFromId(Object id, String nameField) throws RepositoryException;
	
	public void update(T obj) throws RepositoryException;
	
	public void executeTransaction(IBulkTransaction bulkoperation) throws RepositoryException;
}

