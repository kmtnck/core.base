package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the plugin_gestione_utenti_infoautenticazione database table.
 * 
 */
@Entity
public class GestioneUtentiInfoautenticazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idinfo;

	@Column(length=256)
	private String contesto;

	@Column(nullable=false)
	private int idutente;

	@Column(nullable=false)
	private Timestamp istante;

	@Column(length=256)
	private String nomeparametro;

	private String valueparametro;

	public GestioneUtentiInfoautenticazione() {
	}

	public int getIdinfo() {
		return this.idinfo;
	}

	public void setIdinfo(int idinfo) {
		this.idinfo = idinfo;
	}

	public String getContesto() {
		return this.contesto;
	}

	public void setContesto(String contesto) {
		this.contesto = contesto;
	}

	public int getIdutente() {
		return this.idutente;
	}

	public void setIdutente(int idutente) {
		this.idutente = idutente;
	}

	public Timestamp getIstante() {
		return this.istante;
	}

	public void setIstante(Timestamp istante) {
		this.istante = istante;
	}

	public String getNomeparametro() {
		return this.nomeparametro;
	}

	public void setNomeparametro(String nomeparametro) {
		this.nomeparametro = nomeparametro;
	}

	public String getValueparametro() {
		return this.valueparametro;
	}

	public void setValueparametro(String valueparametro) {
		this.valueparametro = valueparametro;
	}

}