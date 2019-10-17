package it.alessandromodica.product.common;

/**
 * Classe che rappresenta il risultato da fornire in output al client. <br>
 * Viene fornito il contesto del risultato fornito, il valore in formato generico Object pronto per essere serializzato.<br>
 * Uno stato e un messaggio che hanno la funzione di metadati aggiuntivi. Il valore in output vero e proprio e' il valore Payload
 * @author Alessandro
 *
 */
public class OutputData {

	private String context;
	private Object payload;
	private String stato;
	private String messaggio;
	
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
}
