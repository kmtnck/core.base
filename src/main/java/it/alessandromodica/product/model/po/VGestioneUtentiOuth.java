package it.alessandromodica.product.model.po;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the sak_gestione_utenti_outh database table.
 * 
 */
@Entity
public class VGestioneUtentiOuth implements Serializable {
	private static final long serialVersionUID = 1L;

	private Timestamp dataregistrazioneouth;

	@Column(nullable=false)
	private Timestamp dataregistrazioneapp;

	@Column(length=256)
	private String emailaccount;

	@Column(length=256)
	private String emailapp;

	private String foto;

	private int idouth;

	@Id
	@Column(nullable=false)
	private int idutente;

	private String lastidtoken;

	@Column(nullable=false, length=256)
	private String nickname;

	@Column(length=256)
	private String nicknameoauth;

	@Column(length=256)
	private String nomeutente;

	private String privatekey;

	private String publickey;

	@Column(length=14)
	private String stato;

	public VGestioneUtentiOuth() {
	}

	public Timestamp getDataregistrazioneouth() {
		return this.dataregistrazioneouth;
	}

	public void setDataregistrazioneouth(Timestamp dataregistrazioneouth) {
		this.dataregistrazioneouth = dataregistrazioneouth;
	}

	public Timestamp getDataregistrazioneapp() {
		return dataregistrazioneapp;
	}

	public void setDataregistrazioneapp(Timestamp dataregistrazioneapp) {
		this.dataregistrazioneapp = dataregistrazioneapp;
	}

	public String getEmailaccount() {
		return this.emailaccount;
	}

	public void setEmailaccount(String emailaccount) {
		this.emailaccount = emailaccount;
	}

	public String getEmailapp() {
		return emailapp;
	}

	public void setEmailapp(String emailapp) {
		this.emailapp = emailapp;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getIdouth() {
		return this.idouth;
	}

	public void setIdouth(int idouth) {
		this.idouth = idouth;
	}

	public int getIdutente() {
		return this.idutente;
	}

	public void setIdutente(int idutente) {
		this.idutente = idutente;
	}

	public String getLastidtoken() {
		return this.lastidtoken;
	}

	public void setLastidtoken(String lastidtoken) {
		this.lastidtoken = lastidtoken;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNicknameoauth() {
		return this.nicknameoauth;
	}

	public void setNicknameoauth(String nicknameoauth) {
		this.nicknameoauth = nicknameoauth;
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

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

}