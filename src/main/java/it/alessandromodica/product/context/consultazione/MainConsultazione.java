package it.alessandromodica.product.context.consultazione;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.alessandromodica.product.app.MainContext;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.context.interfaces.IConsultazione;
import it.alessandromodica.product.model.bo.BOAppPayload;
import it.alessandromodica.product.model.bo.BORequestData;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.services.interfaces.IAppService;

@Controller
public class MainConsultazione extends MainContext implements IConsultazione {

	private static final Logger log = Logger.getLogger(MainConsultazione.class);

	@Autowired
	private IAppService appService;

	public void setInfo(BOUtente utente, BORequestData inputData) {
		setUtenteCorrente(utente);
		this.inputData = inputData;
	}

	@Override
	public BOAppPayload getUtentiConnessi() throws BusinessException {

		try {

			BOAppPayload dataResult = new BOAppPayload();
			dataResult.setPayload(appService
					.getUtentiConnessi());

			return dataResult;

		} catch (ServiceException e) {
			String msg = "Si e' verificato un errore durante il recupero degli utenti loggati " + e.getMessage();
			log.error(msg, e);
			throw new BusinessException(msg, e);
		}

	}

	// XXX: classe rappresentante l'insieme di funzionalità business esposte al
	// frontend per la consultazione dei dati
}
