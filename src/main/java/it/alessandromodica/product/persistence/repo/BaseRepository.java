package it.alessandromodica.product.persistence.repo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.IBulkTransaction;
import it.alessandromodica.product.persistence.interfaces.IUnitOfWork;
import it.alessandromodica.product.persistence.searcher.BOOperatorClause;
import it.alessandromodica.product.persistence.searcher.BOSearch;
import it.alessandromodica.product.persistence.searcher.BOSerializeCriteria;
import it.alessandromodica.product.persistence.uow.UnitOfWork;

@SuppressWarnings("unchecked")
public abstract class BaseRepository<T>  {

	private static final Logger log = Logger.getLogger(BaseRepository.class);

	protected Class<T> classEntity;

	protected void setClass(Class<T> classEntity) {
		
		if (classEntity != null) {
			this.nameClass = classEntity.getName();
			this.classEntity = classEntity;
		}
	}

	protected String nameClass;

	@Autowired
	protected IUnitOfWork uow;
	
	public void executeTransaction(IBulkTransaction bulkoperation) throws RepositoryException {
		uow.submit(bulkoperation);
	}

	public void executeNoTransaction(IBulkTransaction bulkoperation) throws RepositoryException {
		uow.submitNoTransaction(bulkoperation);
	}

	protected Query buildCriteriaQuery(BOSerializeCriteria serializeCriteria, EntityManager em)
			throws RepositoryException {
		return buildCriteriaQuery(null, serializeCriteria, em);
	}

	protected Query buildCriteriaQuery(String alias, BOSerializeCriteria serializeCriteria, EntityManager em)
			throws RepositoryException {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(classEntity);

		Root<T> root = query.from(classEntity);
		if (alias != null)
			root.alias(alias);

		query = query.select(root);

		List<Predicate> predicates = composeQuery(builder, root, serializeCriteria);
		query.where(predicates.toArray(new Predicate[predicates.size()]));

		List<Order> listsOrder = new ArrayList<Order>(0);
		for (String cOrderBy : serializeCriteria.getListOrderBy()) {
			Order cOrder = null;
			if (serializeCriteria.is_isDescendent()) {
				cOrder = builder.desc(root.get(cOrderBy));
			} else {
				cOrder = builder.asc(root.get(cOrderBy));
			}
			listsOrder.add(cOrder);

		}
		query.orderBy(listsOrder);

		Query resultQuery;
		
		if(serializeCriteria.getMaxResult() > 0)
		{
			Query limitedCriteriaQuery = em.createQuery(query)
				     .setMaxResults(serializeCriteria.getMaxResult()).setFirstResult(serializeCriteria.getFirstResult()); 
			resultQuery = limitedCriteriaQuery;
		}
		else
			resultQuery = em.createQuery(query);
		
		/*
		 * if (serializeCriteria.getMaxResult() > 0)
		 * searchCriteria.setMaxResults(serializeCriteria.getMaxResult()); for
		 * (String cOrderBy : serializeCriteria.getListOrderBy()) { if
		 * (serializeCriteria.is_isDescendent()) {
		 * searchCriteria.addOrder(Order.desc(cOrderBy)); } else {
		 * searchCriteria.addOrder(Order.asc(cOrderBy)); } }
		 */
		return resultQuery;
	}

	protected List<Predicate> buildPredicates(String alias, BOSerializeCriteria serializeCriteria, EntityManager em)
			throws RepositoryException {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(classEntity);
		Root<T> root = query.from(classEntity);
		if (alias != null)
			root.alias(alias);

		List<Predicate> predicates = composeQuery(builder, root, serializeCriteria);

		return predicates;
	}

