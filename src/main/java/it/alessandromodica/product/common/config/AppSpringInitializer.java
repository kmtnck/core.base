package it.alessandromodica.product.common.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppSpringInitializer /*extends AbstractAnnotationConfigDispatcherServletInitializer */{

	private static final Logger log = Logger.getLogger(AppSpringInitializer.class);
	public static AnnotationConfigApplicationContext context;

	//@Override
	public void onStartup(ServletContext container) throws ServletException {

		initLog();
		//container.addListener(new ResteasyBootstrap());
		/*
		 * final AnnotationConfigWebApplicationContext rootContext = new
		 * AnnotationConfigWebApplicationContext(); ContextLoaderListener springListener
		 * = new ContextLoaderListener(rootContext);
		 * rootContext.register(AppConfig.class); container.addListener(springListener);
		 */
	}

	//@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	//@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	//@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	private static void initLog() throws FactoryConfigurationError {
		DOMConfigurator.configure(AppConfig.class.getResource("log4j.xml"));
		log.info("Istanziato il logger");
	}
}