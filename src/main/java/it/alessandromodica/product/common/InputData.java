package it.alessandromodica.product.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe che rappresenta la mappa dei valori forniti in input all'applicazione
 * 
 * @author Alessandro
 *
 */
@SuppressWarnings("rawtypes")
public class InputData {

	private Map<Enum,Object> mapRequestData = new HashMap<Enum,Object>();

	public Map<Enum, Object> getMapRequestData() {
		return mapRequestData;
	}
}
