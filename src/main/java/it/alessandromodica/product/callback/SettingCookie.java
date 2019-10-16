package it.alessandromodica.product.callback;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.alessandromodica.product.common.exceptions.RepositoryException;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.PluginGestioneUtentiInfoautenticazione;
import it.alessandromodica.product.persistence.interfaces.IBulkTransaction;
import it.alessandromodica.product.persistence.searcher.BOSearchApp;

/**
 * Callback in cui viene gestita la sessione dei cookie utente
 * 
 * @author Alessandro
 *
 */
@Component
public class SettingCookie extends CallbackCommon implements IBulkTransaction {

	private static final Logger log = Logger.getLogger(SettingCookie.class);

	private String[] cookies;

	@SuppressWarnings("unchecked")
	@Override
	public void persist(EntityManager em) throws RepositoryException {
		if (cookies != null && utenteCorrente.getIdutente() != 0)
			for (String cCookie : cookies) {
				String[] splitCookie = (cCookie.toString()).split("=", 2);
				if (splitCookie.length != 2)
					throw new RepositoryException(
							"Il recupero dei cookie non e' coerente quanto atteso. La procedura e' stata terminata");
				String nomeparametro = splitCookie[0].trim();
				String valoreparametro = splitCookie[1].trim();

				if ("csrftoken".equals(nomeparametro)) {
					BOSearchApp criteria = new BOSearchApp();
					criteria.setIdutente(utenteCorrente.getIdutente());
					criteria.setNomeparametro("csrftoken");
					log.info("Cookie da aggiungere: " + cCookie);
					try {

						PluginGestioneUtentiInfoautenticazione obj = (PluginGestioneUtentiInfoautenticazione) repoquery.setEntity(PluginGestioneUtentiInfoautenticazione.class)
								.getSingleOrDefault(criteria.getSerialized());

						if (obj != null) {
							if (!obj.getValueparametro().equals(valoreparametro)) {
								obj.setValueparametro(valoreparametro);

								if (em == null)
									repocommands.setEntity(PluginGestioneUtentiInfoautenticazione.class).update(obj);
								else
									repocommands.setEntity(PluginGestioneUtentiInfoautenticazione.class).update(obj, em);

								log.info("<<<S Il Cookie e' stato aggiornato: da [" + obj.getValueparametro() + "] a ["
										+ valoreparametro + "]");
							} else
								log.info("<<<S Il cookie non ha subito modifiche");
						} else {
							// int idobj = 0;
							PluginGestioneUtentiInfoautenticazione pObj = new PluginGestioneUtentiInfoautenticazione();
							pObj.setContesto("cookie");
							// pObj.setIdinfo(idobj);
							pObj.setIdutente(utenteCorrente.getIdutente());
							pObj.setNomeparametro(nomeparametro);
							pObj.setValueparametro(valoreparametro);
							pObj.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));

							if (em == null)
								repocommands.add(pObj);
							else
								repocommands.add(pObj, em);

							log.info("<<<S Il Cookie e' stato aggiunto: " + cCookie);
						}

					} catch (RepositoryException e) {
						log.error("Si e' verificato un problema durante l'aggiornamento dei cookie " + e.getMessage(),
								e);
						throw e;
					}

				}
			}
		else
			log.warn("Non sono stati settati cookie requisiti per accedere al sistema");

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setEntities(List obj, BOUtente utente) {
		// TODO Auto-generated method stub
		setUtenteCorrente(utente);
		if (utente.getCookies() != null)
			this.cookies = utente.getCookies().split(";");
	}
}
