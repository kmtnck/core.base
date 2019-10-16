package it.alessandromodica.product.persistence.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.ICommand;

@Component
public class Update<T> implements ICommand<T>{

	@Override
	public void execute(T entity, EntityManager manager) throws RepositoryException {
		manager.merge(entity);

	}

}
