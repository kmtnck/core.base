package it.alessandromodica.legacy.views;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Classe che rappresenta il componente views in cui possono essere implementate
 * logiche per esporre le funzionalità sul web a seconda la strategia scelta.
 * 
 * La classe e' vuota ed e' solo a scopo dimostrativo.
 * 
 * @author Alessandro
 *
 * @param <T>
 */
@Component
@SuppressWarnings({ "unused", "rawtypes" })
public class MainViews<T> {

	private static final Logger log = Logger.getLogger(MainViews.class);

	private Class clazz;

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
