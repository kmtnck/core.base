package it.alessandromodica.product.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.alessandromodica.product.app.AuthContext;
import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.po.VUtentiLoggatiDettaglio;
import it.alessandromodica.product.persistence.interfaces.IRepositoryQueries;
import it.alessandromodica.product.persistence.searcher.BOSearchApp;
import it.alessandromodica.product.services.interfaces.IAppService;

/**
 * Classe che rappresenta il modulo service in cui sono implementati tutti i
 * metodi di tipo Consultazione, lettura, ricerca e tutto cio che e' attinente
 * alla ricerca di dati a seconda le esigenze del software.
 * 
 * Ogni metodo ha la sua firma nella interfaccia IAppService e di fatto potrebbe
 * essere il modulo service piu usato dall'applicazione.
 * 
 * @author Alessandro
 *
 */
@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class ConsultazioneService implements IAppService {

	@Autowired
	protected IRepositoryQueries repoquery;

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
		List<VUtentiLoggatiDettaglio> result = repoquery//.setEntity(VUtentiLoggatiDettaglio.class)
				.search(new BOSearchApp(VUtentiLoggatiDettaglio.class).getSerialized());

		return result;
	}

}
