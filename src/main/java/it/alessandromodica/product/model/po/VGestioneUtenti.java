package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the sak_gestione_utenti database table.
 * 
 */
@Entity
public class VGestioneUtenti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Lob
	private String csrftoken;

	@Column(nullable=false)
	private Timestamp dataregistrazione;

	private Timestamp dataregistrazioneoauth;

	@Column(length=256)
	private String email;

	@Column(length=256)
	private String emailaccount;

	@Lob
	private String foto;

	@Id
	@Column(nullable=false)
	private int idutente;

	@Column(nullable=false, length=256)
	private String utente;

	@Column(length=256)
	private String nomeutente;

	@Lob
	private String privatekey;

	@Lob
	private String publickey;

	@Lob
	private String apptoken;

	@Column(length=14)
	private String stato;

	public VGestioneUtenti() {
	}

	public String getCsrftoken() {
		return this.csrftoken;
	}

	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

	public Timestamp getDataregistrazione() {
		return this.dataregistrazione;
	}

	public void setDataregistrazione(Timestamp dataregistrazione) {
		this.dataregistrazione = dataregistrazione;
	}

	public Timestamp getDataregistrazioneoauth() {
		return this.dataregistrazioneoauth;
	}

	public void setDataregistrazioneoauth(Timestamp dataregistrazioneoauth) {
		this.dataregistrazioneoauth = dataregistrazioneoauth;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailaccount() {
		return this.emailaccount;
	}

	public void setEmailaccount(String emailaccount) {
		this.emailaccount = emailaccount;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getIdutente() {
		return this.idutente;
	}

	public void setIdutente(int idutente) {
		this.idutente = idutente;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getNomeutente() {
		return this.nomeutente;
	}

	public void setNomeutente(String nomeutente) {
		this.nomeutente = nomeutente;
	}

	public String getPrivatekey() {
		return this.privatekey;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	public String getPublickey() {
		return this.publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	public String getApptoken() {
		return apptoken;
	}

	public void setApptoken(String apptoken) {
		this.apptoken = apptoken;
	}

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

}