package it.alessandromodica.product.persistence.repo;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.model.bo.query.BOUtentiConnessi;
import it.alessandromodica.product.persistence.interfaces.IRepositoryCommands;
import it.alessandromodica.product.persistence.interfaces.IRepositoryQueries;
import it.alessandromodica.product.persistence.uow.UnitOfWork;

/**
 * In questa classe repository è possibile definire tutti i metodi legacy
 * necessari ad accedere a sql puro
 * 
 * @param periodo
 * @param nickname
 * @return
 * @throws RepositoryException
 */
@Component
public class AppRepository<T> extends BaseRepository<T> implements IRepositoryQueries<T>, IRepositoryCommands<T> {

	private static final Logger log = Logger.getLogger(AppRepository.class);

	@Override
	public AppRepository<T> setEntity(Class<T> classEntity) {
		// TODO Auto-generated method stub
		setClass(classEntity);
		return this;
		
	}
	public List<BOUtentiConnessi> callUtentiLoggati(int periodo, String nickname) throws RepositoryException {
		EntityManager em = UnitOfWork.getEntityManager().createEntityManager();
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
		} finally {
			em.close();
		}
	}



}
