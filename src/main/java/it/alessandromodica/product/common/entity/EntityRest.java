package it.alessandromodica.product.common.entity;


import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import it.alessandromodica.product.app.AppRoot.ExtractURIValue;
import it.alessandromodica.product.app.GoToBusiness;
import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.IRepositoryCommands;
import it.alessandromodica.product.persistence.interfaces.IRepositoryQueries;
import it.alessandromodica.product.persistence.searcher.BOSerializeCriteria;

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
 */
@Component
@Path(value = "/entity")
public class EntityRest implements IEntityRest {

	protected static final Logger logger = Logger.getLogger(EntityRest.class);

	/**
	 * Nuovi Componenti
	 */
	@Autowired
	IRepositoryQueries reader;
	@Autowired
	IRepositoryCommands writer;

	/*
Template di BOSerializeCriteria in formato json , manipolabile da un qualsiasi client smart
{

  "maxResult": "10",
  "firstResult": 0,
  "descendent": "false",
  "className": "it.ditech.comm.xmodule.product.po.CaricoDettaglio"
  "listOrderBy": [],
  "listOrClause": [],
  "listbetween": [],
  "listLike": [],
  "listLikeInsensitive": [],
  "listOperator": [],
  "listIsNull": [],
  "listIsNotNull": [],
  "listIsZero": [],
  "listIsNotEmpty": [],
  "listFieldsProjection": [],
  
  //per i test bisogna rimuovere questi tag in quanto valori non coerenti generano eccezioni server
  "listEquals": {"ciccio": ""},
  "listIn": {"source":[]},
  "listNotIn":  {"source":[]},
  "listValueBool":  {"namefield": "false"},
  "mapDescendent": {"namefield": "false"},
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
	public List<Object> ricerca(@RequestBody BOSerializeCriteria searcher, @Context UriInfo info)
			throws RepositoryException {

		try {

			ExtractURIValue values = new ExtractURIValue(info);
			Class<?> classEntity = Class.forName(values.getValue(GoToBusiness.CLASSNAME));
			List<Object> result = (List<Object>) reader.setEntity(classEntity).search(searcher);

			return result;

		} catch (ClassNotFoundException e) {
			throw new RepositoryException(e);
		}

	}
	
	@Override
	@POST
	@Path(value = "/get/{id}")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	public Object get(@PathParam(value = "id") int id, @Context UriInfo info)
			throws RepositoryException {

		try {

			ExtractURIValue values = new ExtractURIValue(info);
			Class<?> classEntity = Class.forName(values.getValue(GoToBusiness.CLASSNAME));
			Object result = (List<Object>) reader.setEntity(classEntity).getById(id);

			return result;

		} catch (ClassNotFoundException e) {
			throw new RepositoryException(e);
		}

	}

	@POST
	@Path(value = "/count/")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.TEXT_PLAIN)
	public int count(@RequestBody BOSerializeCriteria searcher, @Context UriInfo info) throws RepositoryException {

		try {

			ExtractURIValue values = new ExtractURIValue(info);
			Class<?> classEntity = Class.forName(values.getValue(GoToBusiness.CLASSNAME));
			int result = reader.setEntity(classEntity).getCount(searcher);

			return result;

		} catch (ClassNotFoundException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	@POST
	@Path(value = "/save")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@RolesAllowed("authenticated")
	public void save(@RequestBody Object toSave) throws RepositoryException {

		writer.add(toSave);
	}

}
