package it.alessandromodica.product.app;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriInfo;

@ApplicationPath("/services")

public class AppRoot extends Application {

	/**
	 * Metodo alternativo e piu programmatico per accedere al valore di un parametro
	 * in get rest
	 * 
	 * Incapsula l'oggetto URIInfo nativo di resteasy e su cui poter fare le
	 * canoniche letture della request, in questo caso il valore dei parametri get.
	 * 
	 * Potrebbe potenziarsi a seconda le esigenze, valutare se utilizzare il bind di
	 * un parambean oppure l'accesso diretto in request con questo componente
	 * 
	 * @author amodica
	 *
	 */
	public static class ExtractURIValue {
		UriInfo info;

		public ExtractURIValue(UriInfo info) {
			// TODO Auto-generated constructor stub
			this.info = info;
		}

		public String getValue(String name) {
			return info.getQueryParameters().getFirst(name);
		}
	}
	
}