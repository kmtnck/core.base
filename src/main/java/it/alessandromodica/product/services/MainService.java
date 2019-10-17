package it.alessandromodica.product.services;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import it.alessandromodica.product.app.GoToBusiness;
import it.alessandromodica.product.app.MainContext;
import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.PluginCommonLogaccesso;
import it.alessandromodica.product.model.po.PluginGestioneUtenti;
import it.alessandromodica.product.persistence.searcher.BOSearchApp;
import it.alessandromodica.product.services.interfaces.IMainService;

/**
 * Classe che rappresenta il modulo service principale. Viene esteso da tutti
 * gli altri service e implementa metodi comuni a tutti i servizi, come ad
 * esempio il recupero dell'utente corrente o il logging di accesso su una
 * tabella dedicata. Il metodo logAccesso non ha alcuna correlazione con gli
 * strumenti log4j, si limita a scrivere in modo personalizzato una tracciatura
 * di accesso utente, permettendo una estrema personalizzazione di tracciamento
 * attivita degli utenti.
 * 
 * @author Alessandro
 *
 */
@Service
@SuppressWarnings("unchecked")
public class MainService extends GoToBusiness implements IMainService {

	private static final Logger log = Logger.getLogger(MainService.class);

	// XXX: questa classe rappresenta il contesto in cui sono implementati i
	// servizi usati dai componenti di consultazione e storage
	// consultazione storage seguono il pattern command query responsability
	// segregation
	// i servizi possono provvedere a dati di natura eterogenea (da database o
	// altri servizi esterni) oppure agire su dati stessi
	// i servizi sono trasversali e iniettati dove serve.

	/**
	 * Recupera le informazioni dell'utente corrente
	 */
	public PluginGestioneUtenti getUtente(String nickname) throws ServiceException {
		BOSearchApp search = new BOSearchApp();
		search.setNickname(nickname);
		try {

			PluginGestioneUtenti result;
			try {
				result = (PluginGestioneUtenti) repoquery.setEntity(PluginGestioneUtenti.class)
						.getSingleOrDefault(search.getSerialized());
				return result;

			} catch (RepositoryException e) {
				throw new ServiceException(e);
			}

		} catch (

		ServiceException ex) {
			log.error(ex);
			throw ex;
		}
	}

	/**
	 * Logga su una tabella l'attivita dell'utente corrente rappresentata da un
	 * messaggio
	 */
	public void logAccesso(String messaggio, BOUtente utentecorrente) throws ServiceException {

		try {

			PluginCommonLogaccesso toAdd = new PluginCommonLogaccesso();
			toAdd.setIpaddress(utentecorrente.getInforemote());
			toAdd.setDescrizione(messaggio);
			toAdd.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));
			repocommands.setEntity(PluginCommonLogaccesso.class).add(toAdd);

			log.info("---> " + messaggio);

		} catch (RepositoryException e) {
			String msg = "Si e' verificato un errore durante la registrazione di una operazione "
					+ MainContext.TITOLO_APP + " [" + e.getMessage() + "]";
			log.warn(msg, e);
			log.warn(
					"L'eccezione non verra rilanciata, per permettere la prosecuzione dell'operazione e rendere inibito il failure dei log");
		}

	}

}
