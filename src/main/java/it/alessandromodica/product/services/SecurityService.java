package it.alessandromodica.product.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.alessandromodica.product.app.GoToBusiness;
import it.alessandromodica.product.app.MainApplication;
import it.alessandromodica.product.app.MainContext;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.model.bo.BOPayloadAuth;
import it.alessandromodica.product.model.bo.BOSecurity;
import it.alessandromodica.product.model.bo.BOSignGoogle;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.bo.BOVerifica;
import it.alessandromodica.product.model.bo.BOVerificaToken;
import it.alessandromodica.product.model.bo.BOPayloadAuth.GoogleUser;
import it.alessandromodica.product.model.bo.BOPayloadAuth.ProfiloUtente;
import it.alessandromodica.product.model.bo.BOPayloadAuth.GoogleUser.Token;
import it.alessandromodica.product.model.po.PluginCommonBlacklist;
import it.alessandromodica.product.model.po.PluginGestioneApps;
import it.alessandromodica.product.model.po.PluginGestioneUtenti;
import it.alessandromodica.product.model.po.PluginGestioneUtentiOauth;
import it.alessandromodica.product.model.po.PluginGestioneUtentiOauthSessioni;
import it.alessandromodica.product.model.po.VGestioneUtenti;
import it.alessandromodica.product.model.po.VGestioneUtentiOuth;
import it.alessandromodica.product.persistence.searcher.BOSearchApp;
import it.alessandromodica.product.services.interfaces.IMainService;
import it.alessandromodica.product.services.interfaces.ISecurityService;

