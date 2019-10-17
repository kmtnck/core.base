package it.alessandromodica.product.persistence.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.ICommand;

/**
 * Classe che rappresenta la rimozione di una entita dal datastorage
 * 
 * @author Alessandro
 *
 * @param <T>
 */
@Component
public class Remove<T> implements ICommand<T> {

	@Override
	public void execute(T entity, EntityManager manager) throws RepositoryException {

		manager.remove(manager.contains(entity) ? entity : manager.merge(entity));
	}
}
