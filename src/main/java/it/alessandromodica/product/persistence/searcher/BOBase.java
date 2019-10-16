package it.alessandromodica.product.persistence.searcher;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import it.alessandromodica.product.common.exceptions.RepositoryException;

public class BOBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9150468309827097773L;

	@SuppressWarnings("rawtypes")
	protected static Map<String, Object> toDictionary(Object src) throws RepositoryException {
		Map<String, Object> result = new HashMap<String, Object>();

		// recupero la classe dell'oggetto
		Class typeClass = src.getClass();

		try {
			for (Field cfield : typeClass.getDeclaredFields()) {

				boolean isStaticField = java.lang.reflect.Modifier.isStatic(cfield.getModifiers())
						&& java.lang.reflect.Modifier.isFinal(cfield.getModifiers());
				if (isStaticField)
					continue;

				if (!cfield.isAccessible())
					cfield.setAccessible(true);

				String nameField = cfield.getName();
				Object value = cfield.get(src);

				if (value != null) {

					if (value instanceof String || value instanceof Integer || value instanceof Character
							|| value instanceof Double || value instanceof Date || (value instanceof Class<?>)) {

						if ((value instanceof String && !StringUtils.isBlank(value.toString()))
								|| (value instanceof Character && !StringUtils.isBlank(value.toString()))
								|| (value instanceof Integer && Integer.parseInt(value.toString()) != 0)
								|| (value instanceof Double && Double.parseDouble(value.toString()) != 0.0)
								|| (value instanceof Date) || (value instanceof Class<?>)) {

							result.put(nameField, value);
						}

					}
				}
			}

			return result;

		} catch (Exception ex) {
			throw new RepositoryException("Errore durante la serializzazione di un oggetto", ex);
		}

	}

}
