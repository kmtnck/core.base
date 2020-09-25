package it.alessandromodica.legacy.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.RequestBody;

import it.alessandromodica.product.common.Constants;
import it.alessandromodica.product.persistence.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.searcher.YAFilterSerializeCriteria;

/***
 * Classe che rappresenta le chiamate rest per accedere al modulo dei carichi
 * 
 * @author amodica
 *
 *         E' annotata come component ed è qualificato con @Patch seguendo le
 *         regole di resteasy
 *
 *         Ciascun metodo ha annotato il tipo di chiamata, il path relativo
 *         associato, consumes, produces e gli eventuali ruoli utenti ammessi,
 *         se non è specificato la chiamata è anonima e libera, altrimenti
 *         forbidden
 *         
 *         Gli endpoint qui implementati sono validi in ogni contesto ma non sono pratici da mantenere in un contesto springboot puro.
 *         Sono pertanto deprecati nel contesto springboot, ma validi in altri contesti spring classico
 *
 */
@Deprecated
@Path(value = "/entity")
public class EntityRest implements IEntityRest {

	protected static final Logger logger = Logger.getLogger(EntityRest.class);

	@Override
	@GET
	@Path(value = "/test/{id}")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	public Object test(@PathParam(value = "id") String id) {

			String result = "Questo e' un test funzionalita rest in modalita Legacy ! " + id.toString();
			
			return result;

	}
	/*
Template di BOSerializeCriteria in formato json , manipolabile da un qualsiasi client smart

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

{
  "insensitive": "insensitive;",
  "nameField": "nameField;",
  "value": "value;",
  
  "}": {}
}

	 * */

	@Override
	@POST
	@Path(value = "/search")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	public List<Object> ricerca(@RequestBody YAFilterSerializeCriteria searcher)
			throws RepositoryException {

		try {

			List<Object> result = (List<Object>) AppRoot.mainservice.search(searcher);

			return result;

		} catch (Exception e) {
			throw new RepositoryException(e);
		}

	}

	@Override
	@GET
	@Path(value = "/get/{id}")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	public Object get(@PathParam(value = "id") int id, @Context UriInfo info) throws RepositoryException {

		try {

			ExtractURIValue values = new ExtractURIValue(info);
			Class<?> classEntity = Class.forName(values.getValue(Constants.CLASSNAME));
			Object result = (Object) AppRoot.mainservice.getById(id, classEntity);

			return result;

		} catch (ClassNotFoundException e) {
			throw new RepositoryException(e);
		}

	}

	@POST
	@Path(value = "/count/")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.TEXT_PLAIN)
	public int count(@RequestBody YAFilterSerializeCriteria searcher) throws RepositoryException {

		try {

			int result = AppRoot.mainservice.count(searcher);

			return result;

		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	@POST
	@Path(value = "/save")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@RolesAllowed("authenticated")
	public void save(@RequestBody Object toSave) throws RepositoryException {

		AppRoot.mainservice.add(toSave);
	}

}
