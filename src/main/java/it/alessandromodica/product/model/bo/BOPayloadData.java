package it.alessandromodica.product.model.bo;

/**
 * Classe container in cui e' valorizzato un generico risultato in formato json
 * oppure in formato reportistica. Il payload qui definito e' fornito in output
 * tramite l'oggetto OutputData
 * 
 * @author Alessandro
 *
 */
public class BOPayloadData extends BOCommon {

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
