package it.alessandromodica.product.model.bo.query;

import javax.persistence.Entity;
import javax.persistence.Id;

import it.alessandromodica.product.model.bo.BOCommon;

/**
 * Entita business mappata come result set della store procedure definita in customquery
 * @author alessandro.modica
 *
 */
@Entity
//XXX: store procedure di esempio @NamedNativeQuery(name = "utentiLoggati", query = "{ call utentiLoggati(:periodo,:nickname)}", resultClass = BOUtentiConnessi.class)
public class BOUtentiConnessi extends BOCommon {

	@Id
	private String descrizione;
	private String dettaglio;
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDettaglio() {
		return dettaglio;
	}
	public void setDettaglio(String dettaglio) {
		this.dettaglio = dettaglio;
	}

}
