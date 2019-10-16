package it.alessandromodica.product.model.bo;

public class BOPayloadAuth {

	public class ProfiloUtente {
		private Number Eea;
		private String ig;
		private String ofa;
		private String wea;
		private String Paa;
		private String U3;

		public Number getEea() {
			return Eea;
		}

		public void setEea(Number eea) {
			Eea = eea;
		}

		public String getIg() {
			return ig;
		}

		public void setIg(String ig) {
			this.ig = ig;
		}

		public String getOfa() {
			return ofa;
		}

		public void setOfa(String ofa) {
			this.ofa = ofa;
		}

		public String getWea() {
			return wea;
		}

		public void setWea(String wea) {
			this.wea = wea;
		}

		public String getPaa() {
			return Paa;
		}

		public void setPaa(String paa) {
			Paa = paa;
		}

		public String getU3() {
			return U3;
		}

		public void setU3(String u3) {
			U3 = u3;
		}
	}

	public class GoogleUser {

		public class Token {
			private String token_type;
			private String access_token;
			private String scope;
			private String login_hint;
			private Number expires_in;
			private String id_token;
			private Number first_issued_at;
			private Number expires_at;
			private String idpId;

			public String getToken_type() {
				return token_type;
			}

			public void setToken_type(String token_type) {
				this.token_type = token_type;
			}

			public String getAccess_token() {
				return access_token;
			}

			public void setAccess_token(String access_token) {
				this.access_token = access_token;
			}

			public String getScope() {
				return scope;
			}

			public void setScope(String scope) {
				this.scope = scope;
			}

			public String getLogin_hint() {
				return login_hint;
			}

			public void setLogin_hint(String login_hint) {
				this.login_hint = login_hint;
			}

			public Number getExpires_in() {
				return expires_in;
			}

			public void setExpires_in(Number expires_in) {
				this.expires_in = expires_in;
			}

			public String getId_token() {
				return id_token;
			}

			public void setId_token(String id_token) {
				this.id_token = id_token;
			}

			public Number getFirst_issued_at() {
				return first_issued_at;
			}

			public void setFirst_issued_at(Number first_issued_at) {
				this.first_issued_at = first_issued_at;
			}

			public Number getExpires_at() {
				return expires_at;
			}

			public void setExpires_at(Number expires_at) {
				this.expires_at = expires_at;
			}

			public String getIdpId() {
				return idpId;
			}

			public void setIdpId(String idpId) {
				this.idpId = idpId;
			}
		}

		private String E1;
		private Token Zi;

		public String getE1() {
			return E1;
		}

		public void setE1(String e1) {
			E1 = e1;
		}

		public Token getZi() {
			return Zi;
		}

		public void setZi(Token zi) {
			Zi = zi;
		}
	}

	private ProfiloUtente profiloUtente;
	private GoogleUser googleUser;
	private String nickname;
	private String datacookie;

	public ProfiloUtente getProfiloUtente() {
		return profiloUtente;
	}

	public void setProfiloUtente(ProfiloUtente profiloUtente) {
		this.profiloUtente = profiloUtente;
	}

	public GoogleUser getGoogleUser() {
		return googleUser;
	}

	public void setGoogleUser(GoogleUser googleUser) {
		this.googleUser = googleUser;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getDatacookie() {
		return datacookie;
	}

	public void setDatacookie(String datacookie) {
		this.datacookie = datacookie;
	}

}
