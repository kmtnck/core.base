package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the plugin_common_warning_accesso database table.
 * 
 */
@Entity
public class PluginCommonWarningAccesso implements Serializable {
	private static final long serialVersionUID = 1L;
	private int idwarning;
	private String descrizione;
	private String ipaddress;
	private Timestamp istante;
	private String utente;

	public String getUtente() {
		return utente;
	}


	public void setUtente(String utente) {
		this.utente = utente;
	}


	public PluginCommonWarningAccesso() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getIdwarning() {
		return this.idwarning;
	}

	public void setIdwarning(int idwarning) {
		this.idwarning = idwarning;
	}


	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}


	public Timestamp getIstante() {
		return this.istante;
	}

	public void setIstante(Timestamp istante) {
		this.istante = istante;
	}


}