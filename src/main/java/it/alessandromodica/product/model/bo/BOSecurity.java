package it.alessandromodica.product.model.bo;

public class BOSecurity {

	/*
	 * 
	 * 'passcode' => $ 'publickey' => 'username' => $ 'esito' => $esi 'status'
	 * => $st 'scarabocchio' 'mustRegister' 'mustupdate' => 'statoutente' =
	 * 'isbeta' => $is 'identity' => $
	 * 
	 */
	private String passcode;
	private String publickey;
	private String username;
	private String esito;
	private boolean status = false;
	private String scarabocchio;
	private boolean mustRegister;
	private String mustupdate = "";
	private String statoutente;
	private boolean isbeta;
	private boolean identity;
	
	public String getPasscode() {
		return passcode;
	}
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	public String getPublickey() {
		return publickey;
	}
	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getScarabocchio() {
		return scarabocchio;
	}
	public void setScarabocchio(String scarabocchio) {
		this.scarabocchio = scarabocchio;
	}
	public boolean isMustRegister() {
		return mustRegister;
	}
	public void setMustRegister(boolean mustRegister) {
		this.mustRegister = mustRegister;
	}
	public String getMustupdate() {
		return mustupdate;
	}
	public void setMustupdate(String mustupdate) {
		this.mustupdate = mustupdate;
	}
	public String getStatoutente() {
		return statoutente;
	}
	public void setStatoutente(String statoutente) {
		this.statoutente = statoutente;
	}
	public boolean isIsbeta() {
		return isbeta;
	}
	public void setIsbeta(boolean isbeta) {
		this.isbeta = isbeta;
	}
	public boolean isIdentity() {
		return identity;
	}
	public void setIdentity(boolean identity) {
		this.identity = identity;
	}
	
}
