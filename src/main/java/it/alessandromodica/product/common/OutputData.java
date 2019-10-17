package it.alessandromodica.product.common;

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
