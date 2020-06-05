package it.alessandromodica.product.context.consultazione;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.alessandromodica.product.app.MainContext;
import it.alessandromodica.product.common.OutputData;
import it.alessandromodica.product.common.InputData;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.context.interfaces.IConsultazione;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.services.interfaces.IAppService;

@Controller
public class MainConsultazione extends MainContext implements IConsultazione {

	private static final Logger log = Logger.getLogger(MainConsultazione.class);

	@Autowired
	private IAppService appService;

	public void setInfo(BOUtente utente, InputData inputData) {
		authContext.setUtenteCorrente(utente);
		this.inputData = inputData;
	}

	@Override
	public OutputData getUtentiConnessi() throws BusinessException {

		try {

			OutputData dataResult = new OutputData();
			dataResult.setPayload(appService
					.getUtentiConnessi());

			return dataResult;

		} catch (ServiceException e) {
			String msg = "Si e' verificato un errore durante il recupero degli utenti loggati " + e.getMessage();
			log.error(msg, e);
			throw new BusinessException(msg, e);
		}

	}

	// XXX: classe rappresentante l'insieme di funzionalit� business esposte al
	// frontend per la consultazione dei dati
}
