package it.alessandromodica.legacy.rest;

import java.util.List;

import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.alessandromodica.product.persistence.searcher.YAFilterSerializeCriteria;

/**
 * Interfaccia che rappresent il modulo rest dei carichi. Si nota che l'unico
 * riferimento definito � swagger. L'interfaccia pilota quindi le interazioni
 * con swagger e solo lui.
 * 
 * @author amodica
 *
 */
@Api(value = "modulo.entity", description = "Interfaccia per la gestione CRUD di una entita database")
@Deprecated
public interface IEntityRest<T> {

	@ApiOperation(value = "Test chiamata rest", response = Object.class)
	@ApiParam(value = "Id per il test")
	public Object test(String id);

	@ApiOperation(value = "Recupera un oggetto da database", response = Object.class)
	@ApiParam(value = "ID dell'oggetto")
	public Object get(int id, UriInfo info) throws Exception;

	@ApiOperation(value = "Recupera un oggetto da database", response = List.class)
	@ApiParam(value = "Token di ricerca")
	public List<Object> ricerca(YAFilterSerializeCriteria searcher) throws Exception;

	public int count(YAFilterSerializeCriteria searcher) throws Exception;

	public void save(T toSave) throws Exception;

}
