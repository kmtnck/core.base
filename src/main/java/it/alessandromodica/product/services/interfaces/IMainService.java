package it.alessandromodica.product.services.interfaces;

import java.util.List;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.GestioneUtenti;
import it.alessandromodica.product.persistence.searcher.BOSerializeCriteria;

public interface IMainService<T> {

	public GestioneUtenti getUtente(String nickname) throws ServiceException;

	public void logAccesso(String messaggio, BOUtente utentecorrente) throws ServiceException;

	List<T> search(BOSerializeCriteria searcher) throws RepositoryException;

	int count(BOSerializeCriteria searcher) throws RepositoryException;
	
	public T getById(Object objId, Class<T> classEntity) throws RepositoryException;

	public void add(T obj) throws RepositoryException;
}
