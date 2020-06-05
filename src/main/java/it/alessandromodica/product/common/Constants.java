package it.alessandromodica.product.common;

/**
 * Costanti comuni
 * 
 * @author mgambini
 *
 */
public class Constants {
	
	public static String TITOLO_APP = "goToBusiness";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String API_DEFAULT_PATH_PREFIX = "/api";

	/**
	 * Elenco di costanti immutabili che non hanno bisogno di essere parametrizzate
	 * e che sono tollerabili riavvii qualora debbano cambiare per forza
	 */
    public static final String SPLIT_ASCDESC = "-";
	public static final String SPLIT_ORDERS = "_";
	public static final String CODICEPDD = "pddCodice";
	public static final String CODICEFORNITORE = "interlCodice";
	public static final String ISMERCEOLOGIA = "ismerceologia";
	public static final String ORDERBY = "orderby";
	public static final String CLASSNAME = "classname";
}
