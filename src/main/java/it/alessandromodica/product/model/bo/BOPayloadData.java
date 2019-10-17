package it.alessandromodica.product.model.bo;

/**
 * Classe container in cui e' valorizzato un generico risultato in formato json oppure in formato reportistica.
 * @author Alessandro
 *
 */
public class BOPayloadData {

	private String report;
	private Object json;
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public Object getJson() {
		return json;
	}
	public void setJson(Object json) {
		this.json = json;
	}
}