	@SuppressWarnings("rawtypes")
	private List<Predicate> composeQuery(CriteriaBuilder builder, Root<T> root, BOSerializeCriteria serializeCriteria)
			throws RepositoryException {

		List<Predicate> predicates = new ArrayList<Predicate>(0);

		Map<String, Object> resultEq = serializeCriteria.getListEquals();
		for (Iterator iterEq = resultEq.entrySet().iterator(); iterEq.hasNext();) {
			Entry cEntry = (Entry) iterEq.next();
			String cKey = cEntry.getKey().toString();

			String fieldHB = cKey;
			try {
				BOSearch instance = (BOSearch) serializeCriteria.getClassSearcher().newInstance();
				if (instance.checkCompositeId(cKey))
					fieldHB = "id." + cKey;
			} catch (Exception e) {
				throw new RepositoryException(e);
			}
			predicates.add(builder.equal(root.get(fieldHB), resultEq.get(cKey)));
		}

		for (Map.Entry<String, Boolean> cBool : serializeCriteria.getListValueBool()) {
			predicates.add(builder.equal(root.get(cBool.getKey()), cBool.getValue()));
		}

		for (Map<String, Object> cLike : serializeCriteria.getListLike()) {
			String field = cLike.get(BOSearch.NAME_FIELD).toString();
			Object value = cLike.get(BOSearch.VALUE_FIELD);
			Expression<String> rootField = root.get(field);
			predicates.add(builder.like(rootField, value.toString()));
		}

		for (Map<String, Object> cInsLike : serializeCriteria.getListLikeInsensitive()) {
			String field = cInsLike.get(BOSearch.NAME_FIELD).toString();
			Object value = cInsLike.get(BOSearch.VALUE_FIELD);
			Expression<String> rootField = root.get(field);
			predicates.add(builder.like(builder.lower(rootField), value.toString().toLowerCase()));
		}

		for (Map<String, Object> cBT : serializeCriteria.getListbetween()) {

			Class<?> typeData = (Class) cBT.get(BOSearch.TYPE_DATA);
			String field = cBT.get(BOSearch.NAME_FIELD).toString();
			if (typeData != null && typeData.getName().contains("Date"))
			{
				Expression<Date> rootField = root.get(field);
				Date dateTo = null;
				Date dateFrom = null;
				dateTo = (Date)cBT.get(BOSearch.VALUE_TO);
				dateFrom = (Date)cBT.get(BOSearch.VALUE_FROM);
				predicates.add(builder.between(rootField, dateFrom, dateTo));
			}
			else if (typeData != null && typeData.getName().contains("Integer"))
			{
				Expression<Integer> rootField = root.get(field);
				Integer valueTo = null;
				Integer valueFrom = null;
				valueTo = (Integer)cBT.get(BOSearch.VALUE_TO);
				valueFrom = (Integer)cBT.get(BOSearch.VALUE_FROM);
				predicates.add(builder.between(rootField, valueFrom, valueTo));
				
			}
			else if (typeData != null && typeData.getName().contains("Double"))
			{
				Expression<Double> rootField = root.get(field);
				Double valueTo = null;
				Double valueFrom = null;
				valueTo = (Double)cBT.get(BOSearch.VALUE_TO);
				valueFrom = (Double)cBT.get(BOSearch.VALUE_FROM);
				predicates.add(builder.between(rootField, valueFrom, valueTo));
				
			}
		}

		for (Map<String, Object> cOper : serializeCriteria.getListOperator()) {
			String field = cOper.get(BOSearch.NAME_FIELD).toString();
			Class<?> typeData = (Class) cOper.get(BOSearch.TYPE_DATA);
			String operatore = cOper.get("_operatore").toString();
			Predicate predicato = null;
			
			if (typeData != null && typeData.getName().contains("Date"))
			{
				Expression<Date> rootField = root.get(field);
				Date value = (Date)cOper.get(BOSearch.VALUE_FIELD);
				if (operatore.equals(BOOperatorClause.MINUSEQUALS)) {
					predicato = builder.lessThanOrEqualTo(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MINUS)) {
					predicato = builder.lessThan(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MAJOREQUALS)) {
					predicato = builder.greaterThanOrEqualTo(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MAJOR)) {
					predicato = builder.greaterThan(rootField, value);
				} else if (operatore.equals(BOOperatorClause.DISEQUALS)) {
					predicato = builder.notEqual(rootField, value);
				}				
			}
			else if (typeData != null && typeData.getName().contains("Integer"))
			{
				Expression<Integer> rootField = root.get(field);
				Integer value = (Integer)cOper.get(BOSearch.VALUE_FIELD);
				if (operatore.equals(BOOperatorClause.MINUSEQUALS)) {
					predicato = builder.lessThanOrEqualTo(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MINUS)) {
					predicato = builder.lessThan(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MAJOREQUALS)) {
					predicato = builder.greaterThanOrEqualTo(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MAJOR)) {
					predicato = builder.greaterThan(rootField, value);
				} else if (operatore.equals(BOOperatorClause.DISEQUALS)) {
					predicato = builder.notEqual(rootField, value);
				}				
			}
			else if (typeData != null && typeData.getName().contains("Double"))
			{
				Expression<Double> rootField = root.get(field);
				Double value = (Double)cOper.get(BOSearch.VALUE_FIELD);
				if (operatore.equals(BOOperatorClause.MINUSEQUALS)) {
					predicato = builder.lessThanOrEqualTo(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MINUS)) {
					predicato = builder.lessThan(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MAJOREQUALS)) {
					predicato = builder.greaterThanOrEqualTo(rootField, value);
				} else if (operatore.equals(BOOperatorClause.MAJOR)) {
					predicato = builder.greaterThan(rootField, value);
				} else if (operatore.equals(BOOperatorClause.DISEQUALS)) {
					predicato = builder.notEqual(rootField, value);
				}				
			}
			
			predicates.add(predicato);
		}

		for (String cIsNull : serializeCriteria.getListIsNull()) {
			predicates.add(builder.isNull(root.get(cIsNull)));
		}

		for (String cIsNotNull : serializeCriteria.getListIsNotNull()) {
			predicates.add(builder.isNotNull(root.get(cIsNotNull)));
		}

		// XXX: la lista dei valori non vuoti converge con quelli non nulli.
		// valutare se e' corretto il giro
		for (String cIsNotWS : serializeCriteria.get_listIsNotEmpty()) {
			predicates.add(builder.isNotNull(root.get(cIsNotWS)));
		}

		for (String cIsZero : serializeCriteria.getListIsZero()) {
			predicates.add(builder.equal(root.get(cIsZero), 0));
		}

		List<Predicate> orPredicates = new ArrayList<Predicate>(0);
		for (BOSerializeCriteria cOr : serializeCriteria.getListOrClause()) {

			List<Predicate> orPred = composeQuery(builder, root, cOr);
			Predicate orPredicate = builder.or(orPred.toArray(new Predicate[orPred.size()]));
			orPredicates.add(orPredicate);

		}
		if (orPredicates.size() > 0) {
			Predicate orPredicate = builder.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
			predicates.add(orPredicate);
		}

		return predicates;

	}

