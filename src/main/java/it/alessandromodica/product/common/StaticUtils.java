package it.alessandromodica.product.common;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.alessandromodica.product.common.exceptions.ServiceException;

public class StaticUtils {

	private static final Logger log = Logger.getLogger(StaticUtils.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	public static ArrayList transformListRawData(String rawPortal) throws ServiceException {
		try {
			ArrayList parseRawData = mapper.readValue(rawPortal.toString(), ArrayList.class);
			return parseRawData;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
}
