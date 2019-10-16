package it.alessandromodica.product.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import it.alessandromodica.product.app.GoToBusiness;
import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.po.VUtentiLoggatiDettaglio;
import it.alessandromodica.product.persistence.searcher.BOSearchApp;
import it.alessandromodica.product.services.interfaces.IAppService;

@Service
@SuppressWarnings("unchecked")
public class ConsultazioneService extends GoToBusiness implements IAppService {

	// @Autowired
	// MainApplication mainApplication;

	private static final Logger log = Logger.getLogger(ConsultazioneService.class);

	public List<VUtentiLoggatiDettaglio> getUtentiConnessi() throws ServiceException {
		try {
			log.info("Utenti loggati recuperati con successo.");
			return recoverUtenti();
		} catch (RepositoryException e) {
			String msg = "Si e' verificato un errore durante il recupero degli utenti loggati " + e.getMessage();
			log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}

	private List<VUtentiLoggatiDettaglio> recoverUtenti() throws RepositoryException {
		List<VUtentiLoggatiDettaglio> result = repoquery.setEntity(VUtentiLoggatiDettaglio.class)
				.search(new BOSearchApp().getSerialized());

		return result;
	}

}
