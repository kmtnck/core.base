package it.alessandromodica.product.context.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.alessandromodica.product.app.MainContext;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.context.interfaces.ISecurity;
import it.alessandromodica.product.model.bo.BOPayloadAuth;
import it.alessandromodica.product.model.bo.BOSecurity;
import it.alessandromodica.product.model.bo.BOSignGoogle;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.bo.BOVerifica;
import it.alessandromodica.product.services.interfaces.ISecurityService;

/**
 * Modulo dedicato al controllo di sicurezza dell'utente corrente, anche se l'utente e' nuovo.
 * @author Alessandro
 *
 */
@Controller
public class MainSecurity extends MainContext implements ISecurity {

	private static final Logger log = Logger.getLogger(MainSecurity.class);

	@Autowired
	ISecurityService _securityService;

	// XXX: classe che rappresenta il contesto di gestione dell'autenticazione e
	// sicurezza

	private MainSecurity() {

	}

	public void setInfo(BOUtente utente) {
		setUtenteCorrente(utente);
		_securityService.setUtenteCorrente(utenteCorrente);
	}

	@Override
	public boolean checkAccessoUtente() throws BusinessException {

		try {
			return _securityService.checkAccessoUtente();
		} catch (ServiceException ex) {
			throw new BusinessException(ex.getMessage(), ex);
		}

	}

	@Override
	public BOSecurity generaScarabocchio() {

		return _securityService.generaScarabocchio();

	}

	@Override
	public BOSignGoogle outhsignout(String email, String nickname) throws BusinessException {
		try {

			return _securityService.outhsignout(email, nickname);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}

	@Override
	public BOSignGoogle outhsignin(BOPayloadAuth payload) throws BusinessException {
		try {

			return _securityService.outhsignin(payload);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}


	@Override
	public BOSecurity checkIntegrity(String versione, boolean isbeta) throws BusinessException {
		try {
			return _securityService.checkIntegrity(versione, isbeta);
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(e);
		}

	}

	@Override
	public BOVerifica verificaUtenza() throws BusinessException {
		try {

			return _securityService.verificaUtenza();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}

	public BOUtente getUtenteCorrente() {
		return utenteCorrente;
	}

}
