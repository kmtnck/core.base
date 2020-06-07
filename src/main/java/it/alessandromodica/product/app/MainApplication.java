package it.alessandromodica.product.app;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.alessandromodica.product.callback.SettingCookie;
import it.alessandromodica.product.common.AdapterGson;
import it.alessandromodica.product.common.Constants;
import it.alessandromodica.product.common.InputData;
import it.alessandromodica.product.common.enumerative.AppContext;
import it.alessandromodica.product.common.enumerative.RequestVariable;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.context.interfaces.ISecurity;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.persistence.interfaces.IRepositoryCommands;
import it.alessandromodica.product.persistence.uow.UnitOfWork;

/**
 * E' il componente principale dell'applicazione. E' contenuto l'intero set di
 * funzionalita' dell'applicazione. E' istanziabile da un contesto spring e da
 * esso e' possibile estrarre tutti i bean per poter fare testing e a sua volta
 * farli interagire in altri componenti.
 * 
 * Può essere fatto partire in qualsiasi contesto runtime java platform.
 * 
 * Eredita il MainContext il quale rappresenta lo scope comune di tutti i
 * controller che manipolano dati a seconda le esigenze.
 * 
 * La superclasse GoToBusiness è condivisa sia dal maincontext che dal
 * mainservice in cui sono raccolti i componenti repository utilizzati per
 * accedere alle risorse su un datastorage.
 * 
 * Questa gerarchia ad albero permette di avere un controllo efficente di tutte
 * le componenti distribuendo gli scope di funzionalità in base alle mutevoli
 * esigenze
 * 
 * @author Alessandro
 *
 */
//@SpringBootApplication
@Component
@SuppressWarnings("rawtypes")
public class MainApplication extends MainContext {

    /*public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }*/
	// XXX: classe principale e cardine di tutto il progetto
	// deve essere visto come il cappello di riferimento per tutta la gerarchia
	// del software

	@Autowired
	protected IRepositoryCommands repocommands;

	private static final Logger log = Logger.getLogger(MainApplication.class);

	@Autowired
	private ISecurity controllerSecurity;

	@Autowired
	private SettingCookie settingcookie;

	@SuppressWarnings("unchecked")
	public static Object getPojo(String rawData, Class clazz) {
		log.debug(rawData);
		Object result = (AdapterGson.GetInstance().fromJson(rawData, clazz));
		return result;
	}