/**
 * Classe che rappresenta il modulo service security. Sono implementati i metodi
 * per il riconoscimento dell'utente, in base ai criteri e permessi definiti
 * dall'applicazione. Questo modulo potrebbe subire modifiche e customizzazioni
 * spinte a seconda il tipo di sicurezza scelta nello sviluppo dell'applicativo.
 * In questo modulo sono implementati i metodi minimi necessari per il
 * riconoscimento utente, preso di ispirazione da una applicazione che fa uso di
 * questo service.
 * 
 * I metodi di login e logout da una sessione google single sign on garantiscono
 * l'autenticita dell'utente google che richiede la registrazione
 * all'applicazione.
 * 
 * I metodi checkIntegrity e verificaUtenza sono implementati a seconda le
 * esigenze dell'applicazione e dal tipo di strategia di sicurezza adottata. In
 * questa classe sono implementati due esempi di autenticazione.
 * 
 * @author Alessandro
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SecurityService extends GoToBusiness implements ISecurityService {

	private static final Logger log = Logger.getLogger(SecurityService.class);

	private static final String VERSION_RELEASE = "0.0.1";
	private static final String VERSION_BETA = "0.0.1-BETA";

	@Autowired
	protected IMainService _mainService;

	public boolean checkAccessoUtente() throws ServiceException {
		boolean esito = false;

		try {

			esito = isRequestBot();

			// XXX: se non e' riconosciuto come bot, allora si verifica
			// l'abilitazione di accesso dell'utente
			// questo controllo e' eseguito ad ogni richiesta . E' il metodo che
			// funge da gestore permessi di accesso
			if (!esito) {
				esito = esitoCipher(utenteCorrente);
			}

			return esito;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}

	}

	public BOSecurity generaScarabocchio() {

		BOSecurity result = new BOSecurity();

		if (utenteCorrente.getPrivatekey() != null) {
			result.setPublickey(utenteCorrente.getPublickey());
			result.setPasscode(utenteCorrente.getPublickey() + utenteCorrente.getPrivatekey());
			try {

				PluginGestioneUtenti data = _mainService.getUtente(utenteCorrente.getNickname());
				String scarabocchio = RandomScraps.generaFrase();
				result.setScarabocchio(scarabocchio);
				data.setScarabocchio(scarabocchio);
				repocommands.setEntity(PluginGestioneUtenti.class).update(data);

			} catch (ServiceException | RepositoryException e) {
				log.warn("Non e' stato possibile recuperare i dati dell'utente", e);
			}

		} else {
			log.info("L'utente non e' registrato oppure e' un applicazione di terze parti");
			result.setScarabocchio(RandomScraps.generaFrase());
		}

		return result;

	}

	public BOSignGoogle outhsignout(String email, String nickname) throws ServiceException {
		BOSignGoogle result = new BOSignGoogle();

		try {

			BOSearchApp criteria = new BOSearchApp();
			criteria.setEmail(email);
			PluginGestioneUtentiOauth data = (PluginGestioneUtentiOauth) repoquery
					.setEntity(PluginGestioneUtentiOauth.class).getSingleOrDefault(criteria.getSerialized());

			if (data != null) {
				repocommands.setEntity(PluginGestioneUtentiOauthSessioni.class).deleteFromId(data.getIdouth(),
						"idouth");
				repocommands.setEntity(PluginGestioneUtentiOauth.class).delete(data);
				result.setEsito("Utente " + email + " rimosso con successo!");
			}

			criteria = new BOSearchApp();
			criteria.setNickname(nickname);

			// XXX: applicare la propria logica di individuazione utente con la
			// propria infrastruttura

			VGestioneUtentiOuth statoapp = (VGestioneUtentiOuth) repoquery.setEntity(VGestioneUtentiOuth.class)
					.getSingleOrDefault(criteria.getSerialized());

			if (statoapp != null)
				result.setStatoutente(statoapp.getStato());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e);
		}

		return result;
	}

	public BOSignGoogle outhsignin(BOPayloadAuth payload) throws ServiceException {
		BOSignGoogle result = new BOSignGoogle();

		result.setEsito("Account google non valido");
		result.setStato("NOTSIGNIN");

		boolean canAddSession = true;

		ProfiloUtente profilo = payload.getProfiloUtente();

		if (profilo != null) {
			String email = profilo.getU3();
			String username = profilo.getIg();
			String uriFoto = profilo.getPaa();
			GoogleUser infogoogle = payload.getGoogleUser();
			Token tokenInfo = infogoogle.getZi();
			String idtoken = tokenInfo.getId_token();

			BOVerificaToken esitoVerifica = verifyIdToken(idtoken);

			boolean validToken = true;
			if (esitoVerifica != null) {
				if (!email.equals(esitoVerifica.getEmail()) || !esitoVerifica.isVerified_email()) {
					result.setEsito("Le email non coincidono " + email + " " + esitoVerifica.getEmail());
					result.setStato("WRONGLOGIN");
					validToken = false;
				}

			} else {
				result.setEsito("Si e' verificato un problema durante la verifica del token");
				result.setStato("ERRORLOGIN");
				validToken = false;
			}

			if (validToken) {
				try {

					BOSearchApp criteria = new BOSearchApp();
					criteria.setEmail(email);

					int checkauth = repoquery.setEntity(PluginGestioneUtentiOauth.class)
							.getCount(criteria.getSerialized());

					if (checkauth == 0) {
						PluginGestioneUtentiOauth toAdd = new PluginGestioneUtentiOauth();
						toAdd.setNomeutente(username);
						toAdd.setFoto(uriFoto);
						toAdd.setIdtoken(idtoken);
						toAdd.setEmail(email);
						toAdd.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));

						repocommands.setEntity(PluginGestioneUtentiOauth.class).add(toAdd);

						result.setEsito("Utente registrato con successo al " + MainContext.TITOLO_APP + ".");
						result.setStato("SUCCESSLOGIN");

					} else {

						criteria = new BOSearchApp();
						criteria.setIdtoken(idtoken);

						int checksessione = repoquery.setEntity(PluginGestioneUtentiOauthSessioni.class)
								.getCount(criteria.getSerialized());

						if (checksessione == 0) {
							result.setEsito("Utente " + email
									+ " risulta registrato ed e' stata aggiunta la richiesta di accesso alla sessione");
						} else {
							canAddSession = false;
							result.setEsito(
									"Utente " + email + " risulta registrato e proveniente da una sessione nota.");
						}
						result.setStato("SIGNEDIN");
					}

					if (canAddSession) {
						criteria = new BOSearchApp();
						criteria.setEmail(email);

						PluginGestioneUtentiOauth poauth = ((PluginGestioneUtentiOauth) repoquery
								.setEntity(PluginGestioneUtentiOauth.class)
								.getFirstOrDefault(criteria.getSerialized()));

						PluginGestioneUtentiOauthSessioni toAdd = new PluginGestioneUtentiOauthSessioni();
						// toAdd.setIdouthsessione(max);
						toAdd.setPluginGestioneUtentiOauth(poauth);
						toAdd.setIdtoken(idtoken);
						toAdd.setLoginhint(tokenInfo.getLogin_hint());
						toAdd.setScope(tokenInfo.getScope());
						toAdd.setTokentype(tokenInfo.getToken_type());
						toAdd.setAccesstoken(tokenInfo.getAccess_token());
						toAdd.setIdpId(tokenInfo.getIdpId());
						// toAdd.setDatascadenza(tokenInfo.getExpires_at());
						toAdd.setValidita(tokenInfo.getExpires_in().intValue());
						toAdd.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));
						// toAdd.setDataemissione(tokenInfo.getFirst_issued_at());
						repocommands.setEntity(PluginGestioneUtentiOauthSessioni.class).add(toAdd);
					}

					criteria = new BOSearchApp();
					criteria.setNickname(payload.getNickname());

					// XXX: applicare la propria logica di individuazione utente
					// con la propria infrastruttura

					VGestioneUtentiOuth dataoauth = (VGestioneUtentiOuth) repoquery.setEntity(VGestioneUtentiOuth.class)
							.getSingleOrDefault(criteria.getSerialized());

					if (dataoauth != null)
						result.setStatoutente(dataoauth.getStato());

					return result;

				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new ServiceException(e);
				}
			}
		}

		return result;
	}

	private BOVerificaToken verifyIdToken(String idtoken) throws ServiceException {
		try {
			URL verifyToken = new URL("https://www.googleapis.com/oauth2/v2/tokeninfo?id_token=" + idtoken);
			URLConnection yc = verifyToken.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String result = "";
			String rawEsito = "";
			while ((result = in.readLine()) != null) {
				rawEsito += result;
			}
			in.close();

			BOVerificaToken esito = new BOVerificaToken();

			esito = (BOVerificaToken) MainApplication.getPojo(rawEsito, BOVerificaToken.class);

			return esito;

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	/**
	 * Questo è un esempio di checkintegrity per un app specifica. in base alla
	 * propria logica si adatterà il sistema di accesso a chiave assimetrica Può
	 * essere anche vuoto
	 */
	public BOSecurity checkIntegrityTemplate(String versione, boolean isbeta) throws ServiceException {

		BOSecurity result = new BOSecurity();
		result.setStatus(true);
		result.setStatoutente("Abile e arruolato");
		result.setUsername(utenteCorrente.getNickname());

		return result;
	}

	/**
	 * Questo è un esempio di verificaUtenza per un app specifica. in base alla
	 * propria logica si adatterà il sistema di accesso a chiave assimetrica Può
	 * essere anche vuoto
	 */
	public BOVerifica verificaUtenzaTemplate() throws ServiceException {

		BOVerifica result = new BOVerifica();

		result.setUsername(utenteCorrente.getNickname());
		result.setTeam(utenteCorrente.getTeam());
		result.setEsito("Tutto ok");
		result.setStato(true);
		result.setStatoutente("VERIFICATO");

		return result;
	}

	/**
	 * In questo esempio viene utilizzato il token csrftoken il quale è utilizzato
	 * per proteggere l'integrità dei dati
	 * https://stackoverflow.com/questions/5207160/what-is-a-csrf-token-what-is-its-importance-and-how-does-it-work
	 * 
	 * Cross-Site Request Forgery
	 * 
	 * Now for the better one with CSRF tokens:
	 * 
	 * The transfer request is extended with a third argument:
	 * http://www.mybank.com/transfer?to=123456;amount=10000;token=31415926535897932384626433832795028841971.
	 * That token is a huge, impossible-to-guess random number that mybank.com will
	 * include on their own web page when they serve it to you. It is different each
	 * time they serve any page to anybody. The attacker is not able to guess the
	 * token, is not able to convince your web browser to surrender it (if the
	 * browser works correctly...), and so the attacker will not be able to create a
	 * valid request, because requests with the wrong token (or no token) will be
	 * refused by www.mybank.com.
	 * 
	 * Questo metodo è predisposto per gestire un login sicura con credenziali
	 * coerenti tali a impedire man in the middle attack
	 * 
	 * @param versione
	 * @param isbeta
	 * @return
	 * @throws ServiceException
	 */
	public BOSecurity checkIntegrity(String versione, boolean isbeta) throws ServiceException {

		BOSecurity result = new BOSecurity();
		result.setStatoutente("Non abilitato");
		String versionToUpdate = null;
		if (!isbeta && !VERSION_RELEASE.equals(versione)) {
			versionToUpdate = VERSION_RELEASE;
		} else if (isbeta && !VERSION_BETA.equals(versione)) {
			versionToUpdate = VERSION_BETA;
		}

		try {

			_mainService.logAccesso("Avvio sessione di qualsiasi cosa tu abbia in mente " + versione, utenteCorrente);

			int check;
			BOSearchApp criteria;
			try {

				criteria = new BOSearchApp();

				criteria.getListIsNull().add("keyaccess");

				check = repoquery.setEntity(PluginCommonBlacklist.class).getCount(criteria.getSerialized());

				// XXX: l'utente non è in blacklist.
				// si verifica che tipo di utente e'
				if (check == 0) {

					/*
					 * if (utenteCorrente.getTeam().equals(
					 * "Appartenenza ad uno specifico gruppo, una implementazione piu semplice potrebbe non averlo"
					 * )) {
					 */

					// verifica se e' un nuovo utente
					if (utenteCorrente.getPrivatekey() != null) {

						String[] cookies = utenteCorrente.getCookies().split(";");
						String checkcookie = getSecureCookie(cookies, "csrftoken");

						// XXX:utente gia' registrato
						criteria = new BOSearchApp();
						criteria.setNickname(utenteCorrente.getNickname());
						VGestioneUtenti inforegistrazione = (VGestioneUtenti) repoquery
								.setEntity(PluginGestioneUtenti.class).getSingleOrDefault(criteria.getSerialized());

						// se l'autenticazione è andata a buon fine decidi
						// tu che cosa vuoi farli fare a questo utente (e
						// qui è il tuo sviluppo)
						// altrimenti lo sbatti fuori.

						if (inforegistrazione != null) {

							String csrftoken = inforegistrazione.getCsrftoken();

							if (checkcookie == null)
								throw new ServiceException("Il token csrftoken non e' stato trovato");

							// L'integrità è andata a buon fine e il cookie
							// è uguale al token che volevi
							if (checkcookie.equals(csrftoken)) {

								result.setEsito(
										"Scrivi quel che ti pare come bentornato, insomma da questo momento in poi il tizio è riconosciuto dalla tua app "
												+ "Bentornato " + utenteCorrente.getNickname()
												+ "!! Cosa vuoi fare oggi?");
								String stato = inforegistrazione.getStato();
								result.setStatoutente(stato + " " + "\nCodice identificativo sessione: "
										+ utenteCorrente.getHashscript());

								// Logica di individuazione della versione
								// da aggiornare
								if (versionToUpdate != null) {
									String noterelease = "<fieldset><legend>Release note</legend><b style='color:red;'></b>"
											+ "<ul><li>Scrivi quello che ti pare, magari questo testo può essere esteso e customizzato dalla tua app</li>"
											+ "</ul>" + "</fieldset>";

									String linkupdate = "uri dello script client che si interfaccia con l'ipotetica app, può essere greasmonkey o un uri rest o quel che ti serve";
									if (isbeta) {
										linkupdate = "qui un uri di un eventuale beta, mai usato, ma fa figo sapere che si può fare";
									}

									String esitoaggiornamento = "Attenzione!! L'app e' stata aggiornata alla versione "
											+ versionToUpdate + "\n" + noterelease + "\nClicca qui per ecc ecc..."
											+ linkupdate;

									result.setEsito(esitoaggiornamento);
									result.setMustupdate(versionToUpdate);

								}

								result.setStatus(true);
								result.setUsername(utenteCorrente.getNickname());

							} else {

								result.setStatoutente("Ci sono problemi di accesso al sistema");
								_mainService.logAccesso("Ci sono problemi di accesso al sistema per l'utente :"
										+ utenteCorrente.getNickname(), utenteCorrente);
							}

						} else {
							throw new ServiceException(
									"Si e' verificato una anomalia al momento del recupero delle info di registrazione utente");
						}

					} else {
						// XXX: utente nuovo.

						String esito = "In attesa di approvazione all'accesso al sistema.";

						// XXX: fase di registrazione di un nuovo utente
						criteria = new BOSearchApp();
						// criteria.setPlayer(utenteCorrente.getNickname());
						criteria.getListIsNotNull().add("keyaccess");
						List<PluginCommonBlacklist> list = repoquery.setEntity(PluginCommonBlacklist.class)
								.search(criteria.getSerialized());

						String registerpasscode = null;
						try {

							registerpasscode = md5(LocalDateTime.now().toString());

							PluginCommonBlacklist info;
							String scarabocchio = RandomScraps.generaFrase();

							if (list.size() == 1) {
								info = list.get(0);
								info.setKeyaccess(registerpasscode);
								info.setDescrizione(esito);
								info.setIpaddress(utenteCorrente.getInforemote());
								info.setScarabocchio(scarabocchio);
								info.setCookie(utenteCorrente.getCookies());

								repocommands.setEntity(PluginCommonBlacklist.class).update(info);

							} else {

								info = new PluginCommonBlacklist();
								info.setUtente(utenteCorrente.getNickname());
								info.setKeyaccess(registerpasscode);
								info.setDescrizione(esito);
								info.setIpaddress(utenteCorrente.getInforemote());
								info.setScarabocchio(scarabocchio);
								info.setCookie(utenteCorrente.getCookies());
								info.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));
								repocommands.setEntity(PluginCommonBlacklist.class).add(info);

							}

							result.setMustRegister(true);

							result.setPasscode(registerpasscode);
							result.setPublickey(getKeys(registerpasscode)[0]);
							result.setUsername(utenteCorrente.getNickname());
							result.setEsito(esito);
							result.setScarabocchio(scarabocchio);
							result.setMustupdate(versionToUpdate);
							result.setStatoutente("In fase di registrazione");
						} catch (NoSuchAlgorithmException e) {
							throw new ServiceException(e.getMessage(), e);
						}

					}

				} else {

					String esito = "Avversario di fazione! Fine dei giochi! :)";

					result = addToBlacklist(esito);
				}
				/*
				 * } else {
				 * 
				 * }
				 */

				// XXX: capire e ricordarsi cosa è il valore identitity. è
				// legato alla autenticazione a due fasi che è sperimentale
				// result.setIdentity(isAllowed);
				result.setIsbeta(isbeta);

				return result;

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				throw new ServiceException(e);
			}

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			throw e;
		}

	}

	public BOVerifica verificaUtenza() throws ServiceException {
		String esito = "Account non verificato";
		boolean stato = false;
		// String nickname = "Anonimo";

		String avvertenza = "I servizi della APP d'ora in avanti sono al tuo servizio. Buon divertimento ;)";
		BOVerifica esitoVerifica = new BOVerifica();

		try {

			BOSearchApp criteria = new BOSearchApp();
			criteria.setNickname(utenteCorrente.getNickname());
			PluginGestioneUtenti utenteregistrato = (PluginGestioneUtenti) repoquery
					.setEntity(PluginGestioneUtenti.class).getSingleOrDefault(criteria.getSerialized());

			if (utenteregistrato != null) {
				PluginGestioneUtentiOauth dataoauth = (PluginGestioneUtentiOauth) repoquery
						.setEntity(PluginGestioneUtentiOauth.class).getSingleOrDefault(criteria.getSerialized());

				if (utenteCorrente != null) {
					if (dataoauth != null) {
						stato = true;
						esito = "Validazione avvenuta con successo!! L'utenza dell'APP e' ora associata al tuo account Google. Risulti verificato e certificato. Buon proseguimento !!";
					} else {
						esito = "Non e' stato eseguito il google signin. Se pensi ci sia un errore contatta lo staff dell'APP";
					}
				}

			} else {

				criteria = new BOSearchApp();
				criteria.setNickname(utenteCorrente.getNickname());
				criteria.getListIsNotNull().add("keyaccess");
				PluginCommonBlacklist info = (PluginCommonBlacklist) repoquery.setEntity(PluginCommonBlacklist.class)
						.getSingleOrDefault(criteria.getSerialized());

				if (info != null) {
					String keyaccess = info.getKeyaccess();
					String scarabocchio = info.getScarabocchio();

					String cipher = md5(keyaccess + scarabocchio);

					criteria = new BOSearchApp();
					criteria.setNickname(utenteCorrente.getNickname());
					PluginGestioneUtentiOauth dataoauth = (PluginGestioneUtentiOauth) repoquery
							.setEntity(PluginGestioneUtentiOauth.class).getSingleOrDefault(criteria.getSerialized());

					if (dataoauth != null) {

						if (cipher.equals(utenteCorrente.getHashscript())) {

							String email = dataoauth.getEmail();
							String[] keys = getKeys(keyaccess);

							/*
							 * registra utente nuovo
							 */
							PluginGestioneUtenti newUser = new PluginGestioneUtenti();
							newUser.setEmail(email);
							newUser.setIdgruppo(2);
							newUser.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));
							newUser.setNickname(utenteCorrente.getNickname());
							newUser.setPublickey(keys[0]);
							newUser.setPrivatekey(keys[1]);
							repocommands.setEntity(PluginGestioneUtenti.class).add(newUser);

							BOSearchApp searcher = new BOSearchApp();
							searcher.setNickname(utenteCorrente.getNickname());
							PluginCommonBlacklist removeBlackListed = (PluginCommonBlacklist) repoquery
									.setEntity(PluginCommonBlacklist.class).getSingle(searcher.getSerialized());
							repocommands.setEntity(PluginCommonBlacklist.class).delete(removeBlackListed);

							esito = "Registrazione avvenuta con successo! Benvenuto " + utenteCorrente.getNickname()
									+ "!! " + avvertenza;

							esitoVerifica.setEmailaccount(email);
							stato = true;
						} else {
							esito = "Firma non valida";
						}

					} else {
						esito = "Non hai ancora eseguito il google signIn. Il signIn e' <b>obbligatorio</b> alla prima registrazione.";
					}
				}

			}

			criteria = new BOSearchApp();
			criteria.setNickname(utenteCorrente.getNickname());

			VGestioneUtentiOuth soauth = (VGestioneUtentiOuth) repoquery.setEntity(VGestioneUtentiOuth.class)
					.getSingleOrDefault(criteria.getSerialized());
			String statoutente = "Non definito";
			if (soauth != null) {
				statoutente = soauth.getStato();
			}

			esitoVerifica.setUsername(utenteCorrente.getNickname());
			esitoVerifica.setTeam(utenteCorrente.getTeam());
			esitoVerifica.setEsito(esito);
			esitoVerifica.setStato(stato);
			esitoVerifica.setStatoutente(statoutente);

			return esitoVerifica;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e);
		}

	}

	private boolean isRequestBot() throws BusinessException {
		boolean esito = false;

		if (utenteCorrente.getTokenapp() != null) {

			if (utenteCorrente.getTokenapp().equals("bot")) {
				esito = true;
			} else {
				BOSearchApp criteria = new BOSearchApp();
				criteria.setTokenapp(utenteCorrente.getTokenapp());
				try {
					repoquery.setEntity(PluginGestioneApps.class).getSingle(criteria.getSerialized());
					esito = true;

				} catch (Exception e) {
					String msg = "Attenzione! L'utente ha richiesto un accesso tramite app terze usando un token non valido";
					log.warn(msg);
					throw new BusinessException(msg);
				}
			}

			if (esito)
				utenteCorrente.setIsbot(true);
		}

		return esito;
	}

	private String[] getKeys(String passcode) {
		int size = passcode.length();
		int pivot = size / 2;
		String publickey = passcode.substring(0, pivot);
		String privatekey = passcode.substring(pivot);

		String[] result = new String[2];

		result[0] = publickey;
		result[1] = privatekey;

		return result;
	}

	private String getSecureCookie(String[] cookies, String namefield) {
		String result = null;

		for (String cCoo : cookies) {

			String[] splitCo = cCoo.split("=");
			String candidateField = splitCo[0].trim();
			if (candidateField.equals(namefield)) {
				result = splitCo[1];
				break;
			}
		}

		return result;
	}

	private BOSecurity addToBlacklist(String esito) throws RepositoryException {

		BOSecurity result = new BOSecurity();

		PluginCommonBlacklist toAdd = new PluginCommonBlacklist();
		toAdd.setIpaddress(utenteCorrente.getInforemote());
		toAdd.setDescrizione(esito);
		toAdd.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));
		repocommands.setEntity(PluginCommonBlacklist.class).add(toAdd);

		result.setUsername(utenteCorrente.getNickname());
		result.setEsito(esito);

		return result;
	}

	private boolean esitoCipher(BOUtente data) throws BusinessException {
		boolean esito = false;

		try {

			try {
				PluginGestioneUtenti utente = _mainService.getUtente(utenteCorrente.getNickname());

				if (utente == null)
					return false;

				String keyAccess = utente.getPublickey() + utente.getPrivatekey();
				String scarabocchio = utente.getScarabocchio();

				String cipher = md5(keyAccess + scarabocchio);

				esito = cipher.equals(utenteCorrente.getHashscript()) ? true : false;
				log.info("Hash fornito dall'utente     : " + utenteCorrente.getHashscript());
				log.info("Hash che si aspetta il server: " + cipher);

				return esito;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new BusinessException(e.getMessage(), e);
			}

		} catch (BusinessException e1) {
			log.error("Errore durante il recupero utente " + e1.getMessage(), e1);
			throw new BusinessException(e1.getMessage(), e1);
		}

	}

	private static String md5(String input) throws NoSuchAlgorithmException {
		String result = input;
		if (input != null) {
			MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);

			if ((result.length() % 2) != 0) {
				result = "0" + result;
			}
		}
		return result;
	}

	private static class RandomScraps {
		protected static String generaFrase() {
			int numeroFrasi = 3;

			List<String> nomi = Arrays.asList("Gatto", "Cane", "Pippo", "Squadra", "Avventura", "Citta", "Cavallo",
					"Automobile", "Rana", "Nano", "Fiore", "Giubba", "Bordello", "Troll", "Leo", "Staffo", "Agente K",
					"Chicco", "TaserTheaft", "Polis", "Giorgione", "Jack", "Fox", "Blood");

			List<String> verbi = Arrays.asList("ha portato", "prendera", "sta spiando", "mangia", "ha corso",
					"ha ggia mangiato", "scorregia", "sta bestemmiando avidamente", "turpiloquando",
					"sta agendo da fottuto grammar nazi", "gioisce", "ha appena pianificato", "kaboom",
					"ha sniffato stando in riga");

			List<String> complementi = Arrays.asList("Gatto", "Cane", "Pippo", "Squadra", "Avventura", "Citta",
					"Cavallo", "Automobile", "Rana", "Nano", "Fiore", "Giubba", "Bordello", "Troll", "Leo", "Staffo",
					"Agente K", "Chicco", "TaserTheaft", "Polis", "Giorgione", "Jack", "Fox", "Blood");

			List<String> congiunzioni = Arrays.asList(

					"e", "anche", "pure", "inoltre", "ancora", "perfino", "altresi", "ne", "neanche", "neppure",
					"nemmeno", "ma", "tuttavia", "pero", "eppure", "anzi", "si", "nonostante", "nondimeno", "bensi",
					"piuttosto", "invece", "mentre", "se non che", "al contrario", "per altro", "cio nonostante");

			String frase = "";
			for (int i = 0; i < numeroFrasi; i++) {
				String nome = getTokenFrase(nomi);
				String verbo = getTokenFrase(verbi);
				String complemento = getTokenFrase(complementi);
				String congiunzione = getTokenFrase(congiunzioni);

				frase += nome + " " + verbo + " " + complemento;

				if (i < numeroFrasi - 1) {
					frase += " " + congiunzione + " ";
				}

			}

			return frase;
		}

		private static String getTokenFrase(List<String> parole) {
			Random engineRandom = new Random();
			String result = parole.get(engineRandom.nextInt(parole.size() - 1));
			return result;
		}

	}

	public BOUtente getUtenteCorrente() {
		return utenteCorrente;
	}

}
