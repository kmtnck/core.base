package it.alessandromodica.product.persistence.searcher;

/**
 * Classe che rappresenta il criterio di ricerca between, supportato dalla
 * maggioranza dei database esistenti
 * 
 * @author Alessandro
 *
 */
public class BOBetweenClause extends BOBase {

	/**
	* 
	*/
	private static final long serialVersionUID = -5369911267912281577L;

	private Class<?> typeData;
	private String _nameField;
	private Object _valueFrom;
	private Object _valueTo;

	public String get_nameField() {
		return _nameField;
	}

	public void set_nameField(String _nameField) {
		this._nameField = _nameField;
	}

	public Class<?> getTypeData() {
		return typeData;
	}

	public void setTypeData(Class<?> typeData) {
		this.typeData = typeData;
	}

	public Object get_valueFrom() {
		return _valueFrom;
	}

	public void set_valueFrom(Object _valueFrom) {
		this._valueFrom = _valueFrom;
	}

	public Object get_valueTo() {
		return _valueTo;
	}

	public void set_valueTo(Object _valueTo) {
		this._valueTo = _valueTo;
	}

}
