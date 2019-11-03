package it.alessandromodica.product.model.bo;

public class BOUtente extends BOCommon {

	private String team;
	private String nickname;
	private String email;

	private String publickey;
	private String privatekey;
	private String scarabocchio;
	private String inforemote;
	private String useragent;
	private String referer;
	private String hashscript;
	private String cookies;
	private int idutente;
	private boolean isbot;
	private String tokenapp;

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPublickey() {
		return publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	public String getScarabocchio() {
		return scarabocchio;
	}

	public void setScarabocchio(String scarabocchio) {
		this.scarabocchio = scarabocchio;
	}

	public String getInforemote() {
		return inforemote;
	}

	public void setInforemote(String inforemote) {
		this.inforemote = inforemote;
	}

	public String getUseragent() {
		return useragent;
	}

	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getHashscript() {
		return hashscript;
	}

	public void setHashscript(String hashscript) {
		this.hashscript = hashscript;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public int getIdutente() {
		return idutente;
	}

	public void setIdutente(int idutente) {
		this.idutente = idutente;
	}

	public boolean isIsbot() {
		return isbot;
	}

	public void setIsbot(boolean isbot) {
		this.isbot = isbot;
	}

	public String getTokenapp() {
		return tokenapp;
	}

	public void setTokenapp(String tokenapp) {
		this.tokenapp = tokenapp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrivatekey() {
		return privatekey;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

}