	/**
	 * Handler di accesso a tutte le richieste esterne online. Ciascuna richiesta
	 * viene processata a partire da questo metodo
	 * 
	 * @param inputData
	 * @param remoteAddrs
	 * @param referer
	 * @param useragent
	 * @param rawUtente
	 * @param appcontext
	 * @return
	 * @throws BusinessException
	 */
	public Object processAction(InputData inputData, String remoteAddrs, String referer, String useragent,
			String rawUtente, AppContext appcontext) throws BusinessException {
		try {

			BOUtente currentUtente = null;
			Object hashscript = inputData.getMapRequestData().get(RequestVariable.hashscript);
			Object datacookie = inputData.getMapRequestData().get(RequestVariable.datacookie);
			currentUtente = getVisitatoreCorrente(remoteAddrs, referer, useragent, rawUtente,
					hashscript != null ? hashscript.toString() : null,
					datacookie != null ? datacookie.toString() : null);

			Object dataToSendClient = accessCapabilities(inputData, currentUtente, appcontext);

			return dataToSendClient;

		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage(), ex);
		}
	}

	/**
	 * Parser dell'header della richiesta esterna per analizzare il visitatore
	 * corrente (umano o macchina che sia)
	 * 
	 * @param remoteAddrs
	 * @param referer
	 * @param useragent
	 * @param rawUtente
	 * @param hashscript
	 * @param datacookie
	 * @return
	 * @throws BusinessException
	 */
	private BOUtente getVisitatoreCorrente(String remoteAddrs, String referer, String useragent, String rawUtente,
			String hashscript, String datacookie) throws BusinessException {
		BOUtente currentUtente = new BOUtente();
		// XXX: si dovrà definire un parametro token per dare
		// autorizzazione di accesso ad applicazioni terze
		if (!AdapterGson.isJSONValid(rawUtente, BOUtente.class)) {
			currentUtente = new BOUtente();
			if (rawUtente == null) {
				rawUtente = "Anonimo";
			}
			currentUtente.setNickname(rawUtente);

		} else {
			currentUtente = (BOUtente) getPojo(rawUtente, BOUtente.class);
		}

		if (currentUtente == null)
			throw new BusinessException("L'utente non e' stato riconosciuto");
		else {
			currentUtente.setInforemote(remoteAddrs);
			currentUtente.setReferer(referer);
			currentUtente.setUseragent(useragent);
			currentUtente.setHashscript(hashscript);
			currentUtente.setCookies(datacookie);
		}
		return currentUtente;
	}

	/**
	 * Handler standard per una canonica sign in out di un account google. La parte
	 * di autenticazione e' a lato javascript in uno script opportunamente
	 * autenticato dalla chiave dominio opportuna.
	 * 
	 * @param inputData
	 * @param payloadOauth
	 * @param appcontext
	 * @return
	 * @throws BusinessException
	 */
	public Object processSignInOutGoogle(InputData inputData, String payloadOauth, AppContext appcontext)
			throws BusinessException {
		cassaforte.setControllerSecurity(controllerSecurity);
		log.info(">>> Si sta richiedendo la fase " + appcontext + " per accedere ai servizi " + Constants.TITOLO_APP
				+ " tramite google signin ...");

		switch (appcontext) {
		case outhsignin:
			if (StringUtils.isNotBlank(payloadOauth))
				cassaforte.setRawPayload(payloadOauth);
			else
				throw new BusinessException(
						"Attenzione! Si sta chiedendo l'autenticazione con google osign senza fornire le corrette credenziali.");
			break;
		case outhsignout:
			cassaforte.setEmail(inputData.getMapRequestData().get(RequestVariable.email).toString());
			cassaforte.setNickname(inputData.getMapRequestData().get(RequestVariable.nickname).toString());

			break;
		default:
			break;
		}

		Object dataToSendClient = cassaforte.getEsito(appcontext);

		log.info("<<< Richiesta " + appcontext + " per accedere ai servizi " + Constants.TITOLO_APP
				+ " tramite google signin terminata!!");

		return dataToSendClient;
	}

	/**
	 * Metodo di fattor comune delle capabilities supportate dall'applicazione. Per
	 * capabilities si intende qualsiasi funzionalità che dia esperienza utente sia
	 * in termini di produttività che di intrattenimento.
	 * 
	 * @param inputData
	 * @param currentUtente
	 * @param appcontext
	 * @return
	 * @throws BusinessException
	 */
	private Object accessCapabilities(InputData inputData, BOUtente currentUtente, AppContext appcontext)
			throws BusinessException {

		log.info(">>> L'utente " + currentUtente.getNickname() + " chiede l'accesso al sistema " + Constants.TITOLO_APP
				+ " per il contesto " + appcontext + " ... ");

		// XXX: istanza del blocco di sicurezza. Serve a verificare la
		// validita e integrita di ciascuna chiamata esterna
		// L'istanza e' sempre valida con qualsiasi tipo di oggetto
		// utente passato in input. Anche adulterato
		controllerSecurity.setUtenteCorrente(currentUtente);
		// XXX: primo controllo di sicurezza che verifica se l'utente e'
		// gia riconosciuto come utente registrato, oppure utente
		// bannato, ma anche un utente nuovo
		
		log.info("Definita la cassaforte di sicurezza per l'utente " + currentUtente.getNickname()
				+ " per eseguire la fase " + appcontext.name());
		
		Object dataToSendClient = null;

		switch (appcontext) {
		//contesto init di primo step di verifica accesso
		case sendscraps:

			dataToSendClient = controllerSecurity.generaScarabocchio();

			break;
		//invio hash valido per l'autenticazione e confidenzialita della richiesta (prima e seconda).
		//se il metodo da esito positivo, viene validata la integrita del dato con gli opportuni cookie session (terza regola)
		case checkintegrity:
			try {

				settingcookie.setEntities(null, currentUtente);
				repocommands.executeTransaction(settingcookie);

				cassaforte.setVersione(inputData.getMapRequestData().get(RequestVariable.versione) != null
						? inputData.getMapRequestData().get(RequestVariable.versione).toString()
						: null);
				cassaforte.setIsbeta(inputData.getMapRequestData().get(RequestVariable.isbeta) != null
						? inputData.getMapRequestData().get(RequestVariable.isbeta).toString()
						: null);

				dataToSendClient = cassaforte.getEsito(appcontext);

			} catch (RepositoryException e) {
				String msg = "Si e' verificato un errore durante il settaggio dei cookie " + e.getMessage();
				log.error(msg, e);
				throw new BusinessException(msg, e);
			}
			break;
		case verificautenza:
			// XXX: i contesti qui definiti sono legati alle fasi di
			// verifica e controllo dell'utente in tutte le fasi
			// previste
			// login google, logout google, auth in due fasi, autenticazione
			// semplice
			dataToSendClient = cassaforte.getEsito(appcontext);

			break;
		default:
			if (controllerSecurity.checkAccessoUtente()) {
				dataToSendClient = getResult(appcontext, inputData, currentUtente);
			} else
				dataToSendClient = "Accesso ai servizi " + Constants.TITOLO_APP + " negato!!";
			break;
		}
		log.info("<<< Richiesta dell'utente per il contesto " + appcontext.name() + " " + currentUtente.getNickname()
				+ " terminata!! ");

		return dataToSendClient;
	}

	@Deprecated
	public static void InitApp(String appcontext) throws BusinessException {

		try {
			setUp(appcontext);
		} catch (RepositoryException exsso) {
			String msg = "Si è verificato un errore durante l'inizializzazione del " + Constants.TITOLO_APP;
			log.error(msg, exsso);
			throw new BusinessException(msg, exsso);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new BusinessException(ex);
		}
	}

	@Deprecated
	private static void setUp(String appcontext) throws RepositoryException {
		log.info("Caricamento ambiente " + Constants.TITOLO_APP + " in corso del contesto [" + appcontext + "]...");
		UnitOfWork.initSessionFactory(appcontext);
		log.info("Istanza Persistence Hibernate avviata.");

		// lettura tipologiche e caricamento in variabili statiche next
		log.info("Caricamento tipologiche " + Constants.TITOLO_APP + " completato.");
		log.info("Ambiente " + appcontext + " caricato con successo!");
	}

}
