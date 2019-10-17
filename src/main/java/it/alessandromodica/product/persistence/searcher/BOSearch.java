package it.alessandromodica.product.persistence.searcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.alessandromodica.product.common.exceptions.RepositoryException;

/**
 * Classe astratta che rappresenta il criterio di ricerca definito in input in una generica richiesta di ricerca sul datastorage.
 * Estende la classe BOBase per poter serializzare in una mappatura i valori.
 * Sono presenti valori statici di tipo stringa utilizzati dal metodo _buildItemClause e dal chiamante BaseRepository.
 * Il metodo _buildItemClause ritorna l'oggetto BOSerializeCritera il quale e' l'unico oggetto riconosciuto dal BaseRepository
 * 
 * @author Alessandro
 *
 */
public abstract class BOSearch extends BOBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3897039654760798485L;

	public static final String NAME_FIELD = "_nameField";
	public static final String TYPE_DATA = "typeData";
	public static final String VALUE_FIELD = "_value";
	public static final String VALUE_FROM = "_valueFrom";
	public static final String VALUE_TO = "_valueTo";
	public static final String VALUE_INT = "_valueInt";
	public static final String VALUE_DOUBLE = "_valueDouble";
	public static final String VALUE_DATE = "_valueDate";

	public static final String VALUE_TO_DOUBLE = "_valueToDouble";
	public static final String VALUE_FROM_DOUBLE = "_valueFromDouble";
	public static final String VALUE_FROM_INT = "_valueFromInt";
	public static final String VALUE_TO_INT = "_valueToInt";
	public static final String VALUE_TO_DATE = "_valueToDate";
	public static final String VALUE_FROM_DATE = "_valueFromDate";

	private List<BOBetweenClause> _listBetweenClause = new ArrayList<BOBetweenClause>();
	private List<BOLikeClause> _listLikeClause = new ArrayList<BOLikeClause>();
	private List<BOOperatorClause> _listOperatorClause = new ArrayList<BOOperatorClause>();
	private boolean _isDescendent = false;
	private List<String> _listOrderBy = new ArrayList<String>();
	private List<String> _listIsNull = new ArrayList<String>();
	private List<String> _listIsNotNull = new ArrayList<String>();
	private List<String> _listIsNotEmpty = new ArrayList<String>();
	private List<String> _listIsZero = new ArrayList<String>();
	private List<BOSearch> _listOrClause = new ArrayList<BOSearch>();
	private List<Map.Entry<String, Boolean>> _listValueBool = new ArrayList<Map.Entry<String, Boolean>>();

	private int maxResult;

	public List<BOBetweenClause> get_listBetweenClause() {
		return _listBetweenClause;
	}

	public void set_listBetweenClause(List<BOBetweenClause> _listBetweenClause) {
		this._listBetweenClause = _listBetweenClause;
	}

	public List<BOLikeClause> get_listLikeClause() {
		return _listLikeClause;
	}

	public void set_listLikeClause(List<BOLikeClause> _listLikeClause) {
		this._listLikeClause = _listLikeClause;
	}

	public List<BOOperatorClause> get_listOperatorClause() {
		return _listOperatorClause;
	}

	public void set_listOperatorClause(List<BOOperatorClause> _listOperatorClause) {
		this._listOperatorClause = _listOperatorClause;
	}

	public boolean is_isDescendent() {
		return _isDescendent;
	}

	public void set_isDescendent(boolean _isDescendent) {
		this._isDescendent = _isDescendent;
	}

	public List<String> get_listOrderBy() {
		return _listOrderBy;
	}

	public void set_listOrderBy(List<String> _listOrderBy) {
		this._listOrderBy = _listOrderBy;
	}

	public List<String> get_listIsNull() {
		return _listIsNull;
	}

	public void set_listIsNull(List<String> _listIsNull) {
		this._listIsNull = _listIsNull;
	}

	public List<String> get_listIsNotNull() {
		return _listIsNotNull;
	}

	public void set_listIsNotNull(List<String> _listIsNotNull) {
		this._listIsNotNull = _listIsNotNull;
	}

	public List<String> get_listIsZero() {
		return _listIsZero;
	}

	public void set_listIsZero(List<String> _listIsZero) {
		this._listIsZero = _listIsZero;
	}

	public List<BOSearch> get_listOrClause() {
		return _listOrClause;
	}

	public void set_listOrClause(List<BOSearch> _listOrClause) {
		this._listOrClause = _listOrClause;
	}

	public List<Map.Entry<String, Boolean>> get_listValueBool() {
		return _listValueBool;
	}

	public void set_listValueBool(List<Map.Entry<String, Boolean>> _listValueBool) {
		this._listValueBool = _listValueBool;
	}

	public BOSerializeCriteria getSerialized() throws RepositoryException {
		return _buildItemClause(this);
	}

	private static BOSerializeCriteria _buildItemClause(BOSearch searcher) throws RepositoryException {

		BOSerializeCriteria result = new BOSerializeCriteria();

		result.setClassSearcher(searcher.getClass());
		// uguaglianze
		result.setListEquals(_serializeBusinessClause(searcher));

		// between
		for (BOBetweenClause cBT : searcher.get_listBetweenClause()) {
			Map<String, Object> serializeBT = new HashMap<String, Object>();

			serializeBT.put(NAME_FIELD, cBT.get_nameField());
			serializeBT.put(TYPE_DATA, cBT.getTypeData());
			serializeBT.put(VALUE_FROM, cBT.get_valueFrom());
			serializeBT.put(VALUE_TO, cBT.get_valueTo());

			result.getListbetween().add(serializeBT);
		}

		for (BOLikeClause cLike : searcher.get_listLikeClause()) {
			if (cLike.is_isInsensitive())
				result.getListLikeInsensitive().add(_serializeBusinessClause(cLike));
			else
				result.getListLike().add(_serializeBusinessClause(cLike));
		}

		for (BOOperatorClause cOper : searcher.get_listOperatorClause()) {
			Map<String, Object> serOper = _serializeBusinessClause(cOper);
			/*if (cOper.get_valueDouble() == 0 && cOper.get_valueInt() == 0) {
				serOper.put(VALUE_INT, 0);
			}*/

			result.getListOperator().add(serOper);
		}

		for (String cIsNull : searcher.get_listIsNull()) {
			result.getListIsNull().add(cIsNull);
		}

		for (String cIsNotNull : searcher.get_listIsNotNull()) {
			result.getListIsNotNull().add(cIsNotNull);
		}

		for (String cIsZero : searcher.get_listIsZero()) {
			result.getListIsZero().add(cIsZero);
		}

		for (String cIsZero : searcher.get_listIsNotEmpty()) {
			result.get_listIsNotEmpty().add(cIsZero);
		}

		for (String cOrderBy : searcher.get_listOrderBy()) {
			result.getListOrderBy().add(cOrderBy);
		}

		for (Map.Entry<String, Boolean> cValueBool : searcher.get_listValueBool()) {
			result.getListValueBool().add(cValueBool);
		}

		if (searcher.is_isDescendent()) {
			result.set_isDescendent(searcher.is_isDescendent());
		}

		for (BOSearch cOr : searcher.get_listOrClause()) {
			if (cOr.get_listOrderBy().size() > 0)
				throw new RepositoryException(
						"Non e' permesso impostare criteri di ordinamento in una clausola OR. Utilizzare l'istanza BOSearch principale per impostare l'oggetto ListOrderBy");

			BOSerializeCriteria orSerialized = _buildItemClause(cOr);
			result.getListOrClause().add(orSerialized);
		}

		if (searcher.getMaxResult() > 0)
			result.setMaxResult(searcher.getMaxResult());
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> _serializeBusinessClause(Object searcher) throws RepositoryException {
		Map<String, Object> effective = new HashMap<String, Object>(0);

		Map<String, Object> resultSearch = toDictionary(searcher);
		for (Iterator keys = resultSearch.entrySet().iterator(); keys.hasNext();) {
			Entry cEntry = (Entry)keys.next();
			String cKey = cEntry.getKey().toString();

			effective.put(cKey, resultSearch.get(cKey));
		}

		return effective;
	}

	public abstract boolean checkCompositeId(String nameProperty);

	public List<String> get_listIsNotEmpty() {
		return _listIsNotEmpty;
	}

	public void set_listIsNotWhiteSpace(List<String> listIsNotEmpty) {
		this._listIsNotEmpty = listIsNotEmpty;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}
}
