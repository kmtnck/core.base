package it.alessandromodica.product.callback;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.alessandromodica.product.context.main.AuthContext;
import it.alessandromodica.product.model.po.GestioneUtentiInfoautenticazione;
import it.alessandromodica.product.persistence.exceptions.RepositoryException;
import it.alessandromodica.product.persistence.interfaces.IBulkTransaction;
import it.alessandromodica.product.persistence.repo.BaseRepository;
import it.alessandromodica.product.persistence.searcher.YAFilterSearchApp;

/**
 * Callback in cui viene gestita la sessione dei cookie utente
 * 
 * @author Alessandro
 *
 */
@SuppressWarnings("rawtypes")
@Component
public class SettingCookie extends CallbackCommon implements IBulkTransaction {

	@Autowired
	AuthContext authContext;

	@Autowired
	protected BaseRepository repository;
	
	private static final Logger log = Logger.getLogger(SettingCookie.class);

	@SuppressWarnings("unchecked")
	@Override
	public void persist() throws RepositoryException {
		
		String[] cookies = authContext.getUtenteCorrente().getCookies().split(";");
		
		if (cookies != null && authContext.getUtenteCorrente().getIdutente() != 0)
			for (String cCookie : cookies) {
				String[] splitCookie = (cCookie.toString()).split("=", 2);
				if (splitCookie.length != 2)
					throw new RepositoryException(
							"Il recupero dei cookie non e' coerente quanto atteso. La procedura e' stata terminata");
				String nomeparametro = splitCookie[0].trim();
				String valoreparametro = splitCookie[1].trim();

				if ("csrftoken".equals(nomeparametro)) {
					YAFilterSearchApp criteria = new YAFilterSearchApp(GestioneUtentiInfoautenticazione.class);
					criteria.setIdutente(authContext.getUtenteCorrente().getIdutente());
					criteria.setNomeparametro("csrftoken");
					log.info("Cookie da aggiungere: " + cCookie);
					try {

						GestioneUtentiInfoautenticazione obj = (GestioneUtentiInfoautenticazione) repository
								//.setEntity(GestioneUtentiInfoautenticazione.class)
								.getSingleOrDefault(criteria.getSerialized());

						if (obj != null) {
							if (!obj.getValueparametro().equals(valoreparametro)) {
								obj.setValueparametro(valoreparametro);

								repository.update(obj);

								log.info("<<<S Il Cookie e' stato aggiornato: da [" + obj.getValueparametro() + "] a ["
										+ valoreparametro + "]");
							} else
								log.info("<<<S Il cookie non ha subito modifiche");
						} else {
							// int idobj = 0;
							GestioneUtentiInfoautenticazione pObj = new GestioneUtentiInfoautenticazione();
							pObj.setContesto("cookie");
							// pObj.setIdinfo(idobj);
							pObj.setIdutente(authContext.getUtenteCorrente().getIdutente());
							pObj.setNomeparametro(nomeparametro);
							pObj.setValueparametro(valoreparametro);
							pObj.setIstante(Timestamp.from(Calendar.getInstance().toInstant()));

							repository.add(pObj);

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
}
