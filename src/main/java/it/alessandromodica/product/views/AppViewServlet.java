package it.alessandromodica.product.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.alessandromodica.product.app.HelloWorldApp;
import it.alessandromodica.product.common.AdapterGson;
import it.alessandromodica.product.common.InputRequest;
import it.alessandromodica.product.common.MagicString;
import it.alessandromodica.product.common.enumerative.AppContext;
import it.alessandromodica.product.common.enumerative.RequestVariable;
import it.alessandromodica.product.common.exceptions.BusinessException;

/**
 * Classe che implementa una semplice servlet a scopo dimostrativo per esporre
 * le chiamate dell'applicazione in un ambiente remoto.
 * 
 * Potrebbero essere implementati altri tipi di approcci che espongano nel modo
 * piu opporturno le funzionalità dell'applicazione, come ad esempio sotto forma
 * di risorse REST
 * 
 * @author Alessandro
 *
 */
public class AppViewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(AppViewServlet.class);

	HelloWorldApp mainApp = AppViewListener.context.getBean(HelloWorldApp.class);

	/**
	 * Convertitore Json
	 */

	/**
	 * La servlet ha lo scopo di intercettare la coppia di valori contesto e azione.
	 * Tali valori vengono impostati dal chiamante e inseriti nella richiesta che
	 * viene passata alla servlet.
	 * 
	 * In base a tale coppia di valori viene intrapresa un'azione specifica
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			Object dataToSendClient = null;
			// <<<--------------------------------------------------
			try {

				log.info(getInfoRemote(request));

				String contesto = request.getParameter(MagicString.ProtocolloApp.P_CONTEXT);

				if (StringUtils.isBlank(contesto)) {
					throw new BusinessException(
							"Il contesto fornito al server non e' valido. Verificare il tipo di richiesta!");
				}
				AppContext context = null;
				try {

					context = AppContext.valueOf(contesto);

				} catch (Exception ex) {
					String msg = "Attenzione il contesto " + contesto + " ricevuto in input non e' valido ";
					log.warn(msg);
					throw new BusinessException(msg, ex);
				}

				InputRequest inputData = new InputRequest();
				switch (context) {
				case outhsignin:
				case outhsignout:
					String payloadOauth = request.getParameter(MagicString.ProtocolloApp.P_PAYLOADAUTH);

					inputData.getMapRequestData().put(RequestVariable.email,
							request.getParameter(RequestVariable.email.name()));
					inputData.getMapRequestData().put(RequestVariable.nickname,
							request.getParameter(RequestVariable.nickname.name()));

					dataToSendClient = mainApp.processSignInOutGoogle(inputData, payloadOauth, context);

					break;
				default:

					String remoteAddrs = request.getRemoteAddr();
					String referer = request.getHeader("Referer");
					String useragent = request.getHeader("User-Agent");
					// XXX: recupero informazioni del giocatore
					String rawUtente = request.getParameter(MagicString.ProtocolloApp.P_OBJPLAYER);

					EnumSet.allOf(RequestVariable.class).forEach(currentEnum -> inputData.getMapRequestData()
							.put(currentEnum, request.getParameter(currentEnum.name())));

					dataToSendClient = mainApp.processAction(inputData, remoteAddrs, referer, useragent, rawUtente,
							context);
					break;
				}

				writeResponse(dataToSendClient, request, response, null);

			} catch (Exception ex) {
				// TODO Auto-generated catch block
				log.error(ex.getMessage(), ex);
				throw ex;
			}

		} catch (Exception ex) {

			responseError(response, ex);
		}
	}

	private String getInfoRemote(HttpServletRequest request) {
		String infoRemote = request.getHeader("x-forwarded-for");
		if (infoRemote == null) {
			infoRemote = request.getHeader("X_FORWARDED_FOR");
			if (infoRemote == null) {
				infoRemote = " Indirizzo ip: " + request.getRemoteAddr();
			}
		}

		if (request.getHeader("User-Agent") != null)
			infoRemote += "  User-agent: " + request.getHeader("User-Agent");

		return ">>>> In arrivo una richiesta web da parte di : " + infoRemote + " <<<<<";
	}

	private void writeResponse(Object dataToRender, HttpServletRequest request, HttpServletResponse response,
			AppContext ltwContext) throws BusinessException, IOException {

		responseJson(dataToRender, response);

	}

	private void responseJson(Object dataToRender, HttpServletResponse response) throws IOException {
		String json = AdapterGson.GetInstance().toJson(dataToRender);

		log.info("Esito richiesta :\n" + json);

		// json = json.replaceAll("null", "\"\"");
		// si invia al client il risultato
		response.setHeader(MagicString.ProtocolloApp.CONTENT_TYPE, MagicString.ProtocolloApp.APPLICATION_JSON_CHARSET);
		response.setHeader(MagicString.ProtocolloApp.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.getWriter().write(json);
	}

	private void responseError(HttpServletResponse response, Exception ex)
			throws UnsupportedEncodingException, IOException {
		StringWriter sw = new StringWriter();
		// PrintWriter pw = new PrintWriter(sw);
		// ex.printStackTrace(pw);
		sw.toString();
		response.reset();

		String msg = "<div ><h2 align='center' >Si e' verificato un errore dal server</h2><h3 align=\"center\"><font color=\"red\">"
				+ ex.getMessage() + "</font></h3><label >" + sw.toString() + "</label></div>";
		byte ptext[] = msg.getBytes("UTF-8");
		String value = new String(ptext, "ISO-8859-1");

		// Tutti gli errori generati dal server vengono veicolati in un
		// getwriter apposito
		// la chiamata jquery intercetta la richiesta come uno status error
		// stampando opportunamente il messaggio d'errore con l'apposito
		// template html
		response.getWriter().print(value);
	}

	@SuppressWarnings("unused")
	private void responseExport(Map<String, Object> dataToRender, HttpServletResponse response) throws IOException {
		String filename_zip = (String) dataToRender.get(MagicString.ParameterName.FILENAME_EXPORT_ZIP);
		String filename_xml = (String) dataToRender.get(MagicString.ParameterName.FILENAME_EXPORT_XML);
		ByteArrayOutputStream resultStream = (ByteArrayOutputStream) dataToRender
				.get(MagicString.ParameterName.RESULTSTREAMEXPORT);

		response.reset();
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename_zip + ".zip");
		ServletOutputStream out = response.getOutputStream();

		ZipOutputStream zs = new ZipOutputStream(out);
		ZipEntry e = new ZipEntry(filename_xml + ".xml");
		zs.putNextEntry(e);
		zs.write(resultStream.toByteArray());
		zs.closeEntry();
		zs.close();
		out.flush();
	}

	@SuppressWarnings("unused")
	private String getFileName(String filename) throws BusinessException {
		Date now = new Date();
		DateFormat formatter_for_file = new SimpleDateFormat("yyyyMMdd-hhmmss");
		try {
			String date_string = formatter_for_file.format(now);
			String archiveName = "";
			return archiveName + "_" + filename + "_" + date_string;
		} catch (Exception pe) {
			throw new BusinessException(pe);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
