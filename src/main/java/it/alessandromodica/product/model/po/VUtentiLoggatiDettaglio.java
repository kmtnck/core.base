package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the sak_utenti_loggati_dettaglio database table.
 * 
 */
@Entity
public class VUtentiLoggatiDettaglio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=256)
	private String descrizione;

	@Id
	@Column(nullable=false)
	private int idaccesso;

	@Column(nullable=false, length=256)
	private String ipaddress;

	@Column(nullable=false)
	private Timestamp istante;

	public VUtentiLoggatiDettaglio() {
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getIdaccesso() {
		return this.idaccesso;
	}

	public void setIdaccesso(int idaccesso) {
		this.idaccesso = idaccesso;
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