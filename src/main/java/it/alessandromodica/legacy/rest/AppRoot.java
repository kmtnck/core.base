package it.alessandromodica.legacy.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.alessandromodica.legacy.views.AppViewListener;
import it.alessandromodica.product.services.interfaces.IMainService;

/**
 * Endpoint rest implementati con resteasy, non sono pero ben supportati
 * dall'infrastruttura springboot costringendo a usare il context spring per
 * recuperare oggetti iniettabili.
 * 
 * La strada alternativa e piu lineare e' l'implementazione dei rest con le
 * annotation springboot vere e proprie, nel package restcontroller
 * 
 * Endpoint resteasy disponibili nel caso in cui l'applicazione e' avviata come artefatto war classico
 * @author kmtnck
 *
 */
@SuppressWarnings("rawtypes")
@ApplicationPath("/services")
@Deprecated
public class AppRoot extends Application {

	public static IMainService mainservice;

	public AppRoot() {
		mainservice = AppViewListener.context.getBean(IMainService.class);
		//getBeanConfig();
	}
	
	/*public BeanConfig getBeanConfig()
	{
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setTitle("Generatore ui swagger");
		beanConfig.setDescription("Espone gli endpoint swagger");
		//beanConfig.setVersion("1.0.0");
		beanConfig.setResourcePackage("it.alessandromodica.legacy");
		beanConfig.setBasePath("/core.base/services");
		beanConfig.setScan(true);
		beanConfig.setPrettyPrint(true);
		
		return beanConfig;
	}*/
}