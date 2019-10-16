package it.alessandromodica.product.persistence.interfaces;

import java.util.List;

import javax.persistence.Query;

import it.alessandromodica.product.common.exceptions.RepositoryException;

public interface ISearcher<T> {
	public List<T> search(Query query) throws RepositoryException;
}


