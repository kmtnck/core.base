package it.alessandromodica.product.persistence.uow;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.IBulkTransaction;
import it.alessandromodica.product.persistence.interfaces.IUnitOfWork;

@Component
public class UnitOfWork implements IUnitOfWork {

	private static final Logger log = Logger.getLogger(UnitOfWork.class);

	private static EntityManagerFactory ENTITY_MANAGER_FACTORY;

	/**
	 * Inizializza il session factory hibernate.
	 * 
	 * @throws RepositoryException
	 */
	public static void initSessionFactory() throws RepositoryException {
		ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("appjpa");
	}

	public static void initSessionFactory(String namePersistenceUnit) throws RepositoryException {
		ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(namePersistenceUnit);
	}

	public void submit(IBulkTransaction instructions) throws RepositoryException {
		// Create an EntityManager
		EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction transaction = null;

		try {
			// Get a transaction
			transaction = manager.getTransaction();
			// Begin the transaction
			transaction.begin();

			instructions.persist(manager);
			log.info("Istruzioni in transazione eseguite correttamente!");

			// Commit the transaction
			transaction.commit();
		} catch (Exception ex) {
			// If there are any exceptions, roll back the changes
			if (transaction != null) {
				transaction.rollback();
			}
			// Print the Exception
			log.error("Si e' verificato un errore durante una transazione db", ex);
			throw new RepositoryException(ex.getMessage(), ex);
		} finally {
			// Close the EntityManager
			manager.close();
		}
	}

	@Override
	public void submitNoTransaction(IBulkTransaction instructions) throws RepositoryException {

		// Create an EntityManager
		EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();

		instructions.persistNoTransaction(manager);

	}

	/*
	 * Metodo statico, che ci permetterà di recuperare la SessionFactory, nella
	 * nostra applicazione.
	 */
	public static EntityManagerFactory getEntityManager() {
		return ENTITY_MANAGER_FACTORY;
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return ENTITY_MANAGER_FACTORY.getCriteriaBuilder();
	}

}
