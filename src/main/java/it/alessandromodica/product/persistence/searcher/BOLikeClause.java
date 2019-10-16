package it.alessandromodica.product.persistence.searcher;

public class BOLikeClause extends BOBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6553070847079003064L;

	public static final String LIKE_STANDARD = "%";

	private String _nameField;
	private String _value;
	private boolean _isInsensitive = true;

	public String get_nameField() {
		return _nameField;
	}

	public void set_nameField(String _nameField) {
		this._nameField = _nameField;
	}

	public String get_value() {
		return _value;
	}

	public void set_value(String _value) {
		this._value = _value;
	}

	public boolean is_isInsensitive() {
		return _isInsensitive;
	}

	public void set_isInsensitive(boolean _isInsensitive) {
		this._isInsensitive = _isInsensitive;
	}

}
