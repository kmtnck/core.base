package it.alessandromodica.product.common.exceptions;

public class ServiceException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8612899408934890958L;

	public ServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServiceException() {
		// TODO Auto-generated constructor stub

	}

	public ServiceException(Throwable exception) {
		super(exception);
	}

	public ServiceException(String message, Throwable exception) {
		super(message, exception);
	}
}
