package it.alessandromodica.product.persistence.interfaces;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.repo.AppRepository;
import it.alessandromodica.product.persistence.searcher.YAFilterSerializeCriteria;

public interface IRepositoryQueries<T, JOIN> {

	/**
	 * E' possibile definire la classe entita di ricerca tramite il searcher e per
	 * il getById lo si fornisce in input
	 * 
	 * @param classEntity
	 * @return
	 */
	@Deprecated
	public AppRepository<T, JOIN> setEntity(Class<T> classEntity);
	
	public T getById(Object objId, Class<T> classEntity) throws RepositoryException;
	
	public List<T> search(CriteriaQuery<T> criteria) throws RepositoryException;

	public List<T> search(YAFilterSerializeCriteria serializeCriteria) throws RepositoryException;

	public T getSingle(CriteriaQuery<T> criteria) throws RepositoryException ;
	
	public T getSingle(YAFilterSerializeCriteria serializeCriteria) throws RepositoryException;
	
	public T getSingleOrDefault(CriteriaQuery<T> criteria) throws RepositoryException;
	
	public T getSingleOrDefault(YAFilterSerializeCriteria serializeCriteria) throws RepositoryException;
	
	public T getFirst(CriteriaQuery<T> criteria) throws RepositoryException ;
	
	public T getFirst(YAFilterSerializeCriteria serializeCriteria) throws RepositoryException;
	
	public T getFirstOrDefault(CriteriaQuery<T> criteria) throws RepositoryException;
	
	public T getFirstOrDefault(YAFilterSerializeCriteria serializeCriteria) throws RepositoryException;
	
	int getCount(YAFilterSerializeCriteria serializeCriteria) throws RepositoryException;

	Number getMax(String nameField) throws RepositoryException;

}
