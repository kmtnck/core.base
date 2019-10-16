package it.alessandromodica.product.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.alessandromodica.product.common.enumerative.AppContext;
import it.alessandromodica.product.common.exceptions.BusinessException;
import it.alessandromodica.product.common.exceptions.ServiceException;
import it.alessandromodica.product.context.consultazione.MainConsultazione;
import it.alessandromodica.product.context.interfaces.IConsultazione;
import it.alessandromodica.product.context.interfaces.IGis;
import it.alessandromodica.product.context.security.Cassaforte;
import it.alessandromodica.product.model.bo.BOAppPayload;
import it.alessandromodica.product.model.bo.BOCoordinate;
import it.alessandromodica.product.model.bo.BODecoderAddress;
import it.alessandromodica.product.model.bo.BORequestData;
import it.alessandromodica.product.model.bo.BOUtente;
import it.alessandromodica.product.model.po.PluginGestioneUtenti;
import it.alessandromodica.product.services.interfaces.IMainService;

/**
 * La classe MainContext rappresenta il punto unico della gestione dei vari
 * contesti applicativi previsti e futuri.
 * 
 * Gestisce le richieste avendo sempre valida l'informazione dell'utente
 * corrente e, ogni esecuzione di funzione è controllata a livello di sicurezza.
 * 
 * Il metodo getResult è un multiplexter il quale, in base ad un elenco
 * enumerico che può essere parametrizzato, permette di avviare la funzione dal
 * componente apposito
 * 
 * Il MainContext implementa i metodi comuni usati dall'applicazione e, in
 * generale, da metodi general purpose che sono utilizzati dalla maggioranza
 * delle app moderne.
 * 
 * Altre funzionalità comuni general purpose possono essere definite in questa
 * classe.
 * 
 * @author Alessandro
 *
 */
public abstract class MainContext extends GoToBusiness {


	private static final Logger log = Logger.getLogger(MainContext.class);

	/**
	 * Componente specifico per gestire l'autenticità dell'utente
	 */
	@Autowired
	protected Cassaforte cassaforte;

	/**
	 * Servizio principale dell'applicazione utilizzato per recuperare le
	 * informazioni utente e loggare dove serve le attivita'
	 */
	@Autowired
	protected IMainService _mainService;

	/**
	 * Set di componenti specifici dell'applicazione, in questo caso sono
	 * consultazione, un componente gis e il modulo per l'accesso al dato
	 */
	@Autowired
	protected IConsultazione consultazione;
	@Autowired
	protected IGis datagis;

	protected BOUtente utenteCorrente;

	/**
	 * Questo field è valorizzato dal componente incaricato ad evadere la
	 * richiesta, di solito viene valorizzato anche l'utente corrente
	 */
	protected BORequestData inputData;

	/**
	 * Questo setter viene sempre inizializzato da tutti i componenti con il
	 * metodo setInfo, in cui si inseriscono le informazioni utente ad ogni
	 * chiamata
	 */
	public void setUtenteCorrente(BOUtente utente) {

		if (utente != null) {

			utenteCorrente = utente;
			try {

				String value = utente.getNickname() != null ? utente.getNickname() : null;
				log.info("Utente corrente identificato :" + value);

				PluginGestioneUtenti data = _mainService.getUtente(value);
				if (data != null) {
					utenteCorrente.setIdutente(data.getIdutente());
					utenteCorrente.setPublickey(data.getPublickey());
					utenteCorrente.setPrivatekey(data.getPrivatekey());
					utenteCorrente.setScarabocchio(data.getScarabocchio());

					log.debug("Configurate le impostazioni dell'utente corrente");
				}

			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				log.warn("Non e' stato possibile recuperare le informazioni dell'utente " + utente.getNickname());
			}
		}
	}

	/**
	 * Metodo che ritorna il risultato di una specifica richiesta. Lo switch è
	 * suddiviso per controller di competenza. Su ciascuna specifica richiesta
	 * viene customizzato il tipo di input da fornire al controller per
	 * l'esecuzione del metodo specifico
	 * 
	 * @param appcontext
	 * @param inputData
	 * @param utenteCorrente
	 * @return
	 * @throws BusinessException
	 */
	public BOAppPayload getResult(AppContext appcontext, BORequestData inputData, BOUtente utenteCorrente)
			throws BusinessException {
		BOAppPayload dataToSendClient = new BOAppPayload();

		try {

			switch (appcontext) {

			case utenticonnessi:

				if (this.consultazione == null)
					consultazione = new MainConsultazione();

				consultazione.setInfo(utenteCorrente, inputData);

				switch (appcontext) {
				case utenticonnessi:
					_mainService.logAccesso("Richiesta utenti online", utenteCorrente);
					return consultazione.getUtentiConnessi();
				default:
					break;
				}

				break;
			default:

				break;
			}

			return dataToSendClient;

		} catch (ServiceException e) {
			log.error("Si e' verificato un errore durante l'elaborazione della richiesta " + e.getMessage(), e);
			throw new BusinessException(e);
		}

	}

