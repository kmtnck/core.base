package it.alessandromodica.product.views;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"unused","rawtypes"})
public class MainViews<T> {

	private static final Logger log = Logger.getLogger(MainViews.class);

	private Class clazz;

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
