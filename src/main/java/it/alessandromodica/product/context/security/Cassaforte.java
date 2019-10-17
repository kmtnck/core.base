package it.alessandromodica.product.context.security;

import org.springframework.stereotype.Component;

import it.alessandromodica.product.app.MainApplication;
import it.alessandromodica.product.common.enumerative.AppContext;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.context.interfaces.ISecurity;
import it.alessandromodica.product.model.bo.BOPayloadAuth;

/**
 * Componente in cui viene verificata l'autenticita' dell'utenza richiedente.
 * Sono definite le informazioni base per il controllo di sicurezza:
 * la versione del software e se e' la versione beta
 * Le informazioni dell'utente richiedente, l'email, e il suo username qualora risulta gia registrato.
 * 
 * @author Alessandro
 *
 */
@Component
public class Cassaforte {

	private String versione;
	private String isbeta;
	private String rawPayload;
	private String email;
	private String nickname;

	ISecurity controllerSecurity;

	public void setControllerSecurity(ISecurity controllerSecurity) {
		this.controllerSecurity = controllerSecurity;
	}

	public Object getEsito(AppContext appcontext) throws BusinessException {

		try {
			Object dataToSendClient = null;
			switch (appcontext) {
			case checkintegrity:

				if (!controllerSecurity.getUtenteCorrente().isIsbot()) {
					String versioneapp = versione;
					dataToSendClient = controllerSecurity.checkIntegrity(versioneapp,
							"true".equals(isbeta) ? true : false);
				} else {
					dataToSendClient = "L'integrita' di sicurezza alle app di terze parti e' in coming soon...";
				}

				break;
			case outhsignin:
				BOPayloadAuth payloadAuth = null;
				try {
					payloadAuth = (BOPayloadAuth) MainApplication.getPojo(rawPayload, BOPayloadAuth.class);

					dataToSendClient = controllerSecurity.outhsignin(payloadAuth);

				} catch (Exception ex) {
					throw new BusinessException(
							"Il token di autenticazione non e' valido. L'attivita e' stata registrata.");
				}

				break;
			case outhsignout:
				dataToSendClient = controllerSecurity.outhsignout(email, nickname);
				break;
			case verificautenza:
				dataToSendClient = controllerSecurity.verificaUtenza();
				break;
			default:
				break;
			}

			return dataToSendClient;

		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage(), ex);
		}
	}

	public void setVersione(String versione) {
		this.versione = versione;
	}

	public void setIsbeta(String isbeta) {
		this.isbeta = isbeta;
	}

	public void setRawPayload(String rawPayload) {
		this.rawPayload = rawPayload;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
