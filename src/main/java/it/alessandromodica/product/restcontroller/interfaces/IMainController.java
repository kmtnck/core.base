package it.alessandromodica.product.restcontroller.interfaces;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.alessandromodica.product.persistence.searcher.YAFilterSerializeCriteria;

/**
 * Controller principale general purpose
 * 
 * @author kmtnck
 *
 * @param <T>
 */
@Api(value = "modulo.entity", description = "Interfaccia per la gestione CRUD di una entita database")
public interface IMainController<T> {

	@ApiOperation(value = "Test chiamata rest", response = Object.class)
	@ApiParam(value = "Id per il test")
	public Object test(String id);

	@ApiOperation(value = "Recupera un oggetto da database", response = Object.class)
	@ApiParam(value = "ID dell'oggetto")
	public Object get(int id, String classname) throws Exception;

	@ApiOperation(value = "Recupera un oggetto da database", response = List.class)
	@ApiParam(value = "Token di ricerca")
	public List<Object> ricerca(YAFilterSerializeCriteria searcher) throws Exception;

	public int count(YAFilterSerializeCriteria searcher) throws Exception;

	public void save(T toSave) throws Exception;

}
