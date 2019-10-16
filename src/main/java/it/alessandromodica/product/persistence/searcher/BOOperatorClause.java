package it.alessandromodica.product.persistence.searcher;

public class BOOperatorClause extends BOBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 387882826647999114L;

	public static final String MINUSEQUALS = "<=";
	public static final String MINUS = "<";
	public static final String MAJOREQUALS = ">=";
	public static final String MAJOR = ">";
	public static final String DISEQUALS = "!=";

	private Class<?> typeData;
	private Object _value;
	private String _nameField;
	private String _operatore;

	public String get_nameField() {
		return _nameField;
	}

	public void set_nameField(String _nameField) {
		this._nameField = _nameField;
	}

	public String get_operatore() {
		return _operatore;
	}

	public void set_operatore(String _operatore) {
		this._operatore = _operatore;
	}

	public Class<?> getTypeData() {
		return typeData;
	}

	public void setTypeData(Class<?> typeData) {
		this.typeData = typeData;
	}

	public Object get_value() {
		return _value;
	}

	public void set_value(Object _value) {
		this._value = _value;
	}

}

