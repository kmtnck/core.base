package it.alessandromodica.product.persistence.uow;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.IBulkTransaction;
import it.alessandromodica.product.persistence.interfaces.IUnitOfWork;

/**
 * Classe che implementa una semplice unit of work, utilizzata per eseguire
 * sequenze di istruzioni in scrittura in transazione. Il metodo submit
 * rappresenta una canonica procedura di transazione su database. L'approccio e'
 * quello supportato da una unita di persistenza di tipo RESOURCE_LOCAL
 * 
 * L'unica implementazione in cui si apre e chiude una transazione classica e'
 * definita in questa classe.
 * 
 * 
 * @author Alessandro
 *
 */
@Component
public class UnitOfWork implements IUnitOfWork {

	private static final Logger log = Logger.getLogger(UnitOfWork.class);

	private static EntityManagerFactory ENTITY_MANAGER_FACTORY_SINGLETON;

	/**
	 * Inizializza il session factory hibernate.
	 * 
	 * @throws RepositoryException
	 */
	@Deprecated
	public static void initSessionFactory() throws RepositoryException {
		ENTITY_MANAGER_FACTORY_SINGLETON = Persistence.createEntityManagerFactory("appjpa");
	}

	@Deprecated
	public static void initSessionFactory(String namePersistenceUnit) throws RepositoryException {
		ENTITY_MANAGER_FACTORY_SINGLETON = Persistence.createEntityManagerFactory(namePersistenceUnit);
	}

	/**
	 * Il metodo prende in input un oggetto di tipo IBulkTransaction, in cui Ë
	 * implementata la serie di istruzioni da far eseguire in transazione.
	 * 
	 * L'efficacia del metodo e' che la logica di transazione risiede in questo
	 * unico punto e il chiamante ha il solo compito di fornire in input le
	 * istruzioni incapsulate nell'oggetto IBulkTransaction
	 */
	public void submit(IBulkTransaction instructions) throws RepositoryException {
		try {

			instructions.persist();

			log.info("Istruzioni in transazione eseguite correttamente!");
		} catch (Exception ex) {
			log.error("Si e' verificato un errore durante una transazione db", ex);
			throw new RepositoryException(ex.getMessage(), ex);
		}
	}

	/*
	 * Metodo statico, che ci permetter√† di recuperare la SessionFactory, nella
	 * nostra applicazione.
	 */
	@Deprecated
	public EntityManagerFactory getEntityManager() {
		return ENTITY_MANAGER_FACTORY_SINGLETON;
	}

	@Deprecated
	public CriteriaBuilder getCriteriaBuilder() {
		return ENTITY_MANAGER_FACTORY_SINGLETON.getCriteriaBuilder();
	}

}
