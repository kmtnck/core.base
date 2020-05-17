package it.alessandromodica.product.persistence.searcher;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

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

	public static final String NAME_FIELD = "nameField";
	public static final String TYPE_DATA = "typeData";
	public static final String VALUE_FIELD = "value";
	public static final String VALUE_FROM = "valueFrom";
	public static final String VALUE_TO = "valueTo";
	public static final String VALUE_INT = "valueInt";
	public static final String VALUE_DOUBLE = "valueDouble";
	public static final String VALUE_DATE = "valueDate";

	public static final String VALUE_TO_DOUBLE = "valueToDouble";
	public static final String VALUE_FROM_DOUBLE = "valueFromDouble";
	public static final String VALUE_FROM_INT = "valueFromInt";
	public static final String VALUE_TO_INT = "valueToInt";
	public static final String VALUE_TO_DATE = "valueToDate";
	public static final String VALUE_FROM_DATE = "valueFromDate";

	private List<String> _listFieldsProjection = new ArrayList<String>();
	private List<String> _listExcludeProjection = new ArrayList<String>();

	private List<BOBetweenClause> _listBetweenClause = new ArrayList<BOBetweenClause>();
	private List<BOLikeClause> _listLikeClause = new ArrayList<BOLikeClause>();
	private List<BOOperatorClause> _listOperatorClause = new ArrayList<BOOperatorClause>();

	@Deprecated
	private boolean descendent = false;
	private Map<String,Boolean> mapDescendent = new HashMap<String,Boolean>();
	
	private List<String> _listOrderBy = new ArrayList<String>();
	private List<String> _listIsNull = new ArrayList<String>();
	private List<String> _listIsNotNull = new ArrayList<String>();
	private List<String> _listIsNotEmpty = new ArrayList<String>();
	private List<String> _listIsZero = new ArrayList<String>();
	private List<BOSearch> _listOrClause = new ArrayList<BOSearch>();
	private Map<String, Boolean> _listValueBool = new HashMap<String, Boolean>();
	private Map<String, Object[]> _listIn = new HashMap<String, Object[]>();
	private Map<String, Object[]> _listNotIn = new HashMap<String, Object[]>();

	private int maxResult;
	private int firstResult;

	public static void setClauseInList(String nameField, Object[] data, BOSearch searcher) {
		if (data != null && data.length > 0)
			searcher.get_listIn().put(nameField, data);
	}

	public static void setClauseNotInList(String nameField, Object[] data, BOSearch searcher) {
		if (data != null && data.length > 0)
			searcher.get_listNotIn().put(nameField, data);
	}

	public static void setLikeClause(String value, String nameField, BOSearch searcher) {
		if (StringUtils.isNotBlank(value)) {
			BOLikeClause likeCl = new BOLikeClause();
			likeCl.setNameField(nameField);
			likeCl.setValue("%" + value + "%");
			searcher.get_listLikeClause().add(likeCl);
		}
	}

	public static void setBetweenClause(Object valueFrom, Object valueTo, String nameField, BOSearch searcher,
			Class<?> typeData) {
		if (valueFrom != null) {
			BOBetweenClause btw = new BOBetweenClause();
			btw.set_nameField(nameField);
			btw.set_valueFrom(valueFrom);
			btw.set_valueTo(valueTo);
			btw.setTypeData(typeData);
			searcher.get_listBetweenClause().add(btw);
		}
	}
	
	public Map<String, Boolean> get_listValueBool() {
		return _listValueBool;
	}

	public void set_listValueBool(Map<String, Boolean> _listValueBool) {
		this._listValueBool = _listValueBool;
	}

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
			if (cLike.isInsensitive())
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

		for (Map.Entry<String, Boolean> cValueDesc : searcher.getMapDescendent().entrySet()) {
			result.getMapDescendent().put(cValueDesc.getKey(), cValueDesc.getValue());
		}
		
		for (String cValueBool : searcher.get_listValueBool().keySet()) {
			result.get_listValueBool().put(cValueBool,searcher.get_listValueBool().get(cValueBool));
		}

		for (String cIn : searcher.get_listIn().keySet()) {
			result.getListIn().put(cIn, searcher.get_listIn().get(cIn));
		}

		for (String cNotIn : searcher.get_listNotIn().keySet()) {
			result.getListNotIn().put(cNotIn, searcher.get_listNotIn().get(cNotIn));
		}

		if (searcher.isDescendent()) {
			result.setDescendent(searcher.isDescendent());
		}

		for (BOSearch cOr : searcher.get_listOrClause()) {
			if (cOr.get_listOrderBy().size() > 0)
				throw new RepositoryException(
						"Non e' permesso impostare criteri di ordinamento in una clausola OR. Utilizzare l'istanza BOSearch principale per impostare l'oggetto ListOrderBy");

			BOSerializeCriteria orSerialized = _buildItemClause(cOr);
			result.getListOrClause().add(orSerialized);
		}

		// La strategia di esclusione field dalla projections e' includere tutti quelli
		// non presenti in questa lista
		if (searcher.get_listExcludeProjection().size() > 0) {

			Class<?> classEntity = searcher.getClassEntity();

			if (classEntity == null)
				throw new RepositoryException(
						"E' stato definito un set di campi da escludere in proiezione, ma non e' stata indicata la classe entita di riferimento");

			Field[] fieldsclass = classEntity.getDeclaredFields();
			Field[] fieldsuperclass = classEntity.getSuperclass().getDeclaredFields();

	        int aLen = fieldsclass.length;
	        int bLen = fieldsuperclass.length;
	        Field[] fields = new Field[aLen + bLen];

	        System.arraycopy(fieldsclass, 0, fields, 0, aLen);
	        System.arraycopy(fieldsuperclass, 0, fields, aLen, bLen);
	        
			
			for (Object cobj : fields) {
				Field cfield = (Field)cobj;
				
				boolean isStaticField = java.lang.reflect.Modifier.isStatic(cfield.getModifiers())
						&& java.lang.reflect.Modifier.isFinal(cfield.getModifiers());
				if (isStaticField)
					continue;

				String cName = cfield.getName();
				boolean canInclude = true;
				for (String exclude : searcher.get_listExcludeProjection()) {
					if (cName.equals(exclude)) {
						canInclude = false;
						break;
					}
				}
				if (canInclude && !cName.equals("version"))
					result.get_listFieldsProjection().add(cName);
			}
		} else {
			for (String cField : searcher.get_listFieldsProjection()) {
				result.get_listFieldsProjection().add(cField);
			}
		}

		if (searcher.getMaxResult() > 0) {
			result.setMaxResult(searcher.getMaxResult());
			result.setFirstResult(searcher.getFirstResult());
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> _serializeBusinessClause(Object searcher) throws RepositoryException {
		Map<String, Object> effective = new HashMap<String, Object>(0);

		Map<String, Object> resultSearch = toDictionary(searcher);
		for (Iterator keys = resultSearch.entrySet().iterator(); keys.hasNext();) {
			Entry cEntry = (Entry) keys.next();
			String cKey = cEntry.getKey().toString();

			effective.put(cKey, resultSearch.get(cKey));
		}

		return effective;
	}

	public abstract boolean checkCompositeId(String nameProperty);

	public List<String> get_listIsNotEmpty() {
		return _listIsNotEmpty;
	}
	
	public void set_listIsNotEmpty(List<String> _listIsNotEmpty) {
		this._listIsNotEmpty = _listIsNotEmpty;
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

	public Map<String, Object[]> get_listIn() {
		return _listIn;
	}

	public void set_listIn(Map<String, Object[]> _listIn) {
		this._listIn = _listIn;
	}

	public Map<String, Object[]> get_listNotIn() {
		return _listNotIn;
	}

	public void set_listNotIn(Map<String, Object[]> _listNotIn) {
		this._listNotIn = _listNotIn;
	}

	public List<String> get_listFieldsProjection() {
		return _listFieldsProjection;
	}

	public void set_listFieldsProjection(List<String> _listFieldsProjection) {
		this._listFieldsProjection = _listFieldsProjection;
	}

	public List<String> get_listExcludeProjection() {
		return _listExcludeProjection;
	}

	public void set_listExcludeProjection(List<String> _listExcludeProjection) {
		this._listExcludeProjection = _listExcludeProjection;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public Map<String, Boolean> getMapDescendent() {
		return mapDescendent;
	}

	public void setMapDescendent(Map<String, Boolean> mapDescendent) {
		this.mapDescendent = mapDescendent;
	}

	public boolean isDescendent() {
		return descendent;
	}

	public void setDescendent(boolean descendent) {
		this.descendent = descendent;
	}
}