	public static BOCoordinate decodeCoordinate(
			String coordinate/* double lat, double lon */) throws Exception {
		try {
			BOCoordinate result = new BOCoordinate();
			// result.setLat(lat / 1E6);
			// result.setLon(lon / 1E6);
			String[] splitCoord = coordinate.split(",");
			String lat = splitCoord[0].trim();
			String lon = splitCoord[1].trim();

			result.setLat(BigDecimal.valueOf(Double.parseDouble(lat)).setScale(4, RoundingMode.HALF_UP).doubleValue());
			result.setLon(BigDecimal.valueOf(Double.parseDouble(lon)).setScale(4, RoundingMode.HALF_UP).doubleValue());
			result.setCoordinate(coordinate);

			return result;
		} catch (Exception ex) {
			log.error("Errore durante la decodifica delle coordinate", ex);
			throw new Exception(ex.getMessage(), ex);
		}

	}

	public static BOCoordinate decodeCoordinateView(
			String coordinate/* double lat, double lon */) throws Exception {
		try {
			BOCoordinate result = new BOCoordinate();
			// result.setLat(lat / 1E6);
			// result.setLon(lon / 1E6);
			String[] splitCoord = coordinate.split(",");
			String lat = splitCoord[0].trim();
			String lon = splitCoord[1].trim();

			result.setLat(Double.parseDouble(lat));
			result.setLon(Double.parseDouble(lon));
			result.setCoordinate(coordinate);

			return result;
		} catch (Exception ex) {
			log.error("Errore durante la decodifica delle coordinate", ex);
			throw new Exception(ex.getMessage(), ex);
		}

	}

	public static Date buildIstante(Date data, Date ora) throws Exception {
		String formatFriendly = "yyyy-MM-dd HH:mm:ss";
		String formatData = "yyyy-MM-dd";
		String formatOra = "HH:mm:ss";
		SimpleDateFormat friendlyData = new SimpleDateFormat(formatFriendly);
		SimpleDateFormat formatterData = new SimpleDateFormat(formatData);
		SimpleDateFormat formatterOra = new SimpleDateFormat(formatOra);
		String dataStr = formatterData.format(data);
		String oraStr = formatterOra.format(ora);
		Date istante = friendlyData.parse(dataStr + " " + oraStr);
		return istante;
	}

	public static Date buildIstante(Date datetime) throws Exception {
		String formatFriendly = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat friendlyData = new SimpleDateFormat(formatFriendly);
		String dataOraStr = friendlyData.format(datetime);
		Date istante = friendlyData.parse(dataOraStr);
		return istante;
	}

	public static String buildFriendlyIstante(Date datetime) throws Exception {
		String formatFriendly = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat friendlyData = new SimpleDateFormat(formatFriendly);
		String dataOraStr = friendlyData.format(datetime);
		return dataOraStr;
	}

	@SuppressWarnings("rawtypes")
	public static Object getData(List containerObj, int indexcontext, String fieldkey) throws BusinessException {

		return getData(containerObj, indexcontext, -1, fieldkey);
	}

	@SuppressWarnings("rawtypes")
	public static Object getData(List containerObj, int indexcontext, int indexsubcontext, String fieldkey)
			throws BusinessException {
		try {
			Map contestoazioneData = null;

			if (indexsubcontext > -1) {
				List contestoazione = (ArrayList) containerObj.get(indexcontext);
				contestoazioneData = (LinkedHashMap) contestoazione.get(indexsubcontext);
			} else {
				contestoazioneData = (LinkedHashMap) containerObj.get(indexcontext);
			}

			Object result = contestoazioneData.get(fieldkey);

			return result;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new BusinessException(ex.getMessage(), ex);
		}
	}

	public static BODecoderAddress getIndirizzo(String coordinate, String indirizzoAttuale) throws ServiceException {
		try {
			return getIndirizzo(MainContext.decodeCoordinate(coordinate), indirizzoAttuale);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}

	@SuppressWarnings("rawtypes")
	public static BODecoderAddress getIndirizzo(BOCoordinate coordinate, String indirizzoAttuale)
			throws ServiceException {
		try {

			BODecoderAddress esito = new BODecoderAddress();
			// XXX: recupero l'indirizzo da due modi:
			//

			if (StringUtils.isBlank(indirizzoAttuale) || "Non disponibile".equals(indirizzoAttuale)) {

				try {
					URL resolveAddress = new URL(
							"https://maps.googleapis.com/maps/api/geocode/json?key=%KEYGOOGLEAPI%"
									+ coordinate.getLat() + "," + coordinate.getLon());
					URLConnection yc = resolveAddress.openConnection();
					BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
					String result = "";
					String rawEsito = "";
					while ((result = in.readLine()) != null) {
						rawEsito += result;
					}
					in.close();

					ObjectMapper mapper = new ObjectMapper();
					LinkedHashMap parseRawData = mapper.readValue(rawEsito, LinkedHashMap.class);
					ArrayList results = (ArrayList) parseRawData.get("results");
					String resultsAddress = MainContext.getData(results, 0, "formatted_address").toString();

					esito.setFormatted_address(resultsAddress);

				} catch (Exception e) {
					log.warn(
							"Non e' stato possibile recuperare l'indirizzo dal decoder google [" + e.getMessage() + "]",
							e);
				}
			} else
				esito.setFormatted_address(indirizzoAttuale);

			return esito;

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServiceException(ex);
		}
	}
}
