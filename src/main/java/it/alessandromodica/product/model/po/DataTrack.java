package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the plugin_loggerchat_tracciatodati database table.
 * 
 */
@Entity
public class DataTrack implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=128)
	private String idtrack;

	@Column(nullable=false, length=2058)
	private String azione;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date data;

	@Column(nullable=false)
	private Time ora;

	public DataTrack() {
	}

	public String getIdtrack() {
		return idtrack;
	}

	public void setIdtrack(String idtrack) {
		this.idtrack = idtrack;
	}

	public String getAzione() {
		return this.azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Time getOra() {
		return this.ora;
	}

	public void setOra(Time ora) {
		this.ora = ora;
	}
	
}