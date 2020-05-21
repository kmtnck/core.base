package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the plugin_gestione_utenti_oauth database table.
 * 
 */
@Entity
public class GestioneUtentiOauth implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int idouth;

	@Column(length=256)
	private String email;

	private String foto;
	
	@Column(length = 65535)
	private String idtoken;

	@Column(nullable=false)
	private Timestamp istante;

	@Column(length=256)
	private String nickname;

	@Column(length=256)
	private String nomeutente;

	//bi-directional many-to-one association to PluginGestioneUtentiOauthSessioni
	@OneToMany(mappedBy="pluginGestioneUtentiOauth")
	private List<GestioneUtentiOauthSessioni> pluginGestioneUtentiOauthSessionis;

	public GestioneUtentiOauth() {
	}

	public int getIdouth() {
		return this.idouth;
	}

	public void setIdouth(int idouth) {
		this.idouth = idouth;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNomeutente() {
		return this.nomeutente;
	}

	public void setNomeutente(String nomeutente) {
		this.nomeutente = nomeutente;
	}

	public List<GestioneUtentiOauthSessioni> getPluginGestioneUtentiOauthSessionis() {
		return this.pluginGestioneUtentiOauthSessionis;
	}

	public void setPluginGestioneUtentiOauthSessionis(List<GestioneUtentiOauthSessioni> pluginGestioneUtentiOauthSessionis) {
		this.pluginGestioneUtentiOauthSessionis = pluginGestioneUtentiOauthSessionis;
	}

	public GestioneUtentiOauthSessioni addPluginGestioneUtentiOauthSessioni(GestioneUtentiOauthSessioni pluginGestioneUtentiOauthSessioni) {
		getPluginGestioneUtentiOauthSessionis().add(pluginGestioneUtentiOauthSessioni);
		pluginGestioneUtentiOauthSessioni.setPluginGestioneUtentiOauth(this);

		return pluginGestioneUtentiOauthSessioni;
	}

	public GestioneUtentiOauthSessioni removePluginGestioneUtentiOauthSessioni(GestioneUtentiOauthSessioni pluginGestioneUtentiOauthSessioni) {
		getPluginGestioneUtentiOauthSessionis().remove(pluginGestioneUtentiOauthSessioni);
		pluginGestioneUtentiOauthSessioni.setPluginGestioneUtentiOauth(null);

		return pluginGestioneUtentiOauthSessioni;
	}

}