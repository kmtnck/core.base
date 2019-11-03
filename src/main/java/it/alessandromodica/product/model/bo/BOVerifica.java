package it.alessandromodica.product.model.bo;

public class BOVerifica extends BOCommon {

	private String team;
	private String username;
	private String emailaccount;
	private String esito;
	private boolean stato;
	private String statoutente;

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailaccount() {
		return emailaccount;
	}

	public void setEmailaccount(String emailaccount) {
		this.emailaccount = emailaccount;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getStatoutente() {
		return statoutente;
	}

	public void setStatoutente(String statoutente) {
		this.statoutente = statoutente;
	}

	public boolean isStato() {
		return stato;
	}

	public void setStato(boolean stato) {
		this.stato = stato;
	}

}
