package it.alessandromodica.product.common;

/**
 * Stringhe magiche che possono essere referenziate sul codice.
 * 
 * Prevedono il set di parametri riconosciuti dall'applicazione e in generale un
 * approccio centralizzato per le varie esigenze di string hard code
 * 
 * @author Alessandro
 *
 */
public class MagicString {

	public static class SuffixAndPrefix {

	}

	public class ProtocolloApp {

		public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
		public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
		public static final String APPLICATION_JSON_CHARSET = "application/json";
		public static final String APPLICATION_JSON_CHARSET_ISO88592 = "application/json; charset=ISO-8859-2";
		public static final String CONTENT_TYPE = "Content-Type";

		public static final String PREFIXERRORESITO = "ERRORE:";

		public static final String P_CONTEXT = "context";
		public static final String P_PAYLOADAUTH = "payloadAuth";
		public static final String P_OBJPLAYER = "objplayer";

		public static final String P_ACTION = "action";

		// Variabile per il passaggio di un generico bean json
		// opera, elementolinea, materialeaccessorio, interferenza, staccotm,
		// altri...
		public static final String P_OBJ_BEAN = "obj_bean";

	}

	public static class UrlDefinition {

		public static String HOME = "/%NOME_PRODOTTO%/";
		public static String HANDLER = HOME + "handler";

		public static String INDEX = HOME + "index.jsp";
		public static String RICERCA = HOME + "ricerca.jsp";
		public static String DETTAGLIO = HOME + "dettaglio.jsp";
		public static String TEMPLATE = HOME + "templates/";
		public static String SCRIPTS = HOME + "scripts/";
		public static String IMAGES = HOME + "images/";
		public static String LEGENDA = HOME + "legenda/";
		public static String MANUALE = HOME + "manuale/";
		public static String MEDIA = HOME + "media/";
		// XXX: ecc ecc... . queste variabili sono parametrizzabili in modo
		// opportuno
	}

	/**
	 * I tag presenti in questa classe si riferiscono a id di tag html
	 * identificati anche dal server. Tutti i tag html utilizzati unicamente
	 * nello scope js/jsp non sono mappati in questa classe
	 * 
	 * @author Alessandro
	 * 
	 */
	public class TagToRender {

		/*
		 * In questa classe saranno raccolti tutti gli id html previsti dalle
		 * pagine dei vari contesti.
		 */

	}

	public static class TagToPermission {

	}

	/**
	 * I parametri di questa classe sono quelli riconosciuti dal server durante
	 * una richiesta. I parametri applicativi, insieme a quelli del protocollo
	 * permettono la personalizzazione dei vari contesti a seconda le esigenze
	 * 
	 * @author Alessandro
	 * 
	 */
	public class ParameterName {

		public static final String OAM_REMOTE_USER = "OAM_REMOTE_USER";
		public static final String FILENAME = "filename";
		public static final String FILENAME_EXPORT_XML = "filename_export_xml";
		public static final String FILENAME_EXPORT_ZIP = "filename_export_zip";
		public static final String RESULTSTREAMEXPORT = "resultstreamexport";

		public static final String USERID = "userID";
		public static final String REDIRECT_POSTACTION = "redirect_postaction";
	}

	public static class Reportistica {
		public static final String RESULTPDF = "resultpdf";
		public static final String RESULTXLS = "resultxls";

		public static final String LEGENDA_TMPL = "Legenda.pdf";
		public static final String ARIAL_FONT = "Arial.ttf";

	}

	public static class Messaggistica {

		public static final String ARCHIVIO_NON_VALIDO = "Il formato del file selezionato non e' valido.";
		public static final String IMPORT_SUCCESS = "Importazione eseguita con successo";

	}

	/**
	 * Classe che raccoglie la definizione di class css riservati, utilizzati
	 * cioè per scopi non di layout grafico
	 * 
	 * @author Alessandro
	 * 
	 */
	public static class CssClassReserved {
		public static final String BLANK_VALIDATE = "blank";
		public static final String NUMERIC_VALIDATE = "numeric";
		public static final String GRADI_VALIDATE = "gradi";
		public static final String PRIMI_VALIDATE = "primi";
		public static final String MSGERROR_VALIDATE = "msgerror";
		public static final String ENTRY_INPUT = "entry";

	}
}
