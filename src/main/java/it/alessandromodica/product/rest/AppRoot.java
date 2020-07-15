package it.alessandromodica.product.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.alessandromodica.product.services.interfaces.IMainService;
import it.alessandromodica.product.views.AppViewListener;

/**
 * Endpoint rest implementati con resteasy, non sono pero ben supportati
 * dall'infrastruttura springboot costringendo a usare il context spring per
 * recuperare oggetti iniettabili.
 * 
 * La strada alternativa e piu lineare e' l'implementazione dei rest con le
 * annotation springboot vere e proprie, nel package restcontroller
 * 
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
	}
}