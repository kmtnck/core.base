package it.alessandromodica.product.app;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.PluginGestioneUtenti;
import it.alessandromodica.product.persistence.interfaces.IRepositoryCommands;
import it.alessandromodica.product.persistence.interfaces.IRepositoryQueries;
import it.alessandromodica.product.persistence.repo.AppRepository;
import it.alessandromodica.product.services.interfaces.IMainService;

/**
 * La classe astratta piu generica dell'applicazione. Ci sono definiti i
 * repository di accesso e le informazioni minimali dell'utente corrente
 * 
 * In forma statica possono essere implementati metodi comuni ad altri
 * componenti Viene ereditata dai service e dal MainContext. Il MainContext �
 * ereditato dai vari componenti controller business
 * 
 * L'obiettivo � tenere a fattor comune quante piu procedure possibili ,
 * rendendo generico il loro uso , facilitando in un unico punto l'accesso alle
 * risorse le quali sono esplicitamente tipizzate dai vari chiamanti con
 * l'entit� voluta
 * 
 * @author Alessandro
 *
 */
@SuppressWarnings("rawtypes")
public abstract class GoToBusiness {

	public static String TITOLO_APP = "goToBusiness";

	private static final Logger log = Logger.getLogger(GoToBusiness.class);

	@Autowired
	protected IMainService mainService;

	@Autowired
	protected IRepositoryQueries repoquery;

	@Autowired
	protected IRepositoryCommands repocommands;

	@Autowired
	protected AppRepository reporawsql;

	private static ObjectMapper mapper = new ObjectMapper();

	protected BOUtente utenteCorrente;

	public void setUtenteCorrente(BOUtente utente) {

		if (utente != null) {

			utenteCorrente = utente;
			try {

				String value = utente.getNickname() != null ? utente.getNickname() : null;

				if (value != null) {
					log.info("Utente corrente identificato :" + value);

					PluginGestioneUtenti data = mainService.getUtente(value);
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

	protected ArrayList transformListRawData(String rawPortal) throws ServiceException {
		try {
			ArrayList parseRawData = mapper.readValue(rawPortal.toString(), ArrayList.class);
			return parseRawData;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

}
