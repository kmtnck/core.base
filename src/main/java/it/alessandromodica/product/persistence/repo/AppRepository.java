package it.alessandromodica.product.persistence.repo;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.model.bo.query.BOUtentiConnessi;
import it.alessandromodica.product.persistence.interfaces.IRepositoryCommands;
import it.alessandromodica.product.persistence.interfaces.IRepositoryQueries;

/**
 * In questa classe repository � possibile definire tutti i metodi legacy
 * necessari ad accedere a sql puro
 * 
 * E' la classe repository dedicata al software che si vuole realizzare e
 * estende la classe astratta BaseRepository
 * 
 * @author Alessandro
 *
 * @param <T>
 */
@Repository
public class AppRepository<T, JOIN> extends BaseRepository<T, JOIN> implements IRepositoryQueries<T, JOIN>, IRepositoryCommands<T, JOIN> {

	private static final Logger log = Logger.getLogger(AppRepository.class);

	/*@Deprecated
	@Override
	public AppRepository<T, JOIN> setEntity(Class<T> classEntity) {
		// TODO Auto-generated method stub
		setClass(classEntity);
		return this;

	}*/

	public List<BOUtentiConnessi> callUtentiLoggati(int periodo, String nickname) throws RepositoryException {
		try {

			List<BOUtentiConnessi> result = null;
			/*
			 * List<BOUtentiConnessi> result =
			 * em.createNamedQuery("utentiLoggati").setParameter("periodo", periodo)
			 * .setParameter("nickname", nickname).getResultList();
			 */

			return result;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RepositoryException(e);
		}
	}

}
