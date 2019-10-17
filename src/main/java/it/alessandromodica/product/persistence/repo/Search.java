package it.alessandromodica.product.persistence.repo;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.ISearcher;

/**
 * Classe che rappresenta tutte le operazioni di ricerca sul datastorage di una entita T
 * 
 * @author Alessandro
 *
 * @param <T>
 */
@Component
public class Search<T> implements ISearcher<T> {

	@SuppressWarnings("unchecked")
	@Override
	public List<T> search(Query query) throws RepositoryException {
		return query.getResultList();
	}
}
