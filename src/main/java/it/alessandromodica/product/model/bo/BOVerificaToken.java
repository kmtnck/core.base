package it.alessandromodica.product.model.bo;

public class BOVerificaToken {

	/*
 "issued_to": "320844424243-4eq51lin1mk54sjjldofjjcqd9dl2dq8.apps.googleusercontent.com",
 "audience": "320844424243-4eq51lin1mk54sjjldofjjcqd9dl2dq8.apps.googleusercontent.com",
 "user_id": "114698683329673820502",
 "expires_in": 3012,
 "email": "alessandro.modica@gmail.com",
 "verified_email": true
 
	 * */
	
	private String email;
	private boolean verified_email;
	private String issued_to;
	private String audience;
	private String user_id;
	private int expires_in;
	
	
	private String error_description;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	public boolean isVerified_email() {
		return verified_email;
	}
	public void setVerified_email(boolean verified_email) {
		this.verified_email = verified_email;
	}
	public String getIssued_to() {
		return issued_to;
	}
	public void setIssued_to(String issued_to) {
		this.issued_to = issued_to;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
}
