package it.alessandromodica.product.app;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.GestioneUtenti;
import it.alessandromodica.product.services.interfaces.IMainService;

@Component
public class AuthContext {

	private static final Logger log = Logger.getLogger(AuthContext.class);

	@Autowired
	private IMainService mainService;

	protected BOUtente utenteCorrente;

	public void setUtenteCorrente(BOUtente utente) {

		if (utente != null) {

			utenteCorrente = utente;
			try {

				String value = utente.getNickname() != null ? utente.getNickname() : null;

				if (value != null) {
					log.info("Utente corrente identificato :" + value);

					GestioneUtenti data = mainService.getUtente(value);
					if (data != null) {
						utenteCorrente.setIdutente(data.getIdutente());
						utenteCorrente.setPublickey(data.getPublickey());
						utenteCorrente.setPrivatekey(data.getPrivatekey());
						utenteCorrente.setScarabocchio(data.getScarabocchio());

						log.debug("Configurate le impostazioni dell'utente corrente");
					}
				}

			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				log.warn("Non e' stato possibile recuperare le informazioni dell'utente " + utente.getNickname());
			}
		}
	}

	public BOUtente getUtenteCorrente() {
		return utenteCorrente;
	}
}
