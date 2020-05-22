package it.alessandromodica.product.persistence.interfaces;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.repo.AppRepository;
import it.alessandromodica.product.persistence.searcher.BOSerializeCriteria;

@Component
public interface IRepositoryQueries<T> {

	public AppRepository<T> setEntity(Class<T> classEntity);
	
	public T getById(int objId) throws RepositoryException;
	
	public T getById(String objId) throws RepositoryException;
	
	public List<T> search(CriteriaQuery<T> criteria) throws RepositoryException;

	public List<T> search(BOSerializeCriteria serializeCriteria) throws RepositoryException;

	public T getSingle(CriteriaQuery<T> criteria) throws RepositoryException ;
	
	public T getSingle(BOSerializeCriteria serializeCriteria) throws RepositoryException;
	
	public T getSingleOrDefault(CriteriaQuery<T> criteria) throws RepositoryException;
	
	public T getSingleOrDefault(BOSerializeCriteria serializeCriteria) throws RepositoryException;
	
	public T getFirst(CriteriaQuery<T> criteria) throws RepositoryException ;
	
	public T getFirst(BOSerializeCriteria serializeCriteria) throws RepositoryException;
	
	public T getFirstOrDefault(CriteriaQuery<T> criteria) throws RepositoryException;
	
	public T getFirstOrDefault(BOSerializeCriteria serializeCriteria) throws RepositoryException;
	
	int getCount(BOSerializeCriteria serializeCriteria) throws RepositoryException;

	Number getMax(String nameField) throws RepositoryException;

}
