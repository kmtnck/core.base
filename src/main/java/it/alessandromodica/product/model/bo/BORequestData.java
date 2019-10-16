package it.alessandromodica.product.model.bo;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BORequestData {

	private Map<Enum,Object> mapRequestData = new HashMap<Enum,Object>();

	public Map<Enum, Object> getMapRequestData() {
		return mapRequestData;
	}
}