	@Autowired
	private Create<T> createctx;

	@Autowired
	private Update<T> updatectx;

	@Autowired
	private Remove<T> removectx;

	@Autowired
	private Search<T> searchctx;

	/*public class Create implements ICommand<T> {
		@Override
		public void execute(T entity, EntityManager manager) throws RepositoryException {
			manager.persist(entity);
		}
	}

	public class Update implements ICommand<T> {
		@Override
		public void execute(T entity, EntityManager manager) throws RepositoryException {
			manager.merge(entity);

		}
	}

	public class Remove implements ICommand<T> {
		@Override
		public void execute(T entity, EntityManager manager) throws RepositoryException {
			manager.remove(manager.contains(entity) ? entity : manager.merge(entity));
		}

	}

	public class Search implements ISearcher<T> {

		@Override
		public List<T> search(Query query) throws RepositoryException {
			// TODO Auto-generated method stub
			return query.getResultList();
		}
	}*/

	public List<T> getAll() throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {
			List<T> obj = em.createQuery("from " + nameClass).getResultList();
			return obj;
		} catch (RuntimeException e) {
			log.error("Errore durante il recupero di tutti gli elementi " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	/**
	 * Il criterio order è una specializzazione del tipo Criterion, il quale ha
	 * però lo scopo di istruire hibernate a costruire la query definendo un
	 * criterio di ordinamento su un campo (nome field, no nome campo su db) asc
	 * o desc
	 */
	public List<T> getAll(Order orderby) throws RepositoryException {

		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(classEntity);
			cq.orderBy(orderby);
			Query query = em.createQuery(cq);

			List<T> obj = query.getResultList();
			return obj;
		} catch (RuntimeException e) {
			log.error("Errore durante il recupero di tutti gli elementi " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	public List<T> getAllOrdered(int elementAt, int amount, Order orderby) throws RepositoryException {

		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(classEntity);
			cq.orderBy(orderby);
			Query query = em.createQuery(cq);
			query.setMaxResults(amount);
			query.setFirstResult(elementAt);

			List<T> obj = query.getResultList();
			return obj;
		} catch (RuntimeException e) {
			log.error("Errore durante il recupero di tutti gli elementi " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	public T getByCompositeId(T objId) throws RepositoryException {

		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		try {

			Serializable id = (Serializable) objId;
			return (T) em.getReference(classEntity, id);

		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	public void add(T obj) throws RepositoryException {

		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {
			em.getTransaction().begin();
			create(obj, em);
			em.getTransaction().commit();
		} catch (RepositoryException ex) {
			throw ex;
		} finally {
			em.close();
		}
	}

	public void add(T obj, EntityManager em) throws RepositoryException {
		create(obj, em);
	}

	public void update(T obj) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {
			em.getTransaction().begin();
			merge(obj, em);
			em.getTransaction().commit();
		} catch (RepositoryException ex) {
			throw ex;
		} finally {
			em.close();
		}
	}

	public void update(T obj, EntityManager em) throws RepositoryException {
		merge(obj, em);
	}

	public void delete(T obj) throws RepositoryException {

		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {
			em.getTransaction().begin();
			remove(obj, em);
			em.getTransaction().commit();
		} catch (RepositoryException ex) {
			throw ex;
		} finally {
			em.close();
		}

	}

	public void deleteFromId(Object id, String nameField) throws RepositoryException {

		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {

			em.getTransaction().begin();
			em.createQuery("DELETE FROM " + nameClass + " WHERE " + nameField + "=" + id).executeUpdate();
			em.getTransaction().commit();

		} catch (Exception ex) {
			throw new RepositoryException(ex);
		} finally {
			em.close();
		}

	}

	public void delete(T obj, EntityManager em) throws RepositoryException {
		remove(obj, em);
	}

	public void deleteAll() throws RepositoryException {
		// TODO Auto-generated method stub
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		String hql = "delete from " + nameClass;
		em.createQuery(hql).executeUpdate();
	}

	public void deleteAll(EntityManager em) throws RepositoryException {
		// TODO Auto-generated method stub
		String hql = "delete from " + nameClass;
		em.createQuery(hql).executeUpdate();
	}

	private void remove(T obj, EntityManager em) throws RepositoryException {
		try {
			removectx.execute(obj, em);
		} catch (RuntimeException e) {
			log.error("Errore durante la rimozione di una entita " + e.getMessage(), e);
			throw new RepositoryException(e);
		}
	}

	private void create(T obj, EntityManager em) throws RepositoryException {
		try {
			createctx.execute(obj, em);
		} catch (RuntimeException e) {
			log.error("Errore durante l'aggiunta di una entita " + e.getMessage(), e);
			throw new RepositoryException(e);
		}
	}

	private void merge(T obj, EntityManager em) throws RepositoryException {
		try {
			updatectx.execute(obj, em);
		} catch (RuntimeException e) {
			log.error("Errore durante una operazione di merge hibernate su una entita " + e.getMessage(), e);
			throw new RepositoryException(e);
		}
	}

	public T getById(int objId) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		return retrieveById(objId, em);

	}

	public T getById(String objId) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		return retrieveById(objId, em);
	}

	private T retrieveById(Object objId, EntityManager em) throws RepositoryException {
		T obj = null;
		try {
			obj = (T) em.find(classEntity, objId);
			return obj;
		} catch (RuntimeException e) {
			log.error("Errore durante il recupero di una entita dall'id " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	public T getById(int objId, EntityManager em) throws RepositoryException {
		// TODO Auto-generated method stub
		T obj = (T) em.find(classEntity, objId);
		return obj;
	}

	public T getById(String objId, EntityManager em) throws RepositoryException {
		// TODO Auto-generated method stub
		T obj = (T) em.find(classEntity, objId);
		return obj;
	}

	private enum UniqueStrategy {
		single, first, singledefault, firstdefault, list
	}

	public List<T> search(CriteriaQuery<T> criteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		return search(em.createQuery(criteria), em);
	}

	public List<T> search(BOSerializeCriteria serializeCriteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		return search(buildCriteriaQuery(serializeCriteria, em), em);
	}

	public List<T> search(BOSerializeCriteria serializeCriteria, EntityManager em) throws RepositoryException {

		return search(buildCriteriaQuery(serializeCriteria, em), em);
	}

	public List<T> search(Query query, EntityManager em) throws RepositoryException {
		List<T> result = null;
		try {
			result = searchctx.search(query);
			return result;
		} catch (RuntimeException e) {
			log.error("Errore durante la ricerca di entita " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}
	
	public T getSingle(CriteriaQuery<T> criteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		return getRetrieve(em.createQuery(criteria), UniqueStrategy.single, em);
	}

	public T getSingle(BOSerializeCriteria serializeCriteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		return getRetrieve(buildCriteriaQuery(serializeCriteria, em), UniqueStrategy.single, em);
	}

	public T getSingleOrDefault(CriteriaQuery<T> criteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		return getRetrieve(em.createQuery(criteria), UniqueStrategy.singledefault, em);
	}

	public T getSingleOrDefault(BOSerializeCriteria serializeCriteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		return getRetrieve(buildCriteriaQuery(serializeCriteria, em), UniqueStrategy.singledefault, em);
	}

	public T getSingleOrDefault(BOSerializeCriteria serializeCriteria, EntityManager em) throws RepositoryException {

		return getRetrieve(buildCriteriaQuery(serializeCriteria, em), UniqueStrategy.singledefault, em);
	}

	public T getSingleOrDefault(CriteriaQuery<T> criteria, EntityManager em) throws RepositoryException {

		return getRetrieve(em.createQuery(criteria), UniqueStrategy.singledefault, em);
	}

	public T getFirst(CriteriaQuery<T> criteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		return getRetrieve(em.createQuery(criteria), UniqueStrategy.first, em);
	}

	public T getFirst(BOSerializeCriteria serializeCriteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		return getRetrieve(buildCriteriaQuery(serializeCriteria, em), UniqueStrategy.first, em);
	}

	public T getFirstOrDefault(CriteriaQuery<T> criteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		return getRetrieve(em.createQuery(criteria), UniqueStrategy.firstdefault, em);
	}

	public T getFirstOrDefault(BOSerializeCriteria serializeCriteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();

		return getRetrieve(buildCriteriaQuery(serializeCriteria, em), UniqueStrategy.firstdefault, em);
	}

	public T getFirstOrDefault(BOSerializeCriteria serializeCriteria, EntityManager em) throws RepositoryException {

		return getRetrieveActiveSession(buildCriteriaQuery(serializeCriteria, em), UniqueStrategy.firstdefault, em);
	}

	public T getFirstOrDefault(CriteriaQuery<T> criteria, EntityManager em) throws RepositoryException {
		return getRetrieveActiveSession(em.createQuery(criteria), UniqueStrategy.firstdefault, em);
	}

	private T getRetrieveActiveSession(Query query, UniqueStrategy uniquestrategy, EntityManager em)
			throws RepositoryException {
		T obj = null;
		try {
			obj = getUnique(query, uniquestrategy, em);
			return obj;
		} catch (RuntimeException e) {
			log.error("Errore durante il recupero di una entita unique " + e.getMessage(), e);
			throw new RepositoryException(e);
		}
	}

	private T getRetrieve(Query query, UniqueStrategy uniquestrategy, EntityManager em)
			throws RepositoryException {
		T obj = null;
		try {
			obj = getUnique(query, uniquestrategy, em);
			return obj;
		} catch (RuntimeException e) {
			log.error("Errore durante il recupero di entita " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	private T getUnique(Query query, UniqueStrategy uniquestrategy, EntityManager em)
			throws RepositoryException {

		T obj = null;

		List<T> result = searchctx.search(query);

		boolean checkStrategy = false;
		boolean isSingle = false;
		switch (uniquestrategy) {
		case singledefault:
			checkStrategy = result.size() == 0 || result.size() == 1;
			isSingle = true;
		case firstdefault:
			checkStrategy = result.size() == 0 || result.size() > 0;
			break;
		case single:
			checkStrategy = result.size() == 1;
			isSingle = true;
		case first:
			checkStrategy = result.size() > 0;
		default:
			break;
		}

		if (!checkStrategy)
			throw new RepositoryException("Non e' stata trovata una corrispondenza valida per l'entita " + nameClass);
		else {
			if (isSingle) {
				if (result.size() == 1)
					obj = result.get(0);
			} else {
				if (result.size() > 0)
					obj = result.get(0);
			}

		}

		return obj;
	}

	public int getCount(BOSerializeCriteria serializeCriteria) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();

			String alias = nameClass.replace(".", "");
			List<Predicate> predicates = buildPredicates(alias, serializeCriteria, em);

			CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
			Root<T> entity_ = countQuery.from(classEntity);
			entity_.alias(alias);
			countQuery.select(builder.count(entity_));
			countQuery.where(predicates.toArray(new Predicate[predicates.size()]));

			Long count = em.createQuery(countQuery).getSingleResult();

			return count.intValue();
		} catch (RuntimeException e) {
			log.error("Errore durante il count di entita " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	public Number getMax(String nameField) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
		try {

			Number result = retrieveMax(nameField, em);
			return result;
		} catch (RuntimeException e) {
			log.error("Errore durante il recupero del max di entita " + e.getMessage(), e);
			throw new RepositoryException(e);

		} finally {
			em.close();
		}
	}

	public Number getMax(String nameField, EntityManager em) throws RepositoryException {

		Number result = retrieveMax(nameField, em);

		return result;
	}

	private Number retrieveMax(String nameField, EntityManager em) {
		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
		Root<T> classRoot = criteriaQuery.from(classEntity);
		criteriaQuery.select(builder.max(classRoot.get(nameField).as(Integer.class)));
		Number result = em.createQuery(criteriaQuery).getSingleResult();
		return result;
	}

}
