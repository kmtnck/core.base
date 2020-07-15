package it.alessandromodica.product.restcontroller;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.alessandromodica.product.app.MainApplication;
import it.alessandromodica.product.common.Constants;
import it.alessandromodica.product.persistence.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.searcher.YAFilterSerializeCriteria;
import it.alessandromodica.product.services.interfaces.IMainService;
import it.alessandromodica.product.views.AppViewListener;

/***
 * Classe che rappresenta le chiamate rest per accedere al modulo dei carichi
 * 
 * @author amodica
 *
 *         E' annotata come component ed � qualificato con @Patch seguendo le
 *         regole di resteasy
 *
 *         Ciascun metodo ha annotato il tipo di chiamata, il path relativo
 *         associato, consumes, produces e gli eventuali ruoli utenti ammessi,
 *         se non � specificato la chiamata � anonima e libera, altrimenti
 *         forbidden
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
public class MainController implements IMainController {

	protected static final Logger logger = Logger.getLogger(MainController.class);

	@Autowired
	private IMainService mainservice;

	MainApplication mainApp;
	
	// @Override
	@RequestMapping(value = "/test/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Object test(@PathVariable("id") String id) {

		String result = "Questo e' un test funzionalita rest e basta! " + id.toString();

		return result;

	}
	/*
	 * Template di BOSerializeCriteria in formato json , manipolabile da un
	 * qualsiasi client smart 

{
  "classEntity": "it.alessandromodica.product.model.po.GestioneUtenti",
  "maxResult": "50",
  "firstResult": 0,
  "descendent": "true",
  "listOrderBy": [],
  "listOrClause": [],
  "listbetween": [],
  "listLike": [],
  "listLikeInsensitive": [],
  "listOperator": [],
  "listIsNull": [],
  "listIsNotNull": [],
  "listIsZero": [],
  "listEquals": {},
  "listIsNotEmpty": [],
  "listFieldsProjection": [],
  
  "}": {}
}

	 * 
	 */

	// @Override
	@RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public List<Object> ricerca(@RequestBody YAFilterSerializeCriteria searcher) throws Exception {

		try {

			if (mainservice != null) {
				List<Object> result = (List<Object>) mainservice.search(searcher);

				return result;
			} else
				throw new Exception("Il servizio main e' non istanziato");

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@Override
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Object get(@PathVariable("id") int id, @RequestParam Map info) throws RepositoryException {

		try {

			Class<?> classEntity = Class.forName((String)info.get(Constants.CLASSNAME));
			Object result = (Object) mainservice.getById(id, classEntity);

			return result;

		} catch (ClassNotFoundException e) {
			throw new RepositoryException(e);
		}

	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
	public int count(@RequestBody YAFilterSerializeCriteria searcher) throws RepositoryException {

		try {

			int result = mainservice.count(searcher);

			return result;

		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	@RolesAllowed("authenticated")
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	public void save(@RequestBody Object toSave) throws RepositoryException {

		mainservice.add(toSave);
	}

}