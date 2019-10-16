package it.alessandromodica.product.common.exceptions;

public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1161385393431385864L;

	public BusinessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BusinessException() {
		// TODO Auto-generated constructor stub

	}

	public BusinessException(Throwable exception) {
		super(exception);
	}

	public BusinessException(String message, Throwable exception) {
		super(message, exception);
	}
}
