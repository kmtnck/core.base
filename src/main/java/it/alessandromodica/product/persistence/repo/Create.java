package it.alessandromodica.product.persistence.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.ICommand;

/**
 * Classe che rappresenta una operazione di creazione di una entita sul
 * datastorage.
 * 
 * @author Alessandro
 *
 * @param <T>
 */
@Component
public class Create<T> implements ICommand<T> {

	@Override
	public void execute(T entity, EntityManager manager) throws RepositoryException {
		manager.persist(entity);
	}

}
