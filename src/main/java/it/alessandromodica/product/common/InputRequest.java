package it.alessandromodica.product.common;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class InputRequest {

	private Map<Enum,Object> mapRequestData = new HashMap<Enum,Object>();

	public Map<Enum, Object> getMapRequestData() {
		return mapRequestData;
	}
}
