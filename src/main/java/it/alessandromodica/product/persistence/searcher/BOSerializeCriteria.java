package it.alessandromodica.product.persistence.searcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BOSerializeCriteria {

	@SuppressWarnings("rawtypes")
	private Class classSearcher;
	
	private Map<String, Object> listEquals = new HashMap<String, Object>();
	private List<Map<String, Object>> listbetween = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listLike = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listLikeInsensitive = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listOperator = new ArrayList<Map<String, Object>>();
	private List<String> listOrderBy = new ArrayList<String>();
	private List<String> listIsNull = new ArrayList<String>();
	private List<String> listIsNotNull = new ArrayList<String>();
	private List<String> listIsZero = new ArrayList<String>();
	private List<String> _listIsNotEmpty = new ArrayList<String>();
	private List<Map.Entry<String, Boolean>> listValueBool = new ArrayList<Map.Entry<String, Boolean>>();
	private List<BOSerializeCriteria> listOrClause = new ArrayList<BOSerializeCriteria>();
	private boolean _isDescendent = false;
	private Map.Entry<String,String> _filterProfilo = null;
	private int maxResult;
	private int firstResult;
	
	public Map<String, Object> getListEquals() {
		return listEquals;
	}

	public void setListEquals(Map<String, Object> listEquals) {
		this.listEquals = listEquals;
	}

	public List<Map<String, Object>> getListbetween() {
		return listbetween;
	}

	public void setListbetween(List<Map<String, Object>> listbetween) {
		this.listbetween = listbetween;
	}

	public List<Map<String, Object>> getListLike() {
		return listLike;
	}

	public void setListLike(List<Map<String, Object>> listLike) {
		this.listLike = listLike;
	}

	public List<Map<String, Object>> getListLikeInsensitive() {
		return listLikeInsensitive;
	}

	public void setListLikeInsensitive(
			List<Map<String, Object>> listLikeInsensitive) {
		this.listLikeInsensitive = listLikeInsensitive;
	}

	public List<Map<String, Object>> getListOperator() {
		return listOperator;
	}

	public void setListOperator(List<Map<String, Object>> listOperator) {
		this.listOperator = listOperator;
	}

	public List<String> getListOrderBy() {
		return listOrderBy;
	}

	public void setListOrderBy(List<String> listOrderBy) {
		this.listOrderBy = listOrderBy;
	}

	public List<String> getListIsNull() {
		return listIsNull;
	}

	public void setListIsNull(List<String> listIsNull) {
		this.listIsNull = listIsNull;
	}

	public List<String> getListIsNotNull() {
		return listIsNotNull;
	}

	public void setListIsNotNull(List<String> listIsNotNull) {
		this.listIsNotNull = listIsNotNull;
	}

	public List<String> getListIsZero() {
		return listIsZero;
	}

	public void setListIsZero(List<String> listIsZero) {
		this.listIsZero = listIsZero;
	}

	public List<Map.Entry<String, Boolean>> getListValueBool() {
		return listValueBool;
	}

	public void setListValueBool(List<Map.Entry<String, Boolean>> listValueBool) {
		this.listValueBool = listValueBool;
	}

	public List<BOSerializeCriteria> getListOrClause() {
		return listOrClause;
	}

	public void setListOrClause(List<BOSerializeCriteria> listOrClause) {
		this.listOrClause = listOrClause;
	}

	public boolean is_isDescendent() {
		return _isDescendent;
	}

	public void set_isDescendent(boolean _isDescendent) {
		this._isDescendent = _isDescendent;
	}

	@SuppressWarnings("rawtypes")
	public void setClassSearcher(Class classSearcher) {
		this.classSearcher = classSearcher;
	}

	@SuppressWarnings("rawtypes")
	public Class getClassSearcher() {
		return classSearcher;
	}

	public Map.Entry<String, String> get_filterProfilo() {
		return _filterProfilo;
	}

	public void set_filterProfilo(Map.Entry<String, String> _filterProfilo) {
		this._filterProfilo = _filterProfilo;
	}

	public List<String> get_listIsNotEmpty() {
		return _listIsNotEmpty;
	}

	public void set_listIsNotEmpty(List<String> listIsNotEmpty) {
		this._listIsNotEmpty = listIsNotEmpty;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}
}
