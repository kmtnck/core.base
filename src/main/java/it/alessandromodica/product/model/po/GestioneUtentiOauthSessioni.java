package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the plugin_gestione_utenti_oauth_sessioni database table.
 * 
 */
@Entity
public class GestioneUtentiOauthSessioni implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idouthsessione;

	private String accesstoken;

	private Timestamp dataemissione;

	private Timestamp datascadenza;

	@Column(length=256)
	private String idpId;

	@Column(length=2048)
	private String idtoken;

	@Column(nullable=false)
	private Timestamp istante;

	private String loginhint;

	@Column(length=2000)
	private String scope;

	@Column(length=256)
	private String tokentype;

	private int validita;

	//bi-directional many-to-one association to PluginGestioneUtentiOauth
	@ManyToOne
	@JoinColumn(name="idouth")
	private GestioneUtentiOauth pluginGestioneUtentiOauth;

	public GestioneUtentiOauthSessioni() {
	}

	public int getIdouthsessione() {
		return this.idouthsessione;
	}

	public void setIdouthsessione(int idouthsessione) {
		this.idouthsessione = idouthsessione;
	}

	public String getAccesstoken() {
		return this.accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	public Timestamp getDataemissione() {
		return this.dataemissione;
	}

	public void setDataemissione(Timestamp dataemissione) {
		this.dataemissione = dataemissione;
	}

	public Timestamp getDatascadenza() {
		return this.datascadenza;
	}

	public void setDatascadenza(Timestamp datascadenza) {
		this.datascadenza = datascadenza;
	}

	public String getIdpId() {
		return this.idpId;
	}

	public void setIdpId(String idpId) {
		this.idpId = idpId;
	}

	public String getIdtoken() {
		return this.idtoken;
	}

	public void setIdtoken(String idtoken) {
		this.idtoken = idtoken;
	}

	public Timestamp getIstante() {
		return this.istante;
	}

	public void setIstante(Timestamp istante) {
		this.istante = istante;
	}

	public String getLoginhint() {
		return this.loginhint;
	}

	public void setLoginhint(String loginhint) {
		this.loginhint = loginhint;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTokentype() {
		return this.tokentype;
	}

	public void setTokentype(String tokentype) {
		this.tokentype = tokentype;
	}

	public int getValidita() {
		return this.validita;
	}

	public void setValidita(int validita) {
		this.validita = validita;
	}

	public GestioneUtentiOauth getPluginGestioneUtentiOauth() {
		return this.pluginGestioneUtentiOauth;
	}

	public void setPluginGestioneUtentiOauth(GestioneUtentiOauth pluginGestioneUtentiOauth) {
		this.pluginGestioneUtentiOauth = pluginGestioneUtentiOauth;
	}

}